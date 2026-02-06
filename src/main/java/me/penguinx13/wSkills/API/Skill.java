package me.penguinx13.wSkills.API;

import java.util.List;

public interface Skill {

    SkillType getType();

    int getMaxLevel();

    double getValuePerLevel();

    List<SkillEffect> getEffects();
}
