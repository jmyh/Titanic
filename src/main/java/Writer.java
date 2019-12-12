import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.*;
import java.util.List;

public class Writer {
    private String headerCSV;
    private int mask;

    public Writer(int mask) {
        this.mask=mask;
    }

    public void convertCSVToARFF(String csvPath, String arffPath) throws IOException {
        // load CSV
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(csvPath));
        Instances data = loader.getDataSet();

        // save ARFF
        File arffFile=new File(arffPath);
        if(!arffFile.getParentFile().exists()) arffFile.getParentFile().mkdirs();

        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(arffPath));
        saver.setDestination(new File(arffPath));
        saver.writeBatch();
    }

    public void writeCSV(String path, List<Passenger> passengers) throws FileNotFoundException {
        File pathFile=new File(path);

        if(!pathFile.getParentFile().exists()) {
            pathFile.getParentFile().mkdirs();
        }
        try {
            pathFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(PrintWriter writer=new PrintWriter(new FileOutputStream(new File(path)))) {
            writer.println(getHeader());
            for(Passenger passenger:passengers) {
                writer.println(passenger.toCSVString(mask));
            }
        }

    }

    private String getHeader() {
        String[] elements=headerCSV.split(",");
        String[] finalElements=new String[16];
        StringBuilder result=new StringBuilder();

        if((mask>>15)%2==1) finalElements[0]=elements[0];
        if((mask>>14)%2==1) finalElements[1]=elements[1];
        if((mask>>13)%2==1) finalElements[2]=elements[2];
        if((mask>>12)%2==1) finalElements[3]=elements[3];
        if((mask>>11)%2==1) finalElements[4]=elements[4];
        if((mask>>10)%2==1) finalElements[5]=elements[5];
        if((mask>>9)%2==1) finalElements[6]=elements[6];
        if((mask>>8)%2==1) finalElements[7]=elements[7];
        if((mask>>7)%2==1) finalElements[8]=elements[8];
        if((mask>>6)%2==1) finalElements[9]=elements[9];
        if((mask>>5)%2==1) finalElements[10]=elements[10];
        if((mask>>4)%2==1) finalElements[11]=elements[11];
        if((mask>>3)%2==1) finalElements[12]="Family";
        if((mask>>2)%2==1) finalElements[13]="Alone";
        if((mask>>1)%2==1) finalElements[14]="Fare Category";
        if((mask>>0)%2==1) finalElements[15]="Salutation";

        for(int i=0;i<finalElements.length;i++) {
            if(finalElements[i]!=null) {
                result.append(finalElements[i]);
                if(i!=finalElements.length-1) result.append(",");
            }
        }
        result.deleteCharAt(result.length()-1);
        return result.toString();
    }

    public String getHeaderCSV() {
        return headerCSV;
    }

    public void setHeaderCSV(String headerCSV) {
        this.headerCSV = headerCSV;
    }
}
