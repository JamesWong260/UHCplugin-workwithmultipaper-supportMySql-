package uhc2023.uhc2023.uhcevent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uhc2023.uhc2023.UHC2023;
import java.util.ArrayList;

import java.sql.*;

public class startteleport implements CommandExecutor {

    private static UHC2023 playerlist;
    int x = 10;
    private UHC2023 plugin;



    static final String SERVERSTATUS = "SELECT id, name, playercount, pvpmode, serversession, firstjoin FROM SERVERSTATUS";
    static final String gamemodedetect = "SELECT id, name, playergamemode, onlinestatus, coordinate, point, point2, offlinecountdown FROM playerlist";

    static final String survivor = "SELECT id, name FROM survivor";

    static final String tp = "SELECT id, name, coordinate FROM playerlist";

    public startteleport(UHC2023 plugin) {
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
        ArrayList<String> players = new ArrayList<String>();
        ArrayList<String> coordinate = new ArrayList<String>();
        String q3 = ID.replace(q1, "").replace(q2, "");
        ArrayList<String> pp = new ArrayList<String>();
        try (Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt2 = connn.createStatement();
             ResultSet rs = stmt2.executeQuery(survivor);
        ) {

            while (rs.next()) {
                String data2 = String.valueOf(rs.getString("name"));
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "attribute" + " " + data2 + " " + "minecraft:generic.max_health base set 40");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "effect give" + " " + data2 + " " + "minecraft:slow_falling 30");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "effect give" + " " + data2 + " " + "minecraft:regeneration 20 2");
            }
        }catch (SQLException es) {
            es.printStackTrace();
        }
        try (Connection connn = DriverManager.getConnection(DB_IND, DB_USERNAME, DB_PASSWORD);
             Statement stmt2 = connn.createStatement();
             ResultSet rs = stmt2.executeQuery(tp);
        ) {

            while (rs.next()) {
                players.add(rs.getString("name")+" "+rs.getString("coordinate"));
            }
        }catch (SQLException es) {
            es.printStackTrace();}
        for (String i : players) {
            p.chat("/minecraft:tp" + " " + i);
        }


        return false;
    }}

