package uhc2023.uhc2023.uhcevent.event;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import uhc2023.uhc2023.UHC2023;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class survive implements Listener {
    private static UHC2023 main;
    ArrayList<survive> result = new ArrayList<survive>();
    public survive() {
        this.main = main;
    }

    static final String SERVERSTATUS = "SELECT id, name, playercount, pvpmode, serversession, firstjoin FROM SERVERSTATUS";
    static final String gamemodedetect = "SELECT id, name, playergamemode, onlinestatus, coordinate, point, point2, offlinecountdown FROM playerlist";

    static final String survivor = "SELECT id, name FROM survivor";

    @EventHandler
    private void onPlayerKILL(PlayerDeathEvent e) {
        Player player = e.getEntity().getPlayer();
        Player damager = player.getKiller();
        System.out.println("DIEEEEEEEEEE"+player);
        System.out.println("DIEEEEEEEEEE"+damager);
        String ip = UHC2023.getMain().getConfig().getString("SQL.ip");
        String table = UHC2023.getMain().getConfig().getString("SQL.table");
        String user =  UHC2023.getMain().getConfig().getString("SQL.user");
        String password = UHC2023.getMain().getConfig().getString("SQL.password");
        String DB_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://"+ ip + "/" +table;
        String DB_USERNAME = user;
        String DB_PASSWORD = password;

        String pID = String.valueOf(player);
        String p1 = "CraftPlayer{name=";
        String p2 = "}";
        String p3 = pID.replace(p1, "").replace(p2, "");

        String ID = String.valueOf(player.getKiller());
        String q1 = "CraftPlayer{name=";
        String q2 = "}";
        String q3 = ID.replace(q1, "").replace(q2, "");

        try (Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement SERVERSTATUSS = connn.prepareStatement("INSERT INTO SERVERSTATUS (id, name, playercount, pvpmode, serversession, firstjoin VALUES (?,?,?,?,?,?)");
             PreparedStatement playerlist = connn.prepareStatement("INSERT INTO playerlist (id, name, playergamemode, onlinestatus, coordinate, point, point2, offlinecountdown) VALUES (?,?,?,?,?,?,?,?)");
             Statement stmt1 = connn.createStatement();
             Statement stmt2 = connn.createStatement();
             ResultSet rs = stmt1.executeQuery(SERVERSTATUS);
             ResultSet rss = stmt2.executeQuery(gamemodedetect);
        ) {
                while (rs.next()) {
                    String data = rs.getString("serversession");
                    if (data.equals("2")) {
                        player.setOp(true);
                        Bukkit.getServer().dispatchCommand(e.getEntity().getPlayer(), "playsound minecraft:entity.wither.spawn master @a");
                        Random random = new Random();
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tp" + " " + p3 + " " + random.nextInt(-100, 100) + " " + 70 + " " + random.nextInt(-100, 100));
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(UHC2023.getMain(), new Runnable() {
                        public void run() {
                            player.setGameMode(GameMode.SPECTATOR);
                        }}, 20L);
                        player.setOp(false);
                            String sqldata = "UPDATE playerlist " +
                                    "SET playergamemode = 3 WHERE name in ('" + p3 + "')";
                            playerlist.executeUpdate(sqldata);

                    }
                }
                player.setOp(true);
                Bukkit.getServer().dispatchCommand(player, "tellraw"+" "+q3+" "+"\"成功擊殺，+5分\"");
                player.setOp(false);
                if (player.getKiller() != null) {
                    System.out.println(player.getKiller());
                while (rss.next()) {
                    String name = rss.getString("name");
                    int point = rss.getInt("point");
                    if (name.equals(q3)) {
                        point+=5;
                        System.out.println(point);
                        System.out.println(q3);
                        String sql = "UPDATE playerlist " +
                                "SET point ='" + point + "'WHERE name in ('" + q3 + "')";
                        playerlist.executeUpdate(sql);
                        System.out.println("done");
                    }
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }}

