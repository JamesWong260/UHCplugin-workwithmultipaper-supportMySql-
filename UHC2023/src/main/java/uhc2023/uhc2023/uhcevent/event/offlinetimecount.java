package uhc2023.uhc2023.uhcevent.event;

import com.github.puregero.multilib.MultiLib;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import uhc2023.uhc2023.UHC2023;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;


public class offlinetimecount  implements CommandExecutor {
    private UHC2023 plugin;

    public offlinetimecount  (UHC2023 plugin) {
        this.plugin = plugin;
    }
    int x = 2700;

    static final String SERVERSTATUS = "SELECT id, name, playercount, pvpmode, serversession, firstjoin FROM SERVERSTATUS";
    static final String gamemodedetect = "SELECT id, name, playergamemode, onlinestatus, coordinate, point, point2, offlinecountdown FROM playerlist";
    static final String tp = "SELECT id, name, coordinate FROM playerlist";

    static final String survivor = "SELECT id, name FROM survivor";

    int offlinecount = 30;
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    final Scoreboard board = manager.getNewScoreboard();
    final Objective objective = board.registerNewObjective("Belowname", "dummy");

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String ip = UHC2023.getMain().getConfig().getString("SQL.ip");
        String table = UHC2023.getMain().getConfig().getString("SQL.table");
        String user = UHC2023.getMain().getConfig().getString("SQL.user");
        String password = UHC2023.getMain().getConfig().getString("SQL.password");
        String DB_DRIVER = "com.mysql.jdbc.Driver";
        String individual = UHC2023.getMain().getConfig().getString("SQL.individual SQL base");
        String DB_IND = "jdbc:mysql://" + ip + "/" + individual;
        String DB_URL = "jdbc:mysql://" + ip + "/" + table;
        String DB_USERNAME = user;
        String DB_PASSWORD = password;
        String pathh = "C:\\multipaper\\multipaper - Copy - Copy\\UHC2023\\";
        final Player p = (Player) sender;
        String ID = String.valueOf(p);
        String q1 = "CraftPlayer{name=";
        String q2 = "}";
        String q3 = ID.replace(q1, "").replace(q2, "");
        Path pa = Paths.get(pathh + "offlinecountdown\\" + q3 + ".txt");
        new BukkitRunnable() {
            public void run() {
                try (Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                     PreparedStatement SERVERSTATUSS = connn.prepareStatement("INSERT INTO SERVERSTATUS (id, name, playercount, pvpmode, serversession, firstjoin) VALUES (?,?,?,?,?,?)");
                     Statement stmt2 = connn.createStatement();
                     Statement stmt3 = connn.createStatement();
                     Statement stmt4 = connn.createStatement();
                     Statement stmt5 = connn.createStatement();
                     ResultSet rs = stmt2.executeQuery(SERVERSTATUS);
                     ResultSet rss = stmt3.executeQuery(gamemodedetect);
                     ResultSet rsss = stmt4.executeQuery(gamemodedetect);
                     ResultSet rssss = stmt5.executeQuery(gamemodedetect);
                ) {
                    while (rss.next()) {
                        String dataa = rss.getString("name");
                        if (dataa.equals(q3)) {
                            while (rsss.next()) {
                                String dataaa = String.valueOf(rsss.getInt("onlinestatus"));
                                if (dataaa.equals("0")) {
                                    while (rssss.next()) {
                                        int count = rssss.getInt("offlinecountdown");
                                        count -= 1;
                                        String stcount = String.valueOf(count);
                                        if (stcount.equals("0")) {
                                            cancel();
                                            for (Player all : MultiLib.getAllOnlinePlayers()) {
                                                all.sendMessage("魷魚" + q3 + "中斷連綫超過5分鐘，已被自動淘汰" + q3 + "仍然可以以旁觀者的身份進來");
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                } catch (SQLException eg) {
                    throw new RuntimeException(eg);
                }

            }
        }.runTaskTimer(plugin, 3, 20);
        return false;
    }}




