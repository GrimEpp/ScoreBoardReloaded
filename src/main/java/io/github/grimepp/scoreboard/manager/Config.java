package io.github.grimepp.scoreboard.manager;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Config {
    private final FileConfiguration yml;

    public Config(FileConfiguration e) {
        this.yml = e;
    }

    public <T> T get(String path) {
        return (T) yml.get(path);
    }

    public ConfigurationSection getSection(String s) {
        return yml.getConfigurationSection(s);
    }

    public String getString(String s) {
    return yml.getString(s);
    }

    public String getColouredString(String s) {
        return ChatColor.translateAlternateColorCodes('&', yml.getString(s));
    }
}
