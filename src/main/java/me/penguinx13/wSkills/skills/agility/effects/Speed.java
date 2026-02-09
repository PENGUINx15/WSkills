package me.penguinx13.wSkills.skills.agility.effects;

import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.service.SkillContext;
import me.penguinx13.wSkills.skills.agility.AgilityEffectID;
import org.bukkit.attribute.Attribute;

public class Speed extends SkillEffect<AgilityEffectID> {

    private static final double BASE_SPEED = 0.1;

    public Speed() {
        super(AgilityEffectID.SPEED, 0.1, 0.5);
    }

    @Override
    public void apply(SkillContext context) {
        var attr = context.getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (attr == null) return;

        double multiplier = 1.0 + getValue(context.getLevel());
        attr.setBaseValue(BASE_SPEED * multiplier);
    }
}
