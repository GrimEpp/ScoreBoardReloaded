package io.github.grimepp.scoreboard.commands;

import io.github.grimepp.scoreboard.ScoreboardPlugin;
import io.github.grimepp.scoreboard.manager.ConfigAccess;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reload implements CommandExecutor, ConfigAccess {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission((String) getConfig().get("settings.configreloadpermission"))) {
            commandSender.sendMessage((String) getConfig().get("settings.noperm"));
            return true;
        }
        ScoreboardPlugin.getInstance().reloadConfig();
        commandSender.sendMessage((String) getConfig().get("settings.reloaded"));
        return true;
    }
}
