package me.penguinx13.wSkills.skills.mining;

import me.penguinx13.wSkills.API.SkillID;
import me.penguinx13.wSkills.service.SkillApplier;
import me.penguinx13.wSkills.service.SkillContext;
import me.penguinx13.wSkills.service.SkillManager;
import me.penguinx13.wSkills.service.SkillStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class XpListener implements Listener {

    private static final int BREAK_XP = 5;

    private final SkillStorage storage;

    public XpListener(SkillStorage storage) {
        this.storage = storage;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (event.isCancelled()) {
            return;
        }

        int level = SkillManager.get().getLevel(player, SkillID.MINING);
        SkillApplier.get().applySkill(SkillID.MINING, new SkillContext(player, level, event));

        int beforeLevel = SkillManager.get().getLevel(player, SkillID.MINING);
        int afterLevel = SkillManager.get().addXp(player, SkillID.MINING, BREAK_XP);

        if (afterLevel > beforeLevel) {
            SkillApplier.get().applySkill(player, SkillID.MINING);
        }

        storage.save(player, SkillManager.get());
    }
}
