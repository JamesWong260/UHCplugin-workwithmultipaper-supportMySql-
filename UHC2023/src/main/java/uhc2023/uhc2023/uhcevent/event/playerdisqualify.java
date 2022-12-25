package uhc2023.uhc2023.uhcevent.event;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import uhc2023.uhc2023.UHC2023;

import java.io.*;

public class playerdisqualify implements Listener {
    private static UHC2023 main;
    public playerdisqualify() {
        this.main = main;
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        String pathh = "C:\\multipaper\\multipaper - Copy - Copy\\UHC2023\\";
        Player p = e.getEntity().getPlayer();
        String IDD = String.valueOf(p);
        String s1 = "CraftPlayer{name=";
        String s2 = "}";
        String s3 = IDD.replace(s1, "").replace(s2, "");
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(UHC2023.getMain(), new Runnable() {
            public void run() {
                File inputFile = new File(pathh+"survivor\\survivor.txt");
                File tempFile = new File(pathh+"survivor\\survivor.txt");

                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(inputFile));
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new FileWriter(tempFile));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                String lineToRemove = s3;
                String currentLine;

                while(true) {
                    try {
                        if (!((currentLine = reader.readLine()) != null)) break;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    // trim newline when comparing with lineToRemove
                    String trimmedLine = currentLine.trim();
                    if(trimmedLine.equals(lineToRemove)) continue;
                    try {
                        writer.write(currentLine + System.getProperty("line.separator"));
                        writer.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                boolean successful = tempFile.renameTo(inputFile);

            }}, 40L);

    }
}


