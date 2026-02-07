package me.penguinx13.wSkills.skills.minning;

import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.API.SkillType;
import me.penguinx13.wSkills.service.SkillManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class MinningSkillEffect implements SkillEffect, Listener {

    private final Random random = new Random();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        int level = SkillManager.get().getLevel(player, SkillType.MINNING);

        if (level <= 0) return;

        double doubleDropChance = 0.05 * level;
        if (random.nextDouble() < doubleDropChance) {
            ItemStack drop = new ItemStack(event.getBlock().getType());
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
        }

        int extraXp = 2 * level;
        SkillManager.get().addXp(player, SkillType.MINNING, extraXp);
    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        int level = SkillManager.get().getLevel(player, SkillType.MINNING);
        if (level <= 0) return;

        double chance = 0.10 * level;
        if (random.nextDouble() < chance) {
            event.setCancelled(true);
        }
    }

    @Override
    public void apply(Player player, int level) {

    }

    @Override
    public void remove(Player player) {

    }
}
