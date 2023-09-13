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

        Map<String, List<String>> recordsByAlphabet = new HashMap<>();
        Map<String, String> pageByParam = new HashMap<>();
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
            String paramInfo = String.format("(%d)-%d", buff.size(), currentPage);
            pageByParam.put(param, paramInfo);
            for (String lineForPrint: buff) {
                printWriter.println(lineForPrint);
                currentPageSize++;
                if(currentPageSize == PAGE_SIZE){
                    printWriter.print("-------------------------------------------------------------------\n");
                    printWriter.print(String.format("%33d",currentPage));
                    printWriter.print("\n-------------------------------------------------------------------");
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

        int currentPageSize = 0;
        int currentPage = 1;

        int size = records.size();
        for(int i = 0; i < size; ++i){
            TMRecord record = records.get(i);
            if(filter.contains(record.getParamName())){
                printWriter.println(record.toString());
                currentPageSize++;
                if(currentPageSize == PAGE_SIZE){
                    printWriter.print("\n----------------------- " + currentPage + " -----------------------\n\n");
                    currentPage++;
                    currentPageSize = 0;
                }
            }
        }
        printWriter.close();
    }

}
