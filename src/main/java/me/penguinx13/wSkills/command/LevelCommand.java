package me.penguinx13.wSkills.command;

import me.penguinx13.wSkills.API.SkillType;
import me.penguinx13.wSkills.service.SkillApplier;
import me.penguinx13.wSkills.service.SkillManager;
import me.penguinx13.wSkills.service.SkillStorage;
import me.penguinx13.wSkills.ui.SkillMenu;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

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

    public void register(Commands commands) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("wskills")
                .executes(context -> {
                    sendUsage(context.getSource().getSender());
                    return 1;
                })
                .then(Commands.literal("menu")
                        .executes(this::openMenu))
                .then(Commands.literal("level")
                        .then(Commands.literal("get")
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .suggests(this::suggestPlayers)
                                        .then(Commands.argument("skill", StringArgumentType.word())
                                                .suggests(this::suggestSkills)
                                                .executes(this::getLevel))))
                        .then(Commands.literal("set")
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .suggests(this::suggestPlayers)
                                        .then(Commands.argument("skill", StringArgumentType.word())
                                                .suggests(this::suggestSkills)
                                                .then(Commands.argument("level", IntegerArgumentType.integer(0))
                                                        .executes(this::setLevel)))))
                        .then(Commands.literal("add")
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .suggests(this::suggestPlayers)
                                        .then(Commands.argument("skill", StringArgumentType.word())
                                                .suggests(this::suggestSkills)
                                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                        .executes(this::addLevel))))));

        commands.register(root.build(), "Manage player skill levels.", List.of("skills"));
    }

    private int openMenu(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can open the skill menu.");
            return 0;
        }
        manager.registerPlayer(player);
        skillMenu.open(player);
        return 1;
    }

    private int getLevel(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        Player target = resolvePlayer(context, sender);
        if (target == null) {
            return 0;
        }
        SkillType type = resolveSkill(context, sender);
        if (type == null) {
            return 0;
        }
        manager.registerPlayer(target);
        int level = manager.getLevel(target, type);
        sender.sendMessage(ChatColor.GREEN + target.getName() + " " + type.name().toLowerCase(Locale.ROOT)
                + " level: " + level);
        return 1;
    }

    private int setLevel(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        Player target = resolvePlayer(context, sender);
        if (target == null) {
            return 0;
        }
        SkillType type = resolveSkill(context, sender);
        if (type == null) {
            return 0;
        }
        int level = IntegerArgumentType.getInteger(context, "level");
        manager.registerPlayer(target);
        manager.setLevel(target, type, level);
        applier.applySkill(target, type);
        storage.save(target, manager);
        sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + " " + type.name().toLowerCase(Locale.ROOT)
                + " level to " + level + ".");
        return 1;
    }

    private int addLevel(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        Player target = resolvePlayer(context, sender);
        if (target == null) {
            return 0;
        }
        SkillType type = resolveSkill(context, sender);
        if (type == null) {
            return 0;
        }
        int amount = IntegerArgumentType.getInteger(context, "amount");
        manager.registerPlayer(target);
        int current = manager.getLevel(target, type);
        int updated = Math.max(0, current + amount);
        manager.setLevel(target, type, updated);
        applier.applySkill(target, type);
        storage.save(target, manager);
        sender.sendMessage(ChatColor.GREEN + "Updated " + target.getName() + " " + type.name().toLowerCase(Locale.ROOT)
                + " level to " + updated + ".");
        return 1;
    }

    private Player resolvePlayer(CommandContext<CommandSourceStack> context, CommandSender sender) {
        String playerName = StringArgumentType.getString(context, "player");
        Player target = Bukkit.getPlayerExact(playerName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found or not online.");
            return null;
        }
        return target;
    }

    private SkillType resolveSkill(CommandContext<CommandSourceStack> context, CommandSender sender) {
        String skillName = StringArgumentType.getString(context, "skill");
        SkillType type = parseSkillType(skillName);
        if (type == null) {
            sender.sendMessage(ChatColor.RED + "Unknown skill type.");
            return null;
        }
        return type;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Usage:");
        sender.sendMessage(ChatColor.YELLOW + "/wskills menu");
        sender.sendMessage(ChatColor.YELLOW + "/wskills level get <player> <skill>");
        sender.sendMessage(ChatColor.YELLOW + "/wskills level set <player> <skill> <level>");
        sender.sendMessage(ChatColor.YELLOW + "/wskills level add <player> <skill> <amount>");
    }

    private SkillType parseSkillType(String input) {
        try {
            return SkillType.valueOf(input.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private CompletableFuture<com.mojang.brigadier.suggestion.Suggestions> suggestPlayers(
            CommandContext<CommandSourceStack> context,
            SuggestionsBuilder builder
    ) {
        String remaining = builder.getRemainingLowerCase();
        for (Player player : Bukkit.getOnlinePlayers()) {
            String name = player.getName();
            if (remaining.isEmpty() || name.toLowerCase(Locale.ROOT).startsWith(remaining)) {
                builder.suggest(name);
            }
        }
        return builder.buildFuture();
    }

    private CompletableFuture<com.mojang.brigadier.suggestion.Suggestions> suggestSkills(
            CommandContext<CommandSourceStack> context,
            SuggestionsBuilder builder
    ) {
        String remaining = builder.getRemainingLowerCase();
        for (SkillType type : SkillType.values()) {
            String name = type.name().toLowerCase(Locale.ROOT);
            if (remaining.isEmpty() || name.startsWith(remaining)) {
                builder.suggest(name);
            }
        }
        return builder.buildFuture();
    }
}
