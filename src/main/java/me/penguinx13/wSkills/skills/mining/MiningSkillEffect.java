package me.penguinx13.wSkills.skills.mining;

import me.penguinx13.wSkills.API.Skill;
import me.penguinx13.wSkills.API.SkillEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class MiningSkillEffect implements SkillEffect {

    public static void apply(Player player, BlockBreakEvent event, int level) {
        double chance = Math.min(1.0, 0.05 * level);
        if (Math.random() < chance && event.isDropItems()) {
            event.getBlock().getDrops(player.getInventory().getItemInMainHand())
                    .forEach(drop -> event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop.clone()));
        }

        player.giveExp(2 * level);
    }

    public static void apply(Player player, PlayerItemDamageEvent event, int level) {
        double chance = Math.min(1.0, 0.10 * level);
        if (Math.random() < chance) {
            event.setCancelled(true);
        }
    }

    @Override
    public void apply(Player player, int level) {

    }
}
