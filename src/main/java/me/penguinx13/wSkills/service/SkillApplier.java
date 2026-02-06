package me.penguinx13.wSkills.service;

import ru.yourplugin.skills.api.*;
import org.bukkit.entity.Player;

public class SkillApplier {

    private final SkillManager manager;

    public SkillApplier(SkillManager manager) {
        this.manager = manager;
    }

    public void applyAll(Player player) {
        PlayerSkills ps = manager.getPlayerSkills(player);
        if (ps == null) return;

        for (Skill skill : manager.getAllSkills()) {
            int level = ps.getLevel(skill.getType());
            skill.getEffects().forEach(effect -> effect.apply(player, level));
        }
    }

    public void applySkill(Player player, SkillType type) {
        Skill skill = manager.getSkill(type);
        if (skill == null) return;

        int level = manager.getLevel(player, type);
        skill.getEffects().forEach(effect -> effect.apply(player, level));
    }
}
