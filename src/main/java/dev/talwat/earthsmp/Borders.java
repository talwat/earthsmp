package dev.talwat.earthsmp;

import dev.talwat.earthsmp.nations.Nation;
import dev.talwat.earthsmp.nations.NationsConfig;
import dev.talwat.earthsmp.nations.Territory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.marker.Polygon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.logging.Level;

import static dev.talwat.earthsmp.ContourTracing.IsGrayScale;
import static dev.talwat.earthsmp.Squaremap.SetupLayerProvider;
import static java.lang.String.format;
import static org.bukkit.Bukkit.getOfflinePlayer;
import static org.bukkit.Bukkit.getServer;

class Cache {
    private final Map<UUID, Nation> playerToNation;
    private final Borders borders;

    public Cache(Borders borders) {
        this.borders = borders;
        this.playerToNation = new HashMap<>();
    }

    public Nation playerToNation(Player player) {
        if (!playerToNation.containsKey(player.getUniqueId())) {
            playerToNation.put(player.getUniqueId(), null);

            for (Nation nation : borders.nations.values()) {
                if (nation.members().contains(player.getUniqueId())) {
                    playerToNation.put(player.getUniqueId(), nation);
                    break;
                }
            }
        }

        return playerToNation.get(player.getUniqueId());
    }
}

public class Borders {
    private final Earthsmp plugin;
    // The key is also the `hue` value.
    public Map<Integer, Nation> nations;
    public Scoreboard scoreboard;
    public Map<String, Team> teams;
    private Cache cache;
    private BufferedImage image;

    public Borders(Earthsmp plugin) {
        this.plugin = plugin;
        this.Load();
    }

    private static Color convertToColor(int rgb) {
        int a = (rgb >> 24) & 0xFF;
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb) & 0xFF;

