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

    public static int PAGE_SIZE = 50;

    public static void printToFileByAlphabet(String filePath, RecordsHolder records, List<String> filter) throws IOException {
        fileWriter = new FileWriter(filePath);
        printWriter = new PrintWriter(fileWriter);

        int currentPageSize = 0;
        int currentPage = 1;

        Map<String, List<TMRecord>> recordsByAlphabet = new HashMap<>();
        Map<String, String> pageByParam = new HashMap<>();
        for (String param: filter) {
            List<TMRecord> buff = new ArrayList<TMRecord>();
            recordsByAlphabet.put(param, buff);
        }

        int size = records.size();
        for(int i = 0; i < size; ++i){
            TMRecord record = records.get(i);
            if(filter.contains(record.getParamName())){
                recordsByAlphabet.get(record.getParamName()).add(record);
            }
        }
        boolean firstTime;
        for (String param: filter) {
            List<TMRecord> buff = recordsByAlphabet.get(param);
            String paramInfo = String.format("(%d)-%d", buff.size(), currentPage);
            pageByParam.put(param, paramInfo);
            firstTime = true;
            if(PAGE_SIZE - currentPageSize < 3){
                printWriter.print("-------------------------------------------------------------------\n");
                printWriter.print(String.format("%33d",currentPage));
                printWriter.print("\n-------------------------------------------------------------------");
                printWriter.print("\n-------------------------------------------------------------------\n\n");
                currentPage++;
                currentPageSize = 0;
            }
            for (TMRecord recordToPrint: buff) {
                if(firstTime || currentPageSize == 0){
                    printWriter.println("-".repeat(58));
                    printWriter.println(recordToPrint.toHeaderString());
                    firstTime = false;
                    currentPageSize+=2;
                }
                printWriter.println(recordToPrint.toDataString());
                currentPageSize++;
                if(currentPageSize == PAGE_SIZE){
                    printWriter.print("-------------------------------------------------------------------\n");
                    printWriter.print(String.format("%33d",currentPage));
                    printWriter.print("\n-------------------------------------------------------------------\n\n");
                    currentPage++;
                    currentPageSize = 0;
                }
            }
        }
        printWriter.print("-------------------------------------------------------------------\n");
        printWriter.print(String.format("%33d",currentPage));
        printWriter.print("\n-------------------------------------------------------------------\n\n");

        printPageInfo(filter,  pageByParam);
        printWriter.close();
        fileWriter.close();
    }
    private static void printPageInfo(List<String> params, Map<String, String> pagesInfo){
        printWriter.println("\nСодержание:");

        for (String param:params) {
            String[] info = pagesInfo.get(param).split("-");
            printWriter.println(String.format("%-20s%-8s", param + info[0], info[1]));
        }

    }

    public static void printToFileByTime(String filePath, RecordsHolder records, List<String> filter) throws IOException {
        fileWriter = new FileWriter(filePath);
        printWriter = new PrintWriter(fileWriter);

        int size = records.size();
        for(int i = 0; i < size; ++i){
            TMRecord record = records.get(i);
            if(filter.contains(record.getParamName())){
                printWriter.println(record.toHeaderString());
                printWriter.println(record.toDataString());
            }
        }
        printWriter.close();
    }

}
