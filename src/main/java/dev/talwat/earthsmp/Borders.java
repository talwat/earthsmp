package dev.talwat.earthsmp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.marker.Polygon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.logging.Level;

import static dev.talwat.earthsmp.ParseUtils.getUUIDs;
import static java.lang.String.format;
import static org.bukkit.Bukkit.getOfflinePlayer;
import static org.bukkit.Bukkit.getServer;

class ParseUtils {
    static UUID[] getUUIDs(List<String> args) {
        return args.stream().map(UUID::fromString).toArray(UUID[]::new);
    }
}

class Territory {
    public String tag;
    public String name;
    public int saturation;
    public UUID ruler;
    public UUID[] members;
    public boolean colony;

    public Territory(String tag, String name, int saturation, UUID ruler, UUID[] members, boolean colony) {
        this.tag = tag;
        this.name = name;
        this.saturation = saturation;
        this.ruler = ruler;
        this.members = members;
        this.colony = colony;
    }

    public static Territory deserialize(Map<String, Object> args) {
        return new Territory(
                (String) args.get("tag"),
                (String) args.get("name"),
                (int) args.get("color"),
                UUID.fromString((String) args.get("ruler")),
                getUUIDs((List<String>) args.get("members")),
                (boolean) args.get("colony")
        );
    }
}

class Nation {
    public String tag;
    public String name;
    public String nick;
    public int hue;
    public UUID ruler;
    public UUID[] members;
    public Map<Integer, Territory> territories;

    public Nation(String tag, String name, String nick, int hue, UUID ruler, UUID[] members, Map<Integer, Territory> territories) {
        this.tag = tag;
        this.name = name;
        this.nick = nick;
        this.hue = hue;
        this.ruler = ruler;
        this.members = members;
        this.territories = territories;
    }

    public static Nation deserialize(Map<String, Object> args) {
        getServer().getLogger().info(format("%s", args.get("members")));
        UUID[] uuids = getUUIDs((List<String>) args.get("members"));

        Map<Integer, Territory> territories = new HashMap<>();
        Object rawTerritories = args.get("territories");

        if (rawTerritories != null) {
            for (Map<String, Object> raw : (List<Map<String, Object>>) rawTerritories) {
                Territory territory = Territory.deserialize(raw);
                territories.put(territory.saturation, territory);
            }
        }

        return new Nation(
                (String) args.get("tag"),
                (String) args.get("name"),
                (String) args.get("nick"),
                (int) args.get("color"),
                UUID.fromString((String) args.get("ruler")),
                uuids,
                territories
        );
    }
}

public class Borders {
    private final Earthsmp plugin;
    // The key is also the `hue` value.
    private final Map<Integer, Nation> nations;
    private BufferedImage image;

    public Borders(Earthsmp plugin) {
        this.plugin = plugin;
        this.image = loadImage();
        this.nations = loadNations();

        HashMap<Color, List<java.awt.Point>> boundaries = Shapes.TraceShapes(image);
        plugin.getLogger().info("Done Tracing!");

        SimpleLayerProvider layerProvider = setupLayerProvider();
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

            plugin.getLogger().info(format("Found shape with color %s", color));

            plugin.getLogger().info(format("HSB: %f, %f, %f", hsb[0], hsb[1], hsb[2]));
            plugin.getLogger().info(format("HSB: %f, %f, %f", hsb[0] * 360, hsb[1] * 100, hsb[2] * 100));
            plugin.getLogger().info(format("Nations: %s", nations));

            Nation nation = nations.get(Math.round(hsb[0] * 360));

            if (nation == null) {
                continue;
            }

            Polygon marker = Polygon.polygon(filtered);
            marker.markerOptions(
                    marker.markerOptions()
                            .asBuilder()
                            .fillColor(color)
                            .strokeColor(color.brighter())
                            .clickTooltip(getLabel(nation, hsb))
            );
            layerProvider.addMarker(Key.key(format("borders_%s", i)), marker);

            i++;
        }

        plugin.getLogger().info(plugin.getConfig().getString("test"));
    }

    private static Color convertToColor(int rgb) {
        int a = (rgb >> 24) & 0xFF;
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb) & 0xFF;

        return new Color(r, g, b, a);
    }

    private Map<Integer, Nation> loadNations() {
        File file = new File(plugin.getDataFolder(), "nations.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        Map<Integer, Nation> nations = new HashMap<>();
        for (Map<?, ?> raw : config.getMapList("nations")) {
            Nation nation = Nation.deserialize((Map<String, Object>) raw);
            nations.put(nation.hue, nation);
        }

        return nations;
    }

    private String getLabel(Nation nation, float[] hsb) {
        StringBuilder label = new StringBuilder();

        Territory territory = nation.territories.get(Math.round(hsb[1] * 100));
        plugin.getLogger().info(format("%s", territory));

        if (territory != null) {
            label.append(format("<h2>%s <i>(%s)</i></h2>", territory.name, nation.nick));

            if (territory.colony) {
                label.append("<b>Inhabitants</b><br>");
                label.append("<ul>");
                for (UUID user : territory.members) {
                    if (user == territory.ruler) {
                        label.append(format("<li><b>* %s</b></li><br>", getOfflinePlayer(user).getName()));
                    } else {
                        label.append(format("<li>* %s</li><br>", getOfflinePlayer(user).getName()));
                    }
                }
                label.append("</ul>");
            }
        } else {
            label.append(format("<h2>%s</h2>", nation.nick));
        }

        if (territory == null || !territory.colony) {
            label.append("<b>Members</b><br>");
            label.append("<ul>");
            for (UUID user : nation.members) {
                String username = getOfflinePlayer(user).getName();
                plugin.getLogger().info(format("%s ?= %s", user, nation.ruler));
                if (user.equals(nation.ruler)) {
                    label.append(format("<li><b>* %s</b></li><br>", username));
                } else {
                    label.append(format("<li>* %s</li><br>", username));
                }
            }
            label.append("</ul>");
        }

        return label.toString();
    }

    private BufferedImage loadImage() {
        File file = new File(plugin.getDataFolder().getPath(), "borders.png");

        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't open the map image file", e);
        }

        return image;
    }

    private SimpleLayerProvider setupLayerProvider() {
        Squaremap api = SquaremapProvider.get();

        World world = Bukkit.getWorld("world");
        if (world == null) {
            return null;
        }
        WorldIdentifier identifier = BukkitAdapter.worldIdentifier(world);
        MapWorld mapWorld = api.getWorldIfEnabled(identifier).orElse(null);
        if (mapWorld == null) {
            return null;
        }

        Key key = Key.key("borders");

        if (mapWorld.layerRegistry().hasEntry(key)) {
            mapWorld.layerRegistry().unregister(key);
        }

        // TODO: Add labels and shit
        SimpleLayerProvider layerProvider = SimpleLayerProvider.builder("Borders")
                .showControls(true)
                .defaultHidden(false)
                .layerPriority(5)
                .zIndex(250)
                .build();

        mapWorld.layerRegistry().register(key, layerProvider);

        layerProvider.clearMarkers();

        return layerProvider;
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

    public Color getColor(Location pos) {
        java.awt.Point mapToImage = mapToImage(pos);
        return convertToColor(image.getRGB(mapToImage.x, mapToImage.y));
    }
}

