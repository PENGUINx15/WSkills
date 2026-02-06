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

        double bonus = level * 0.002;
        attr.setBaseValue(BASE_SPEED + bonus);
    }
}
