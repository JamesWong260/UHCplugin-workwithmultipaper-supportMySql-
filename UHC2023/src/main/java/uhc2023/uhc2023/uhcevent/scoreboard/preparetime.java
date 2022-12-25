package uhc2023.uhc2023.uhcevent.scoreboard;

import com.github.puregero.multilib.MultiLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import uhc2023.uhc2023.UHC2023;

import java.util.Random;


public class preparetime implements CommandExecutor {
    private UHC2023 plugin;

    public preparetime (UHC2023 plugin) {
        this.plugin = plugin;
    }

    int border = 2000;
    int bordercount = 3600;
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    final Scoreboard board = manager.getNewScoreboard();
    final Objective objective = board.registerNewObjective("Belowname", "dummy");


    static final String SERVERSTATUS = "SELECT id, name, playercount, pvpmode, serversession, firstjoin FROM SERVERSTATUS";
    static final String gamemodedetect = "SELECT id, name, playergamemode, onlinestatus, coordinate, point, offlinecountdown FROM playerlist";

    static final String survivor = "SELECT id, name FROM survivor";
    String pathh = "C:\\multipaper\\multipaper - Copy - Copy\\UHC2023\\";
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String ip = UHC2023.getMain().getConfig().getString("SQL.ip");
        String table = UHC2023.getMain().getConfig().getString("SQL.table");
        String user =  UHC2023.getMain().getConfig().getString("SQL.user");
        String password = UHC2023.getMain().getConfig().getString("SQL.password");
        String DB_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://"+ ip + "/" +table;
        String DB_USERNAME = user;
        String DB_PASSWORD = password;
        Random random = new Random();
        final int[] preparetime = {UHC2023.getMain().getConfig().getInt("UHC.prepare time")};
        final int[] battle = {UHC2023.getMain().getConfig().getInt("UHC.battle time")};
        final Player p = (Player) sender;
            if (p.isOp()) {
                try{
                    new BukkitRunnable() {
                        public void run() {
                        int playeremain = 0;
                        for(Player alive : MultiLib.getAllOnlinePlayers()) {
                            if(alive.getGameMode().equals(GameMode.SURVIVAL)) {
                                playeremain++;
                            }
                        }
                        double wB = Bukkit.getServer().getWorld("world").getWorldBorder().getSize();
                        int wBint = (int) wB;
                        ScoreboardManager manager = Bukkit.getScoreboardManager();
                        final Scoreboard board = manager.getNewScoreboard();
                        final Objective objective = board.registerNewObjective("test", "dummy");
                        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                        objective.setDisplayName(ChatColor.RED + "UHC大賽第二季");
                        Score score2 = objective.getScore("距離戰鬥時間");
                        Score score3 = objective.getScore("邊界半徑");
                        Score score4 = objective.getScore("目前分流剩餘玩家");
                        score3.setScore(Integer.parseInt(String.valueOf(wBint)) / 2);
                        score2.setScore(preparetime[0]);
                        score4.setScore(playeremain);

                        for (Player all : MultiLib.getAllOnlinePlayers()) {
                            all.setScoreboard(board);
                        }
                        preparetime[0] -= 1;
                        if (preparetime[0] == 0) {
                            cancel();
                        }
                        }}.runTaskTimer(plugin, 0, 20);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                }
                }else {
                p.sendMessage("no permission");
            }
        return false;
}
}



