package view;

import application.*;
import complexSignals.ComplexOperations;
import complexSignals.ComplexPoint;
import complexSignals.ComplexSignal;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import signals.*;


import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.UnaryOperator;

public class Controller implements Initializable {

    private Loader signalLoader = new Loader();
    private SignalsCalculator calculator;
    private Signal signal;
    private ComplexSignal complexSignal;
    private SignalType type;
    private FilterType filterType;
    private SensorType sensorType;
    private boolean isSignalWithSignaLFrequencyParameter;
    private double time=0.0;
    private boolean isWorking=false;
    private boolean isCreated=false;
    private Sensor sensor;
    String pathToFile= new String();
    String pathfirstSignal, pathsecondSignal,pathComplexSignal;

    List<Label> labelList= Arrays.asList(
            new Label("Amplituda (A):"),
            new Label("Czas początkowy (t1):"),
            new Label("Czas trwania (d):"),
            new Label("Okres podstawowy (T):"),
            new Label("Częstotliwość próbkowania (f):"),
            new Label("Współczynnik wypełnienia (kw):"),
            new Label("Skok jednostkowy (ts):"),
            new Label("Numer pierwszej próbki (n1):"),
            new Label("Numer ostatniej próbki (n2)"),
            new Label("Skok dla próbki nr (ns):"),
            new Label("Prawdopodobieństwo (p):"),
            new Label("Częstotliwość sygnału (sf):"));


    List <TextField> textFieldsList=new ArrayList<>();

    @FXML
    ChoiceBox menu,sensorTypeBox, filterTypeBox;
    @FXML
    VBox labels;
    @FXML
    VBox textBoxes;
    @FXML
    Button generateButton;
    @FXML TextField loadedComplexFilePath,loadedFilePathToTransform, PathToLoadFile, loadedFirstFilePath, loadedFirstFilePathFilter,loadedSecondFilePathFilter, loadedSecondFilePath, avg, avg2, avgPow, effVal, variance,targetSpeed,
            signalSpeed,sondagePeriod,sondagePeriod1,samplingFrequency,raportPeriod,bufferLength,cutFrequency, numberOfFactor,current,calculatedPosition;

    public String loadFile(TextField tf, boolean ifLoaded) throws IOException {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll();
        File selectedFile = fc.showOpenDialog(null);
        String path = selectedFile.getAbsolutePath();
        DecimalFormat f = new DecimalFormat("0.###E0");
        DecimalFormat df=new java.text.DecimalFormat(); //tworzymy obiekt DecimalFormat
        df.setMaximumFractionDigits(3); //dla df ustawiamy największą ilość miejsc po przecinku
        df.setMinimumFractionDigits(3);
        if (selectedFile != null) {
            tf.setText(path);
            if (ifLoaded) {

                signal=signalLoader.load(path);
                menu.setValue(signal.getType());
                fillTextFields();
                signal.average();
                signal.absoluteMean();
                signal.averagePower();
                signal.effectiveValue();
                signal.variance();


                avg.setText(String.valueOf(f.format(signal.getAverage())));
                avg2.setText(String.valueOf(df.format(signal.getAbsoluteMean())));
                avgPow.setText(String.valueOf(df.format(signal.getAveragePower())));
                effVal.setText(String.valueOf(df.format(signal.getEffectiveValue())));
                variance.setText(String.valueOf(df.format(signal.getVariance())));
                Stage stage = new Stage();
                Parent root = null;
                FXMLLoader loader = new FXMLLoader();
                States.getInstance().setSignal(signal);
                root = FXMLLoader.load(getClass().getClassLoader().getResource("chart.fxml"));
                Scene scene = new Scene(root);
                stage.setTitle("Loaded signal charts");
                stage.setScene(scene);
                stage.show();
            }
            } else {
                System.out.println("file is not valid");
            }

        return path;
    }

    public void readFile(ActionEvent actionEvent) throws IOException {
       pathToFile=loadFile(PathToLoadFile, true);
    }

