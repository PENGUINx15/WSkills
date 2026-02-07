package me.penguinx13.wSkills.skills.mining;

import me.penguinx13.wSkills.API.SkillType;
import me.penguinx13.wSkills.service.SkillApplier;
import me.penguinx13.wSkills.service.SkillManager;
import me.penguinx13.wSkills.service.SkillStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class MiningXpListener implements Listener {

    private static final int BREAK_XP = 5;

    private final SkillManager manager;
    private final SkillApplier applier;
    private final SkillStorage storage;

    public MiningXpListener(SkillManager manager, SkillApplier applier, SkillStorage storage) {
        this.manager = manager;
        this.applier = applier;
        this.storage = storage;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (event.isCancelled()) {
            return;
        }

        int beforeLevel = manager.getLevel(player, SkillType.MINING);
        int afterLevel = manager.addXp(player, SkillType.MINING, BREAK_XP);

        if (afterLevel > beforeLevel) {
            applier.applySkill(player, SkillType.MINING);
        }

        storage.save(player, manager);
    }
}
