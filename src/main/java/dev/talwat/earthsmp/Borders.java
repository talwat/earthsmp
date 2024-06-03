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
import java.util.*;
import java.util.List;
import java.util.logging.Level;

import static dev.talwat.earthsmp.ParseUtils.getUUIDs;
import static java.lang.String.format;

class ParseUtils {
    static UUID[] getUUIDs(String[] args) {
        UUID[] uuids = (UUID[]) Arrays.stream(args).map(UUID::fromString).toArray();
        return uuids;
    }
}

class Territory {
    public String tag;
    public String name;
    public UUID[] members;
    public int saturation;
    public boolean colony;

    public Territory(String tag, String name, int saturation, UUID[] members, boolean colony) {
        this.tag = tag;
        this.name = name;
        this.members = members;
        this.saturation = saturation;
        this.colony = colony;
    }

    public static Territory deserialize(Map<String, Object> args) {
        return new Territory(
                (String) args.get("tag"),
                (String) args.get("name"),
                (int) args.get("saturation"),
                getUUIDs((String[])args.get("members")),
                (boolean) args.get("colony")
        );
    }
}

class Nation {
    public String tag;
    public String name;
    public String nick;
    public int hue;
    public UUID[] members;
    public Territory[] territories;

    public Nation(String tag, String name, String nick, int hue, UUID[] members, Territory[] territories) {
        this.tag = tag;
        this.name = name;
        this.nick = nick;
        this.hue = hue;
        this.members = members;
        this.territories = territories;
    }

    public static Nation deserialize(Map<String, Object> args) {
        String[] rawUuids = (String[]) args.get("members");
        UUID[] uuids = (UUID[]) Arrays.stream(rawUuids).map(UUID::fromString).toArray();

        List<Map<String, Object>> rawTerritories = (List<Map<String, Object>>) args.get("territories");
        Territory[] territories = (Territory[]) rawTerritories.stream().map(Territory::deserialize).toArray();
        return new Nation(
                (String) args.get("tag"),
                (String) args.get("name"),
                (String) args.get("nick"),
                (int) args.get("color"),
                uuids,
                territories
        );
    }
}

public class Borders {
    private BufferedImage image;
    private final Earthsmp plugin;
    private Nation[] nations;

    private Nation[] loadNations() {
        File file = new File(plugin.getDataFolder(), "nations.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        return (Nation[]) config.getMapList(".").stream().map(x -> {
            return Nation.deserialize((Map<String, Object>) x);
        }).toArray();
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

    public Borders(Earthsmp plugin) {
        this.plugin = plugin;
        this.image = loadImage();

        HashMap<Color, List<List<java.awt.Point>>> boundaries = Shapes.TraceShapes(image);
        plugin.getLogger().info("Done Tracing!");

        SimpleLayerProvider layerProvider = setupLayerProvider();
        if (layerProvider == null) {
            return;
        }

        int i = 0;
        for (Map.Entry<Color, List<List<java.awt.Point>>> nation : boundaries.entrySet()) {
            for (List<java.awt.Point> shape : nation.getValue()) {
                ArrayList<Point> filtered = new ArrayList<>();

                for (java.awt.Point point : shape) {
                    Point p = imageToMap(point);
                    filtered.add(p);
                }

                plugin.getLogger().info(format("Found shape with color %s", nation.getKey()));

                Polygon marker = Polygon.polygon(filtered);
                marker.markerOptions(marker.markerOptions().asBuilder().fillColor(nation.getKey()).strokeColor(nation.getKey().brighter()));
                layerProvider.addMarker(Key.key(format("borders_%s", i)), marker);

                i++;
            }
        }

        plugin.getLogger().info(plugin.getConfig().getString("test"));
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

    private static Color convertToColor(int rgb) {
        int a = (rgb >> 24) & 0xFF;
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb) & 0xFF;

        return new Color(r, g, b, a);
    }

    public Color getColor(Location pos) {
        java.awt.Point mapToImage = mapToImage(pos);
        return convertToColor(image.getRGB(mapToImage.x, mapToImage.y));
    }
}

