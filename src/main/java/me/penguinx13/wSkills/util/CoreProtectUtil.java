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
        List<String[]> lookup = api.blockLookup(block, 2);
        if (lookup.isEmpty()) {
            return false;
        }
        if (isAction(lookup.get(0), "block-place")) {
            return true;
        }
        return lookup.size() > 1
                && isAction(lookup.get(0), "block-break")
                && isAction(lookup.get(1), "block-place");
    }

    private static boolean isAction(String[] data, String action) {
        return data.length > 0 && action.equalsIgnoreCase(data[0]);
    }
}
