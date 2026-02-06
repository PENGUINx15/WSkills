package me.penguinx13.wSkills.command;

import me.penguinx13.wSkills.API.SkillType;
import me.penguinx13.wSkills.service.SkillApplier;
import me.penguinx13.wSkills.service.SkillManager;
import me.penguinx13.wSkills.service.SkillStorage;
import me.penguinx13.wSkills.ui.SkillMenu;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public class LevelCommand {

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

    public LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return literal("wskills")
                .executes(context -> {
                    sendUsage(context.getSource().getSender(), "wskills");
                    return 1;
                })
                .then(literal("menu")
                        .executes(context -> {
                            openMenu(context.getSource().getSender());
                            return 1;
                        }))
                .then(literal("level")
                        .executes(context -> {
                            sendUsage(context.getSource().getSender(), "wskills");
                            return 1;
                        })
                        .then(literal("get")
                                .then(com.mojang.brigadier.builder.RequiredArgumentBuilder.<CommandSourceStack, String>argument("player", StringArgumentType.word())
                                        .suggests(PLAYER_SUGGESTIONS)
                                        .then(com.mojang.brigadier.builder.RequiredArgumentBuilder.<CommandSourceStack, String>argument("skill", StringArgumentType.word())
                                                .suggests(SKILL_SUGGESTIONS)
                                                .executes(context -> {
                                                    CommandSender sender = context.getSource().getSender();
                                                    String playerName = StringArgumentType.getString(context, "player");
                                                    String skillName = StringArgumentType.getString(context, "skill");
                                                    handleGet(sender, playerName, skillName);
                                                    return 1;
                                                }))))
                        .then(literal("set")
                                .then(com.mojang.brigadier.builder.RequiredArgumentBuilder.<CommandSourceStack, String>argument("player", StringArgumentType.word())
                                        .suggests(PLAYER_SUGGESTIONS)
                                        .then(com.mojang.brigadier.builder.RequiredArgumentBuilder.<CommandSourceStack, String>argument("skill", StringArgumentType.word())
                                                .suggests(SKILL_SUGGESTIONS)
                                                .then(com.mojang.brigadier.builder.RequiredArgumentBuilder.<CommandSourceStack, Integer>argument("level", IntegerArgumentType.integer(0))
                                                        .executes(context -> {
                                                            CommandSender sender = context.getSource().getSender();
                                                            String playerName = StringArgumentType.getString(context, "player");
                                                            String skillName = StringArgumentType.getString(context, "skill");
                                                            int level = IntegerArgumentType.getInteger(context, "level");
                                                            handleSet(sender, playerName, skillName, level);
                                                            return 1;
                                                        })))))
                        .then(literal("add")
                                .then(com.mojang.brigadier.builder.RequiredArgumentBuilder.<CommandSourceStack, String>argument("player", StringArgumentType.word())
                                        .suggests(PLAYER_SUGGESTIONS)
                                        .then(com.mojang.brigadier.builder.RequiredArgumentBuilder.<CommandSourceStack, String>argument("skill", StringArgumentType.word())
                                                .suggests(SKILL_SUGGESTIONS)
                                                .then(com.mojang.brigadier.builder.RequiredArgumentBuilder.<CommandSourceStack, Integer>argument("amount", IntegerArgumentType.integer())
                                                        .executes(context -> {
                                                            CommandSender sender = context.getSource().getSender();
                                                            String playerName = StringArgumentType.getString(context, "player");
                                                            String skillName = StringArgumentType.getString(context, "skill");
                                                            int amount = IntegerArgumentType.getInteger(context, "amount");
                                                            handleAdd(sender, playerName, skillName, amount);
                                                            return 1;
                                                        })))))));
    }

    private void openMenu(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can open the skill menu.");
            return;
        }
        manager.registerPlayer(player);
        skillMenu.open(player);
    }

    private void handleGet(CommandSender sender, String playerName, String skillName) {
        Player target = findPlayer(sender, playerName);
        if (target == null) {
            return;
        }
        SkillType type = parseSkillType(sender, skillName);
        if (type == null) {
            return;
        }
        manager.registerPlayer(target);
        int level = manager.getLevel(target, type);
        sender.sendMessage(ChatColor.GREEN + target.getName() + " " + type.name().toLowerCase(Locale.ROOT)
                + " level: " + level);
    }

    private void handleSet(CommandSender sender, String playerName, String skillName, int level) {
        Player target = findPlayer(sender, playerName);
        if (target == null) {
            return;
        }
        SkillType type = parseSkillType(sender, skillName);
        if (type == null) {
            return;
        }
        manager.registerPlayer(target);
        manager.setLevel(target, type, level);
        applier.applySkill(target, type);
        storage.save(target, manager);
        sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + " " + type.name().toLowerCase(Locale.ROOT)
                + " level to " + level + ".");
    }

    private void handleAdd(CommandSender sender, String playerName, String skillName, int amount) {
        Player target = findPlayer(sender, playerName);
        if (target == null) {
            return;
        }
        SkillType type = parseSkillType(sender, skillName);
        if (type == null) {
            return;
        }
        manager.registerPlayer(target);
        int current = manager.getLevel(target, type);
        int updated = current + amount;
        if (updated < 0) {
            updated = 0;
        }
        manager.setLevel(target, type, updated);
        applier.applySkill(target, type);
        storage.save(target, manager);
        sender.sendMessage(ChatColor.GREEN + "Updated " + target.getName() + " " + type.name().toLowerCase(Locale.ROOT)
                + " level to " + updated + ".");
    }

    private void sendUsage(CommandSender sender, String label) {
        sender.sendMessage(ChatColor.YELLOW + "Usage:");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " menu");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " level get <player> <skill>");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " level set <player> <skill> <level>");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " level add <player> <skill> <amount>");
    }

    private SkillType parseSkillType(String input) {
        try {
            return SkillType.valueOf(input.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private SkillType parseSkillType(CommandSender sender, String input) {
        SkillType type = parseSkillType(input);
        if (type == null) {
            sender.sendMessage(ChatColor.RED + "Unknown skill type.");
        }
        return type;
    }

    private Player findPlayer(CommandSender sender, String name) {
        Player target = Bukkit.getPlayerExact(name);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found or not online.");
            return null;
        }
        return target;
    }

    private static final SuggestionProvider<CommandSourceStack> PLAYER_SUGGESTIONS = (context, builder) -> {
        for (Player player : Bukkit.getOnlinePlayers()) {
            builder.suggest(player.getName());
        }
        return builder.buildFuture();
    };

    private static final SuggestionProvider<CommandSourceStack> SKILL_SUGGESTIONS = (context, builder) -> {
        for (SkillType type : SkillType.values()) {
            builder.suggest(type.name().toLowerCase(Locale.ROOT));
        }
        return builder.buildFuture();
    };
}
