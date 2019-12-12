import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reader {
    private StringBuilder headerCSV=new StringBuilder();

    public List<Passenger> parseCSV(String path) {
        headerCSV=new StringBuilder();

        List<Passenger> passengers=new ArrayList<>();
        try(CSVReader reader=new CSVReader(new FileReader(path))) {
            String[] line;
            boolean firstLine=true;
            while ((line = reader.readNext()) != null) {
                if(!firstLine)
                    passengers.add(new Passenger(line[0],line[1],line[2],line[3],line[4],line[5],line[6],line[7],line[8],line[9],line[10],line[11]));
                else {
                    for(int i=0;i<line.length;i++) {
                        if(i!=line.length-1) headerCSV.append(line[i]+",");
                        else headerCSV.append(line[i]);
                    }
                    firstLine=false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return passengers;
    }

    public Map<Integer, Boolean> readGS(String path) {
        Map<Integer, Boolean> map=new HashMap<>();
        try(CSVReader reader=new CSVReader(new FileReader(path))) {
            String[] line;
            boolean firstLine=true;
            while ((line = reader.readNext()) != null) {
                if(!firstLine)
                    map.put(Integer.parseInt(line[0]),(line[1].equals("1"))?true:false);
                else {
                    firstLine=false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public String getHeaderCSV() {
        return headerCSV.toString();
    }

    public void setHeaderCSV(String headerCSV) {
        this.headerCSV = new StringBuilder(headerCSV);
    }
}
