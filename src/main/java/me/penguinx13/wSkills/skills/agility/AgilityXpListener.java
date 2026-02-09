package me.penguinx13.wSkills.skills.agility;

import me.penguinx13.wSkills.API.SkillID;
import me.penguinx13.wSkills.service.SkillApplier;
import me.penguinx13.wSkills.service.SkillManager;
import me.penguinx13.wSkills.service.SkillStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AgilityXpListener implements Listener {

    private static final int WALK_XP = 1;
    private static final int SPRINT_XP = 2;
    private static final int JUMP_XP = 3;
    private static final long MOVE_XP_INTERVAL_MS = 1000L;
    private static final long JUMP_XP_INTERVAL_MS = 750L;

    private final SkillManager manager;
    private final SkillApplier applier;
    private final SkillStorage storage;
    private final Map<UUID, Long> lastMoveXp = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastJumpXp = new ConcurrentHashMap<>();

    public AgilityXpListener(SkillManager manager, SkillApplier applier, SkillStorage storage) {
        this.manager = manager;
        this.applier = applier;
        this.storage = storage;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        boolean moved = event.getFrom().getX() != event.getTo().getX()
                || event.getFrom().getZ() != event.getTo().getZ();
        boolean jumped = event.getTo().getY() > event.getFrom().getY() && !player.isOnGround();

        long now = System.currentTimeMillis();
        boolean awarded = false;

        if (moved && shouldAward(lastMoveXp, player.getUniqueId(), now, MOVE_XP_INTERVAL_MS)) {
            int xp = player.isSprinting() ? SPRINT_XP : WALK_XP;
            awarded = awardXp(player, xp);
        }

        if (jumped && shouldAward(lastJumpXp, player.getUniqueId(), now, JUMP_XP_INTERVAL_MS)) {
            awarded = awardXp(player, JUMP_XP) || awarded;
        }

        if (awarded) {
            storage.markDirty(player);
        }
    }

    private boolean awardXp(Player player, int xp) {
        int beforeLevel = manager.getLevel(player, SkillID.AGILITY);
        int afterLevel = manager.addXp(player, SkillID.AGILITY, xp);
        if (xp <= 0) {
            return false;
        }
        if (afterLevel > beforeLevel) {
            applier.applySkill(player, SkillID.AGILITY);
        }
        return true;
    }

    private boolean shouldAward(Map<UUID, Long> tracker, UUID uuid, long now, long intervalMs) {
        Long lastTime = tracker.get(uuid);
        if (lastTime != null && now - lastTime < intervalMs) {
            return false;
        }
        tracker.put(uuid, now);
        return true;
    }
}
