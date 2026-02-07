package me.penguinx13.wSkills.service;

import me.penguinx13.wSkills.API.PlayerSkills;
import me.penguinx13.wSkills.API.Skill;
import me.penguinx13.wSkills.API.SkillType;
import org.bukkit.entity.Player;

public class SkillApplier {
    private static SkillApplier instance;

    public SkillApplier() {
        instance = this;
    }
    public void applyAll(Player player) {
        PlayerSkills ps = SkillManager.get().getPlayerSkills(player);
        if (ps == null) return;

        for (Skill skill : SkillManager.get().getAllSkills()) {
            int level = ps.getLevel(skill.getType());
            skill.getEffects().forEach(effect -> effect.apply(player, level));
        }
    }

    public void applySkill(Player player, SkillType type) {
        Skill skill = SkillManager.get().getSkill(type);
        if (skill == null) return;

        int level = SkillManager.get().getLevel(player, type);
        skill.getEffects().forEach(effect -> effect.apply(player, level));
    }
    public static SkillApplier get() {
        return instance;
    }
}
