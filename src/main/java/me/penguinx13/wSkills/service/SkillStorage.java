package me.penguinx13.wSkills.service;

import me.penguinx13.wSkills.API.SkillType;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SkillStorage {

    private final File file;
    private final FileConfiguration config;

    public SkillStorage(File dataFolder) {
        this.file = new File(dataFolder, "skills.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void load(Player player, SkillManager manager) {
        String path = "players." + player.getUniqueId();

        manager.registerPlayer(player);

        for (SkillType type : SkillType.values()) {
            int level = config.getInt(path + "." + type.name(), 0);
            manager.setLevel(player, type, level);
        }
    }

    public void save(Player player, SkillManager manager) {
        String path = "players." + player.getUniqueId();

        for (SkillType type : SkillType.values()) {
            int level = manager.getLevel(player, type);
            config.set(path + "." + type.name(), level);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
