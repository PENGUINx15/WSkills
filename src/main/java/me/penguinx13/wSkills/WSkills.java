package me.penguinx13.wSkills;

import me.penguinx13.wSkills.command.LevelCommand;
import me.penguinx13.wSkills.skills.agility.AgilityXpListener;
import me.penguinx13.wSkills.listener.DamageListener;
import me.penguinx13.wSkills.listener.JoinQuitListener;
import me.penguinx13.wSkills.listener.SkillMenuListener;
import me.penguinx13.wSkills.service.SkillApplier;
import me.penguinx13.wSkills.service.SkillManager;
import me.penguinx13.wSkills.service.SkillStorage;
import me.penguinx13.wSkills.skills.agility.AgilitySkill;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class WSkills extends JavaPlugin {

    private SkillManager skillManager;
    private SkillApplier skillApplier;
    private SkillStorage skillStorage;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.skillManager = new SkillManager();
        this.skillApplier = new SkillApplier(skillManager);
        this.skillStorage = new SkillStorage(getDataFolder());

        registerSkills();

        registerListeners();
        registerCommands();

        getLogger().info("WSkills enabled");
    }

    @Override
    public void onDisable() {
        getServer().getOnlinePlayers().forEach(player ->
                skillStorage.save(player, skillManager)
        );
        getLogger().info("WSkills disabled");
    }


    private void registerSkills() {
        skillManager.registerSkill(new AgilitySkill());
    }

    private void registerListeners() {
        var pm = getServer().getPluginManager();

        pm.registerEvents(
                new JoinQuitListener(skillManager, skillApplier, skillStorage),
                this
        );

        pm.registerEvents(
                new DamageListener(),
                this
        );

        pm.registerEvents(
                new AgilityXpListener(skillManager, skillApplier, skillStorage),
                this
        );

        pm.registerEvents(
                new SkillMenuListener(),
                this
        );
    }

    private void registerCommands() {
        LevelCommand levelCommand = new LevelCommand(skillManager, skillApplier, skillStorage);
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();
            commands.register("wskills", levelCommand.createCommand(), "skills");
        });
    }

    /* ============================ */

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public SkillApplier getSkillApplier() {
        return skillApplier;
    }

    public SkillStorage getSkillStorage() {
        return skillStorage;
    }
}
