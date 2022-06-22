package application;

import signals.Point;
import signals.Signal;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class Loader {


    public Signal load(String name) throws FileNotFoundException, IOException {


        Signal signal=new Signal ();
        BufferedReader fileReader = new BufferedReader(new FileReader(name));

        List<String> list = new ArrayList<String>();
        File file = new File(name);
        if(file.exists()){
            try {
                list = Files.readAllLines(file.toPath(), Charset.defaultCharset());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if(list.isEmpty())
                return new Signal();
        }
        //Loading parameters
        signal.setType(SignalType.valueOf(SignalType.class,list.get(0)));
        signal.setAmplitude(Double.parseDouble(list.get(1)));
        signal.setInitialTime(Double.parseDouble(list.get(2)));
        signal.setLastTime(Double.parseDouble(list.get(3)));
        signal.setBasicPeriod(Double.parseDouble(list.get(4)));
        signal.setSignalFrequency(Double.parseDouble(list.get(5)));
        signal.setFillFactor(Double.parseDouble(list.get(6)));
        signal.setEntityChange(Double.parseDouble(list.get(7)));
        signal.setFirstSampleNr(Integer.parseInt(list.get(8)));
        signal.setLastSampleNr(Integer.parseInt(list.get(9)));
        signal.setChangeForSample(Integer.parseInt(list.get(10)));
        signal.setFrequency(Double.parseDouble(list.get(11)));
        signal.setProbability(Double.parseDouble(list.get(12)));
        //Loading points
        for (int i=13;i<list.size();i++){

            String [] res = list.get(i).split(" ");
            signal.getPoints().add(new Point(Double.valueOf(res[0]),Double.valueOf(res[1])));
        }

        return signal;
    }
}
