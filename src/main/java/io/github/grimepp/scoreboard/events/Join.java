package io.github.grimepp.scoreboard.events;

import io.github.grimepp.scoreboard.manager.ScoreBoardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        new ScoreBoardManager(e.getPlayer()).enable();
    }
}
