package Records;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecordsHolder {
    private List<TMRecord> records = new ArrayList<>();
    private int statTechCount = 0, statDataCount = 0;
    private Set<String> parametrsInside = new HashSet<String>();

    public void printToConsole(){
        String result = "";
        for(TMRecord record : records){
            result = record.toString();
            System.out.println(result);
        }
    }
    public void addRecord(TMRecord toAdd){
        try {
            records.add(toAdd);
            parametrsInside.add(toAdd.paramName);
            if (toAdd.isTech()) {
                statTechCount++;
            } else {
                statDataCount++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public int size(){
        return statDataCount + statTechCount;
    }
    public TMRecord get(int index){
        if(index > records.size()){
            return null;
        }
        return records.get(index);
    }
    public int getTechCount(){
        return statTechCount;
    }
    public int getDataCount(){
        return statDataCount;
    }
    public Set<String> getParametrsInside(){
        return parametrsInside;
    }
    String createRecordsFile(String fileName){
        return "result";
    }
    String createStatsFile(String fileName){
        return "result";
    }
}
