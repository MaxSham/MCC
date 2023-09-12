package Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

public class DimParser {
    Map<Short, String> map;

    public DimParser(String fileName){
        map = new HashMap<Short, String>();
        short i = 1;
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            while (line != null) {
                map.put(i++, line);
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
