package dev.talwat.earthsmp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.marker.Polygon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static java.lang.String.format;

public class BorderImage {
    private BufferedImage image;

    public BorderImage(Earthsmp plugin) {
        File file = new File(plugin.getDataFolder().getPath(), "borders.png");

        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't open the map image file", e);
        }

        HashMap<Color, List<List<java.awt.Point>>> boundaries = Shapes.TraceShapes(image);
        plugin.getLogger().info("Done Tracing!");

        Squaremap api = SquaremapProvider.get();

        World world = Bukkit.getWorld("world");
        if (world == null) {
            return;
        }
        WorldIdentifier identifier = BukkitAdapter.worldIdentifier(world);
        MapWorld mapWorld = api.getWorldIfEnabled(identifier).orElse(null);
        if (mapWorld == null) {
            return;
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

