package Utility;

import Telemetry.Config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DimParser {
    Map<Short, String> map;

    public DimParser(){
        map = new HashMap<Short, String>();
        short i = 1;
        BufferedReader reader;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(Config.DimFilePath)), "WINDOWS-1251"));
            String line = reader.readLine();

            while (line != null) {
                line = line.trim();
                map.put(i, line);
                i++;
                line = reader.readLine();
            }

            reader.close();
        } catch (Exception e) {
            System.out.println("Dim parser error: ");
            e.printStackTrace();
        }
    }
    public String getRazm(short num){
        return map.get(num);
    }
}
