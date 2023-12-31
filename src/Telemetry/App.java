package Telemetry;
import Records.*;
import Utility.Writer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

public class App {
    File dataFile;
    RecordsHolder recordsHolder;

    public App(String DataFileName) throws ParserConfigurationException, IOException, SAXException {
        dataFile = new File(DataFileName);
        recordsHolder = new RecordsHolder();
    }
    public void run(){
        try(FileInputStream stream = new FileInputStream(dataFile)) {

            byte[] recordInBytes = new byte[16];

            while(stream.read(recordInBytes) != -1 ){

                byte[] paramNum = Arrays.copyOf(recordInBytes, 2);
                byte[] bTime = Arrays.copyOfRange(recordInBytes, 2, 6);
                TMRecord record;
                if((paramNum[0]&0xFF) == 0xFF && (paramNum[1]&0xFF) == 0xFF){ // Служебная

                    byte bMes = recordInBytes[6];
                    byte bZnach = recordInBytes[7];
                    byte[] bData = Arrays.copyOfRange(recordInBytes, 8, 16);
                    if(bMes == 1){
                        byte[] extraData = new byte[16];
                        stream.read(extraData);
                        byte[] buff = bData;
                        bData = new byte[24];
                        System.arraycopy(buff, 0, bData, 0, 8);
                        System.arraycopy(extraData, 0, bData, 8, 16);
                    }
                    record = new TechRecord(paramNum, bTime, bMes, bZnach, bData);
                }
                else{                                           // Обычная
                    byte dim = recordInBytes[6];
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
                    record = DataRecordsBuilder.createDataRecord(paramNum, bTime, dim, atr_type, bData);
                }
                recordsHolder.addRecord(record);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getStatTech(){
        return recordsHolder.getTechCount();
    }
    public int getStatData(){
        return recordsHolder.getDataCount();
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
    public List<String> getParamsInside(){
        Set<String> buff = recordsHolder.getParametrsInside();
        List<String> result = new ArrayList<>(buff);
        Collections.sort(result);
        return result;
    }

}
