package me.penguinx13.wSkills.skills.agility;

import me.penguinx13.wSkills.API.Skill;
import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.API.SkillID;
import me.penguinx13.wSkills.skills.agility.effects.AgilityDodgeEffect;
import me.penguinx13.wSkills.skills.agility.effects.AgilitySpeedEffect;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public class AgilitySkill implements Skill {

    private final Map<AgilityEffectID, SkillEffect<AgilityEffectID>> effects = new EnumMap<>(AgilityEffectID.class);

    public AgilitySkill() {
        effects.put(AgilityEffectID.SPEED, new AgilitySpeedEffect());
        effects.put(AgilityEffectID.DODGE, new AgilityDodgeEffect());
    }

    public SkillEffect<AgilityEffectID> getEffect(AgilityEffectID id) {
        return effects.get(id);
    }

    @Override
    public SkillID getType() {
        return SkillID.AGILITY;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public Collection<? extends SkillEffect<?>> getEffects() {
        return effects.values();
    }
}