    public void loadFirstSignal(ActionEvent actionEvent) throws IOException {
        pathfirstSignal =loadFile(loadedFirstFilePath,false);
    }

    public void loadSecondSignal(ActionEvent actionEvent) throws IOException {
        pathsecondSignal =loadFile(loadedSecondFilePath,false);
    }

    public void loadFirstSignalFilter(ActionEvent actionEvent) throws IOException {
        pathfirstSignal =loadFile(loadedFirstFilePathFilter,false);
    }

    public void loadSecondSignalFilter() throws IOException {
        pathsecondSignal =loadFile(loadedSecondFilePathFilter,false);
    }

    public void saveFile() throws IOException {


        Date date = new Date();
        String saveNameFile = date.toString();
        saveNameFile = saveNameFile.replaceAll("[\\s|:]+","");
        PrintWriter saver = new PrintWriter(saveNameFile);
        saver.println(signal.getType().name());
        saver.println(signal.getAmplitude());
        saver.println(signal.getInitialTime());
        saver.println(signal.getLastTime());
        saver.println(signal.getBasicPeriod());
        saver.println(signal.getSignalFrequency());
        saver.println(signal.getFillFactor());
        saver.println(signal.getEntityChange());
        saver.println(signal.getFirstSampleNr());
        saver.println(signal.getLastSampleNr());
        saver.println(signal.getChangeForSample());
        saver.println(signal.getFrequency());
        saver.println(signal.getProbability());

        for (Point i : signal.getPoints()) {
            saver.println(i.getX() + " "+ i.getY());
        }
        saver.close();

    }

    public void fillTextFields(){

        switch (type){

            case noiseUniDis:
                textFieldsList.get(0).setText(String.valueOf(signal.getAmplitude()));
                textFieldsList.get(1).setText (String.valueOf(signal.getInitialTime()));
                textFieldsList.get(2).setText (String.valueOf(signal.getLastTime()));
                textFieldsList.get(3).setText (String.valueOf(signal.getFrequency()));
                break;

            case noiseGauss:
                textFieldsList.get(0).setText(String.valueOf(signal.getInitialTime()));
                textFieldsList.get(1).setText(String.valueOf(signal.getLastTime()));
                textFieldsList.get(2).setText(String.valueOf(signal.getFrequency()));
                break;

            case sin:
            case sinOneHalf:
            case sinTwoHalf:
                textFieldsList.get(0).setText(String.valueOf(signal.getAmplitude()));
                textFieldsList.get(1).setText(String.valueOf(signal.getInitialTime()));
                textFieldsList.get(2).setText(String.valueOf(signal.getLastTime()));
                textFieldsList.get(3).setText(String.valueOf(signal.getBasicPeriod()));
                textFieldsList.get(4).setText(String.valueOf(signal.getSignalFrequency()));
                textFieldsList.get(5).setText(String.valueOf(signal.getFrequency()));
                break;

            case rec:
            case recSym:
            case tri:
                textFieldsList.get(0).setText(String.valueOf(signal.getAmplitude()));
                textFieldsList.get(1).setText(String.valueOf(signal.getInitialTime()));
                textFieldsList.get(2).setText(String.valueOf(signal.getLastTime()));
                textFieldsList.get(3).setText(String.valueOf(signal.getBasicPeriod()));
                textFieldsList.get(4).setText(String.valueOf(signal.getSignalFrequency()));
                textFieldsList.get(5).setText(String.valueOf(signal.getFrequency()));
                textFieldsList.get(6).setText(String.valueOf(signal.getFillFactor()));
                break;
            case entityChange:
                textFieldsList.get(0).setText(String.valueOf(signal.getAmplitude()));
                textFieldsList.get(1).setText(String.valueOf(signal.getInitialTime()));
                textFieldsList.get(2).setText(String.valueOf(signal.getLastTime()));
                textFieldsList.get(3).setText(String.valueOf(signal.getFrequency()));
                textFieldsList.get(4).setText(String.valueOf(signal.getEntityChange()));
                break;
            case entityImpulse:

                textFieldsList.get(0).setText(String.valueOf(signal.getFirstSampleNr()));
                textFieldsList.get(1).setText(String.valueOf(signal.getLastSampleNr()));
                textFieldsList.get(2).setText(String.valueOf(signal.getChangeForSample()));
                break;
            case noiseImpulse:
                textFieldsList.get(0).setText(String.valueOf(signal.getFirstSampleNr()));
                textFieldsList.get(1).setText(String.valueOf(signal.getLastSampleNr()));
                textFieldsList.get(2).setText(String.valueOf(signal.getProbability()));
                break;
            case s2:
                textFieldsList.get(0).setText(String.valueOf(signal.getAmplitude()));
                textFieldsList.get(1).setText(String.valueOf(signal.getInitialTime()));
                textFieldsList.get(2).setText(String.valueOf(signal.getLastTime()));
                textFieldsList.get(3).setText(String.valueOf(signal.getBasicPeriod()));
                textFieldsList.get(4).setText(String.valueOf(signal.getSignalFrequency()));
                break;
        }

    }

