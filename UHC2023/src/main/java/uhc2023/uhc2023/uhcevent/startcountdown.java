package uhc2023.uhc2023.uhcevent;

import com.github.puregero.multilib.MultiLib;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import uhc2023.uhc2023.UHC2023;

import java.sql.*;
import java.util.ArrayList;

public class startcountdown implements CommandExecutor {

    private static UHC2023 playerlist;
    int x = 10;
    private UHC2023 plugin;



    static final String SERVERSTATUS = "SELECT id, name, playercount, pvpmode, serversession, firstjoin FROM SERVERSTATUS";
    static final String gamemodedetect = "SELECT id, name, playergamemode, onlinestatus, coordinate, point, point2, offlinecountdown FROM playerlist";

    static final String survivor = "SELECT id, name FROM survivor";

    static final String tp = "SELECT id, name, coordinate FROM playerlist";

    public startcountdown(UHC2023 plugin) {
        this.plugin = plugin;
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String ip = UHC2023.getMain().getConfig().getString("SQL.ip");
        String table = UHC2023.getMain().getConfig().getString("SQL.table");
        String user = UHC2023.getMain().getConfig().getString("SQL.user");
        String password = UHC2023.getMain().getConfig().getString("SQL.password");
        String DB_DRIVER = "com.mysql.jdbc.Driver";
        String individual = UHC2023.getMain().getConfig().getString("SQL.individual SQL base");
        String DB_IND = "jdbc:mysql://"+ ip + "/" +individual;
        String DB_URL = "jdbc:mysql://"+ ip + "/" +table;
        String DB_USERNAME = user;
        String DB_PASSWORD = password;
        int boradersize =  plugin.getConfig().getInt("UHC.Border size");
           Player p = (Player) sender;
            String ID = String.valueOf(p);
            String q1 = "CraftPlayer{name=";
            String q2 = "}";
            String q3 = ID.replace(q1, "").replace(q2, "");
            if (sender instanceof Player) {
                if (p.isOp()) {
                    new BukkitRunnable() {
                        public void run() {
                            p.sendMessage(String.valueOf(x));
                            Bukkit.getServer().dispatchCommand(p, "playsound ui.button.click master @a");
                            x -= 1;
                            if (x == 0) {
                                cancel();
                            }
                        }
                    }.runTaskTimer(plugin, 0, 20);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(UHC2023.getMain(), new Runnable() {
                        public void run() {
                            p.sendMessage("go");
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(UHC2023.getMain(), new Runnable() {
                                public void run() {
                                    try (Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                                         PreparedStatement SERVERSTATUSS = connn.prepareStatement("INSERT INTO SERVERSTATUS (id, name, playercount, pvpmode, serversession, firstjoin) VALUES (?,?,?,?,?,?)");
                                         Statement stmt2 = connn.createStatement();
                                         ResultSet rs = stmt2.executeQuery(SERVERSTATUS);
                                    ) {
                                        while (rs.next()) {
                                            String sqldata = "UPDATE SERVERSTATUS " +
                                                    "SET serversession = 2 WHERE id in (1)";
                                            SERVERSTATUSS.executeUpdate(sqldata);
                                            System.out.println("Inserted records into the table...");
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 5L);
                            Bukkit.getServer().dispatchCommand(p, "playsound minecraft:entity.wither.spawn master @a");
                            final int[] preparetime = {UHC2023.getMain().getConfig().getInt("UHC.prepare time")};
                            final int[] battle = {UHC2023.getMain().getConfig().getInt("UHC.battle time")};
                                    try (Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                                         Statement stmt2 = connn.createStatement();
                                         ResultSet rs = stmt2.executeQuery(gamemodedetect);
                                    ) {

                                        while (rs.next()) {
                                            String data2 = String.valueOf(rs.getString("name"));
                                            String datatp = String.valueOf(rs.getString("coordinate"));
                                            for (Player all : MultiLib.getAllOnlinePlayers()) {
                                                if (!all.isOp()) {
                                                    all.setGameMode(GameMode.SURVIVAL);
                                                }
                                            }
                                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "worldborder set "+ boradersize*2 +" 0");
                                            try (Connection connnn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                                                 PreparedStatement playerlist = connnn.prepareStatement("INSERT INTO playerlist (id, name, playergamemode, onlinestatus, coordinate, point, point2, offlinecountdown) VALUES (?,?,?,?,?,?,?,?)");
                                            ) {
                                                String sqldata = "UPDATE playerlist " +
                                                        "SET playergamemode = 2 WHERE name in ('" + data2 + "')";
                                                playerlist.executeUpdate(sqldata);
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }}catch (SQLException es) {
                                        es.printStackTrace();
                                    }
                                }


                    }, 202L);
                    ArrayList<String> players = new ArrayList<String>();
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(UHC2023.getMain(), new Runnable() {
                        public void run() {
                            for (Player all : MultiLib.getAllOnlinePlayers()) {
                                all.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                            }
                            String commandStr = "session7";
                            MultiLib.chatOnOtherServers(p, "/" + commandStr);
                        }}, 218L);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(UHC2023.getMain(), new Runnable() {
                        public void run() {
                            String commandStr2 = "session1";
                            Bukkit.getServer().dispatchCommand(p, "session1");
                            MultiLib.chatOnOtherServers(p, "/" + commandStr2);
                        }}, 500L);
                    final int[] preparetime = {UHC2023.getMain().getConfig().getInt("UHC.prepare time")};
                    final int[] battle = {UHC2023.getMain().getConfig().getInt("UHC.battle time")};
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(UHC2023.getMain(), new Runnable() {
                        public void run() {
                    Bukkit.getServer().dispatchCommand(p, "playsound minecraft:entity.wither.spawn master @a");
                    Bukkit.getServer().dispatchCommand(p, "fill -15 200 -15 15 210 15 air");
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "worldborder set 10 " + battle[0]);
                        }}, preparetime[0] * 20+540L);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(UHC2023.getMain(), new Runnable() {
                        public void run() {
                            for (Player all : MultiLib.getAllOnlinePlayers()) {
                                all.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                            }
                            String commandStr = "session6";
                            MultiLib.chatOnOtherServers(p, "/" + commandStr);
                            Bukkit.getServer().dispatchCommand(p, "session6");
                            try (Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                                 PreparedStatement SERVERSTATUSS = connn.prepareStatement("INSERT INTO SERVERSTATUS (id, name, playercount, pvpmode, serversession, firstjoin) VALUES (?,?,?,?,?,?)");
                            ) {
                                String sqldata = "UPDATE SERVERSTATUS " +
                                        "SET pvpmode = 1 WHERE id in (1)";
                                SERVERSTATUSS.executeUpdate(sqldata);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }, preparetime[0] * 20+540L);

                } else {
                    p.sendMessage("no permission");

                }
            }
            return false;
}}
