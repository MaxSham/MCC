package Records;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class TechRecord extends TMRecord{
    TechRecordsType type;
    byte[] data;
    public TechRecord(byte[] _paramNum,
                      byte[] _time,
                      byte _mesType,
                      byte _znachType,
                      byte[] _mes)
    {
        super(_paramNum, _time, true);

        type = getType(_mesType);
        data = _mes;
    }

    public String toString() {
        String t = switch (type){
            case START -> "Start";
            case TIME -> "Time";
            case END -> "End";
            case NEW_MODE -> "New-mode";
            case NEW_DATE -> "New-date";
            case ERROR -> "Error";
            case EMPTY -> "Empty";
        };

        String result = String.format("%12s) %10s  %8s  |%20s", getTimeString(), "-Tech-", t, getData());

        return result;
    }

    private TechRecordsType getType(byte b){
        return switch (b){
            case 1 -> TechRecordsType.START;
            case 2 -> TechRecordsType.TIME;
            case 3 -> TechRecordsType.END;
            case 4 -> TechRecordsType.NEW_MODE;
            case 5 -> TechRecordsType.NEW_DATE;
            case 6 -> TechRecordsType.ERROR;
            default -> TechRecordsType.EMPTY;
        };
    }
    private Object getData(){
        switch (type){
            case EMPTY:
                return "";
            case START:
                return "Start-data";
            case TIME:
                byte[] buff = Arrays.copyOfRange(data, 0, 4);
                return ByteBuffer.wrap(buff).getInt();
            case END:
                return "END";
            case ERROR, NEW_DATE, NEW_MODE:
                byte[] buff2 = Arrays.copyOfRange(data, 4, 8);
                return ByteBuffer.wrap(buff2).getInt();
        }
        return "TECH DATA ERROR";
    }
}
enum TechRecordsType{
    EMPTY, START, TIME, END, NEW_MODE, NEW_DATE, ERROR
}
