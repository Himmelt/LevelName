package org.soraworld.lvlname.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.soraworld.lvlname.manager.LevelManager;

public class EventListener implements Listener {

    private final LevelManager manager;

    public EventListener(LevelManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat1(AsyncPlayerChatEvent event) {
        if (event.getFormat().contains("%1$s")) {
            int level = event.getPlayer().getLevel();
            String lvlName = manager.getLevelName(level);
            if (lvlName != null) {
                StringBuilder build = new StringBuilder(event.getFormat());
                build.insert(build.indexOf("%1$s"), lvlName);
                event.setFormat(build.toString());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerChat2(AsyncPlayerChatEvent event) {
        int level = event.getPlayer().getLevel();
        event.setFormat(manager.getLevelFormat(level) + event.getFormat());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        manager.broadcastLevelChange(event.getPlayer(), event.getOldLevel(), event.getNewLevel());
    }
}
