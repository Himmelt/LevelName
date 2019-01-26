package org.soraworld.lvlname;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.event.Listener;
import org.soraworld.lvlname.expansion.NameExpansion;
import org.soraworld.lvlname.listener.EventListener;
import org.soraworld.lvlname.manager.LevelManager;
import org.soraworld.violet.command.SpigotBaseSubs;
import org.soraworld.violet.command.SpigotCommand;
import org.soraworld.violet.manager.SpigotManager;
import org.soraworld.violet.plugin.SpigotPlugin;
import org.soraworld.violet.util.ChatColor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LevelName extends SpigotPlugin {

    private static final boolean placeholderApi;

    static {
        boolean placeholder = false;
        try {
            PlaceholderAPI.class.getName();
            PlaceholderExpansion.class.getName();
            placeholder = true;
        } catch (Throwable ignored) {
        }

        placeholderApi = placeholder;
    }

    public SpigotManager registerManager(Path path) {
        return new LevelManager(this, path);
    }

    public void registerCommands() {
        SpigotCommand command = new SpigotCommand(this.getId(), this.manager.defAdminPerm(), false, this.manager, "lvlname", "lname");
        command.extractSub(SpigotBaseSubs.class);
        command.setUsage("/lvlname ");
        register(this, command);
    }

    public List<Listener> registerListeners() {
        ArrayList<Listener> listeners = new ArrayList<>();
        listeners.add(new EventListener((LevelManager) manager));
        return listeners;
    }

    public void afterEnable() {
        if (placeholderApi) {
            try {
                PlaceholderExpansion expansion = NameExpansion.class.getConstructor(LevelManager.class).newInstance(manager);
                if (PlaceholderAPI.registerPlaceholderHook(expansion.getIdentifier(), expansion)) {
                    manager.consoleKey("placeholder.expansionSuccess");
                } else manager.consoleKey("placeholder.expansionFailed");
            } catch (Throwable ignored) {
                manager.console(ChatColor.RED + "NameExpansion Construct Instance failed !!!");
            }
        } else manager.consoleKey("placeholder.notHook");
    }
}
