package io.github.grimepp.scoreboard.commands;

import io.github.grimepp.scoreboard.ScoreboardPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("admin.reload")) {
            commandSender.sendMessage("&c&lFlame&b&lHeroes&8: &cDu har ikke tilgang!");
            return true;
        }
        ScoreboardPlugin.getInstance().reloadConfig();
        commandSender.sendMessage("Du reloaded config!");
        return true;
    }
}
