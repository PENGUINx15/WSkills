package me.penguinx13.wSkills.service;

import me.penguinx13.wSkills.API.PlayerSkills;
import me.penguinx13.wSkills.API.Skill;
import me.penguinx13.wSkills.API.SkillType;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SkillManager {

    private static SkillManager instance;

    private final Map<UUID, PlayerSkills> playerSkills = new ConcurrentHashMap<>();
    private final Map<SkillType, Skill> registeredSkills = new EnumMap<>(SkillType.class);

    public SkillManager() {
        instance = this;
    }

    public static SkillManager get() {
        return instance;
    }

    /* ===== SKILLS ===== */

    public void registerSkill(Skill skill) {
        registeredSkills.put(skill.getType(), skill);
    }

    public Skill getSkill(SkillType type) {
        return registeredSkills.get(type);
    }

    public Collection<Skill> getAllSkills() {
        return registeredSkills.values();
    }

    /* ===== PLAYERS ===== */

    public void registerPlayer(Player player) {
        playerSkills.putIfAbsent(player.getUniqueId(), new PlayerSkills());
    }

    public void unregisterPlayer(Player player) {
        playerSkills.remove(player.getUniqueId());
    }

    public int getLevel(Player player, SkillType type) {
        PlayerSkills ps = playerSkills.get(player.getUniqueId());
        return ps == null ? 0 : ps.getLevel(type);
    }

    public void setLevel(Player player, SkillType type, int level) {
        playerSkills.get(player.getUniqueId()).setLevel(type, level);
    }

    public int getXp(Player player, SkillType type) {
        PlayerSkills ps = playerSkills.get(player.getUniqueId());
        return ps == null ? 0 : ps.getXp(type);
    }

    public void setXp(Player player, SkillType type, int xp) {
        playerSkills.get(player.getUniqueId()).setXp(type, xp);
    }

    public int addXp(Player player, SkillType type, int amount) {
        if (amount <= 0) {
            return getLevel(player, type);
        }

        registerPlayer(player);

        Skill skill = registeredSkills.get(type);
        if (skill == null) {
            return getLevel(player, type);
        }

        PlayerSkills ps = playerSkills.get(player.getUniqueId());
        int level = ps.getLevel(type);
        int xp = ps.getXp(type) + amount;
        while (level < skill.getMaxLevel()) {
            int requiredXp = getRequiredXp(level);
            if (xp < requiredXp) {
                break;
            }
            xp -= requiredXp;
            level += 1;
        }

        if (level >= skill.getMaxLevel()) {
            xp = 0;
        }

        ps.setLevel(type, level);
        ps.setXp(type, xp);

        return level;
    }

    public PlayerSkills getPlayerSkills(Player player) {
        return playerSkills.get(player.getUniqueId());
    }

    private int getRequiredXp(int level) {
        return 5000 * (level + 1);
    }
}
