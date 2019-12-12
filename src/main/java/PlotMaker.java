import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYZDataset;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Stream;

public class PlotMaker {

    public JFreeChart createHeatMap(List<Passenger> passengers) throws IOException {
        double[] values = new double[passengers.size() * 12];
        double[] xVal = new double[passengers.size() * 12];
        double[] yVal = new double[passengers.size() * 12];
        for (int i = 0; i < passengers.size(); i++) {
            values[i * 12] = (passengers.get(i).getId() != Passenger.INT_NULL) ? 1 : 0;
            values[i * 12 + 1] = (passengers.get(i).isSurvived() != null) ? 1 : 0;
            values[i * 12 + 2] = (passengers.get(i).getpClass() != Passenger.INT_NULL) ? 1 : 0;
            values[i * 12 + 3] = (passengers.get(i).getName() != null) ? 1 : 0;
            values[i * 12 + 4] = (passengers.get(i).getSex() != null) ? 1 : 0;
            values[i * 12 + 5] = (passengers.get(i).getAge() != Passenger.INT_NULL) ? 1 : 0;
            values[i * 12 + 6] = (passengers.get(i).getSibSp() != Passenger.INT_NULL) ? 1 : 0;
            values[i * 12 + 7] = (passengers.get(i).getParch() != Passenger.INT_NULL) ? 1 : 0;
            values[i * 12 + 8] = (passengers.get(i).getTicket() != null) ? 1 : 0;
            values[i * 12 + 9] = (passengers.get(i).getFare() != Passenger.INT_NULL) ? 1 : 0;
            values[i * 12 + 10] = (passengers.get(i).getCabin() != null) ? 1 : 0;
            values[i * 12 + 11] = (passengers.get(i).getEmbarked() != null) ? 1 : 0;

            for (int j = 0; j < 12; j++) {
                int idx = i * 12 + j;
                xVal[idx] = j;
                yVal[idx] = i;
            }
        }

        NumberAxis yAxis = new NumberAxis("ID");

        // visible y-axis with symbols
        String labels[] = {"ID", "Survived", "pClass", "Name", "Sex", "Age", "Sibsp", "Parch", "Ticket", "Fare", "Cabin", "Embarked"};

        SymbolAxis xAxis = new SymbolAxis(null, labels);

        // create a paint-scale and a legend showing it
        LookupPaintScale paintScale = new LookupPaintScale(0, 300, Color.black);
        paintScale.add(0.0, Color.red);
        paintScale.add(1.0, Color.green);

        DefaultXYZDataset dataset = new DefaultXYZDataset();
        dataset.addSeries("Just one Series", new double[][]{xVal, yVal, values});
        // finally a renderer and a plot
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, new XYBlockRenderer());
        ((XYBlockRenderer) plot.getRenderer()).setPaintScale(paintScale);

