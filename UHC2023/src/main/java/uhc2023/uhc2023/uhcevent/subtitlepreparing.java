package uhc2023.uhc2023.uhcevent;


import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import uhc2023.uhc2023.UHC2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
public class subtitlepreparing implements Listener {
    private static UHC2023 plugin;
    public subtitlepreparing() {
        this.plugin = plugin;
    }
    public ArrayList<String> playerlist = new ArrayList<>();

    // more access methods
    @EventHandler

    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String ID = String.valueOf(player);
        String q1 = "CraftPlayer{name=";
        String q2 = "}";
        String q3 = ID.replace(q1, "").replace(q2, "");
        String pathh = "C:\\multipaper\\multipaper - Copy - Copy\\UHC2023\\";
        try {
            File myObj = new File(pathh+"serversession\\serversession.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
                if (data.equals("1")){
                    new BukkitRunnable() {
                        public void run() {
                            String before = ChatColor.DARK_GRAY + "比賽即將開始請耐心等待";
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(before));
                        }}.runTaskTimer(plugin, 0, 1);
                }
            }
        } catch (FileNotFoundException ee) {
            System.out.println("An error occurred.");
            ee.printStackTrace();
        }
    }
}