    public void choose(){

        labels.spacingProperty().setValue(19);
        labels.setPadding(new Insets(0,0,0,20));
        labels.getChildren().clear();
        textBoxes.spacingProperty().setValue(10);
        textBoxes.setPadding(new Insets(0,10,0,10));
        textBoxes.getChildren().clear();
        textFieldsList.clear();
        isSignalWithSignaLFrequencyParameter=false;
        switch (type){

            case noiseUniDis:
                labels.getChildren().addAll(Arrays.asList(
                        labelList.get(0),
                        labelList.get(1),
                        labelList.get(2),
                        labelList.get(4)));
                break;

            case noiseGauss:
                labels.getChildren().addAll(Arrays.asList(
                        labelList.get(1),
                        labelList.get(2),
                        labelList.get(4)));
                break;

            case sin:
            case sinOneHalf:
            case sinTwoHalf:
                labels.getChildren().addAll(Arrays.asList(
                        labelList.get(0),
                        labelList.get(1),
                        labelList.get(2),
                        labelList.get(3),
                        labelList.get(11),
                        labelList.get(4)));
                isSignalWithSignaLFrequencyParameter=true;
                break;

            case rec:
            case recSym:
            case tri:
                labels.getChildren().addAll(Arrays.asList(
                        labelList.get(0),
                        labelList.get(1),
                        labelList.get(2),
                        labelList.get(3),
                        labelList.get(11),
                        labelList.get(4),
                        labelList.get(5)));
                isSignalWithSignaLFrequencyParameter=true;
                break;
            case entityChange:
                labels.getChildren().addAll(Arrays.asList(
                        labelList.get(0),
                        labelList.get(1),
                        labelList.get(2),
                        labelList.get(4),
                        labelList.get(6)));

                break;
            case entityImpulse:
                labels.getChildren().addAll(Arrays.asList(
                        labelList.get(7),
                        labelList.get(8),
                        labelList.get(9)));
                break;
            case noiseImpulse:
                labels.getChildren().addAll(Arrays.asList(
                        labelList.get(7),
                        labelList.get(8),
                        labelList.get(10)));
                break;
            case s2:
                labels.getChildren().addAll(Arrays.asList(
                        labelList.get(0),
                        labelList.get(1),
                        labelList.get(2),
                        labelList.get(3),
                        labelList.get(11)));
                isSignalWithSignaLFrequencyParameter=true;
        }

        for(int i =0;i<labels.getChildren().size();i++){
            TextField tmp=new TextField();
            tmp.setTextFormatter(new TextFormatter<>(filter));
            textFieldsList.add(tmp);
            textBoxes.getChildren().add(tmp);
        }

        if(isSignalWithSignaLFrequencyParameter) {
            textFieldsList.get(3).addEventHandler(EventType.ROOT, event -> {
                double helper;
                if (event.getEventType().getName().substring(0, 3).equals("KEY")) {
                    if(!textFieldsList.get(3).getText().isEmpty()){
                        textFieldsList.get(4).setDisable(true);
                        helper=1/Double.parseDouble(textFieldsList.get(3).getText());
                        textFieldsList.get(4).setText(Double.toString(helper));
                    }else {
                        textFieldsList.get(4).setDisable(false);
                        textFieldsList.get(4).setText("");

                    }
                }
            });

            textFieldsList.get(4).addEventHandler(EventType.ROOT, event -> {
                double helper;
                if (event.getEventType().getName().substring(0, 3).equals("KEY")) {
                    if(!textFieldsList.get(4).getText().isEmpty()){
                        textFieldsList.get(3).setDisable(true);
                        helper=1/Double.parseDouble(textFieldsList.get(4).getText());
                        textFieldsList.get(3).setText(Double.toString(helper));
                    }else {
                        textFieldsList.get(3).setDisable(false);
                        textFieldsList.get(3).setText("");
                    }
                }
            });
        }
    }

