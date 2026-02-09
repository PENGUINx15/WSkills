package me.penguinx13.wSkills.listener;

import me.penguinx13.wSkills.API.SkillID;
import me.penguinx13.wSkills.service.SkillApplier;
import me.penguinx13.wSkills.service.SkillContext;
import me.penguinx13.wSkills.service.SkillManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    private final SkillManager manager;
    private final SkillApplier applier;

    public EntityDamageListener(SkillManager manager, SkillApplier applier) {
        this.manager = manager;
        this.applier = applier;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        int level = manager.getLevel(player, SkillID.AGILITY);
        applier.applySkill(SkillID.AGILITY, new SkillContext(player, level, event));
    }
}
