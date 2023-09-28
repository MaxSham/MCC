package Records;

import Utility.XMLParser;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public class DoubleRecord extends DataRecord{
    public DoubleRecord(byte[] _paramNum, byte[] _time, byte _razmernost, byte _attribute_type, byte[] _data) {
        super(_paramNum, _time, _razmernost, _attribute_type, _data);
    }
    public String toHeaderString(){
        return String.format("%-8s %-21s %-7s %19s", paramName, "Double", razmernost, "Время");
    }

    public String toDataString(){
        return String.format("%-8s %-21s %27s", " ", getData(), getTimeString());
    }

    private double getData(){
        return ByteBuffer.wrap(data).getDouble();
    }
}
