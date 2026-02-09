package me.penguinx13.wSkills.API;

import java.util.List;

public interface Skill {

    SkillID getType();

    int getMaxLevel();

    List<SkillEffect> getEffects();
}
