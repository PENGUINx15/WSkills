package me.penguinx13.wSkills.service;

import me.penguinx13.wSkills.API.SkillID;
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
                            "xp INTEGER NOT NULL DEFAULT 0," +
                            "PRIMARY KEY (player_uuid, skill)" +
                            ")"
            );
            ensureXpColumn(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ensureXpColumn(Connection connection) throws SQLException {
        boolean hasXpColumn = false;
        try (PreparedStatement statement = connection.prepareStatement("PRAGMA table_info(player_skills)");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                if ("xp".equalsIgnoreCase(name)) {
                    hasXpColumn = true;
                    break;
                }
            }
        }

        if (!hasXpColumn) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("ALTER TABLE player_skills ADD COLUMN xp INTEGER NOT NULL DEFAULT 0");
            }
        }
    }

    public void load(Player player, SkillManager manager) {
        manager.registerPlayer(player);

        Map<SkillID, Integer> levels = new EnumMap<>(SkillID.class);
        String sql = "SELECT skill, level, xp FROM player_skills WHERE player_uuid = ?";

        try (Connection connection = DriverManager.getConnection(databaseUrl);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String skillName = resultSet.getString("skill");
                    int level = resultSet.getInt("level");
                    int xp = resultSet.getInt("xp");
                    try {
                        SkillID type = SkillID.valueOf(skillName);
                        levels.put(type, level);
                        manager.setXp(player, type, xp);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (SkillID type : SkillID.values()) {
            int level = levels.getOrDefault(type, 0);
            manager.setLevel(player, type, level);
        }
    }

    public void save(Player player, SkillManager manager) {
        String sql = "INSERT INTO player_skills (player_uuid, skill, level, xp) VALUES (?, ?, ?, ?) " +
                "ON CONFLICT(player_uuid, skill) DO UPDATE SET level = excluded.level, xp = excluded.xp";

        try (Connection connection = DriverManager.getConnection(databaseUrl);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (SkillID type : SkillID.values()) {
                statement.setString(1, player.getUniqueId().toString());
                statement.setString(2, type.name());
                statement.setInt(3, manager.getLevel(player, type));
                statement.setInt(4, manager.getXp(player, type));
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
