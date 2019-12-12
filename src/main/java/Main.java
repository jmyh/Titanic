import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static String headerCSV;
    public static void main(String[] args) throws IOException {

        Reader reader=new Reader();

        //read input data
        List<Passenger> passengersTrn=reader.parseCSV("files/default/train.csv");
        headerCSV=reader.getHeaderCSV();
        //addSurvivedField("files/test.csv");
        List<Passenger> passengersTst=reader.parseCSV("files/default/test.csv");

        //concat all data
        List<Passenger> passengersAll=new ArrayList<>();
        passengersAll.addAll(passengersTrn);
        passengersAll.addAll(passengersTst);

        //show all data
        System.out.println("Data: ");
        for(Passenger passenger:passengersAll)
            System.out.println(passenger);

        //show empty fields
        PlotMaker plotMaker=new PlotMaker();
        JFreeChart chartEmpty=plotMaker.createHeatMap(passengersAll);

        ApplicationFrame app=new ApplicationFrame("Empty fields");
        app.setContentPane(new ChartPanel(chartEmpty));
        app.pack();
        app.setVisible(true);

        //print unique values
        printUniqueValues(passengersAll);

        //show distribution of survivors by class
        JFreeChart chartClass=plotMaker.createClassPlot(passengersAll);
        app=new ApplicationFrame("Distribution of survivors by class");
        app.setContentPane(new ChartPanel(chartClass));
        app.pack();
        app.setVisible(true);

        //show distribution of survivors by sex
        JFreeChart  chartSex=plotMaker.createSexPlot(passengersAll);
        app=new ApplicationFrame("Distribution of survivors by sex");
        app.setContentPane(new ChartPanel(chartSex));
        app.pack();
        app.setVisible(true);

        //show distribution of survivors by sex
        JFreeChart  chartAge=plotMaker.createAgePlot(passengersAll);
        app=new ApplicationFrame("Distribution of survivors by age");
        app.setContentPane(new ChartPanel(chartAge));
        app.pack();
        app.setVisible(true);

        //show distribution of survivors by SibSp
        JFreeChart  chartSibSp=plotMaker.createSibSpPlot(passengersAll);
        app=new ApplicationFrame("Distribution of survivors by SibSp");
        app.setContentPane(new ChartPanel(chartSibSp));
        app.pack();
        app.setVisible(true);

        //show distribution of survivors by Fare Category
        JFreeChart  chartFareCat=plotMaker.createFareCategoryChart(passengersAll);
        app=new ApplicationFrame("Distribution of survivors by Fare Category");
        app.setContentPane(new ChartPanel(chartFareCat));
        app.pack();
        app.setVisible(true);

        //show distribution of survivors by embarked
        JFreeChart  chartEmbarked=plotMaker.createEmbarkedChart(passengersAll);
        app=new ApplicationFrame("Distribution of survivors by embarked");
        app.setContentPane(new ChartPanel(chartEmbarked));
        app.pack();
        app.setVisible(true);

        //fill embarked field
        for(Passenger passenger:passengersAll) {
            if (passenger.getEmbarked() == null)
                passenger.setEmbarked(Passenger.SOUTHAMPTON);
        }

        //fill cabin field
        for(Passenger passenger:passengersAll) {
            if (passenger.getCabin() == null)
                passenger.setCabin(Passenger.UNKNOWN);

        }

        //fill age field
        fillAgeField(passengersAll);

        //fill fare field
        fillFareField(passengersAll);

        //check new values
        JFreeChart chartEmpty2=plotMaker.createHeatMap(passengersAll);
        ApplicationFrame appEmpty=new ApplicationFrame("Empty fields after fill");
        appEmpty.setContentPane(new ChartPanel(chartEmpty2));
        appEmpty.pack();
        appEmpty.setVisible(true);

        Writer writer=new Writer(Passenger.MASK_DEFAULT);
        writer.setHeaderCSV(headerCSV);
        writer.writeCSV("files/modified/csv/train.csv",passengersTrn);

        fillSurvivedFieldTst(passengersTst);
        writer.writeCSV("files/modified/csv/test.csv",passengersTst);

        writer.convertCSVToARFF("files/modified/csv/train.csv","files/modified/arff/train.arff");
        writer.convertCSVToARFF("files/modified/csv/test.csv","files/modified/arff/test.arff");

        try {
            trainModel("files/modified/arff/train.arff","files/modified/arff/test.arff",0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Instances getDataSet(String filePath, int classIdx) throws IOException {
            ArffLoader loader = new ArffLoader();
            loader.setSource(new File(filePath));
            Instances dataSet = loader.getDataSet();
            /** set the index based on the data given in the arff files */
            dataSet.setClassIndex(classIdx);
            return dataSet;
        }

 
    public static void trainModel(String trainPath, String testPath, int classIdx) throws Exception {
        Instances trainingDataSet=null, testingDataSet=null;
        try {
            trainingDataSet = getDataSet(trainPath, classIdx);
            testingDataSet = getDataSet(testPath, classIdx);
        } catch (IOException e) {
            e.printStackTrace();
        }


        RandomForest forest=new RandomForest();
        
        forest.buildClassifier(trainingDataSet);
  
        Evaluation eval = new Evaluation(trainingDataSet);
        eval.evaluateModel(forest, testingDataSet);


        /** Print the algorithm summary */
        System.out.println("** Decision Tress Evaluation with Datasets **");
        System.out.println(eval.toSummaryString());
        System.out.print(" the expression for the input data as per alogorithm is ");
        System.out.println(forest);
        System.out.println(eval.toMatrixString());
        System.out.println(eval.toClassDetailsString());

    }


    private static void fillSurvivedFieldTst(List<Passenger> passengersTst) {
        Reader reader=new Reader();
        Map<Integer, Boolean> map=reader.readGS("files/default/gender_submission.csv");
        for(Passenger passenger:passengersTst) {
            passenger.setSurvived(map.get(passenger.getId()));
        }
    }

    private static void fillFareField(List<Passenger> passengers) {
        double average=passengers.stream().filter(p->p.getFare()!=Passenger.INT_NULL).mapToDouble(Passenger::getFare).average().getAsDouble();
        for(Passenger passenger:passengers) {
            if(passenger.getFare()==Passenger.INT_NULL)
                passenger.setFare(average);
        }
    }

    private static void fillAgeField(List<Passenger> passengers) {
        Map<String, Map<Integer, List<Passenger>>> groupForAge=passengers.stream().collect(Collectors.groupingBy(Passenger::getSex,Collectors.groupingBy(Passenger::getpClass)));
        Set<Map.Entry<String, Map<Integer, List<Passenger>>>> setSexGroup=groupForAge.entrySet();
        for(Map.Entry<String, Map<Integer,List<Passenger>>> entrySex:setSexGroup) {
            Set<Map.Entry<Integer, List<Passenger>>> setClassGroup=entrySex.getValue().entrySet();
            for(Map.Entry<Integer,List<Passenger>> entryClass:setClassGroup) {
                List<Passenger> groupPass=entryClass.getValue();
                double average=groupPass.stream().filter(p->p.getAge()!=Passenger.INT_NULL).mapToDouble(Passenger::getAge).average().getAsDouble();

                for(Passenger p:groupPass) {
                    if(p.getAge()==Passenger.INT_NULL)
                        p.setAge((float) average);
                }
            }
        }
    }

    private static void printUniqueValues(List<Passenger> passengers) {
        System.out.println("\nUnique values");
        Set<Integer> id=new HashSet<>();
        Set<Boolean> survived=new HashSet<>();
        Set<Integer> pClass=new HashSet<>();
        Set<String> name=new HashSet<>();
        Set<String> sex=new HashSet<>();
        Set<Float> age=new HashSet<>();
        Set<Integer> sibSp=new HashSet<>();
        Set<Integer> parch=new HashSet<>();
        Set<String> ticket=new HashSet<>();
        Set<Double> fare=new HashSet<>();
        Set<String> cabin=new HashSet<>();
        Set<Character> embarked=new HashSet<>();
        Set<Integer> family=new HashSet<>();
        Set<Boolean> alone=new HashSet<>();
        Set<FareCategory> fareCategories=new HashSet<>();
        Set<String> salutation=new HashSet<>();

        for(Passenger passenger:passengers) {
            id.add(passenger.getId());
            survived.add(passenger.isSurvived());
            pClass.add(passenger.getpClass());
            name.add(passenger.getName());
            sex.add(passenger.getSex());
            age.add(passenger.getAge());
            sibSp.add(passenger.getSibSp());
            parch.add(passenger.getParch());
            ticket.add(passenger.getTicket());
            fare.add(passenger.getFare());
            cabin.add(passenger.getCabin());
            embarked.add(passenger.getEmbarked());
            family.add(passenger.getFamily());
            alone.add(passenger.isAlone());
            fareCategories.add(passenger.getFareCategory());
            salutation.add(passenger.getSalutation());
        }

        System.out.format("%-14s %8d","ID:",id.size());System.out.println("\t\t"+id);
        System.out.format("%-14s %8d","survived:",survived.size());System.out.println("\t\t"+survived);
        System.out.format("%-14s %8d","pClass:",pClass.size());System.out.println("\t\t"+pClass);
        System.out.format("%-14s %8d","name:",name.size());System.out.println("\t\t"+name);
        System.out.format("%-14s %8d","sex:",sex.size());System.out.println("\t\t"+sex);
        System.out.format("%-14s %8d","age",age.size());System.out.println("\t\t"+age);
        System.out.format("%-14s %8d","sibSp:",sibSp.size());System.out.println("\t\t"+sibSp);
        System.out.format("%-14s %8d","parch:",parch.size());System.out.println("\t\t"+parch);
        System.out.format("%-14s %8d","ticket:",ticket.size());System.out.println("\t\t"+ticket);
        System.out.format("%-14s %8d","fare:",fare.size());System.out.println("\t\t"+fare);
        System.out.format("%-14s %8d","cabin:",cabin.size());System.out.println("\t\t"+cabin);
        System.out.format("%-14s %8d","embarked:",embarked.size());System.out.println("\t\t"+embarked);
        System.out.format("%-14s %8d","family:",family.size());System.out.println("\t\t"+family);
        System.out.format("%-14s %8d","alone",alone.size());System.out.println("\t\t"+alone);
        System.out.format("%-14s %8d","fareCategory:",fareCategories.size());System.out.println("\t\t"+fareCategories);
        System.out.format("%-14s %8d","salutation:",salutation.size());System.out.println("\t\t"+salutation);


    }

    private static void addSurvivedField(String path) throws FileNotFoundException {
        List<String> strings = new ArrayList<>();
        try(Scanner scan=new Scanner(new FileInputStream(path))) {

            boolean firstLine = true;
            //read file
            while (scan.hasNext()) {
                if (!firstLine) {
                    StringBuilder inputStr = new StringBuilder(scan.nextLine());
                    inputStr.insert(inputStr.indexOf(","), ",");
                    strings.add(inputStr.toString());
                } else {
                    strings.add(scan.nextLine());
                    firstLine = false;
                }
            }
        }
        //write file
        try(PrintWriter writer=new PrintWriter(path)) {
            for (int i = 0; i < strings.size(); i++) {
                if (i != strings.size() - 1) writer.println(strings.get(i));
                else writer.format(strings.get(i));
            }
        }
    }

}
