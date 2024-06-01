package dev.talwat.earthsmp;

import org.bukkit.Location;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import static java.lang.String.format;
import static org.bukkit.Bukkit.getServer;

public class BorderImage {
    private BufferedImage image;

    public Color getColor(Location pos) {
        int x = Math.floorDiv(pos.getBlockX(), 16);
        int z = Math.floorDiv(pos.getBlockZ(), 16);

        int height = image.getHeight();
        int width = image.getWidth();

        int imgX = x + width/2;
        int imgY = z + height/2;

        getServer().getLogger().info(format("%d, %d", x, z));
        getServer().getLogger().info(format("%d, %d", imgX, imgY));

        return new Color(image.getRGB(imgX, imgY));
    }

    public BorderImage(Earthsmp plugin) {
        File file = new File(plugin.getDataFolder().getPath(), "borders.png");

        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't open the map image file", e);
        }

        SquareTracing.GetBoundaries(image);

        plugin.getLogger().info(plugin.getConfig().getString("test"));
    }
}

