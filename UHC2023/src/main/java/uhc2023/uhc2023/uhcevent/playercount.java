package uhc2023.uhc2023.uhcevent;

import com.github.puregero.multilib.MultiLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import uhc2023.uhc2023.UHC2023;

import java.sql.*;

public class playercount implements CommandExecutor {

    int x = 0;
    private UHC2023 plugin;


    static final String SERVERSTATUS = "SELECT id, name, playercount, pvpmode, serversession, firstjoin FROM SERVERSTATUS";
    static final String gamemodedetect = "SELECT id, name, playergamemode, onlinestatus, coordinate, point, point2, offlinecountdown FROM playerlist";

    static final String survivor = "SELECT id, name FROM survivor";

    public playercount(UHC2023 plugin) {
        this.plugin = plugin;
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String ip = plugin.getConfig().getString("SQL.ip");
        String table = plugin.getConfig().getString("SQL.table");
        String user = plugin.getConfig().getString("SQL.user");
        String password = plugin.getConfig().getString("SQL.password");
        String DB_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://"+ ip + "/" +table;
        String DB_USERNAME = user;
        String DB_PASSWORD = password;

        int count = 0;
            try (Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                 PreparedStatement SERVERSTATUSS = connn.prepareStatement("INSERT INTO SERVERSTATUS (id, name, playercount, pvpmode, serversession, firstjoin) VALUES (?,?,?,?,?,?)");
                 Statement stmt1 = connn.createStatement();
                 Statement stmt2 = connn.createStatement();
                 ResultSet rs = stmt1.executeQuery(SERVERSTATUS);
                 ResultSet rss = stmt2.executeQuery(gamemodedetect);

            ) {
                while (rs.next()) {
                    String data = rs.getString("serversession");
                    if (data.equals("1")) {
                        for(Player all : MultiLib.getAllOnlinePlayers()) {
                            if(all.getGameMode().equals(GameMode.ADVENTURE)) {
                                count++;
                            }
                        }
                        System.out.println(count);
                        String dataa = String.valueOf(count);
                        String sqldata = "UPDATE SERVERSTATUS " +
                                "SET playercount ='" + dataa + "'WHERE id in (1)";
                        SERVERSTATUSS.executeUpdate(sqldata);
                    }
                    if (data.equals("2")) {
                        while (rss.next()) {
                            for(Player all : MultiLib.getAllOnlinePlayers()) {
                                if(all.getGameMode().equals(GameMode.SURVIVAL)) {
                                    count++;
                                }
                            }
                            System.out.println(count);
                            String dataa = String.valueOf(count);
                            String sqldata = "UPDATE SERVERSTATUS " +
                                    "SET playercount ='" + dataa + "'WHERE id in (1)";
                            SERVERSTATUSS.executeUpdate(sqldata);
                        }
                    }

                }

            } catch (SQLException ed) {
                ed.printStackTrace();
            }

            try (Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                 Statement stmt2 = connn.createStatement();
                 ResultSet rs = stmt2.executeQuery(SERVERSTATUS);
            ) {
                while (rs.next()) {
                    int xxx = rs.getInt("playercount");
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
            } catch (SQLException eQ) {
                eQ.printStackTrace();
            }

        return false;
    }
}
