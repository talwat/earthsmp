package dev.talwat.earthsmp;

import dev.talwat.earthsmp.nations.Nation;
import dev.talwat.earthsmp.nations.NationsConfig;
import dev.talwat.earthsmp.nations.Territory;
import org.bukkit.Location;
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

public class Borders {
    private final Earthsmp plugin;
    // The key is also the `hue` value.
    public Map<Integer, Nation> nations;
    public Map<UUID, Nation> playerCache;
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
        this.playerCache = new HashMap<>();

        if (this.image == null || this.nations == null) {
            return;
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

    public Nation getNationFromLocation(Location pos) {
        Color color = plugin.borders.getColor(pos.toBlockLocation());

        if (IsGrayScale(color) || color.getAlpha() == 0) {
            return null;
        }

        float[] hsv = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

        return plugin.borders.nations.get(Math.round(hsv[0] * 360));
    }

    private String getName(Nation nation, float[] hsb) {
        String base = format("<h2>%s</h2>", nation.nick());

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
        return convertToColor(image.getRGB(mapToImage.x, mapToImage.y));
    }
}

