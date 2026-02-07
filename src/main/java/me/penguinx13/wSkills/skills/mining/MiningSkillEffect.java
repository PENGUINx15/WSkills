package me.penguinx13.wSkills.skills.mining;

import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.API.SkillType;
import me.penguinx13.wSkills.service.SkillManager;
import me.penguinx13.wSkills.util.ChanceUtil;
import me.penguinx13.wSkills.util.CoreProtectUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class MiningSkillEffect implements SkillEffect, Listener {

    private static final double DROP_CHANCE_PER_LEVEL = 0.05;
    private static final double DURABILITY_CHANCE_PER_LEVEL = 0.10;

    public static void apply(Player player, BlockBreakEvent event, int level) {
        if (CoreProtectUtil.isPlayerPlaced(event.getBlock())) return;
        double chance = Math.min(1.0, DROP_CHANCE_PER_LEVEL * level);
        if (ChanceUtil.roll(chance) && event.isDropItems()) {
            event.getBlock().getDrops(player.getInventory().getItemInMainHand())
                    .forEach(drop -> event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop.clone()));
        }

        player.giveExp(2 * level);
    }

    public static void apply(PlayerItemDamageEvent event, int level) {
        double chance = Math.min(1.0, DURABILITY_CHANCE_PER_LEVEL * level);
        if (ChanceUtil.roll(chance)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        int level = SkillManager.get().getLevel(player, SkillType.MINING);
        if (level <= 0) {
            return;
        }
        apply(player, event, level);
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        int level = SkillManager.get().getLevel(player, SkillType.MINING);
        if (level <= 0) {
            return;
        }
        apply(event, level);
    }

    @Override
    public void apply(Player player, int level) {

    }
}
