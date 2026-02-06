package me.penguinx13.wSkills.service;

import me.penguinx13.wSkills.API.SkillType;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumMap;
import java.util.Map;

public class SkillStorage {

    private final String databaseUrl;

    public SkillStorage(File dataFolder) {
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            throw new IllegalStateException("Unable to create plugin data folder: " + dataFolder.getAbsolutePath());
        }
        File databaseFile = new File(dataFolder, "skills.db");
        this.databaseUrl = "jdbc:sqlite:" + databaseFile.getAbsolutePath();
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(databaseUrl);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS player_skills (" +
                            "player_uuid TEXT NOT NULL," +
                            "skill TEXT NOT NULL," +
                            "level INTEGER NOT NULL," +
                            "PRIMARY KEY (player_uuid, skill)" +
                            ")"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void load(Player player, SkillManager manager) {
        manager.registerPlayer(player);

        Map<SkillType, Integer> levels = new EnumMap<>(SkillType.class);
        String sql = "SELECT skill, level FROM player_skills WHERE player_uuid = ?";

        try (Connection connection = DriverManager.getConnection(databaseUrl);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String skillName = resultSet.getString("skill");
                    int level = resultSet.getInt("level");
                    try {
                        SkillType type = SkillType.valueOf(skillName);
                        levels.put(type, level);
                    } catch (IllegalArgumentException ignored) {
                        // Ignore unknown skill names
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (SkillType type : SkillType.values()) {
            int level = levels.getOrDefault(type, 0);
            manager.setLevel(player, type, level);
        }
    }

    public void save(Player player, SkillManager manager) {
        String sql = "INSERT INTO player_skills (player_uuid, skill, level) VALUES (?, ?, ?) " +
                "ON CONFLICT(player_uuid, skill) DO UPDATE SET level = excluded.level";

        try (Connection connection = DriverManager.getConnection(databaseUrl);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (SkillType type : SkillType.values()) {
                statement.setString(1, player.getUniqueId().toString());
                statement.setString(2, type.name());
                statement.setInt(3, manager.getLevel(player, type));
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
