package me.penguinx13.wSkills.listener;

import me.penguinx13.wSkills.API.SkillID;
import me.penguinx13.wSkills.service.SkillApplier;
import me.penguinx13.wSkills.service.SkillContext;
import me.penguinx13.wSkills.service.SkillManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    public void onBlockBreak (BlockBreakEvent event){
        Player player = event.getPlayer();
        int level = SkillManager.get().getLevel(player, SkillID.MINING);

        SkillContext context = new SkillContext(player, level, event);
        SkillApplier.get().applySkill(SkillID.MINING, context);
    }
}
