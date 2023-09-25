package Records;
import Utility.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

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
        short razmNum = (short) _razmernost;
        if(razmNum == 18){
            razmernost = "TEXT";
        }else{
            razmernost = dimParser.getRazm(razmNum);
        }


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
    public String toHeaderString(){
        String t = switch (type){
            case LONG -> "Long";
            case DOUBLE -> "Double";
            case CODE -> "Code";
            case POINT -> "Point";
        };
        String result;
        if(Objects.equals(razmernost, "TEXT")){
            result = String.format("%-8s %-8s %-20s %19s", paramName, t, "Значение", "Время");
        }
        else{
            result = String.format("%-8s %-21s %-7s %19s", paramName, t, razmernost, "Время");
        }
        return result;
    }
    public String toDataString(){
        String result;
        if(Objects.equals(razmernost, "TEXT")){
            result = String.format("%-8s %-8s %-20s %19s", " ", getData(), XMLParser.getText(paramName, (int)getData()), getTimeString());
        }
        else{
            result = String.format("%-8s %-21s %27s", " ", getData(), getTimeString());
        }
        return result;
    }
    public String toString(){

        String t = switch (type){
            case LONG -> "Long";
            case DOUBLE -> "Double";
            case CODE -> "Code";
            case POINT -> "Point";
        };

        if(Objects.equals(razmernost, "TEXT")){
            return String.format("%12s) %-10s  %8s  %25s", getTimeString(), paramName, t, XMLParser.getText(paramName,(int)getData()));
        }
        return String.format("%12s) %-10s  %8s  %25s  %10s", getTimeString(), paramName, t, getData().toString(), razmernost);
    }
    private Object getData(){
        return switch (type) {
            case LONG, CODE -> {
                byte[] buff = Arrays.copyOfRange(data, 4, 8);
                yield ByteBuffer.wrap(buff).getInt();
            }
            case DOUBLE -> ByteBuffer.wrap(data).getDouble();
            case POINT -> "point";
        };
    }
}

enum RecordsType{
    LONG, DOUBLE, CODE, POINT
}

