package me.penguinx13.wSkills;

import me.penguinx13.wSkills.listener.DamageListener;
import me.penguinx13.wSkills.listener.JoinQuitListener;
import me.penguinx13.wSkills.service.SkillApplier;
import me.penguinx13.wSkills.service.SkillManager;
import me.penguinx13.wSkills.service.SkillStorage;
import me.penguinx13.wSkills.skills.agility.AgilitySkill;
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
