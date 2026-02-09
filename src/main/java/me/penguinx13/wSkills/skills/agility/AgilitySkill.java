package me.penguinx13.wSkills.skills.agility;

import me.penguinx13.wSkills.API.Skill;
import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.API.SkillID;
import me.penguinx13.wSkills.skills.agility.effects.AgilityDodgeEffect;
import me.penguinx13.wSkills.skills.agility.effects.AgilitySpeedEffect;

import java.util.List;

public class AgilitySkill implements Skill {

    private final List<SkillEffect> effects = List.of(
            new AgilitySpeedEffect(),
            new AgilityDodgeEffect()
    );

    @Override
    public SkillID getType() {
        return SkillID.AGILITY;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public double getValuePerLevel() {
        return 0.1;
    }

    @Override
    public List<SkillEffect> getEffects() {
        return effects;
    }
}
