package me.penguinx13.wSkills.skills.mining.effects;

import me.penguinx13.wSkills.API.SkillEffect;
import me.penguinx13.wSkills.service.SkillContext;
import me.penguinx13.wSkills.skills.mining.MiningEffectID;
import org.bukkit.event.block.BlockBreakEvent;

public class ExtraXp extends SkillEffect<MiningEffectID> {

    public ExtraXp() {
        super(MiningEffectID.EXTRA_XP, 2.0, 10.0);
    }

    @Override
    public void apply(SkillContext context) {
        BlockBreakEvent event = context.getEvent(BlockBreakEvent.class);
        if (event == null) {
            return;
        }

        int bonusXp = (int) Math.round(getValue(context.getLevel()));
        if (bonusXp <= 0) {
            return;
        }

        event.setExpToDrop(event.getExpToDrop() + bonusXp);
    }
}
