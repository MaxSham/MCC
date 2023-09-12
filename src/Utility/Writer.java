package Utility;

import Records.RecordsHolder;
import Records.TMRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Writer {
    static FileWriter fileWriter;
    static PrintWriter printWriter;

    public static void printToFileByAlphabet(String filePath, RecordsHolder records, List<String> filter) throws IOException {
        fileWriter = new FileWriter(filePath);
        printWriter = new PrintWriter(fileWriter);

        Map<String, List<String>> recordsByAlphabet = new HashMap<>();
        for (String param: filter) {
            List<String> buff = new ArrayList<String>();
            recordsByAlphabet.put(param, buff);
        }

        int size = records.size();
        for(int i = 0; i < size; ++i){
            TMRecord record = records.get(i);
            if(filter.contains(record.getParamName())){
                recordsByAlphabet.get(record.getParamName()).add(record.toString());
            }
        }

        for (String param: filter) {
            List<String> buff = recordsByAlphabet.get(param);
            for (String lineForPrint: buff) {
                printWriter.println(lineForPrint);
            }
        }
    }

    public static void printToFileByTime(String filePath, RecordsHolder records, List<String> filter) throws IOException {
        fileWriter = new FileWriter(filePath);
        printWriter = new PrintWriter(fileWriter);

        int size = records.size();
        for(int i = 0; i < size; ++i){
            TMRecord record = records.get(i);
            if(filter.contains(record.getParamName())){
                printWriter.println(record.toString());
            }
        }
        printWriter.close();
    }

}
