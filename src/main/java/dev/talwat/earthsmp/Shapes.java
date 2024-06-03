package dev.talwat.earthsmp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static dev.talwat.earthsmp.ContourTracing.trace;

class ContourTracing {
    // Directions in clockwise order: up, right, down, left
    private static final int[][] DIRECTIONS = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

    private static int turnLeft(int orientation) {
        return (orientation + 3) % 4;
    }

    private static int turnRight(int orientation) {
        return (orientation + 1) % 4;
    }

    private static boolean isWithinBounds(Point p, BufferedImage image) {
        return p.x >= 0 && p.x < image.getWidth() && p.y >= 0 && p.y < image.getHeight();
    }

    private static Point getNextPoint(Point p, int orientation) {
        return new Point(p.x + DIRECTIONS[orientation][0], p.y + DIRECTIONS[orientation][1]);
    }

    public static List<Point> trace(Point start, Color target, BufferedImage image) {
        List<Point> boundary = new ArrayList<>();
        Point current = new Point(start);
        int orientation = 0; // Start by moving up

        do {
            boundary.add(new Point(current));
            boolean foundNext = false;

            // Try to find the next boundary pixel
            for (int i = 0; i < 4; i++) {
                Point next = getNextPoint(current, orientation);
                if (isWithinBounds(next, image) && new Color(image.getRGB(next.x, next.y)).equals(target)) {
                    current = next;
                    orientation = turnLeft(orientation); // Turn left after finding the target
                    foundNext = true;
                    break;
                }

                orientation = turnRight(orientation); // Turn right if no target found
            }

            if (!foundNext) {
                break; // No next point found, stop the loop
            }
        } while (!current.equals(start) || boundary.size() == 1);

        return boundary;
    }
}

public class Shapes {
    public static HashMap<Color, List<List<Point>>> TraceShapes(BufferedImage image) {
        HashMap<Color, List<List<Point>>> shapes = new HashMap<>();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = image.getHeight()-1; j > 0; j--) {
                Color pixel = new Color(image.getRGB(i, j));

                // Grayscale is treated as a "comment"
                if (pixel.getAlpha() == 0 || shapes.containsKey(pixel) || (pixel.getRed() == pixel.getGreen() && pixel.getGreen() == pixel.getBlue())) {
                    continue;
                }

                List<Point> shape = trace(new Point(i, j), pixel, image);
                shapes.putIfAbsent(pixel, new ArrayList<>());
                shapes.get(pixel).add(shape);
            }
        }

        return shapes;
    }
}