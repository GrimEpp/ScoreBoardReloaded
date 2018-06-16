package io.github.grimepp.scoreboard;

import io.github.grimepp.scoreboard.commands.Reload;
import io.github.grimepp.scoreboard.events.Join;
import io.github.grimepp.scoreboard.manager.Config;
import io.github.grimepp.scoreboard.manager.ScoreBoardManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ScoreboardPlugin extends JavaPlugin {
    private static ScoreboardPlugin scoreboardpl;
    private Config config;

    public static Config getConfig1() {
        return scoreboardpl.config;
    }
    public static ScoreboardPlugin getInstance() {
        return scoreboardpl;
    }

    @Override
    public void onEnable() {
        scoreboardpl = this;
        registerConfig();
        registerEvents();
        registerCommands();
    }


    private void registerCommands() {
        getCommand("reloadScoreboard").setExecutor(new Reload());
    }

    private void registerConfig() {
        saveDefaultConfig();
        config = new Config(this.getConfig());
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new Join(), this);
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        config = new Config(getConfig());
        Bukkit.getOnlinePlayers().forEach(p->new ScoreBoardManager(p).enable());
    }

}
