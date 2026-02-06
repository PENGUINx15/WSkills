package me.penguinx13.wSkills.skills.agility;

import me.penguinx13.wSkills.API.Skill;
import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.API.SkillType;

import java.util.List;

public class AgilitySkill implements Skill {

    private final List<SkillEffect> effects = List.of(
            new AgilitySpeedEffect(),
            new AgilityDodgeEffect()
    );

    @Override
    public SkillType getType() {
        return SkillType.AGILITY;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public double getValuePerLevel() {
        return 0.05; // 5%
    }

    @Override
    public List<SkillEffect> getEffects() {
        return effects;
    }
}
