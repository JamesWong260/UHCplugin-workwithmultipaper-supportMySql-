package uhc2023.uhc2023.uhcevent;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import uhc2023.uhc2023.UHC2023;

import java.sql.*;
public class gamemodeonjoin implements Listener {
    private static UHC2023 main;
    static final String SERVERSTATUS = "SELECT id, name, playercount, pvpmode, serversession, firstjoin FROM SERVERSTATUS";
    static final String gamemodedetect = "SELECT id, name, playergamemode, onlinestatus, coordinate, point, point2, offlinecountdown FROM playerlist";

    static final String survivor = "SELECT id, name FROM survivor";
    public gamemodeonjoin() {
        this.main = main;
    }
    private UHC2023 plugin = UHC2023.getMain();

    private Connection connection;
    int x= 0;
    // more access methods
    @EventHandler

    public void onPlayerJoin(PlayerJoinEvent event) {
        String ip = UHC2023.getMain().getConfig().getString("SQL.ip");
        String table = UHC2023.getMain().getConfig().getString("SQL.table");
        String user = UHC2023.getMain().getConfig().getString("SQL.user");
        String password = UHC2023.getMain().getConfig().getString("SQL.password");
        String DB_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://"+ ip + "/" +table;
        String DB_USERNAME = user;
        String DB_PASSWORD = password;
        Player player = event.getPlayer();
        String ID = String.valueOf(player);
        String q1 = "CraftPlayer{name=";
        String q2 = "}";
        String q3 = ID.replace(q1, "").replace(q2, "");

        try(Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement stmt2 = connn.createStatement();
        ) {
            String sql = "SELECT playergamemode FROM playerlist WHERE name in ('"+ q3 +"')";
            ResultSet rs = stmt2.executeQuery(sql);

            while(rs.next()){
                String dataa = String.valueOf(rs.getInt("playergamemode"));
                if (dataa.equals("3")){
                    player.setGameMode(GameMode.SPECTATOR);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


}
}