        JFreeChart chart = new JFreeChart(null, null, plot, false);
        return chart;
    }

    public JFreeChart createClassPlot(List<Passenger> passengers) {
        long firstSurvived=passengers.stream().filter(p->(p.getpClass()==1 && (p.isSurvived()!=null && p.isSurvived()))).count();
        long firstNotSurvived=passengers.stream().filter(p->(p.getpClass()==1 && (p.isSurvived()==null || !p.isSurvived()))).count();

        long secondSurvived=passengers.stream().filter(p->(p.getpClass()==2 && (p.isSurvived()!=null && p.isSurvived()))).count();
        long secondNotSurvived=passengers.stream().filter(p->(p.getpClass()==2 && (p.isSurvived()==null || !p.isSurvived()))).count();

        long thirdSurvived=passengers.stream().filter(p->(p.getpClass()==3 && (p.isSurvived()!=null && p.isSurvived()))).count();
        long thirdNotSurvived=passengers.stream().filter(p->(p.getpClass()==3 && (p.isSurvived()==null || !p.isSurvived()))).count();

        DefaultCategoryDataset dataset=new DefaultCategoryDataset();
        dataset.addValue(firstSurvived,"Survived","First class");
        dataset.addValue(firstNotSurvived,"Not survived or unknown","First class");
        dataset.addValue(secondSurvived,"Survived", "Second class");
        dataset.addValue(secondNotSurvived,"Not survived or unknown","Second class");
        dataset.addValue(thirdSurvived,"Survived","Third class");
        dataset.addValue(thirdNotSurvived,"Not survived or unknown","Third class");

        JFreeChart chart=createBarChart(dataset, "Distribution of survivors by class", "Classes", "Number of passengers");
        return chart;
    }

    public JFreeChart createSexPlot(List<Passenger> passengers) {
        long maleAll=passengers.stream().filter(p->p.getSex().equals(Passenger.MALE)).count();
        long femaleAll=passengers.stream().filter(p->p.getSex().equals(Passenger.FEMALE)).count();

        long maleSurvived=passengers.stream().filter(p->(p.getSex().equals(Passenger.MALE) && (p.isSurvived()!=null && p.isSurvived()))).count();
        long femaleSurvived=passengers.stream().filter(p->(p.getSex().equals(Passenger.FEMALE) && (p.isSurvived()!=null && p.isSurvived()))).count();

        DefaultCategoryDataset dataset=new DefaultCategoryDataset();
        dataset.addValue(maleAll,"Male", "All");
        dataset.addValue(femaleAll,"Female", "All");
        dataset.addValue(maleSurvived,"Male", "Survived");
        dataset.addValue(femaleSurvived,"Female", "Survived");

        JFreeChart chart=createBarChart(dataset,"Distribution of survivors by sex", "","Number of passengers");
        return chart;
    }

    public JFreeChart createAgePlot(List<Passenger> passengers) {
        long ageSurvived01=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>0 && p.getAge()<=10) && (p.isSurvived()!=null && p.isSurvived()))).count();
        long ageSurvived12=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>10 && p.getAge()<=20) && (p.isSurvived()!=null && p.isSurvived()))).count();
        long ageSurvived23=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>20 && p.getAge()<=30) && (p.isSurvived()!=null && p.isSurvived()))).count();
        long ageSurvived34=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>30 && p.getAge()<=40) && (p.isSurvived()!=null && p.isSurvived()))).count();
        long ageSurvived45=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>40 && p.getAge()<=50) && (p.isSurvived()!=null && p.isSurvived()))).count();
        long ageSurvived56=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>50 && p.getAge()<=60) && (p.isSurvived()!=null && p.isSurvived()))).count();
        long ageSurvived67=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>60 && p.getAge()<=70) && (p.isSurvived()!=null && p.isSurvived()))).count();
        long ageSurvived78=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>70 && p.getAge()<=80) && (p.isSurvived()!=null && p.isSurvived()))).count();

        long ageNotSurvived01=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>0 && p.getAge()<=10) && (p.isSurvived()!=null && !p.isSurvived()))).count();
        long ageNotSurvived12=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>10 && p.getAge()<=20) && (p.isSurvived()!=null && !p.isSurvived()))).count();
        long ageNotSurvived23=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>20 && p.getAge()<=30) && (p.isSurvived()!=null && !p.isSurvived()))).count();
        long ageNotSurvived34=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>30 && p.getAge()<=40) && (p.isSurvived()!=null && !p.isSurvived()))).count();
        long ageNotSurvived45=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>40 && p.getAge()<=50) && (p.isSurvived()!=null && !p.isSurvived()))).count();
        long ageNotSurvived56=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>50 && p.getAge()<=60) && (p.isSurvived()!=null && !p.isSurvived()))).count();
        long ageNotSurvived67=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>60 && p.getAge()<=70) && (p.isSurvived()!=null && !p.isSurvived()))).count();
        long ageNotSurvived78=passengers.stream().filter(p->(p.getAge()!=Passenger.INT_NULL && (p.getAge()>70 && p.getAge()<=80) && (p.isSurvived()!=null && !p.isSurvived()))).count();

        DefaultCategoryDataset dataset=new DefaultCategoryDataset();
        dataset.addValue(ageSurvived01,"Survived", "(0;10]");
        dataset.addValue(ageNotSurvived01,"Not Survived", "(0;10]");

        dataset.addValue(ageSurvived12,"Survived", "(10;20]");
        dataset.addValue(ageNotSurvived12,"Not Survived", "(10;20]");

        dataset.addValue(ageSurvived23,"Survived", "(20;30]");
        dataset.addValue(ageNotSurvived23,"Not Survived", "(20;30]");

        dataset.addValue(ageSurvived34,"Survived", "(30;40]");
        dataset.addValue(ageNotSurvived34,"Not Survived", "(30;40]");

        dataset.addValue(ageSurvived45,"Survived", "(40;50]");
        dataset.addValue(ageNotSurvived45,"Not Survived", "(40;50]");

        dataset.addValue(ageSurvived56,"Survived", "(50;60]");
        dataset.addValue(ageNotSurvived56,"Not Survived", "(50;60]");

        dataset.addValue(ageSurvived67,"Survived", "(60;70]");
        dataset.addValue(ageNotSurvived67,"Not Survived", "(60;70]");

        dataset.addValue(ageSurvived78,"Survived", "(70;80]");
        dataset.addValue(ageNotSurvived78,"Not Survived", "(70;80]");


        JFreeChart chart=createBarChart(dataset,"Distribution of survivors by age", "","Number of passengers");
        return chart;
    }

    public JFreeChart createSibSpPlot(List<Passenger> passengers) {
        long sibSpSurvived01=passengers.stream().filter(p->(p.getSibSp()>0 && p.getSibSp()<=1) && (p.isSurvived()!=null && p.isSurvived())).count();
        long sibSpSurvived12=passengers.stream().filter(p->(p.getSibSp()>1 && p.getSibSp()<=2) && (p.isSurvived()!=null && p.isSurvived())).count();
        long sibSpSurvived23=passengers.stream().filter(p->(p.getSibSp()>2 && p.getSibSp()<=3) && (p.isSurvived()!=null && p.isSurvived())).count();
        long sibSpSurvived34=passengers.stream().filter(p->(p.getSibSp()>3 && p.getSibSp()<=4) && (p.isSurvived()!=null && p.isSurvived())).count();
        long sibSpSurvived45=passengers.stream().filter(p->(p.getSibSp()>4 && p.getSibSp()<=5) && (p.isSurvived()!=null && p.isSurvived())).count();
        long sibSpSurvived56=passengers.stream().filter(p->(p.getSibSp()>5 && p.getSibSp()<=6) && (p.isSurvived()!=null && p.isSurvived())).count();
        long sibSpSurvived67=passengers.stream().filter(p->(p.getSibSp()>6 && p.getSibSp()<=7) && (p.isSurvived()!=null && p.isSurvived())).count();
        long sibSpSurvived78=passengers.stream().filter(p->(p.getSibSp()>7 && p.getSibSp()<=8) && (p.isSurvived()!=null && p.isSurvived())).count();

        long sibSpNotSurvived01=passengers.stream().filter(p->(p.getSibSp()>0 && p.getSibSp()<=1) && (p.isSurvived()!=null && !p.isSurvived())).count();
        long sibSpNotSurvived12=passengers.stream().filter(p->(p.getSibSp()>1 && p.getSibSp()<=2) && (p.isSurvived()!=null && !p.isSurvived())).count();
        long sibSpNotSurvived23=passengers.stream().filter(p->(p.getSibSp()>2 && p.getSibSp()<=3) && (p.isSurvived()!=null && !p.isSurvived())).count();
        long sibSpNotSurvived34=passengers.stream().filter(p->(p.getSibSp()>3 && p.getSibSp()<=4) && (p.isSurvived()!=null && !p.isSurvived())).count();
        long sibSpNotSurvived45=passengers.stream().filter(p->(p.getSibSp()>4 && p.getSibSp()<=5) && (p.isSurvived()!=null && !p.isSurvived())).count();
        long sibSpNotSurvived56=passengers.stream().filter(p->(p.getSibSp()>5 && p.getSibSp()<=6) && (p.isSurvived()!=null && !p.isSurvived())).count();
        long sibSpNotSurvived67=passengers.stream().filter(p->(p.getSibSp()>6 && p.getSibSp()<=7) && (p.isSurvived()!=null && !p.isSurvived())).count();
        long sibSpNotSurvived78=passengers.stream().filter(p->(p.getSibSp()>7 && p.getSibSp()<=8) && (p.isSurvived()!=null && !p.isSurvived())).count();

        DefaultCategoryDataset dataset=new DefaultCategoryDataset();
        dataset.addValue(sibSpSurvived01,"Survived","(0,1]");
        dataset.addValue(sibSpNotSurvived01,"Not Survived","(0,1]");

        dataset.addValue(sibSpSurvived12,"Survived","(1,2]");
        dataset.addValue(sibSpNotSurvived12,"Not Survived","(1,2]");

        dataset.addValue(sibSpSurvived23,"Survived","(2,3]");
        dataset.addValue(sibSpNotSurvived23,"Not Survived","(2,3]");

        dataset.addValue(sibSpSurvived34,"Survived","(3,4]");
        dataset.addValue(sibSpNotSurvived34,"Not Survived","(3,4]");

        dataset.addValue(sibSpSurvived45,"Survived","(4,5]");
        dataset.addValue(sibSpNotSurvived45,"Not Survived","(4,5]");

        dataset.addValue(sibSpSurvived56,"Survived","(5,6]");
        dataset.addValue(sibSpNotSurvived56,"Not Survived","(5,6]");

        dataset.addValue(sibSpSurvived67,"Survived","(6,7]");
        dataset.addValue(sibSpNotSurvived67,"Not Survived","(6,7]");

        dataset.addValue(sibSpSurvived78,"Survived","(7,8]");
        dataset.addValue(sibSpNotSurvived78,"Not Survived","(7,8]");

        JFreeChart chart=createBarChart(dataset,"Distribution of survivors by SibSp", "","Number of passengers");
        return chart;
    }

    public JFreeChart createFareCategoryChart(List<Passenger> passengers) {
        long lowCatSurvived=passengers.stream().filter(p->(p.isSurvived()!=null && p.isSurvived()) && p.getFareCategory().equals(FareCategory.LOW)).count();
        long lowCatNotSurvived=passengers.stream().filter(p->(p.isSurvived()!=null && !p.isSurvived()) && p.getFareCategory().equals(FareCategory.LOW)).count();

        long midCatSurvived=passengers.stream().filter(p->(p.isSurvived()!=null && p.isSurvived()) && p.getFareCategory().equals(FareCategory.MIDDLE)).count();
        long midCatNotSurvived=passengers.stream().filter(p->(p.isSurvived()!=null && !p.isSurvived()) && p.getFareCategory().equals(FareCategory.MIDDLE)).count();

        long highMidCatSurvived=passengers.stream().filter(p->(p.isSurvived()!=null && p.isSurvived()) && p.getFareCategory().equals(FareCategory.HIGH_MIDDLE)).count();
        long highMidCatNotSurvived=passengers.stream().filter(p->(p.isSurvived()!=null && !p.isSurvived()) && p.getFareCategory().equals(FareCategory.HIGH_MIDDLE)).count();

        long highCatSurvived=passengers.stream().filter(p->(p.isSurvived()!=null && p.isSurvived()) && p.getFareCategory().equals(FareCategory.HIGH)).count();
        long highCatNotSurvived=passengers.stream().filter(p->(p.isSurvived()!=null && !p.isSurvived()) && p.getFareCategory().equals(FareCategory.HIGH)).count();

        DefaultCategoryDataset dataset=new DefaultCategoryDataset();
        dataset.addValue(lowCatSurvived,"Survived","Low Category");
        dataset.addValue(lowCatNotSurvived,"Not Survived","Low Category");

        dataset.addValue(midCatSurvived,"Survived","Middle Category");
        dataset.addValue(midCatNotSurvived,"Not Survived","Middle Category");

        dataset.addValue(highMidCatSurvived,"Survived","High Middle Category");
        dataset.addValue(highMidCatNotSurvived,"Not Survived","High Middle Category");

        dataset.addValue(highCatSurvived,"Survived","High Category");
        dataset.addValue(highCatNotSurvived,"Not Survived","High Category");

        JFreeChart chart=createBarChart(dataset,"Distribution of survivors by Fare Category", "","Number of passengers");
        return chart;
    }

    public JFreeChart createEmbarkedChart(List<Passenger> passengers) {
        long southamptonSurvived=passengers.stream().filter(p->(p.getEmbarked()!=null && p.getEmbarked().equals(Passenger.SOUTHAMPTON)) && (p.isSurvived()!=null && p.isSurvived())).count();
        long southamptonNotSurvived=passengers.stream().filter(p->(p.getEmbarked()!=null && p.getEmbarked().equals(Passenger.SOUTHAMPTON)) && (p.isSurvived()!=null && !p.isSurvived())).count();

        long cherbourgSurvived=passengers.stream().filter(p->(p.getEmbarked()!=null && p.getEmbarked().equals(Passenger.CHERBOURG)) && (p.isSurvived()!=null && p.isSurvived())).count();
        long cherbourgNotSurvived=passengers.stream().filter(p->(p.getEmbarked()!=null && p.getEmbarked().equals(Passenger.CHERBOURG)) && (p.isSurvived()!=null && !p.isSurvived())).count();

        long queenstownSurvived=passengers.stream().filter(p->(p.getEmbarked()!=null && p.getEmbarked().equals(Passenger.QUEENSTOWN)) && (p.isSurvived()!=null && p.isSurvived())).count();
        long queenstownNotSurvived=passengers.stream().filter(p->(p.getEmbarked()!=null && p.getEmbarked().equals(Passenger.QUEENSTOWN)) && (p.isSurvived()!=null && !p.isSurvived())).count();

        DefaultCategoryDataset dataset=new DefaultCategoryDataset();
        dataset.addValue(southamptonSurvived,"Survived","Southampton");
        dataset.addValue(southamptonNotSurvived," Not Survived","Southampton");

        dataset.addValue(cherbourgSurvived,"Survived","Cherbourg");
        dataset.addValue(cherbourgNotSurvived," Not Survived","Cherbourg");

        dataset.addValue(queenstownSurvived,"Survived","Queenstown");
        dataset.addValue(queenstownNotSurvived," Not Survived","Queenstown");

        JFreeChart chart=createBarChart(dataset,"Distribution of survivors by embarked", "Embarked", "Number of passengers");
        return chart;
    }

    private JFreeChart createBarChart(DefaultCategoryDataset dataset, String mainLabel, String xLabel, String yLabel) {

        final JFreeChart chart = ChartFactory.createBarChart3D(
                mainLabel,                      // chart title
                xLabel,                         // domain axis label
                yLabel,                         // range axis label
                dataset,                        // data
                PlotOrientation.VERTICAL,       // orientation
                true,                    // include legend
                true,                   // tooltips
                false                      // urls
        );
//        NumberAxis valueAxis = new NumberAxis(yLabel+",%");
//
//        CategoryPlot plot=chart.getCategoryPlot();
//        plot.setRangeAxis(1,valueAxis);
//        //plot.mapDatasetToRangeAxis(1,100);
//
//        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis(1);
//        DecimalFormat pctFormat = new DecimalFormat("#.0%");
//        rangeAxis.setNumberFormatOverride(pctFormat);
        return chart;
    }
}
