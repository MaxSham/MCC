package Telemetry;
import Records.DataRecord;
import Records.RecordsHolder;
import Records.TMRecord;
import Records.TechRecord;
import Utility.*;
import Utility.Writer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class App {
    File knpFile;
    RecordsHolder recordsHolder;

    public App(String KNPFileName) throws ParserConfigurationException, IOException, SAXException {
        knpFile = new File(KNPFileName);
        recordsHolder = new RecordsHolder();
    }
    public void run(){
        run(Integer.MAX_VALUE);
    }
    public void run(int size){
        try(FileInputStream stream = new FileInputStream(knpFile)) {

            byte[] recordInBytes = new byte[16];

            while(stream.read(recordInBytes) != -1 && size != 0){
                size -= 1;

                byte[] paramNum = Arrays.copyOf(recordInBytes, 2);
                byte[] bTime = Arrays.copyOfRange(recordInBytes, 2, 6);
                TMRecord record;
                if((paramNum[0]&0xFF) == 0xFF && (paramNum[1]&0xFF) == 0xFF){ // Служебная

                    byte[] bMesZnach = Arrays.copyOfRange(recordInBytes, 6, 8);
                    byte[] bData = Arrays.copyOfRange(recordInBytes, 8, 16);
                    if(bMesZnach[0] == 1){
                        byte[] extraData = new byte[16];
                        stream.read(extraData);
                        byte[] buff = bData;
                        bData = new byte[24];
                        System.arraycopy(buff, 0, bData, 0, 8);
                        System.arraycopy(extraData, 0, bData, 8, 16);
                    }
                    record = new TechRecord(paramNum, bTime, bMesZnach[0], bMesZnach[1],bData);
                }
                else{                                           // Обычная
                    byte razm = recordInBytes[6];
                    byte atr_type = recordInBytes[7];
                    byte[] bData = Arrays.copyOfRange(recordInBytes, 8, 16);
                    if((atr_type&0x0F) == 3){
                        int extraDataSize = ((Byte.toUnsignedInt(recordInBytes[10]) << 8) + Byte.toUnsignedInt(recordInBytes[11])) - 4;
                        byte[] extraData = new byte[extraDataSize];
                        stream.read(extraData);

                        byte[] buff = bData;
                        bData = new byte[extraDataSize + 8];
                        System.arraycopy(buff, 0, bData, 0, 8);
                        System.arraycopy(extraData, 0, bData, 8, extraDataSize);
                    }
                    record = new DataRecord(paramNum, bTime, razm, atr_type, bData);
                }
                recordsHolder.addRecord(record);
            }
            if(size != 0){
                System.out.println("-=-=-=-=-=-=-=-=-=-=-=-END FILE-=-=-=-=-=-=-=-=-=");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void printToFile(List<String> filter, String path, boolean alphabetSort){
        try {
            if(alphabetSort){
                Writer.printToFileByAlphabet(path, recordsHolder, filter);
            }
            else{
                Writer.printToFileByTime(path, recordsHolder, filter);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void print(){
        recordsHolder.printToConsole();
        System.out.println("Data:"+recordsHolder.getDataCount() + " Tech:"+recordsHolder.getTechCount());
    }
    public List<String> getParamsInside(){
        Set<String> buff = recordsHolder.getParametrsInside();
        List<String> result = new ArrayList<>(buff);
        Collections.sort(result);
        return result;
    }


    public void task1(){

    }
}
