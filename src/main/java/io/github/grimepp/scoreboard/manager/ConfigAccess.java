package io.github.grimepp.scoreboard.manager;

import io.github.grimepp.scoreboard.ScoreboardPlugin;

public interface ConfigAccess {
    default Config getConfig() {
      return ScoreboardPlugin.getConfig1();
    }
}
