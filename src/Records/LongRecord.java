package Records;

import Utility.XMLParser;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public class LongRecord extends DataRecord{

    public LongRecord(byte[] _paramNum, byte[] _time, byte _razmernost, byte _attribute_type, byte[] _data) {
        super(_paramNum, _time, _razmernost, _attribute_type, _data);
    }

    public String toHeaderString(){

        String result;
        if(Objects.equals(razmernost, "TEXT")){
            result = String.format("%-8s %-8s %-20s %19s", paramName, "Long", "Значение", "Время");
        }
        else{
            result = String.format("%-8s %-21s %-7s %19s", paramName, "Long", razmernost, "Время");
        }
        return result;
    }

    public String toDataString(){
        String result;
        if(Objects.equals(razmernost, "TEXT")){
            result = String.format("%-8s %-8s %-20s %19s", " ", getData(), XMLParser.getText(paramName, getData()), getTimeString());
        }
        else{
            result = String.format("%-8s %-21s %27s", " ", getData(), getTimeString());
        }
        return result;
    }

    private int getData(){
        byte[] buff = Arrays.copyOfRange(data, 4, 8);
        return ByteBuffer.wrap(buff).getInt();
    }
}
