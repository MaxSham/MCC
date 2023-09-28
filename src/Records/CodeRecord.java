package Records;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class CodeRecord extends DataRecord{
    public CodeRecord(byte[] _paramNum, byte[] _time, byte _razmernost, byte _attribute_type, byte[] _data) {
        super(_paramNum, _time, _razmernost, _attribute_type, _data);
    }
    public String toHeaderString(){
        return String.format("%-8s %-21s %-7s %19s", paramName, "Code", razmernost, "Время");
    }

    public String toDataString(){
        return String.format("%-8s %-21s %27s", " ", getData(), getTimeString());
    }

    private int getData(){
        byte[] buff = Arrays.copyOfRange(data, 4, 8);
        return ByteBuffer.wrap(buff).getInt();
    }
}
