package uhc2023.uhc2023.uhcevent.scoreboard;

import com.github.puregero.multilib.MultiLib;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import uhc2023.uhc2023.UHC2023;

import java.sql.*;
import java.util.Random;

public class beforestart implements Listener{
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    final Scoreboard board = manager.getNewScoreboard();
    final Objective objective = board.registerNewObjective("Belowname", "dummy");
    UHC2023 plugin;
    public beforestart(UHC2023 plugin) {
        this.plugin = plugin;
    }
    static final String SERVERSTATUS = "SELECT id, name, playercount, pvpmode, serversession, firstjoin FROM SERVERSTATUS";
    static final String gamemodedetect = "SELECT id, name, playergamemode, onlinestatus, coordinate, point, point2, offlinecountdown FROM playerlist";
    static final String tp = "SELECT id, name, coordinate FROM playerlist";

    static final String survivor = "SELECT id, name FROM survivor";

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
        String ip = UHC2023.getMain().getConfig().getString("SQL.ip");
        String table = UHC2023.getMain().getConfig().getString("SQL.table");
        String user = UHC2023.getMain().getConfig().getString("SQL.user");
        String password = UHC2023.getMain().getConfig().getString("SQL.password");
        String DB_DRIVER = "com.mysql.jdbc.Driver";
        String individual = UHC2023.getMain().getConfig().getString("SQL.individual SQL base");
        String offlinecountdown = UHC2023.getMain().getConfig().getString("UHC.offlinecountdown");
        String DB_IND = "jdbc:mysql://"+ ip + "/" +individual;
        String DB_URL = "jdbc:mysql://"+ ip + "/" +table;
        String DB_USERNAME = user;
        String DB_PASSWORD = password;
        int boradersize =  UHC2023.getMain().getConfig().getInt("UHC.Border size");
        Player player = e.getPlayer();
        Random random = new Random();
        String ID = String.valueOf(player);
        String q1 = "CraftPlayer{name=";
        String q2 = "}";
        String q3 = ID.replace(q1, "").replace(q2, "");



