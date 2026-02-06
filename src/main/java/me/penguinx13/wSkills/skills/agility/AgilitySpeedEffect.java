package me.penguinx13.wSkills.skills.agility;

import me.penguinx13.wSkills.API.SkillEffect;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class AgilitySpeedEffect implements SkillEffect {

    private static final double BASE_SPEED = 0.1;

    @Override
    public void apply(Player player, int level) {
        var attr = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (attr == null) return;

        int cappedLevel = Math.min(level, 5);
        double multiplier = 1.0 + (cappedLevel / 5.0) * 0.5;
        attr.setBaseValue(BASE_SPEED * multiplier);
    }
}
