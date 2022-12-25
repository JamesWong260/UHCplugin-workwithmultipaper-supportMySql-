package uhc2023.uhc2023.uhcevent.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import uhc2023.uhc2023.UHC2023;

import java.sql.*;
import java.util.ArrayList;

public class pvpmode implements Listener {
    private static UHC2023 main;
    ArrayList<survive> result = new ArrayList<survive>();
    public pvpmode() {
        this.main = main;
    }

    static final String SERVERSTATUS = "SELECT id, name, playercount, pvpmode, serversession, firstjoin FROM SERVERSTATUS";
    static final String gamemodedetect = "SELECT id, name, playergamemode, onlinestatus, coordinate, point, point2, offlinecountdown FROM playerlist";

    static final String survivor = "SELECT id, name FROM survivor";
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        String ip = UHC2023.getMain().getConfig().getString("SQL.ip");
        String table = UHC2023.getMain().getConfig().getString("SQL.table");
        String user =  UHC2023.getMain().getConfig().getString("SQL.user");
        String password = UHC2023.getMain().getConfig().getString("SQL.password");
        String DB_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://"+ ip + "/" +table;
        String DB_USERNAME = user;
        String DB_PASSWORD = password;
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            try(Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                Statement stmt2 = connn.createStatement();
                ResultSet rs = stmt2.executeQuery(SERVERSTATUS);
            ) {
                while(rs.next()){
                    String data = rs.getString("pvpmode");
                    if (data.equals("0")) {
                        event.setCancelled(true);
                        damager.sendMessage("PVP尚未開啓");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }}


