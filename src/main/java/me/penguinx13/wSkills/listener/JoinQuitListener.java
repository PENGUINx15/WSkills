package me.penguinx13.wSkills.listener;

import ru.yourplugin.skills.service.*;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    private final SkillManager manager;
    private final SkillApplier applier;
    private final SkillStorage storage;

    public JoinQuitListener(SkillManager manager, SkillApplier applier, SkillStorage storage) {
        this.manager = manager;
        this.applier = applier;
        this.storage = storage;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        storage.load(e.getPlayer(), manager);
        applier.applyAll(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        storage.save(e.getPlayer(), manager);
        manager.unregisterPlayer(e.getPlayer());
    }
}