        return new Color(r, g, b, a);
    }

    public void Load() {
        loadImage();
        loadNations();

        if (this.image == null || this.nations == null) {
            return;
        }

        this.cache = new Cache(this);
        this.teams = new HashMap<>();

        if (scoreboard == null) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }

        for (Nation nation : nations.values()) {
            Team team;
            try {
                team = scoreboard.registerNewTeam(nation.tag());
            } catch (Exception e) {
                team = scoreboard.getTeam(nation.tag());
                for (String entry : team.getEntries()) {
                    team.removeEntries(entry);
                }
            }

            teams.put(nation.tag(), team);
        }

        for (Player player : getServer().getOnlinePlayers()) {
            player.playerListName(formatUsername(player, null));
            Nation nation = cache.playerToNation(player);
            if (nation != null) {
                teams.get(nation.tag()).addPlayer(player);
            }
        }

        HashMap<Color, List<java.awt.Point>> boundaries = Shapes.TraceShapes(image);
        SimpleLayerProvider layerProvider = SetupLayerProvider("Borders", "borders");
        if (layerProvider == null) {
            return;
        }

        int i = 0;
        for (Map.Entry<Color, List<java.awt.Point>> entry : boundaries.entrySet()) {
            List<java.awt.Point> shape = entry.getValue();
            Color color = entry.getKey();
            float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            ArrayList<Point> filtered = new ArrayList<>();

            for (java.awt.Point point : shape) {
                Point p = imageToMap(point);
                filtered.add(p);
            }

            Nation nation = get(hsb[0]);

            if (nation == null) {
                continue;
            }

            Polygon marker = Polygon.polygon(filtered);
            marker.markerOptions(
                    marker.markerOptions()
                            .asBuilder()
                            .fillColor(color)
                            .strokeColor(color.brighter())
                            .clickTooltip(getMapLabel(nation, hsb))
            );
            layerProvider.addMarker(Key.key(format("borders_%s", i)), marker);

            i++;
        }
    }

    public void loadNations() {
        NationsConfig config = NationsConfig.Load(plugin);

        Map<Integer, Nation> nations = new HashMap<>();
        for (Map<String, Object> raw : config.parsed) {
            Nation nation = Nation.deserialize(raw);
            nations.put(nation.hue(), nation);
        }

        this.nations = nations;
    }

    public @Nullable Nation get(Location pos) {
        Color color = plugin.borders.getColor(pos.toBlockLocation());

        if (color == null || IsGrayScale(color) || color.getAlpha() == 0) {
            return null;
        }

        float[] hsv = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

        return plugin.borders.get(hsv[0]);
    }

    public @Nullable Nation getFromCache(Player player) {
        return cache.playerToNation(player);
    }

    public @Nullable Nation get(UUID ruler) {
        for (Nation nation : nations.values()) {
            if (nation.ruler() != null && nation.ruler().equals(ruler)) {
                return nation;
            }
        }

        return null;
    }

    public @Nullable Nation get(String tag) {
        for (Nation nation : nations.values()) {
            if (nation.tag().equals(tag)) {
                return nation;
            }
        }

        return null;
    }

    public Nation get(float hue) {
        Nation ceiled = nations.get((int) Math.ceil(hue * 360.0));
        if (ceiled != null) {
            return ceiled;
        }

        Nation floored = nations.get((int) Math.floor(hue * 360.0));
        return floored;
    }

    private String getName(Nation nation, float[] hsb) {
        String base = format("<h2>%s</h2>", nation.nick());
        if (nation.master() != null) {
            Nation master = get(nation.master());
            if (master != null) {
                base = format("<h2>%s (Puppet of %s)</h2>", nation.nick(), master.nick());
            }
        }

        if (nation.territories() == null) {
            return base;
        }

        Territory territory = nation.territories().get(Math.round(hsb[1] * 100));
        if (territory == null) {
            return base;
        }

        if (territory.colony()) {
            return format("<h2>%s <i>(Colony of %s)</i></h2>", territory.name(), nation.nick());
        } else {
            return format("<h2>%s <i>(%s)</i></h2>", territory.name(), nation.nick());
        }
    }

    private String getMapLabel(Nation nation, float[] hsb) {
        StringBuilder label = new StringBuilder();
        label.append(getName(nation, hsb));

        if (!nation.members().isEmpty()) {
            label.append("<b>Members</b><br>");
            label.append("<ul>");

            for (UUID user : nation.members()) {
                String username = getOfflinePlayer(user).getName();
                if (user.equals(nation.ruler())) {
                    label.append(format("<li><b>* %s</b></li><br>", username));
                } else {
                    label.append(format("<li>* %s</li><br>", username));
                }
            }

            label.append("</ul>");
        }

        return label.toString();
    }

    public @NotNull Component formatUsername(Player player, @Nullable Component name) {
        net.kyori.adventure.text.Component prefix;
        Nation nation = cache.playerToNation(player);
        if (nation == null) {
            prefix = net.kyori.adventure.text.Component.text("[Uncivilized]");
        } else {
            prefix = net.kyori.adventure.text.Component.text('[', Style.empty()).
                    append(net.kyori.adventure.text.Component.text(nation.nick(), nation.textColor()))
                    .append(net.kyori.adventure.text.Component.text(']', Style.empty()));
        }

        if (name == null) {
            name = player.name();
        }

        return prefix.appendSpace().append(name);
    }

    private void loadImage() {
        File file = new File(plugin.getDataFolder().getPath(), "borders.png");

        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't open the map image file", e);
        }
    }

    public java.awt.Point mapToImage(Location pos) {
        int x = Math.floorDiv(pos.getBlockX(), 16);
        int z = Math.floorDiv(pos.getBlockZ(), 16);

        int height = image.getHeight();
        int width = image.getWidth();

        int imgX = x + width / 2;
        int imgY = z + height / 2;

        return new java.awt.Point(imgX, imgY);
    }

    public Point imageToMap(java.awt.Point pos) {
        int height = image.getHeight();
        int width = image.getWidth();

        int x = (pos.x - width / 2) * 16;
        int z = (pos.y - height / 2) * 16;

        return Point.of(x, z);
    }

    private Color getColor(Location pos) {
        java.awt.Point mapToImage = mapToImage(pos.toBlockLocation());

        if (mapToImage.x < 0 || mapToImage.x >= image.getWidth()) {
            return null;
        }

        if (mapToImage.y < 0 || mapToImage.y >= image.getHeight()) {
            return null;
        }

        return convertToColor(image.getRGB(mapToImage.x, mapToImage.y));
    }
}

