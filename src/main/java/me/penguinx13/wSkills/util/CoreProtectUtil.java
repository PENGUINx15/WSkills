package me.penguinx13.wSkills.util;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import java.util.List;

public final class CoreProtectUtil {

    private static CoreProtectAPI api;

    public static void init() {
        if (Bukkit.getPluginManager().getPlugin("CoreProtect") instanceof CoreProtect cp) {
            api = cp.getAPI();
        }
    }

    public static boolean isPlayerPlaced(Block block) {
        if (api == null) return false;
        List<String[]> lookup = api.blockLookup(block, 1);
        for (String[] data : lookup) {
            if ("block-place".equalsIgnoreCase(data[0])) {
                return true;
            }
        }
        return false;
    }
}
