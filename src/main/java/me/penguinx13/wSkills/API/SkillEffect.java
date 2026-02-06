package me.penguinx13.wSkills.API;

import org.bukkit.entity.Player;

public interface SkillEffect {

    void apply(Player player, int level);

    default void remove(Player player) {}
}
