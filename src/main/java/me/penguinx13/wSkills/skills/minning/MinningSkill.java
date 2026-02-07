package me.penguinx13.wSkills.skills.minning;

import me.penguinx13.wSkills.API.Skill;
import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.API.SkillType;

import java.util.List;

public class MinningSkill implements Skill {

    private final List<SkillEffect> effects = List.of(
            new MinningHasteEffect()
    );

    @Override
    public SkillType getType() {
        return SkillType.MINNING;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public double getValuePerLevel() {
        return 0.15;
    }

    @Override
    public List<SkillEffect> getEffects() {
        return effects;
    }
}
