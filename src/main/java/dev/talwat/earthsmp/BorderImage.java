package dev.talwat.earthsmp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.marker.MultiPolygon;
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

        HashMap<Color, ArrayList<List<java.awt.Point>>> boundaries = Trace.TraceShapes(image);

        plugin.getLogger().info("Done Tracing!");

        Squaremap api = SquaremapProvider.get();
        World world = Bukkit.getWorld("world");
        if (world == null) {
            return;
        }
        WorldIdentifier identifier = BukkitAdapter.worldIdentifier(world);

        SimpleLayerProvider provider;

        MapWorld mapWorld = api.getWorldIfEnabled(identifier).orElse(null);
        if (mapWorld == null) {
            return;
        }

        // TODO: Add labels and shit
        Key key = Key.of("borders");
        provider = SimpleLayerProvider.builder("Borders")
                .showControls(true)
                .defaultHidden(false)
                .layerPriority(5)
                .zIndex(250)
                .build();

        mapWorld.layerRegistry().register(key, provider);

        // TODO: These types be nasty AF
        for (Map.Entry<Color, ArrayList<List<java.awt.Point>>> nation : boundaries.entrySet()) {
            for (List<java.awt.Point> shape : nation.getValue()) {
                ArrayList<Point> filtered = new ArrayList();
                ArrayList<MultiPolygon.MultiPolygonPart> points = new ArrayList();

                for (int i = 0; i < shape.size(); i++) {
                    // TODO: Adjust for image offset, like in Events
                    Point p = imageToMap(shape.get(i));

                    if (i >= 1 && i < shape.size() - 1) {
                        if (shape.get(i).y == shape.get(i - 1).y && shape.get(i).x == shape.get(i + 1).x) {
                            continue;
                        }
                    }

                    filtered.add(p);
                }

                plugin.getLogger().info(format("%s", nation.getKey()));

                Polygon marker = Polygon.polygon(filtered);
                marker.markerOptions(marker.markerOptions().asBuilder().fillColor(nation.getKey()));
                provider.addMarker(key, marker);
            }

            plugin.getLogger().info(format("%s", provider.getMarkers()));
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

