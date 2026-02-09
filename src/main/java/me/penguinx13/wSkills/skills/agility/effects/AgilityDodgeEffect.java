package me.penguinx13.wSkills.skills.agility.effects;

import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.service.SkillContext;

public class AgilityDodgeEffect implements SkillEffect {

    @Override
    public void apply(SkillContext context) {

    }
}
//@EventHandler
//public void onDamage(EntityDamageEvent event) {
//    if (!(event.getEntity() instanceof Player player)) return;
//
//    int level = SkillManager.get().getLevel(player, SkillType.AGILITY);
//    double chance = Math.min(0.25, level * 0.025);
//
//    if (ChanceUtil.roll(chance)) {
//        event.setCancelled(true);
//        player.getWorld().spawnParticle(
//                Particle.CLOUD,
//                player.getLocation().add(0, 1, 0),
//                10
//        );
//    }
//}