package dev.talwat.earthsmp;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static java.lang.String.format;
import static org.bukkit.Bukkit.getServer;

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point add(Point p) {
        return new Point(this.x + p.x, this.y + p.y);
    }

    public Point sub(Point p) {
        return new Point(this.x - p.x, this.y - p.y);
    }
}

public class SquareTracing {
    public static void GetBoundaries(BufferedImage image)
    {
        HashMap<Color, ArrayList<Point[]>> shapes = new HashMap();
        for (int j = 0; j < image.getWidth(); j++)
            for (int i = 0; i < image.getHeight(); i++) {
                Color pixel = new Color(image.getRGB(i, j));

                if (pixel.transparent()) {
                    continue;
                }

                Point[] shape = SquareTrace(new Point(i, j), pixel, image);
                shapes.putIfAbsent(pixel, new ArrayList<Point[]>());
                shapes.get(pixel).add(shape);

                getServer().getLogger().info(String.valueOf(shapes));
            }
    }

    static Point[] SquareTrace(Point start, Color target, BufferedImage image)
    {
        getServer().getLogger().info(format("Square Trace for %s", target.toString()));
        getServer().getLogger().info(format("Start: %s", start.toString()));
        HashSet<Point> boundaryPoints = new HashSet<Point>();
        boundaryPoints.add(start);

        Point nextStep = GoLeft(new Point(1, 0));
        Point next = start.add(nextStep);
        while (next != start)
        {
            if (new Color(image.getRGB(next.x, next.y)).equals(target))
            {
                boundaryPoints.add(next);
                nextStep = GoLeft(nextStep);
                next = next.add(nextStep);
            }
        else
            {
                next = next.sub(nextStep);
                nextStep = GoRight(nextStep);
                next = next.sub(nextStep);
            }
        }

        Point[] arr = new Point[boundaryPoints.size()];
        boundaryPoints.toArray(arr);

        return arr;
    }

    private static Point GoLeft(Point p) {
        return new Point(-p.x, p.y);
    }
    private static Point GoRight(Point p) {
        return new Point(p.x, -p.y);
    }
}
