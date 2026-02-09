package me.penguinx13.wSkills.skills.mining.effects;

import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.service.SkillContext;
import me.penguinx13.wSkills.skills.mining.MiningEffectID;
import me.penguinx13.wSkills.util.ChanceUtil;
import me.penguinx13.wSkills.util.CoreProtectUtil;
import org.bukkit.event.block.BlockBreakEvent;

public class DoubleDrop extends SkillEffect {

    public DoubleDrop() {
        super(MiningEffectID.DOUBLE_DROP, 0.05, 1.0);
    }


    @Override
    public void apply(SkillContext context) {
        BlockBreakEvent event = context.getEvent(BlockBreakEvent.class);
        if (event == null) return;

        if (CoreProtectUtil.isPlayerPlaced(event.getBlock())) return;

        double chance = getChance(context.getLevel());
        if (!ChanceUtil.roll(chance)) return;

        event.getBlock().getDrops(
                context.getPlayer().getInventory().getItemInMainHand()
        ).forEach(drop ->
                event.getBlock().getWorld()
                        .dropItemNaturally(event.getBlock().getLocation(), drop.clone())
        );
    }
}

