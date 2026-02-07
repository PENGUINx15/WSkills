package me.penguinx13.wSkills.skills.minning;

import me.penguinx13.wSkills.API.SkillEffect;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MinningHasteEffect implements SkillEffect {

    private static final int EFFECT_DURATION_TICKS = 20 * 60 * 60;

    @Override
    public void apply(Player player, int level) {
        if (level <= 0) {
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
            return;
        }

        int amplifier = Math.min(2, (level - 1) / 2);
        PotionEffect effect = new PotionEffect(
                PotionEffectType.FAST_DIGGING,
                EFFECT_DURATION_TICKS,
                amplifier,
                true,
                false,
                true
        );
        player.addPotionEffect(effect);
    }
}
