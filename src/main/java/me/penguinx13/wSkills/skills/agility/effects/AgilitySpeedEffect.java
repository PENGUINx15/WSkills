package me.penguinx13.wSkills.skills.agility.effects;

import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.service.SkillContext;
import org.bukkit.attribute.Attribute;

public class AgilitySpeedEffect implements SkillEffect {

    private static final double BASE_SPEED = 0.1;

    @Override
    public void apply(SkillContext context) {
        var attr = context.getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (attr == null) return;

        int cappedLevel = Math.min(context.getLevel(),5);
        double multiplier = 1.0 + (cappedLevel / 5.0) * 0.5;
        attr.setBaseValue(BASE_SPEED * multiplier);
    }
}