    public void setParameters(){

        signal=new Signal();
        signal.setType(type);
        switch (type){

            case noiseUniDis:
                signal.setAmplitude(Double.parseDouble(textFieldsList.get(0).getText()));
                signal.setInitialTime(Double.parseDouble(textFieldsList.get(1).getText()));
                signal.setLastTime(Double.parseDouble(textFieldsList.get(2).getText()));
                signal.setFrequency(Double.parseDouble(textFieldsList.get(3).getText()));
                break;

            case noiseGauss:
                signal.setInitialTime(Double.parseDouble(textFieldsList.get(0).getText()));
                signal.setLastTime(Double.parseDouble(textFieldsList.get(1).getText()));
                signal.setFrequency(Double.parseDouble(textFieldsList.get(2).getText()));
                break;

            case sin:
            case sinOneHalf:
            case sinTwoHalf:

                signal.setAmplitude(Double.parseDouble(textFieldsList.get(0).getText()));
                signal.setInitialTime(Double.parseDouble(textFieldsList.get(1).getText()));
                signal.setLastTime(Double.parseDouble(textFieldsList.get(2).getText()));
                signal.setBasicPeriod(Double.parseDouble(textFieldsList.get(3).getText()));
                signal.setSignalFrequency(Double.parseDouble(textFieldsList.get(4).getText()));
                signal.setFrequency(Double.parseDouble(textFieldsList.get(5).getText()));
                break;

            case rec:
            case recSym:
            case tri:
                signal.setAmplitude(Double.parseDouble(textFieldsList.get(0).getText()));
                signal.setInitialTime(Double.parseDouble(textFieldsList.get(1).getText()));
                signal.setLastTime(Double.parseDouble(textFieldsList.get(2).getText()));
                signal.setBasicPeriod(Double.parseDouble(textFieldsList.get(3).getText()));
                signal.setSignalFrequency(Double.parseDouble(textFieldsList.get(4).getText()));
                signal.setFrequency(Double.parseDouble(textFieldsList.get(5).getText()));
                signal.setFillFactor(Double.parseDouble(textFieldsList.get(6).getText()));
                break;
            case entityChange:
                signal.setAmplitude(Double.parseDouble(textFieldsList.get(0).getText()));
                signal.setInitialTime(Double.parseDouble(textFieldsList.get(1).getText()));
                signal.setLastTime(Double.parseDouble(textFieldsList.get(2).getText()));
                signal.setFrequency(Double.parseDouble(textFieldsList.get(3).getText()));
                signal.setEntityChange(Double.parseDouble(textFieldsList.get(4).getText()));
                break;
            case entityImpulse:

                signal.setFirstSampleNr(Integer.parseInt(textFieldsList.get(0).getText()));
                signal.setLastSampleNr(Integer.parseInt(textFieldsList.get(1).getText()));
                signal.setChangeForSample(Integer.parseInt(textFieldsList.get(2).getText()));
                break;
            case noiseImpulse:
                signal.setFirstSampleNr(Integer.parseInt(textFieldsList.get(0).getText()));
                signal.setLastSampleNr(Integer.parseInt(textFieldsList.get(1).getText()));
                signal.setProbability(Double.parseDouble(textFieldsList.get(2).getText()));
                break;
            case s2:
                signal.setAmplitude(Double.parseDouble(textFieldsList.get(0).getText()));
                signal.setInitialTime(Double.parseDouble(textFieldsList.get(1).getText()));
                signal.setLastTime(Double.parseDouble(textFieldsList.get(2).getText()));
                signal.setBasicPeriod(Double.parseDouble(textFieldsList.get(3).getText()));
                signal.setSignalFrequency(Double.parseDouble(textFieldsList.get(4).getText()));
                break;
        }

        signal.generateSignal();
    }

