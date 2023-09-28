package Records;
import Utility.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public class DataRecord extends TMRecord{

    private static DimParser dimParser = new DimParser();
    String razmernost;
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
        data = _data;
    }
    public String toHeaderString(){return null;}
    public String toDataString(){
        return null;
    }
    public String toString(){
        return null;
    }
}

