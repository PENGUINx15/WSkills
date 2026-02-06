package me.penguinx13.wSkills.listener;

import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent event) {
        // Логика урона обрабатывается в SkillEffect'ах
    }
}
