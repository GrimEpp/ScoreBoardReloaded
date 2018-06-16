package io.github.grimepp.scoreboard.manager;

import me.clip.placeholderapi.PlaceholderAPI;
import io.github.grimepp.scoreboard.ScoreboardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ScoreBoardManager implements ConfigAccess {
        private static Map<Player, ScoreBoardManager> plasys;
        static {
            plasys = new HashMap<>();
        }
        private Map<Integer,String> map;
        private Scoreboard scoreboard;
        private boolean bb;
        private Player p;

        public ScoreBoardManager(Player p) {
            this.p = p;
            bb  = false;
            if (plasys.containsKey(p))
                plasys.get(p).bb = true;
            plasys.put(p, this);
        }
        public void enable() {
                ConfigurationSection section = getConfig().getSection("settings.scoreboard.lines");
                AtomicInteger integer = new AtomicInteger(section.getKeys(false).size());
                ColorSession session = new ColorSession();
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                Objective objective = scoreboard.registerNewObjective("sc", "dummy");
                objective.setDisplayName(getConfig().getColouredString("settings.scoreboard.name"));
                Map<Integer, String> stringMap = new HashMap<>();
            section.getKeys(false).forEach(s -> {
                    final String ss = prepare(getConfig().getString("settings.scoreboard.lines."+s), p);
                    String ss1 = prepareStatic(getConfig().getString("settings.scoreboard.lines."+s));
                    if (ss.equals(ss1)) {
                        if (ss.length() < 48) {
                            String next = session.next();
                            Team team = scoreboard.registerNewTeam(s);
                            int i = integer.decrementAndGet();
                            String ssss = null;
                            if (ss.length() > 31) {
                                team.setPrefix(ss.substring(0,16));
                                team.addEntry(ss.substring(16,32));
                                ssss = ss.substring(16, 32);
                                team.setSuffix(ss.substring(32, ss.length()));
                        } else if (ss.length() > 15) {
                                team.setPrefix(ss.substring(0, 16));
                                team.setSuffix((ChatColor.getLastColors(ss.substring(0, 16)).equals("") ? ChatColor.RESET.toString() : ChatColor.getLastColors(ss.substring(0, 16))) + ss.substring(16, ss.length()));
                                team.addEntry(next);
                            } else {
                                team.setPrefix(ss);
                                team.addEntry(next);
                            }
                            if (ssss == null)
                            objective.getScore(next).setScore(i);
                            else
                                objective.getScore(ssss).setScore(i);
                        }
                    } else {
                        if (ss.length() < 32) {
                            String next = session.next();
                            Team team = scoreboard.registerNewTeam(s);
                            int i = integer.decrementAndGet();
                            team.addEntry(next);
                            if (ss.length() > 15) {
                                team.setPrefix(ss.substring(0, 16));
                                team.setSuffix((ChatColor.getLastColors(ss.substring(0, 16)).equals("") ? ChatColor.RESET.toString() : ChatColor.getLastColors(ss.substring(0, 16))) + ss.substring(16, ss.length()));
                            } else {
                                team.setPrefix(ss);
                            }
                            objective.getScore(next).setScore(i);
                            stringMap.put(i, s);
                        }
                    }
                });
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                this.map = stringMap;
                this.scoreboard = scoreboard;
                p.setScoreboard(scoreboard);
            update();
        }

    private String prepareStatic(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
        }

    private void update() {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!p.isOnline() || bb)
                        cancel();
                    try {
                            map.forEach((i, s) -> {
                                Team team = scoreboard.getTeam(s);
                                String real = prepare(getConfig().getString("settings.scoreboard.lines."+s), p);
                                if (real.length() > 16) {
                                    team.setPrefix(real.substring(0, 16));
                                    team.setSuffix((ChatColor.getLastColors(real.substring(0, 16)).equals("") ? ChatColor.RESET.toString() :ChatColor.getLastColors(real.substring(0, 16))) + real.substring(16, real.length()));
                                } else {
                                    team.setPrefix(real);
                                    team.setSuffix("");
                                }
                            });
                    } catch (NullPointerException e) {
                        cancel();
                    }
                }
            }.runTaskTimer(ScoreboardPlugin.getInstance(), 10, 10);
        }
        private String prepare(String s, Player p) {
           if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            s=PlaceholderAPI.setPlaceholders(p,s);
            return ChatColor.translateAlternateColorCodes('&', s);
        }


        public class ColorSession {
            private boolean append;
            private Queue<ChatColor> colors;
            ColorSession() {
                colors = new PriorityQueue<>();
                prepare();
            }

            String next() {
                ChatColor poll = colors.poll();
                if (poll == null) {
                    prepare();
                    append = true;
                    poll = colors.poll();
                }
                if (append) {
                    return ChatColor.GREEN.toString() + poll;
                }
                return poll + "";
            }

            private void prepare() {
                for (ChatColor chatColor : ChatColor.values()) {
                    if (!chatColor.equals(ChatColor.STRIKETHROUGH) && !chatColor.equals(ChatColor.BOLD) && !chatColor.equals(ChatColor.MAGIC) && !chatColor.equals(ChatColor.ITALIC) && !chatColor.equals(ChatColor.UNDERLINE) )
                        colors.add(chatColor);
                }
            }
        }
    }
