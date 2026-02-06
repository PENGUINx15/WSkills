package me.penguinx13.wSkills.util;

import java.util.concurrent.ThreadLocalRandom;

public class ChanceUtil {

    public static boolean roll(double chance) {
        if (chance <= 0) return false;
        if (chance >= 1) return true;
        return ThreadLocalRandom.current().nextDouble() < chance;
    }
}
