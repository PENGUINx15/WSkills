package me.penguinx13.wSkills.listener;

import me.penguinx13.wSkills.API.SkillID;
import me.penguinx13.wSkills.service.SkillApplier;
import me.penguinx13.wSkills.service.SkillContext;
import me.penguinx13.wSkills.service.SkillManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class PlayerItemDamageListener implements Listener {

    private final SkillManager manager;
    private final SkillApplier applier;

    public PlayerItemDamageListener(SkillManager manager, SkillApplier applier) {
        this.manager = manager;
        this.applier = applier;
    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        int level = manager.getLevel(player, SkillID.MINING);
        applier.applySkill(SkillID.MINING, new SkillContext(player, level, event));
    }
}
