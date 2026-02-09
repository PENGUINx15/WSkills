package me.penguinx13.wSkills.skills.mining.effects;

import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.service.SkillContext;
import me.penguinx13.wSkills.skills.mining.MiningEffectID;
import me.penguinx13.wSkills.util.ChanceUtil;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class DurabilitySave extends SkillEffect<MiningEffectID> {

    public DurabilitySave() {
        super(MiningEffectID.DURABILITY_SAVE, 0.10, 0.50);
    }

    @Override
    public void apply(SkillContext context) {
        PlayerItemDamageEvent event = context.getEvent(PlayerItemDamageEvent.class);
        if (event == null) {
            return;
        }

        if (!isMiningTool(event.getItem().getType())) {
            return;
        }

        double chance = getValue(context.getLevel());
        if (!ChanceUtil.roll(chance)) {
            return;
        }

        event.setCancelled(true);
    }

    private boolean isMiningTool(Material material) {
        return material.name().endsWith("_PICKAXE");
    }
}
