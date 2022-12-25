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

import java.sql.*;
import java.util.Random;

public class preparescoreboardcommand implements CommandExecutor {

    int x = 0;
    private UHC2023 plugin;



    static final String SERVERSTATUS = "SELECT id, name, playercount, pvpmode, serversession, firstjoin FROM SERVERSTATUS";
    static final String gamemodedetect = "SELECT id, name, playergamemode, onlinestatus, coordinate, point, point2, offlinecountdown FROM playerlist";

    static final String survivor = "SELECT id, name FROM survivor";

    public preparescoreboardcommand(UHC2023 plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String ip = UHC2023.getMain().getConfig().getString("SQL.ip");
        String table = UHC2023.getMain().getConfig().getString("SQL.table");
        String user =  UHC2023.getMain().getConfig().getString("SQL.user");
        String password = UHC2023.getMain().getConfig().getString("SQL.password");
        String DB_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://"+ ip + "/" +table;
        String DB_USERNAME = user;
        String DB_PASSWORD = password;
        Player player = (Player) sender;
        Random random = new Random();
        String ID = String.valueOf(player);
        String q1 = "CraftPlayer{name=";
        String q2 = "}";
        String q3 = ID.replace(q1, "").replace(q2, "");
        Bukkit.getServer().dispatchCommand(player, "session3");

        new BukkitRunnable() {
            public void run() {
                try (Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                     PreparedStatement SERVERSTATUSS = connn.prepareStatement("INSERT INTO SERVERSTATUS (id, name, playercount, pvpmode, serversession, firstjoin) VALUES (?,?,?,?,?,?)");
                     Statement stmt2 = connn.createStatement();
                     Statement stmt3 = connn.createStatement();
                     ResultSet rs = stmt2.executeQuery(SERVERSTATUS);
                     ResultSet rss = stmt3.executeQuery(SERVERSTATUS);
                ) {
                    String sqldataa = "UPDATE SERVERSTATUS " +
                            "SET firstjoin = 1 WHERE id in (1)";
                    while (rs.next()) {
                        String dataa = String.valueOf(rs.getInt("serversession"));
                        if (dataa.equals("1")) {
                            if (player.isOp()) {
                                int count = 0;
                                for (Player alive : MultiLib.getAllOnlinePlayers()) {
                                    if (alive.getGameMode().equals(GameMode.ADVENTURE)) {
                                        count++;
                                    }
                                }
                                int xxx = Math.toIntExact(count);
                                System.out.println("Inserted records into the table...");
                                ScoreboardManager manager = Bukkit.getScoreboardManager();
                                final Scoreboard board = manager.getNewScoreboard();
                                final Objective objective = board.registerNewObjective("test", "dummy");
                                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                                objective.setDisplayName(ChatColor.RED + "UHC大賽第二季");
                                Score score = objective.getScore("在綫人數");
                                score.setScore(xxx);
                                for (Player all : MultiLib.getAllOnlinePlayers()) {
                                    all.setScoreboard(board);
                                }
                            }
                        }
                        if (dataa.equals("2")) {
                                cancel();
                        }
                    }
                } catch (SQLException eg) {
                    throw new RuntimeException(eg);
                }
            }
        }.runTaskTimer(plugin, 0, 10);
        return false;
    }
}

