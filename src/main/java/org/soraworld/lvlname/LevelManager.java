package org.soraworld.lvlname;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.soraworld.hocon.node.Setting;
import org.soraworld.violet.manager.SpigotManager;
import org.soraworld.violet.util.ChatColor;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LevelManager extends SpigotManager {

    @Setting(comment = "comment.defaultName")
    private String defaultName = "";
    @Setting(comment = "comment.levelUP")
    private String levelUP = "";
    @Setting(comment = "comment.levelDN")
    private String levelDN = "";
    @Setting(comment = "comment.levels")
    private HashMap<String, String> lvlNames = new HashMap<>();

    private String textUP = "", textDN = "";
    private HashMap<LevelRange, String> rangeNames = new HashMap<>();
    public static final Pattern LEVEL_RANGE = Pattern.compile("\\d+-\\d+");

    public LevelManager(LevelName plugin, Path path) {
        super(plugin, path);
    }

    public ChatColor defChatColor() {
        return ChatColor.BLUE;
    }

    public void afterLoad() {
        textUP = ChatColor.colorize(levelUP);
        textDN = ChatColor.colorize(levelDN);
        rangeNames.clear();
        lvlNames.forEach((key, val) -> {
            if (key != null && LEVEL_RANGE.matcher(key).matches() && val != null) {
                rangeNames.put(new LevelRange(key), val);
            }
        });
    }

    public LevelRange getRange(int level) {
        for (LevelRange range : rangeNames.keySet()) {
            if (range.match(level)) return range;
        }
        return null;
    }

    public String getLevelName(int level) {
        for (Map.Entry<LevelRange, String> entry : rangeNames.entrySet()) {
            if (entry.getKey().match(level)) return entry.getValue();
        }
        return defaultName;
    }

    public boolean save() {
        lvlNames.clear();
        rangeNames.forEach((range, name) -> lvlNames.put(range.toString(), name));
        return super.save();
    }

    public String getLevelName(LevelRange range) {
        return rangeNames.getOrDefault(range, defaultName);
    }

    public String getLevelName(Player player) {
        return getLevelName(player.getLevel());
    }

    public void broadcastLevelChange(Player player, int oldLevel, int newLevel) {
        LevelRange oldRange = getRange(oldLevel);
        LevelRange newRange = getRange(newLevel);
        if (oldRange != newRange) {
            String oldName = getLevelName(oldRange);
            String newName = getLevelName(newRange);
            String message = (newLevel > oldLevel ? textUP : textDN).replaceAll("%player%", player.getName())
                    .replaceAll("%oldlvl%", String.valueOf(oldLevel)).replaceAll("%oldname%", oldName)
                    .replaceAll("%newlvl%", String.valueOf(newLevel)).replaceAll("%newname%", newName);
            if (oldLevel != newLevel && !message.isEmpty()) Bukkit.broadcastMessage(message);
        }
    }
}