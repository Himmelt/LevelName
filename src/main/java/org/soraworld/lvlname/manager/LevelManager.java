package org.soraworld.lvlname.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.soraworld.hocon.node.Setting;
import org.soraworld.lvlname.LevelName;
import org.soraworld.lvlname.range.LevelRange;
import org.soraworld.violet.manager.SpigotManager;
import org.soraworld.violet.util.ChatColor;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class LevelManager extends SpigotManager {

    @Setting(comment = "comment.defaultName")
    private String defaultName = "";
    @Setting(comment = "comment.levelUP")
    private String levelUP = "";
    @Setting(comment = "comment.levelDN")
    private String levelDN = "";
    @Setting
    private String levelFormat = "&8[&fLv.%level%&b&7]&r";
    private String levelColorFormat = "";
    @Setting(comment = "comment.levels")
    private HashMap<String, String> lvlNames = new HashMap<>();
    @Setting(comment = "comment.awards")
    private HashMap<String, Integer> lvlAwards = new HashMap<>();
    @Setting
    private TreeMap<String, Integer> recordLevels = new TreeMap<>();

    private HashMap<LevelRange, String> rangeNames = new HashMap<>();
    private HashMap<LevelRange, Integer> rangeAwards = new HashMap<>();
    public static final Pattern LEVEL_RANGE = Pattern.compile("\\d+-\\d+");

    public LevelManager(LevelName plugin, Path path) {
        super(plugin, path);
    }

    public ChatColor defChatColor() {
        return ChatColor.BLUE;
    }

    public void afterLoad() {
        levelColorFormat = ChatColor.colorize(levelFormat) + ChatColor.RESET;
        rangeNames.clear();
        rangeAwards.clear();
        lvlNames.forEach((key, val) -> {
            if (key != null && LEVEL_RANGE.matcher(key).matches() && val != null) {
                rangeNames.put(new LevelRange(key), val);
            }
        });
        lvlAwards.forEach((key, val) -> {
            if (key != null && LEVEL_RANGE.matcher(key).matches() && val != null) {
                rangeAwards.put(new LevelRange(key), val);
            }
        });
    }

    public boolean save() {
        lvlNames.clear();
        lvlAwards.clear();
        rangeNames.forEach((range, name) -> lvlNames.put(range.toString(), name));
        rangeAwards.forEach((range, award) -> lvlAwards.put(range.toString(), award));
        return super.save();
    }

    public String getLevelFormat(int level) {
        return levelColorFormat.replaceAll("%level%", String.valueOf(level));
    }

    public LevelRange getRange(int level) {
        for (LevelRange range : rangeNames.keySet()) {
            if (range.match(level)) return range;
        }
        return null;
    }

    public String getLevelName(int level) {
        for (Map.Entry<LevelRange, String> entry : rangeNames.entrySet()) {
            if (entry.getKey().match(level)) {
                return ChatColor.colorize(entry.getValue());
            }
        }
        return ChatColor.colorize(defaultName);
    }

    public String getLevelName(LevelRange range) {
        return ChatColor.colorize(rangeNames.getOrDefault(range, defaultName));
    }

    public String getLevelName(Player player) {
        return getLevelName(player.getLevel());
    }

    public void broadcastLevelChange(Player player, int oldLevel, int newLevel) {
        LevelRange oldRange = getRange(oldLevel);
        LevelRange newRange = getRange(newLevel);
        if (oldRange != newRange) {
            int award = rangeAwards.getOrDefault(newRange, 0);
            if (newLevel > oldLevel && newLevel > recordLevels.computeIfAbsent(player.getName(), name -> 0) && award != 0) {
                recordLevels.put(player.getName(), newLevel);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + player.getName() + " " + award);
            }
            String oldName = getLevelName(oldRange);
            String newName = getLevelName(newRange);
            String message = (newLevel > oldLevel ? levelUP : levelDN).replaceAll("%player%", player.getName())
                    .replaceAll("%oldlvl%", String.valueOf(oldLevel)).replaceAll("%oldname%", oldName)
                    .replaceAll("%newlvl%", String.valueOf(newLevel)).replaceAll("%newname%", newName);
            if (oldLevel != newLevel && !message.isEmpty()) {
                Bukkit.broadcastMessage(ChatColor.colorize(message));
            }
        }
    }
}
