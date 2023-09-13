package Records;
import Utility.*;
import Telemetry.Config;

import javax.print.DocFlavor;
import java.io.ObjectInputFilter;
import java.nio.ByteBuffer;
import java.lang.Integer;
import java.util.Arrays;

public class DataRecord extends TMRecord{

    private static DimParser dimParser = new DimParser();
    String razmernost;
    RecordsType type;
    byte[] data;

    public DataRecord(byte[] _paramNum,
                      byte[] _time,
                      byte _razmernost,
                      byte _attribute_type,
                      byte[] _data)
    {
        super(_paramNum, _time, false);

        //short paramNum=(short)(((_paramNum[0] & 0xFF) << 8) | (_paramNum[1] & 0xFF));

        razmernost = dimParser.getRazm((short) _razmernost);

        type = getType(_attribute_type);
        data = _data;
    }
    public RecordsType getType(byte b){
        int tb = b&0x0F;
        switch (tb){
            case 0:
                return RecordsType.LONG;
            case 1:
                return RecordsType.DOUBLE;
            case 2:
                return RecordsType.CODE;
            case 3:
                return RecordsType.POINT;
            default:
                System.out.println("Record TYPE ERROR " + tb);
                return RecordsType.CODE;
        }
    }

    public String toString(){

        String t = switch (type){
            case LONG -> "Long";
            case DOUBLE -> "Double";
            case CODE -> "Code";
            case POINT -> "Point";
        };


        return String.format("%12s) %-10s  %8s  %25s  %10s", getTimeString(), paramName, t, getData().toString(), razmernost);
    }
    private Object getData(){
        switch (type){
            case LONG, CODE:
                byte[] buff = Arrays.copyOfRange(data, 4, 8);
                return ByteBuffer.wrap(buff).getInt();
            case DOUBLE:
                return ByteBuffer.wrap(data).getDouble();
            case POINT:
                return "POINT_DATA";
        }
        return "NO DATA";
    }

    public static XMLParser getXmlParser(){
        return xmlParser;
    }
}

enum RecordsType{
    LONG, DOUBLE, CODE, POINT
}