    @FXML
    public void handleGenerateButton(javafx.event.ActionEvent actionEvent) throws IOException {

        Stage stage = new Stage();
        Parent root = null;
        FXMLLoader loader=new FXMLLoader();
        setParameters();
        States.getInstance().setSignal(signal);
        root = FXMLLoader.load(getClass().getClassLoader().getResource("chart.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Signal charts");
        stage.setScene(scene);
        stage.show();

        DecimalFormat f = new DecimalFormat("0.###E0");
        DecimalFormat df=new java.text.DecimalFormat(); //tworzymy obiekt DecimalFormat
        df.setMaximumFractionDigits(3); //dla df ustawiamy największą ilość miejsc po przecinku
        df.setMinimumFractionDigits(3);

        signal.average();
        signal.absoluteMean();
        signal.averagePower();
        signal.effectiveValue();
        signal.variance();


        avg.setText(String.valueOf(f.format(signal.getAverage())));
        avg2.setText(String.valueOf(df.format(signal.getAbsoluteMean())));
        avgPow.setText(String.valueOf(df.format(signal.getAveragePower())));
        effVal.setText(String.valueOf(df.format(signal.getEffectiveValue())));
        variance.setText(String.valueOf(df.format(signal.getVariance())));
    }

    @FXML
    public void handleAddSignalsButton(ActionEvent actionEvent) throws IOException {

        Signal firstSignal=signalLoader.load(pathfirstSignal);
        Signal secondSignal=signalLoader.load(pathsecondSignal);
        calculator=new SignalsCalculator(firstSignal,secondSignal);
        calculator.addSignals();

        Stage stage = new Stage();
        Parent root = null;
        FXMLLoader loader=new FXMLLoader();
        States.getInstance().setSignal(calculator.getCalculatedSignal());
        root = FXMLLoader.load(getClass().getClassLoader().getResource("chart.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Final chart");
        stage.setScene(scene);
        stage.show();
    }

    public void handleSubtractSignalsButton(ActionEvent actionEvent) throws IOException {
        calculator=new SignalsCalculator(signalLoader.load(pathfirstSignal),signalLoader.load(pathsecondSignal));
        calculator.subtractSignals();

        Stage stage = new Stage();
        Parent root = null;
        FXMLLoader loader=new FXMLLoader();
        States.getInstance().setSignal(calculator.getCalculatedSignal());
        root = FXMLLoader.load(getClass().getClassLoader().getResource("chart.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Final chart");
        stage.setScene(scene);
        stage.show();
    }

    public void handleMultiplySignalsButton(ActionEvent actionEvent) throws IOException {
        calculator=new SignalsCalculator(signalLoader.load(pathfirstSignal),signalLoader.load(pathsecondSignal));
        calculator.multiplySignals();

        Stage stage = new Stage();
        Parent root = null;
        FXMLLoader loader=new FXMLLoader();
        States.getInstance().setSignal(calculator.getCalculatedSignal());
        root = FXMLLoader.load(getClass().getClassLoader().getResource("chart.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Final chart");
        stage.setScene(scene);
        stage.show();
    }

    public void handleDivideSignalsButton(ActionEvent actionEvent) throws IOException {
        calculator=new SignalsCalculator(signalLoader.load(pathfirstSignal),signalLoader.load(pathsecondSignal));
        calculator.divideSignals();

        Stage stage = new Stage();
        Parent root = null;
        FXMLLoader loader=new FXMLLoader();
        States.getInstance().setSignal(calculator.getCalculatedSignal());
        root = FXMLLoader.load(getClass().getClassLoader().getResource("chart.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Final chart");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void sensorController() throws IOException {
        if(!isCreated) {
            Stage stage = new Stage();
            Parent root = null;
            FXMLLoader loader = new FXMLLoader();
            Signal first = new Signal();
            Signal second = new Signal();
            first.setType(SignalType.sin);
            second.setType(SignalType.sin);
            first.setFrequency(Double.parseDouble(samplingFrequency.getText()));
            second.setFrequency(Double.parseDouble(samplingFrequency.getText()));
            first.setInitialTime(0);
            second.setInitialTime(0);
            double lastTime = Double.parseDouble(bufferLength.getText()) / Double.parseDouble(samplingFrequency.getText());
            first.setLastTime(lastTime);
            second.setLastTime(lastTime);
            first.setAmplitude(2);
            second.setAmplitude(2);
            first.setBasicPeriod(Double.parseDouble(sondagePeriod.getText()));
            second.setBasicPeriod(Double.parseDouble(sondagePeriod1.getText()));
            first.generateSignal();
            second.generateSignal();
            double frequency = Double.parseDouble(samplingFrequency.getText());//1/Math.abs(first.getPoints().get(first.getPoints().size()-1).getX()-second.getPoints().get(0).getX())/second.getPoints().size();

             sensor = new Sensor(Integer.valueOf(bufferLength.getText()),
                    frequency,
                    Double.valueOf(raportPeriod.getText()),
                    Double.valueOf(signalSpeed.getText()),
                    this.sensorType,
                    new Target(Double.valueOf(targetSpeed.getText())), first, second);


            States.getInstance().setSensor(sensor);
            isCreated=true;
        }


        if(isWorking){
            isWorking=false;
            //button.setText("stop");
        } else {
            isWorking=true;
           // button.setText("start");
        }


        Thread t = new Thread(new MyRunnable());
        t.start();

    }

    public class MyRunnable implements Runnable {


        public void run() {
            while (isWorking){

                try {
                    Thread.sleep((long) (1000*sensor.getRaportPeriod()));
                    time++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sensor.getTarget().setParameters(time*sensor.getRaportPeriod());
                sensor.distanceSensor();
                calculatedPosition.setText(String.valueOf(sensor.getDistance()));
                current.setText(String.valueOf(sensor.getTarget().getObjectPosition()));

            }
        }
    }

    public void filterController() throws IOException {

        Stage stage = new Stage();
        Parent root = null;
        FXMLLoader loader=new FXMLLoader();
        Signal firstSignal=signalLoader.load(pathfirstSignal);
        Signal secondSignal=signalLoader.load(pathsecondSignal);
        calculator=new SignalsCalculator(firstSignal,secondSignal);
        calculator.addSignals();
        Signal calculatedSignal=calculator.getCalculatedSignal();

        double frequency=1/Math.abs(calculatedSignal.getPoints().get(calculatedSignal.getPoints().size()-1).getX()-calculatedSignal.getPoints().get(0).getX())/calculatedSignal.getPoints().size();
        double K=frequency/Double.parseDouble(cutFrequency.getText());
        States.getInstance().setSignal(calculatedSignal);
        States.getInstance().setSecondSignal(Filter.filtrateSignal(filterType,calculatedSignal, Integer.parseInt(numberOfFactor.getText()),K));
        root = FXMLLoader.load(getClass().getClassLoader().getResource("filter.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Signal chart");
        stage.setScene(scene);
        stage.show();
    }

    UnaryOperator<TextFormatter.Change> filter = new UnaryOperator<TextFormatter.Change>() {

        @Override
        public TextFormatter.Change apply(TextFormatter.Change t) {

            if (t.isReplaced())
                if(t.getText().matches("[^0-9]"))
                    t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));


            if (t.isAdded()) {
                if (t.getControlText().contains(".")) {
                    if (t.getText().matches("[^0-9]")) {
                        t.setText("");
                    }
                } else if (t.getText().matches("[^0-9.]")) {
                    t.setText("");
                }
            }

            return t;
        }
    };


    ////////////////////////COMPLEX//////////////////////////////////////////
    public void saveComplexSignalToFile() throws IOException {
        Date date = new Date();
        String saveNameFile = date.toString()+"[complex]";
        saveNameFile = saveNameFile.replaceAll("[\\s|:]+","");
        PrintWriter saver = new PrintWriter(saveNameFile);


        for (ComplexPoint i : complexSignal.getPoints()) {
            saver.println(i.getX()+ " "+ i.getY()+ " "+i.getYI());
        }
        saver.close();
    }

    public String loadComplexSignalFile(TextField tf, boolean ifLoaded) throws IOException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll();
        File selectedFile = fc.showOpenDialog(null);
        String path = selectedFile.getAbsolutePath();
        DecimalFormat f = new DecimalFormat("0.###E0");
        DecimalFormat df=new java.text.DecimalFormat(); //tworzymy obiekt DecimalFormat
        df.setMaximumFractionDigits(3); //dla df ustawiamy największą ilość miejsc po przecinku
        df.setMinimumFractionDigits(3);
        if (selectedFile != null) {
            tf.setText(path);
            if (ifLoaded) {

                complexSignal= signalLoader.loadComplexSignal(path);

            }
        } else {
            System.out.println("file is not valid");
        }

        return path;
    }

    public void loadSignalToTransform() throws IOException {
        pathfirstSignal = loadFile(loadedFilePathToTransform,true);
    }

    public void loadComplexSignal() throws IOException {
        pathComplexSignal =loadComplexSignalFile(loadedComplexFilePath,true);
    }

    public void transform() throws IOException {

        complexSignal= ComplexOperations.computeDft(ComplexOperations.transformToComplexSignal(signal),signal.getSignalFrequency());
        Stage stage = new Stage();
        Parent root = null;
        FXMLLoader loader=new FXMLLoader();
        States.getInstance().setComplexSignal(complexSignal);
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fourier.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Transformation charts");
        stage.setScene(scene);
        stage.show();
    }

    public void showTransformed() throws IOException {

        Stage stage = new Stage();
        Parent root = null;
        FXMLLoader loader=new FXMLLoader();
        States.getInstance().setComplexSignal(complexSignal);
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fourier.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Transformation charts");
        stage.setScene(scene);
        stage.show();
    }
    ////////////////////////////////////////////////////////////////////////
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        menu.setItems( FXCollections.observableArrayList( SignalType.values()));
        menu.setValue(SignalType.noiseUniDis);
        this.type=(SignalType) menu.getValue();
        choose();
        menu.getSelectionModel().selectedItemProperty().addListener((obs,oldValue,newValue)->{
            this.type=(SignalType) menu.getValue();
            choose();
        });

        sensorTypeBox.setItems( FXCollections.observableArrayList( SensorType.values()));
        sensorTypeBox.setValue(SensorType.directCorrelation);
        sensorType=(SensorType)sensorTypeBox.getValue();
        menu.getSelectionModel().selectedItemProperty().addListener((obs,oldValue,newValue)->{
            sensorType=(SensorType)sensorTypeBox.getValue();
        });

        filterTypeBox.setItems( FXCollections.observableArrayList( FilterType.values()));
        filterTypeBox.setValue(FilterType.bottomHanning);
        this.filterType=(FilterType) filterTypeBox.getValue();
        menu.getSelectionModel().selectedItemProperty().addListener((obs,oldValue,newValue)->{
            this.filterType=(FilterType) filterTypeBox.getValue();
        });

    }

}