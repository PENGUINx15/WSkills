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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkillMenu {

    public static final Component TITLE = Component.text("Навыки", NamedTextColor.BLACK);

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

            inventory.setItem(base+1, createHeader(type, skill, level, xp));

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
        meta.displayName(Component.text(getDisplayName(type), NamedTextColor.GOLD)
                .style(Style.style(TextDecoration.BOLD)));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Опыт: ", NamedTextColor.WHITE)
                .append(Component.text(xp, NamedTextColor.GOLD)));
        lore.addAll(getBonusLore(type, skill, level));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createLevelItem(int levelNumber, int currentLevel, int xp) {
        Material material = getLevelMaterial(levelNumber, currentLevel);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Уровень " + levelNumber, NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        int requiredXp = 5000 * levelNumber;
        lore.add(Component.text("Опыт: ", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(xp, NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
                .append(Component.text("/", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                .append(Component.text(requiredXp, NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false)));
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

    private List<Component> getBonusLore(SkillType type, Skill skill, int level) {
        List<Component> lore = new ArrayList<>();
        switch (type) {
            case AGILITY -> {
                lore.add(Component.text("Бонус скорости: ", NamedTextColor.WHITE)
                        .append(Component.text(formatPercent(skill.getValuePerLevel() * level), NamedTextColor.GOLD)));
                lore.add(Component.text("Бонус уклонения: ", NamedTextColor.WHITE)
                        .append(Component.text(formatPercent(Math.min(0.25, level * 0.025)), NamedTextColor.GOLD)));
            }
            case MINNING -> lore.add(Component.text("Бонус скорости добычи: ", NamedTextColor.WHITE)
                    .append(Component.text(formatPercent(skill.getValuePerLevel() * level), NamedTextColor.GOLD)));
            default -> {
            }
        }
        return lore;
    }

    private String getDisplayName(SkillType type) {
        return switch (type) {
            case AGILITY -> "Ловкость";
            case MINNING -> "Горное дело";
            case STRENGTH -> "Сила";
            case ENDURANCE -> "Выносливость";
        };
    }
}
