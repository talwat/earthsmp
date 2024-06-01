package dev.talwat.earthsmp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.marker.Polygon;

import javax.imageio.ImageIO;
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

                for (int i = 0; i < shape.size(); i++) {
                    // TODO: Adjust for image offset, like in Events
                    Point p = Point.of(shape.get(i).x * 16, shape.get(i).y * 16);

                    if (i >= 1) {
                        // TODO: Invert this because it should show control accurately
                        if (shape.get(i - 1).y == shape.get(i).y && shape.get(i + 1).x == shape.get(i).x) {
                            continue;
                        }
                    }

                    plugin.getLogger().info(format("%f, %f", p.x(), p.z()));

                    filtered.add(p);
                }

                Polygon marker = Polygon.polygon(filtered);
                provider.addMarker(key, marker);
            }
        }

        plugin.getLogger().info(plugin.getConfig().getString("test"));
    }

    public Color getColor(Location pos) {
        int x = Math.floorDiv(pos.getBlockX(), 16);
        int z = Math.floorDiv(pos.getBlockZ(), 16);

        int height = image.getHeight();
        int width = image.getWidth();

        int imgX = x + width / 2;
        int imgY = z + height / 2;

        return new Color(image.getRGB(imgX, imgY));
    }
}

