package dev.talwat.earthsmp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trace {
    public static HashMap<Color, ArrayList<List<Point>>> TraceShapes(BufferedImage image) {
        HashMap<Color, ArrayList<List<Point>>> shapes = new HashMap<>();
        main: for (int i = 0; i < image.getWidth(); i++) {
            for (int j = image.getHeight()-1; j > 0; j--) {
                Color pixel = new Color(image.getRGB(i, j));

                if (pixel.getAlpha() == 0 || shapes.containsKey(pixel)) {
                    continue;
                }

                List<Point> shape = Trace(new Point(i, j), pixel, image);
                shapes.putIfAbsent(pixel, new ArrayList<>());
                shapes.get(pixel).add(shape);
            }
        }

        return shapes;
    }

    private static int TurnLeft(int orientation) {
        switch (orientation) {
            case 0:
                orientation = 1;
                break;
            case 1:
                orientation = 3;
                break;
            case 2:
                orientation = 0;
                break;
            case 3:
                orientation = 2;
                break;
        }

        return orientation;
    }

    private static int TurnRight(int orientation) {
        switch (orientation) {
            case 0:
                orientation = 2;
                break;
            case 1:
                orientation = 0;
                break;
            case 2:
                orientation = 3;
                break;
            case 3:
                orientation = 1;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + orientation);
        }

        return orientation;
    }

    private static boolean isWithinBounds(Point p, BufferedImage image) {
        return p.x >= 0 && p.x < image.getWidth() && p.y >= 0 && p.y < image.getHeight();
    }

    static List<Point> Trace(Point s, Color target, BufferedImage image) {
        ArrayList<Point> b = new ArrayList<>();

        // TODO: Make this an enum
        int orientation = 0; // Up: 0, Left: 1, Right: 2, Down: 3

        b.add(s);
        Point p = (Point) s.clone();
        p.x--;

        while (!p.equals(s)) {
            switch (orientation) {
                case 0:
                    p.y--;
                    break;
                case 1:
                    p.x--;
                    break;
                case 2:
                    p.x++;
                    break;
                case 3:
                    p.y++;
                    break;
            }

            if (isWithinBounds(p, image) && new Color(image.getRGB(p.x, p.y)).equals(target)) {
                b.add((Point) p.clone());
                orientation = TurnLeft(orientation);
            } else {
                orientation = TurnRight(orientation);
            }
        }

        return b;
    }
}
