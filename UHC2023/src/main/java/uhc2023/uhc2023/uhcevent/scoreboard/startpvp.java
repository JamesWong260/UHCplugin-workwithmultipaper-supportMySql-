package uhc2023.uhc2023.uhcevent.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import uhc2023.uhc2023.UHC2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class startpvp implements CommandExecutor {

    private static UHC2023 playerlist;
    private UHC2023 plugin;

    public startpvp(UHC2023 plugin) {
        this.plugin = plugin;
    }

    int x = 2700;
    int border = 2000;
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    final Scoreboard board = manager.getNewScoreboard();
    final Objective objective = board.registerNewObjective("Belowname", "dummy");
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (sender instanceof Player) {
            String ID = String.valueOf(p);
            if (p.isOp()){
                new BukkitRunnable() {
                    public void run() {
                        p.sendMessage(String.valueOf(x));
                        Bukkit.getServer().dispatchCommand(p, "playsound ui.button.click master @a");
                        x -= 1;
                        if (x == 0) {
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin,0, 20);
            }

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(UHC2023.getMain(), new Runnable() {
                public void run() {
                    p.sendMessage("go");
                    Bukkit.getServer().dispatchCommand(p, "playsound entity.experience_orb.pickup master @a");
                    try {
                        File myObj = new File("plugins\\UHC2023\\playerlist\\playerlist.txt");
                        Scanner myReader = new Scanner(myObj);
                        while (myReader.hasNextLine()) {
                            String data = myReader.nextLine();
                            String data2 = data.replaceAll("\\s.*", "");
                            System.out.println(data);
                            Bukkit.getServer().dispatchCommand(p, "worldborder set 2000 0");
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tp"+" "+data);
                            Bukkit.getServer().dispatchCommand(p, "effect give"+" "+data2+" "+"slow_falling 20");
                            p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                            Bukkit.getServer().dispatchCommand(p, "session1");

                        }
                        myReader.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }




                }}, 200L);


        }
        else {
            p.sendMessage("no permission");

        }

        return false;
    }
}


