package me.penguinx13.wSkills.skills.agility.effects;

import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.service.SkillContext;
import me.penguinx13.wSkills.skills.agility.AgilityEffectID;
import me.penguinx13.wSkills.util.ChanceUtil;
import org.bukkit.Particle;
import org.bukkit.event.entity.EntityDamageEvent;

public class AgilityDodgeEffect extends SkillEffect<AgilityEffectID> {

    public AgilityDodgeEffect() {
        super(AgilityEffectID.DODGE, 0.025, 0.25);
    }

    @Override
    public void apply(SkillContext context) {
        EntityDamageEvent event = context.getEvent(EntityDamageEvent.class);
        if (event == null) {
            return;
        }

        double chance = getValue(context.getLevel());
        if (!ChanceUtil.roll(chance)) {
            return;
        }

        event.setCancelled(true);
        context.getPlayer().getWorld().spawnParticle(
                Particle.CLOUD,
                context.getPlayer().getLocation().add(0, 1, 0),
                10
        );
    }
}
