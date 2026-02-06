package me.penguinx13.wSkills.util;

public class MathUtil {

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double diminishing(double base, double factor, int level) {
        return base * (1 - Math.exp(-level * factor));
    }
}
