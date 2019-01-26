package org.soraworld.lvlname.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.soraworld.lvlname.manager.LevelManager;

public class NameExpansion extends PlaceholderExpansion {

    private final LevelManager manager;

    public NameExpansion(LevelManager manager) {
        this.manager = manager;
    }

    public String getIdentifier() {
        return manager.getPlugin().getId();
    }

    public String getPlugin() {
        return manager.getPlugin().getId();
    }

    public String getAuthor() {
        return "Himmelt";
    }

    public String getVersion() {
        return manager.getPlugin().getVersion();
    }

    public String onPlaceholderRequest(Player player, String params) {
        if (params.equalsIgnoreCase("name")) return manager.getLevelName(player);
        return "";
    }
}
