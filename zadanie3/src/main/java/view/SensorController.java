package view;

import application.States;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import signals.Sensor;
import signals.Signal;

import javax.swing.plaf.nimbus.State;
import java.net.URL;
import java.util.ResourceBundle;

public class SensorController  implements Initializable {

    private boolean isWorking=false;
    private Sensor sensor = States.getInstance().getSensor();
    private double  time=0.0;

    @FXML
    Pane soundingChart;

    @FXML
    Pane reflectedChart;

    @FXML
    Pane corelatedChart;

    @FXML
    TextField current, calculatedPosition;

    @FXML
    Button button;


    public void createLineChart(Pane pane , Signal signal){

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("numer próbki");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);
        //defining a series
        XYChart.Series series = new XYChart.Series();
        //populating the series with data
        for(int i=0;i<signal.getPoints().size();i++){
            series.getData().add(new XYChart.Data(i, signal.getPoints().get(i).getY()));
        }

        lineChart.getData().add(series);
        lineChart.prefWidthProperty().bind(pane.widthProperty());
        lineChart.prefHeightProperty().bind(pane.heightProperty());
        pane.getChildren().clear();
        pane.getChildren().add(lineChart);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
       /* while (true){
            createLineChart(soundingChart,States.getInstance().getSensor().getSoundingSignal());
            createLineChart(reflectedChart,States.getInstance().getSensor().getReflectedSignal());
            createLineChart(corelatedChart,States.getInstance().getSensor().getCorelatedSignal());
        }*/
    }
}
