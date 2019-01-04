package org.soraworld.lvlname;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class EventListener implements Listener {

    private final LevelManager manager;

    public EventListener(LevelManager manager) {
        this.manager = manager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        manager.broadcastLevelChange(event.getPlayer(), event.getOldLevel(), event.getNewLevel());
    }
}