        try (Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement SERVERSTATUSS = connn.prepareStatement("INSERT INTO SERVERSTATUS (id, name, playercount, pvpmode, serversession, firstjoin) VALUES (?,?,?,?,?,?)");
             Statement stmt = connn.createStatement();
             ResultSet rs = stmt.executeQuery(SERVERSTATUS);
        ) {
            String sqldataa = "UPDATE SERVERSTATUS " +
                    "SET firstjoin = 1 WHERE id in (1)";
            while (rs.next()) {
                String dataa = String.valueOf(rs.getInt("serversession"));
                if (dataa.equals("1")) {
                    if (player.isOp()) {
                        player.setGameMode(GameMode.SPECTATOR);
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tp" + " " + q3 + " " + random.nextInt(-12, 12)+" 203 "+random.nextInt(-12, 12));
                        SERVERSTATUSS.executeUpdate(sqldataa);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(UHC2023.getMain(), new Runnable() {
                            public void run() {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "worldborder set 10000");
                                Bukkit.getServer().dispatchCommand(player, "fill -15 200 -15 15 210 15 glass");
                                Bukkit.getServer().dispatchCommand(player, "fill -14 201 -14 14 209 14 air");
                                Bukkit.getServer().dispatchCommand(player, "session5");
                                MultiLib.chatOnOtherServers(player, "/" + "session5");
                            }
                        }, 70L);
                    }
                    try (Connection connn2 = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                         Statement stmt2 = connn2.createStatement();
                         ResultSet rs2 = stmt2.executeQuery(SERVERSTATUS);
                    ) {
                        while (rs2.next()) {
                            String dataaa = String.valueOf(rs2.getInt("firstjoin"));
                            if (dataaa.equals("1")) {
                                try(Connection connn3 = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                                    Statement stmt3 = connn3.createStatement();
                                    ResultSet rs3 = stmt3.executeQuery(SERVERSTATUS);
                                ) {
                                    while(rs3.next()){
                                        if (!player.isOp()) {
                                            int count = 0;
                                            for(Player alive : MultiLib.getAllOnlinePlayers()) {
                                                if(alive.getGameMode().equals(GameMode.ADVENTURE)) {
                                                    count++;
                                                }
                                            }
                                            count+=1;
                                            String coordinate = random.nextInt(-boradersize/2, boradersize/2) + " " + 250 + " " + random.nextInt(-boradersize/2, boradersize/2);
                                            String playerinf = "INSERT INTO playerlist VALUES (" + count + ", '" + q3 + "', 1, 1,'" + coordinate + "', 0, 0, "+offlinecountdown+")";
                                            stmt3.executeUpdate(playerinf);
                                            String survivorinf = "INSERT INTO survivor VALUES (" + count + ", '" + q3 + "')";
                                            stmt3.executeUpdate(survivorinf);
                                            System.out.println("Inserted records into the table AAA");
                                        }

                                    }
                                } catch (SQLException ed) {
                                    ed.printStackTrace();
                                }
                                try(Connection connn3 = DriverManager.getConnection(DB_IND, DB_USERNAME, DB_PASSWORD);
                                    Statement stmt3 = connn3.createStatement();
                                    ResultSet rs3 = stmt3.executeQuery(tp);
                                ) {
                                    while(rs3.next()){
                                        if (!player.isOp()) {
                                            int count = 0;
                                            for(Player alive : MultiLib.getAllOnlinePlayers()) {
                                                if(alive.getGameMode().equals(GameMode.ADVENTURE)) {
                                                    count++;
                                                }
                                            }
                                            count+=1;
                                            String coordinate = random.nextInt(-boradersize, boradersize) + " " + 150 + " " + random.nextInt(-boradersize, boradersize);
                                            String playerinf = "INSERT INTO playerlist VALUES (" + count + ", '" + q3 + "', '" + coordinate + "')";
                                            stmt3.executeUpdate(playerinf);
                                            System.out.println("Inserted records into the table AAA");
                                        }

                                    }
                                } catch (SQLException ed) {
                                    ed.printStackTrace();
                                }

                                try(Connection connn4 = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                                    PreparedStatement SERVERSTATUSS2 = connn.prepareStatement("INSERT INTO SERVERSTATUS (id, name, playercount, pvpmode, serversession, firstjoin) VALUES (?,?,?,?,?,?)");
                                    Statement stmt4 = connn4.createStatement();
                                    Statement stmt5 = connn4.createStatement();
                                    ResultSet rs4 = stmt4.executeQuery(SERVERSTATUS);
                                    ResultSet rs5 = stmt5.executeQuery(gamemodedetect);

                                ) {
                                    while(rs4.next()){
                                        String data = rs4.getString("serversession");
                                        if (data.equals("1")){
                                            int count = 0;
                                            for(Player alive : MultiLib.getAllOnlinePlayers()) {
                                                if(alive.getGameMode().equals(GameMode.ADVENTURE)) {
                                                    count++;
                                                }
                                            }
                                                if (!player.hasPlayedBefore()) {
                                                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(UHC2023.getMain(), new Runnable() {
                                                        public void run() {
                                                            player.setGameMode(GameMode.ADVENTURE);
                                                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tp" + " " + q3 + " " + random.nextInt(-12, 12)+" 203 "+random.nextInt(-12, 12));
                                                        }
                                                    }, 10L);
                                                }
                                                count+=1;
                                                System.out.println(count);
                                                String dataaa2 = String.valueOf(count);
                                                String sqldata = "UPDATE SERVERSTATUS " +
                                                "SET playercount ='"+ dataaa2 +"'WHERE id in (1)";
                                                SERVERSTATUSS2.executeUpdate(sqldata);

                                        }
                                        if (data.equals("2")){
                                            while(rs5.next()){
                                                int count = 0;
                                                for(Player alive : MultiLib.getAllOnlinePlayers()) {
                                                    if(alive.getGameMode().equals(GameMode.SURVIVAL)) {
                                                        count++;
                                                    }
                                                }
                                                System.out.println(count);
                                                String dataaa2 = String.valueOf(count);
                                                String sqldata = "UPDATE SERVERSTATUS " +
                                                        "SET playercount ='"+ dataaa2 +"'WHERE id in (1)";
                                                SERVERSTATUSS2.executeUpdate(sqldata);
                                            }}

                                    }

                                } catch (SQLException ed) {
                                    ed.printStackTrace();
                                }
                            }
                            if (dataaa.equals("0")) {
                                player.kickPlayer("需要由管理員來啓動這場比賽");
                            }
                        }
                    } catch (SQLException ess) {
                        throw new RuntimeException(ess);
                    }
                }
                if (dataa.equals("2")) {
                    String onlinestatus = "UPDATE playerlist " +
                            "SET onlinestatus = 1 WHERE name in '"+q3+"'";
                    SERVERSTATUSS.executeUpdate(onlinestatus);
                    if (!player.hasPlayedBefore()) {
                    player.setGameMode(GameMode.SPECTATOR);
                    }
                }

            }
        } catch (SQLException eg) {
            throw new RuntimeException(eg);
        }



}
    @EventHandler
    public void PlayerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        String ip = UHC2023.getMain().getConfig().getString("SQL.ip");
        String table = UHC2023.getMain().getConfig().getString("SQL.table");
        String user = UHC2023.getMain().getConfig().getString("SQL.user");
        String password = UHC2023.getMain().getConfig().getString("SQL.password");
        String DB_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://"+ ip + "/" +table;
        String DB_USERNAME = user;
        String DB_PASSWORD = password;
        String ID = String.valueOf(player);
        String q1 = "CraftPlayer{name=";
        String q2 = "}";
        String q3 = ID.replace(q1, "").replace(q2, "");
        try(Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement SERVERSTATUSS = connn.prepareStatement("INSERT INTO SERVERSTATUS (id, name, playercount, pvpmode, serversession, firstjoin) VALUES (?,?,?,?,?,?)");
            Statement stmt1 = connn.createStatement();
            Statement stmt2 = connn.createStatement();
            ResultSet rs = stmt1.executeQuery(SERVERSTATUS);
            ResultSet rss = stmt2.executeQuery(gamemodedetect);
        ) {
            while(rs.next()){
                String data = rs.getString("serversession");
                if (data.equals("1")){
                    int count = 0;
                    for(Player alive : Bukkit.getServer().getOnlinePlayers()) {
                        if(alive.getGameMode().equals(GameMode.ADVENTURE)) {
                            count++;
                        }
                    }
                    count-=1;
                    String dataa = String.valueOf(count);
                    String sqldata = "UPDATE SERVERSTATUS " +
                            "SET playercount ='"+ dataa +"'WHERE id in (1)";
                    SERVERSTATUSS.executeUpdate(sqldata);
                    System.out.println(count);
                }
                if (data.equals("2")){
                    while(rss.next()){
                        int count = 0;
                        for(Player alive : Bukkit.getServer().getOnlinePlayers()) {
                            if(alive.getGameMode().equals(GameMode.SURVIVAL)) {
                                count++;
                            }
                        }
                        count-=1;
                        String dataa = String.valueOf(count);
                        String sqldata = "UPDATE SERVERSTATUS " +
                                "SET playercount ='"+ dataa +"'WHERE id in (1)";
                        String onlinestatus = "UPDATE playerlist " +
                                "SET onlinestatus = 0 WHERE name in ('"+q3+"')";
                        SERVERSTATUSS.executeUpdate(sqldata);
                        SERVERSTATUSS.executeUpdate(onlinestatus);
                        System.out.println(count);
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
                        }
                }
            }
        } catch (SQLException ed) {
            ed.printStackTrace();
        }

}
}

