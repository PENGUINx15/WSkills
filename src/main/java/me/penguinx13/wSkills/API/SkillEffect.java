package me.penguinx13.wSkills.API;

import me.penguinx13.wSkills.service.SkillContext;

public abstract class SkillEffect<E extends Enum<E>> {
    private final E id;
    private final double valuePerLevel;
    private final double maxValue;

    protected SkillEffect(E id, double valuePerLevel, double maxValue) {
        this.id = id;
        this.valuePerLevel = valuePerLevel;
        this.maxValue = maxValue;
    }

    public E getId() {
        return id;
    }

    public double getChance(int level) {
        return Math.min(level * valuePerLevel, maxValue);
    }

    public abstract void apply(SkillContext context);
}
