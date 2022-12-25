package uhc2023.uhc2023;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import uhc2023.uhc2023.uhcevent.event.deadevent;
import uhc2023.uhc2023.uhcevent.event.offlinetimecount;
import uhc2023.uhc2023.uhcevent.event.pvpmode;
import uhc2023.uhc2023.uhcevent.event.survive;
import uhc2023.uhc2023.uhcevent.*;
import uhc2023.uhc2023.uhcevent.scoreboard.beforestart;
import uhc2023.uhc2023.uhcevent.scoreboard.preparescoreboardcommand;
import uhc2023.uhc2023.uhcevent.scoreboard.preparetime;
import uhc2023.uhc2023.uhcevent.scoreboard.preparetimeMAIN;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UHC2023 extends JavaPlugin implements Listener {
    private static UHC2023 main;
    public beforestart beforestart;
    @Override
    public void onEnable() {
        main = this;
        main.saveDefaultConfig();
        System.out.println("started");
        String ip = main.getConfig().getString("SQL.ip");
        String table = main.getConfig().getString("SQL.table");
        String individual = main.getConfig().getString("SQL.individual SQL base");
        String user = main.getConfig().getString("SQL.user");
        String password = main.getConfig().getString("SQL.password");
        String offlinecountdown = main.getConfig().getString("UHC.offlinecountdown");
        String DB_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://"+ ip + "/" +table;
        String DB_IND = "jdbc:mysql://"+ ip + "/" +individual;
        String DB_USERNAME = user;
        String DB_PASSWORD = password;
        Connection conn = null;
        try{
            //Register the JDBC driver
            Class.forName(DB_DRIVER);
            //Open the connection
            conn = DriverManager.
                    getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            if(conn != null){
                System.out.println("Successfully connected.");
            }else{
                System.out.println("Failed to connect.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        beforestart = new beforestart(this);
        Bukkit.getServer().getPluginManager().registerEvents(new gamemodeonjoin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new deadevent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new pvpmode(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new survive(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new beforestart(this), this);
        getCommand("UHCstart").setExecutor(new startcountdown(this));
        getCommand("session1").setExecutor(new preparetime(this));
        getCommand("session2").setExecutor(new offlinetimecount(this));
        getCommand("session3").setExecutor(new playercount(this));
        getCommand("session4").setExecutor(new playercountonleave(this));
        getCommand("session5").setExecutor(new preparescoreboardcommand(this));
        getCommand("session6").setExecutor(new preparetimeMAIN(this));
        getCommand("session7").setExecutor(new startteleport(this));

        try(Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement stmt2 = connn.createStatement();
        ) {
            String sql = "DROP TABLE SERVERSTATUS";
            stmt2.executeUpdate(sql);
            String sql2 = "DROP TABLE playerlist";
            stmt2.executeUpdate(sql2);
            String sql3 = "DROP TABLE survivor";
            stmt2.executeUpdate(sql3);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try(Connection connn = DriverManager.getConnection(DB_IND, DB_USERNAME, DB_PASSWORD);
            Statement stmt2 = connn.createStatement();
        ) {
            String sql2 = "DROP TABLE playerlist";
            stmt2.executeUpdate(sql2);

        } catch (SQLException e) {
            e.printStackTrace();
        }

            try(Connection connnz = DriverManager.getConnection(DB_IND, DB_USERNAME, DB_PASSWORD);
                Statement stmt2 = connnz.createStatement();)
            {
            String playerlist = "CREATE TABLE playerlist " +
                    "(id INTEGER not NULL, " +
                    " name VARCHAR(255), " +
                    " coordinate VARCHAR(255), " +
                    " PRIMARY KEY ( id ))";
            stmt2.executeUpdate(playerlist);
            System.out.println("Created table in given database...");
            System.out.println("Inserting records into the table...");
            String playerinf = "INSERT INTO playerlist VALUES (0, 0, 0)";
            stmt2.executeUpdate(playerinf);
            System.out.println("Inserted records into the table...");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(Connection connn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement stmt = connn.createStatement();
            Statement stmt2 = connn.createStatement();
            Statement stmt3 = connn.createStatement();
        ) {
            String SERVERSTATUS = "CREATE TABLE SERVERSTATUS " +
                    "(id INTEGER not NULL, " +
                    " name VARCHAR(255), " +
                    " playercount INTEGER, " +
                    " pvpmode INTEGER, " +
                    " serversession INTEGER, " +
                    " firstjoin VARCHAR(255), " +
                    " PRIMARY KEY ( id ))";
            String playerlist = "CREATE TABLE playerlist " +
                    "(id INTEGER not NULL, " +
                    " name VARCHAR(255), " +
                    " playergamemode VARCHAR(255), " +
                    " onlinestatus VARCHAR(255), " +
                    " coordinate VARCHAR(255), " +
                    " point INTEGER, " +
                    " point2 INTEGER, " +
                    " offlinecountdown VARCHAR(255), " +
                    " PRIMARY KEY ( id ))";
            String survivor = "CREATE TABLE survivor " +
                    "(id INTEGER not NULL, " +
                    " name VARCHAR(255), " +
                    " PRIMARY KEY ( id ))";

            stmt.executeUpdate(SERVERSTATUS);
            stmt2.executeUpdate(playerlist);
            stmt3.executeUpdate(survivor);
            System.out.println("Created table in given database...");
            System.out.println("Inserting records into the table...");
            String sqldata = "INSERT INTO SERVERSTATUS VALUES (1, 'UHC championship', 0, 0, 1, 0)";
            String playerinf = "INSERT INTO playerlist VALUES (0, 0, 0, 0, 0, 0, 0, '"+offlinecountdown+ "')";
            String survivorinf = "INSERT INTO survivor VALUES (0, 0)";
            stmt.executeUpdate(survivorinf);
            stmt2.executeUpdate(playerinf);
            stmt3.executeUpdate(sqldata);
            System.out.println("Inserted records into the table...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static UHC2023 getPlugin() {
        return new UHC2023();
    }
    public static UHC2023 getMain(){
        return main;
    }

    public boolean contains(String cyber) {
        return false;
    }
}
