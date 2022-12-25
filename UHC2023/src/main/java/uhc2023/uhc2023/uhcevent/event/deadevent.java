package uhc2023.uhc2023.uhcevent.event;

import com.github.puregero.multilib.MultiLib;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import uhc2023.uhc2023.UHC2023;

import java.sql.*;

public class deadevent implements Listener {
    private static UHC2023 main;

    public deadevent() {
        this.main = main;
    }
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    final Scoreboard board = manager.getNewScoreboard();
    final Objective objective = board.registerNewObjective("Belowname", "dummy");
    String pathh = "C:\\multipaper\\multipaper - Copy - Copy\\UHC2023\\";
    static final String SERVERSTATUS = "SELECT id, name, playercount, pvpmode, serversession, firstjoin FROM SERVERSTATUS";
    static final String gamemodedetect = "SELECT id, name, playergamemode, onlinestatus, coordinate, point, point2, offlinecountdown FROM playerlist";

    static final String survivor = "SELECT id, name FROM survivor";
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        String ip = UHC2023.getMain().getConfig().getString("SQL.ip");
        String table = UHC2023.getMain().getConfig().getString("SQL.table");
        String user =  UHC2023.getMain().getConfig().getString("SQL.user");
        String password = UHC2023.getMain().getConfig().getString("SQL.password");
        String DB_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://"+ ip + "/" +table;
        String DB_USERNAME = user;
        String DB_PASSWORD = password;
        Player damager = e.getEntity().getKiller();
        Player p = e.getEntity().getPlayer();


        String pID = String.valueOf(p);
        String p1 = "CraftPlayer{name=";
        String p2 = "}";
        String p3 = pID.replace(p1, "").replace(p2, "");

