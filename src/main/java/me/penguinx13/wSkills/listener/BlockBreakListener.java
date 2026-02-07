package me.penguinx13.wSkills.listener;


import me.penguinx13.wSkills.API.SkillType;
import me.penguinx13.wSkills.service.SkillApplier;
import me.penguinx13.wSkills.service.SkillManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        int miningLevel = SkillManager.get().getLevel(player, SkillType.MINING);
        if (miningLevel > 0) {
            SkillApplier.get().applySkill(player, SkillType.MINING);
        }
    }
}
