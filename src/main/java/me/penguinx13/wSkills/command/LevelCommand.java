package me.penguinx13.wSkills.command;

import me.penguinx13.wSkills.API.SkillType;
import me.penguinx13.wSkills.service.SkillApplier;
import me.penguinx13.wSkills.service.SkillManager;
import me.penguinx13.wSkills.service.SkillStorage;
import me.penguinx13.wSkills.ui.SkillMenu;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LevelCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList("get", "set", "add");

    private final SkillManager manager;
    private final SkillApplier applier;
    private final SkillStorage storage;
    private final SkillMenu skillMenu;

    public LevelCommand(SkillManager manager, SkillApplier applier, SkillStorage storage) {
        this.manager = manager;
        this.applier = applier;
        this.storage = storage;
        this.skillMenu = new SkillMenu(manager);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sendUsage(sender, label);
            return true;
        }

        if (args[0].equalsIgnoreCase("menu")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(NamedTextColor.RED + "Only players can open the skill menu.");
                return true;
            }
            manager.registerPlayer(player);
            skillMenu.open(player);
            return true;
        }

        if (!args[0].equalsIgnoreCase("level")) {
            sendUsage(sender, label);
            return true;
        }

        if (args.length < 2) {
            sendUsage(sender, label);
            return true;
        }

        String action = args[1].toLowerCase(Locale.ROOT);
        if (!SUBCOMMANDS.contains(action)) {
            sendUsage(sender, label);
            return true;
        }

        if (args.length < 4) {
            sender.sendMessage(NamedTextColor.RED + "Usage: /" + label + " level " + action + " <player> <skill> [value]");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[2]);
        if (target == null) {
            sender.sendMessage(NamedTextColor.RED + "Player not found or not online.");
            return true;
        }

        SkillType type = parseSkillType(args[3]);
        if (type == null) {
            sender.sendMessage(NamedTextColor.RED + "Unknown skill type.");
            return true;
        }

        manager.registerPlayer(target);

        switch (action) {
            case "get" -> {
                int level = manager.getLevel(target, type);
                sender.sendMessage(NamedTextColor.GREEN + target.getName() + " " + type.name().toLowerCase(Locale.ROOT)
                        + " level: " + level);
            }
            case "set" -> {
                if (args.length < 5) {
                    sender.sendMessage(NamedTextColor.RED + "Usage: /" + label + " level set <player> <skill> <level>");
                    return true;
                }
                Integer level = parseLevel(args[4], sender);
                if (level == null) {
                    return true;
                }
                if (level < 0) {
                    sender.sendMessage(NamedTextColor.RED + "Level cannot be negative.");
                    return true;
                }
                manager.setLevel(target, type, level);
                applier.applySkill(target, type);
                storage.save(target, manager);
                sender.sendMessage(NamedTextColor.GREEN + "Set " + target.getName() + " " + type.name().toLowerCase(Locale.ROOT)
                        + " level to " + level + ".");
            }
            case "add" -> {
                if (args.length < 5) {
                    sender.sendMessage(NamedTextColor.RED + "Usage: /" + label + " level add <player> <skill> <amount>");
                    return true;
                }
                Integer amount = parseLevel(args[4], sender);
                if (amount == null) {
                    return true;
                }
                int current = manager.getLevel(target, type);
                int updated = current + amount;
                if (updated < 0) {
                    updated = 0;
                }
                manager.setLevel(target, type, updated);
                applier.applySkill(target, type);
                storage.save(target, manager);
                sender.sendMessage(NamedTextColor.GREEN + "Updated " + target.getName() + " " + type.name().toLowerCase(Locale.ROOT)
                        + " level to " + updated + ".");
            }
            default -> sendUsage(sender, label);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return filterPrefix(Arrays.asList("level", "menu"), args[0]);
        }
        if (!args[0].equalsIgnoreCase("level")) {
            return Collections.emptyList();
        }
        if (args.length == 2) {
            return filterPrefix(SUBCOMMANDS, args[1]);
        }
        if (args.length == 3) {
            List<String> players = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                players.add(player.getName());
            }
            return filterPrefix(players, args[2]);
        }
        if (args.length == 4) {
            List<String> skills = new ArrayList<>();
            for (SkillType type : SkillType.values()) {
                skills.add(type.name().toLowerCase(Locale.ROOT));
            }
            return filterPrefix(skills, args[3]);
        }
        return Collections.emptyList();
    }

    private void sendUsage(CommandSender sender, String label) {
        sender.sendMessage(NamedTextColor.YELLOW + "Usage:");
        sender.sendMessage(NamedTextColor.YELLOW + "/" + label + " menu");
        sender.sendMessage(NamedTextColor.YELLOW + "/" + label + " level get <player> <skill>");
        sender.sendMessage(NamedTextColor.YELLOW + "/" + label + " level set <player> <skill> <level>");
        sender.sendMessage(NamedTextColor.YELLOW + "/" + label + " level add <player> <skill> <amount>");
    }

    private SkillType parseSkillType(String input) {
        try {
            return SkillType.valueOf(input.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Integer parseLevel(String input, CommandSender sender) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            sender.sendMessage(NamedTextColor.RED + "Level must be a number.");
            return null;
        }
    }

    private List<String> filterPrefix(List<String> options, String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return options;
        }
        String lower = prefix.toLowerCase(Locale.ROOT);
        List<String> matches = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase(Locale.ROOT).startsWith(lower)) {
                matches.add(option);
            }
        }
        return matches;
    }
}