package me.penguinx13.wSkills.service;

import me.penguinx13.wSkills.API.PlayerSkills;
import me.penguinx13.wSkills.API.Skill;
import me.penguinx13.wSkills.API.SkillID;

public class SkillApplier {
    private static SkillApplier instance;

    public SkillApplier() {
        instance = this;
    }
    public void applyAll(SkillContext context) {
        PlayerSkills ps = SkillManager.get().getPlayerSkills(context.getPlayer());
        if (ps == null) return;

        for (Skill skill : SkillManager.get().getAllSkills()) {
            skill.getEffects().forEach(effect -> effect.apply(context));
        }
    }

    public void applySkill(SkillID type, SkillContext context) {
        Skill skill = SkillManager.get().getSkill(type);
        if (skill == null) return;

        skill.getEffects().forEach(effect -> effect.apply(context));
    }

    public static SkillApplier get() {
        return instance;
    }
}
