package Records;
import Utility.XMLParser;

import java.nio.ByteBuffer;

public abstract class TMRecord {

    final static int SEC = 1000;
    final static int MIN = SEC*60;
    final static int HOUR = MIN*60;
    final short paramNum;
    String paramName;
    final int time;
    private final Boolean isTech;
    static XMLParser xmlParser = new XMLParser();

    TMRecord(byte[] _paramNum, byte[] _time, Boolean _isTech){
        paramNum = ByteBuffer.wrap(_paramNum).getShort();
        paramName = xmlParser.getParamName(paramNum);
        time = ByteBuffer.wrap(_time).getInt();
        isTech = _isTech;
    }
    public Boolean isTech(){
        return isTech;
    }

    public short getParamNum(){
        return paramNum;
    }
    public String getParamName(){
        return paramName;
    }

    String getTimeString(){
        if(time < 0) return "NEGATIVE TIME";
        int buff = time, h, m, s;

        h = buff/HOUR;
        buff -= HOUR*h;

        m = buff/MIN;
        buff -= MIN*m;

        s = buff/SEC;
        buff -= SEC*s;

        String result = String.format("%02d:%02d:%02d,%03d", h,m,s,buff);

        return result;
    }

    public abstract String toString();
}
