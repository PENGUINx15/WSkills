package me.penguinx13.wSkills;

import me.penguinx13.wSkills.command.LevelCommand;
import me.penguinx13.wSkills.listener.JoinQuitListener;
import me.penguinx13.wSkills.listener.SkillMenuListener;
import me.penguinx13.wSkills.listener.EntityDamageListener;
import me.penguinx13.wSkills.listener.PlayerItemDamageListener;
import me.penguinx13.wSkills.service.SkillApplier;
import me.penguinx13.wSkills.service.SkillManager;
import me.penguinx13.wSkills.service.SkillStorage;
import me.penguinx13.wSkills.skills.agility.AgilitySkill;
import me.penguinx13.wSkills.skills.agility.AgilityXpListener;
import me.penguinx13.wSkills.skills.mining.MiningSkill;
import me.penguinx13.wSkills.skills.mining.MiningXpListener;
import me.penguinx13.wSkills.util.CoreProtectUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class WSkills extends JavaPlugin {

    private SkillManager skillManager;
    private SkillApplier skillApplier;
    private SkillStorage skillStorage;

    @Override
    public void onEnable() {
        this.skillManager = new SkillManager();
        this.skillApplier = new SkillApplier();
        this.skillStorage = new SkillStorage(getDataFolder());

        CoreProtectUtil.init();

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
        skillManager.registerSkill(new MiningSkill());
    }

    private void registerListeners() {
        var pm = getServer().getPluginManager();

        pm.registerEvents(
                new JoinQuitListener(skillManager, skillApplier, skillStorage),
                this
        );

        pm.registerEvents(
                new AgilityXpListener(skillManager, skillApplier, skillStorage),
                this
        );

        pm.registerEvents(
                new MiningXpListener(skillStorage),
                this
        );

        pm.registerEvents(
                new EntityDamageListener(skillManager, skillApplier),
                this
        );

        pm.registerEvents(
                new PlayerItemDamageListener(skillManager, skillApplier),
                this
        );

        pm.registerEvents(
                new SkillMenuListener(),
                this
        );
    }

    private void registerCommands() {
        LevelCommand levelCommand = new LevelCommand(skillManager, skillApplier, skillStorage);
        var command = getCommand("wskills");
        if (command != null) {
            command.setExecutor(levelCommand);
            command.setTabCompleter(levelCommand);
        }
    }
}
