package me.penguinx13.wSkills.ui;

import me.penguinx13.wSkills.API.Skill;
import me.penguinx13.wSkills.API.SkillType;
import me.penguinx13.wSkills.service.SkillManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkillMenu {

    public static final String TITLE = ChatColor.DARK_GREEN + "Навыки";

    private static final int LEVELS_PER_SKILL = 10;
    private static final int SKILL_SECTION_SIZE = 18;
    private static final int INVENTORY_SIZE = 54;

    private final SkillManager manager;

    public SkillMenu(SkillManager manager) {
        this.manager = manager;
    }

    public void open(Player player) {
        player.openInventory(createInventory(player));
    }

    private Inventory createInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, INVENTORY_SIZE, TITLE);

        List<SkillType> types = Arrays.stream(SkillType.values())
                .filter(type -> manager.getSkill(type) != null)
                .toList();

        for (int index = 0; index < types.size(); index++) {
            int base = index * SKILL_SECTION_SIZE;
            if (base >= INVENTORY_SIZE) {
                break;
            }
            SkillType type = types.get(index);
            Skill skill = manager.getSkill(type);
            if (skill == null) {
                continue;
            }

            int level = manager.getLevel(player, type);
            int xp = manager.getXp(player, type);

            inventory.setItem(base, createHeader(type, skill, level, xp));

            for (int i = 1; i <= LEVELS_PER_SKILL; i++) {
                int slot = base + i;
                if (slot >= INVENTORY_SIZE) {
                    break;
                }
                inventory.setItem(slot, createLevelItem(skill, i, level));
            }
        }

        return inventory;
    }

    private ItemStack createHeader(SkillType type, Skill skill, int level, int xp) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + getDisplayName(type));
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Уровень: " + ChatColor.GREEN + level + ChatColor.GRAY + "/" + skill.getMaxLevel());
        lore.add(ChatColor.GRAY + "Опыт: " + ChatColor.AQUA + xp);
        lore.add(ChatColor.GRAY + "Бонус за уровень: " + ChatColor.GREEN + formatPercent(skill.getValuePerLevel()));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createLevelItem(Skill skill, int levelNumber, int currentLevel) {
        Material material = getLevelMaterial(levelNumber, currentLevel);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Уровень " + levelNumber);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Бонус уровня: " + ChatColor.GREEN + formatPercent(skill.getValuePerLevel()));
        lore.add(ChatColor.GRAY + "Суммарно: " + ChatColor.GREEN + formatPercent(skill.getValuePerLevel() * levelNumber));
        lore.add(ChatColor.GRAY + "Требуется опыта: " + ChatColor.AQUA + (5000 * levelNumber));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private Material getLevelMaterial(int levelNumber, int currentLevel) {
        if (currentLevel <= 0) {
            return Material.GRAY_STAINED_GLASS_PANE;
        }
        if (levelNumber < currentLevel) {
            return Material.LIME_STAINED_GLASS_PANE;
        }
        if (levelNumber == currentLevel) {
            return Material.YELLOW_STAINED_GLASS_PANE;
        }
        return Material.GRAY_STAINED_GLASS_PANE;
    }

    private String formatPercent(double valuePerLevel) {
        double percent = valuePerLevel * 100.0;
        if (percent % 1.0 == 0) {
            return String.format("%.0f%%", percent);
        }
        return String.format("%.1f%%", percent);
    }

    private String getDisplayName(SkillType type) {
        return switch (type) {
            case AGILITY -> "Ловкость";
            case STRENGTH -> "Сила";
            case ENDURANCE -> "Выносливость";
        };
    }
}
