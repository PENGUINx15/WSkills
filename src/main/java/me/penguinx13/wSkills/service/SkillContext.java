package me.penguinx13.wSkills.service;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class SkillContext {

    private final Player player;
    private final int level;
    private final Event event; // может быть null

    public SkillContext(Player player, int level, @Nullable Event event) {
        this.player = player;
        this.level = level;
        this.event = event;
    }

    public Player getPlayer() {
        return player;
    }

    public int getLevel() {
        return level;
    }

    @Nullable
    public Event getEvent() {
        return event;
    }

    public <T extends Event> @Nullable T getEvent(Class<T> type) {
        return type.isInstance(event) ? type.cast(event) : null;
    }
}
