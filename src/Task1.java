import Telemetry.Config;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Task1 {
    public static void run(int amountOfLines, int weight){
        if(weight <= 0 || amountOfLines <= 0) return;

        try(FileInputStream stream = new FileInputStream(Config.KNPFilePath)) {
            FileWriter fileWriter = new FileWriter("Data/out/Task1.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            byte[] bytes = new byte[weight];
            int currentLineNum = 0;

            while(stream.read(bytes) != -1 && currentLineNum < amountOfLines){
                System.out.print(String.format("%04X  ", currentLineNum * weight));
                for(int i = 0; i < weight; ++i){
                    System.out.print(String.format("%02X ", bytes[i]));
                }
                System.out.print("\n");
                currentLineNum++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
