//package dev.talwat.earthsmp;
//
//import static java.lang.String.format;
//
//public class Color {
//    public int r;
//    public int g;
//    public int b;
//    public int a;
//
//    Color(int r, int g, int b, int a) {
//        this.r = r;
//        this.g = g;
//        this.b = b;
//        this.a = a;
//    }
//
//    Color(int rgb) {
//        a = (rgb >> 24) & 0xFF;
//        r = (rgb >> 16) & 0xFF;
//        g = (rgb >> 8) & 0xFF;
//        b = (rgb) & 0xFF;
//    }
//
//    public boolean transparent() {
//        return this.a == 0;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        Color color = (Color) o;
//        return r == color.r && g == color.g && b == color.b;
//    }
//
//    @Override
//    public String toString() {
//        return format("(%d/%d/%d/%d)", r, g, b, a);
//    }
//}
