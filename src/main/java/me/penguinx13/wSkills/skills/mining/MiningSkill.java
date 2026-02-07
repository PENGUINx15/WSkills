package me.penguinx13.wSkills.skills.mining;

import me.penguinx13.wSkills.API.Skill;
import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.API.SkillType;

import java.util.List;

public class MiningSkill implements Skill {

    private final List<SkillEffect> effects = List.of(
            new MiningSkillEffect()
    );

    @Override
    public SkillType getType() {
        return SkillType.MINING;
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
