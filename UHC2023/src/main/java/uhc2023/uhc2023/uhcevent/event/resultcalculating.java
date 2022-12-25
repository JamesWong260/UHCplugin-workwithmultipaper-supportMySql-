package uhc2023.uhc2023.uhcevent.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import uhc2023.uhc2023.UHC2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;



public class resultcalculating implements Listener {
    private static UHC2023 main;
    ArrayList<String> list = new ArrayList<String>();
    public resultcalculating() {
        this.main = main;
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        String pathh = "C:\\multipaper\\multipaper - Copy - Copy\\UHC2023\\";
        try {
            File myObj = new File(pathh+"playercount\\playercount.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data.equals("1")) {
                    try {
                        File myObj3 = new File(pathh+"finaleresult\\finaleresult.txt");
                        Scanner myReader3 = new Scanner(myObj3);
                        while (myReader3.hasNextLine()) {
                            String data3 = myReader3.nextLine();
                            list.add(myReader3.next());
                            Collections.sort(list);
                        }
                        System.out.println("After Sorting: "+ list);
                    } catch (FileNotFoundException eg) {
                        System.out.println("An error occurred.");
                        eg.printStackTrace();
                    }


                }
            }
        } catch (FileNotFoundException ee) {
            System.out.println("An error occurred.");
            ee.printStackTrace();
        }

    }}