        String qID = String.valueOf(damager);
        String q1 = "CraftPlayer{name=";
        String q2 = "}";
        String q3 = qID.replace(q1, "").replace(q2, "");

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(UHC2023.getMain(), new Runnable() {
            public void run() {
                try (Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                     PreparedStatement SERVERSTATUSS = connn.prepareStatement("INSERT INTO SERVERSTATUS (id, name, playercount, pvpmode, serversession, firstjoin) VALUES (?,?,?,?,?,?,?)");
                     Statement stmt2 = connn.createStatement();
                     ResultSet rs = stmt2.executeQuery(SERVERSTATUS);
                ) {
                    while (rs.next()) {
                        int count = 0;
                        for(Player alive : Bukkit.getServer().getOnlinePlayers()) {
                            if(alive.getGameMode().equals(GameMode.SURVIVAL)) {
                                count++;
                            }
                        }
                        String sqldata = "UPDATE SERVERSTATUS " +
                                "SET playercount ='" + count + "'WHERE id in (1)";
                        SERVERSTATUSS.executeUpdate(sqldata);
                        System.out.println("Inserted records into the table...");
                    }
                } catch (SQLException ef) {
                    ef.printStackTrace();
                }


                try (Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                     PreparedStatement SERVERSTATUSS = connn.prepareStatement("INSERT INTO SERVERSTATUS (id, name, playercount, pvpmode, serversession, firstjoin) VALUES (?,?,?,?,?,?)");
                     PreparedStatement playerlist = connn.prepareStatement("INSERT INTO playerlist (id, name, playergamemode, onlinestatus, coordinate, point, point2, offlinecountdown) VALUES (?,?,?,?,?,?,?,?)");
                     Statement stmt1 = connn.createStatement();
                     Statement stmt2 = connn.createStatement();
                     ResultSet rs = stmt1.executeQuery(gamemodedetect);
                     ResultSet rss = stmt2.executeQuery(SERVERSTATUS);

                ) {

                    int count = 0;
                    for(Player alive : MultiLib.getAllOnlinePlayers()) {
                        if(alive.getGameMode().equals(GameMode.SURVIVAL)) {
                            count++;
                        }
                    }       count-= 1;
                            System.out.println("jess"+count);
                            String stcount = String.valueOf(count);
                            while (rs.next()) {
                                String modedata = rs.getString("playergamemode");
                                if (modedata.equals("2")) {
                                    if(stcount.equals("1")){
                                        String name = rs.getString("name");
                                        System.out.println(name);
                                        int data = rs.getInt("point2");
                                        data += 4;
                                        String sqldata = "UPDATE playerlist " +
                                                "SET point2 ='" + data + "'WHERE playergamemode in (2)";
                                        playerlist.executeUpdate(sqldata);
                                        Bukkit.broadcastMessage("比賽結束，所有數據會記錄在MYSQL數據庫，請等待管理員公佈結果，感謝你的參與");
                                        for(Player alive : MultiLib.getAllOnlinePlayers()) {
                                          if(alive.getName().equalsIgnoreCase(name) ) {
                                            if(alive.getGameMode().equals(GameMode.SURVIVAL)) {
                                                alive.sendMessage("最後1名存活者+4分");
                                                alive.playSound(alive.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 20, 1);
                                            }}}
                                    }
                                    if(stcount.equals("2")){
                                        String name = rs.getString("name");
                                        System.out.println(name);
                                        int data = rs.getInt("point2");
                                        data += 3;
                                        String sqldata = "UPDATE playerlist " +
                                                "SET point2 ='" + data + "'WHERE playergamemode in (2)";
                                        playerlist.executeUpdate(sqldata);
                                        for(Player alive : MultiLib.getAllOnlinePlayers()) {
                                            if(alive.getName().equalsIgnoreCase(name) ) {
                                            if(alive.getGameMode().equals(GameMode.SURVIVAL)) {
                                                alive.sendMessage("最後2名存活者+3分");
                                                alive.playSound(alive.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 20, 1);
                                            }}}
                                    }
                                    if(stcount.equals("3")){
                                        String name = rs.getString("name");
                                        System.out.println(name);
                                        int data = rs.getInt("point2");
                                        data += 3;
                                        String sqldata = "UPDATE playerlist " +
                                                "SET point2 ='" + data + "'WHERE playergamemode in (2)";
                                        playerlist.executeUpdate(sqldata);
                                        for(Player alive : MultiLib.getAllOnlinePlayers()) {
                                            if(alive.getName().equalsIgnoreCase(name) ) {
                                            if(alive.getGameMode().equals(GameMode.SURVIVAL)) {
                                                alive.sendMessage("最後3名存活者+3分");
                                                alive.playSound(alive.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 20, 1);
                                            }}}
                                }
                                    if(stcount.equals("5")){
                                        String name = rs.getString("name");
                                        System.out.println(name);
                                        int data = rs.getInt("point2");
                                        data += 1;
                                        String sqldata = "UPDATE playerlist " +
                                                "SET point2 ='" + data + "'WHERE playergamemode in (2)";
                                        playerlist.executeUpdate(sqldata);
                                        for(Player alive : MultiLib.getAllOnlinePlayers()) {
                                            if(alive.getName().equalsIgnoreCase(name) ) {
                                            if(alive.getGameMode().equals(GameMode.SURVIVAL)) {
                                                alive.sendMessage("最後5名存活者+1分");
                                                alive.playSound(alive.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 20, 1);
                                            }}}
                                    }
                                    if(stcount.equals("10")){
                                        String name = rs.getString("name");
                                        System.out.println(name);
                                        int data = rs.getInt("point2");
                                        data += 1;
                                        String sqldata = "UPDATE playerlist " +
                                                "SET point2 ='" + data + "'WHERE playergamemode in (2)";
                                        playerlist.executeUpdate(sqldata);
                                        for(Player alive : MultiLib.getAllOnlinePlayers()) {
                                            if(alive.getName().equalsIgnoreCase(name) ) {
                                            if(alive.getGameMode().equals(GameMode.SURVIVAL)) {
                                                alive.sendMessage("最後10名存活者+1分");
                                                alive.playSound(alive.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 20, 1);
                                            }}}
                                    }
                                    if(stcount.equals("15")){
                                        String name = rs.getString("name");
                                        System.out.println(name);
                                        int data = rs.getInt("point2");
                                        data += 1;
                                        String sqldata = "UPDATE playerlist " +
                                                "SET point2 ='" + data + "'WHERE playergamemode in (2)";
                                        playerlist.executeUpdate(sqldata);
                                        for(Player alive : MultiLib.getAllOnlinePlayers()) {
                                            if(alive.getName().equalsIgnoreCase(name) ) {
                                            if(alive.getGameMode().equals(GameMode.SURVIVAL)) {
                                                alive.sendMessage("最後15名存活者+1分");
                                                alive.playSound(alive.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 20, 1);
                                            }}}
                                    }
                        }
}}  catch (SQLException ef) {
                    ef.printStackTrace();
                }

            }
        }, 5L);

    }}


