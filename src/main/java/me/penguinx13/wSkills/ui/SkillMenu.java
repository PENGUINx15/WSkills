package me.penguinx13.wSkills.ui;

import me.penguinx13.wSkills.API.Skill;
import me.penguinx13.wSkills.API.SkillType;
import me.penguinx13.wSkills.service.SkillManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkillMenu {

    public static final String TITLE = NamedTextColor.DARK_GREEN + "Навыки";

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

            int maxLevel = skill.getMaxLevel();
            for (int i = 1; i <= maxLevel; i++) {
                int slot = base + i;
                if (slot >= INVENTORY_SIZE) {
                    break;
                }
                inventory.setItem(slot+1, createLevelItem(i, level, xp));
            }
        }

        return inventory;
    }

    private ItemStack createHeader(SkillType type, Skill skill, int level, int xp) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(NamedTextColor.GOLD + getDisplayName(type)).style(Style.style(TextDecoration.BOLD)));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(NamedTextColor.WHITE + "Уровень: " + NamedTextColor.GOLD + level));
        lore.add(Component.text(NamedTextColor.WHITE + "Опыт: " + NamedTextColor.GOLD + xp));
        lore.add(Component.text(NamedTextColor.WHITE + "Бонус скорости: " + NamedTextColor.GOLD + formatPercent(getSpeedBonus(type, skill, level))));
        lore.add(Component.text(NamedTextColor.WHITE + "Бонус уклонения: " + NamedTextColor.GOLD + formatPercent(getDodgeBonus(type, level))));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createLevelItem(int levelNumber, int currentLevel, int xp) {
        Material material = getLevelMaterial(levelNumber, currentLevel);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(NamedTextColor.YELLOW + "Уровень " + levelNumber));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(NamedTextColor.WHITE + "Опыт: "+ NamedTextColor.YELLOW + xp + NamedTextColor.WHITE + NamedTextColor.GOLD + (5000 * levelNumber)));
        meta.lore(lore);
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

    private double getSpeedBonus(SkillType type, Skill skill, int level) {
        if (type == SkillType.AGILITY) {
            return skill.getValuePerLevel() * level;
        }
        return 0.0;
    }

    private double getDodgeBonus(SkillType type, int level) {
        if (type == SkillType.AGILITY) {
            return Math.min(0.25, level * 0.025);
        }
        return 0.0;
    }

    private String getDisplayName(SkillType type) {
        return switch (type) {
            case AGILITY -> "Ловкость";
            case STRENGTH -> "Сила";
            case ENDURANCE -> "Выносливость";
        };
    }
}
