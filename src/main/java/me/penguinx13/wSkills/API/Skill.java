package me.penguinx13.wSkills.API;

import java.util.Collection;

public interface Skill {

    SkillID getType();

    int getMaxLevel();

    Collection<? extends SkillEffect<?>> getEffects();
}
