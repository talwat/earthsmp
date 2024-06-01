package dev.talwat.earthsmp;

public class Color {
    public int r;
    public int g;
    public int b;
    public int a;

    Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    Color(int x) {
        r = (x      ) & 0xFF;;
        g = (x >>  8) & 0xFF;
        b = (x >> 16) & 0xFF;
        a = (x >> 24) & 0xFF;
    }

    public boolean transparent() {
        return this.equals(new Color(0, 0, 0, 0));
    }
}
