package me.penguinx13.wSkills.service;

import me.penguinx13.wSkills.API.Skill;
import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.API.SkillID;
import org.bukkit.entity.Player;

public class SkillApplier {
    private static SkillApplier instance;

    public SkillApplier() {
        instance = this;
    }

    public void applyAll(Player player) {
        if (SkillManager.get().getPlayerSkills(player) == null) {
            return;
        }

        for (Skill skill : SkillManager.get().getAllSkills()) {
            int level = SkillManager.get().getLevel(player, skill.getType());
            applySkill(skill, new SkillContext(player, level, null));
        }
    }

    public void applySkill(Player player, SkillID type) {
        int level = SkillManager.get().getLevel(player, type);
        applySkill(type, new SkillContext(player, level, null));
    }

    public void applySkill(SkillID type, SkillContext context) {
        Skill skill = SkillManager.get().getSkill(type);
        if (skill == null) {
            return;
        }

        applySkill(skill, context);
    }

    private void applySkill(Skill skill, SkillContext context) {
        for (SkillEffect<?> effect : skill.getEffects()) {
            effect.apply(context);
        }
    }

    public static SkillApplier get() {
        return instance;
    }
}
