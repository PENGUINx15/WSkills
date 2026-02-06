package me.penguinx13.wSkills.skills.agility;

import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.API.SkillType;
import me.penguinx13.wSkills.service.SkillManager;
import me.penguinx13.wSkills.util.ChanceUtil;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class AgilityDodgeEffect implements SkillEffect, Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        int level = SkillManager.get().getLevel(player, SkillType.AGILITY);
        double chance = Math.min(0.25, level * 0.025);

        if (ChanceUtil.roll(chance)) {
            event.setCancelled(true);
            player.getWorld().spawnParticle(
                    Particle.CLOUD,
                    player.getLocation().add(0, 1, 0),
                    10
            );
        }
    }

    @Override
    public void apply(Player player, int level) {

    }
}
