package jss;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {   //Inits some UI things along with variables.
    public Label lblTimer;
    public TableColumn clmTime;
    public Button btnAdd;
    public Button btnStop;
    public Button btnStart;
    public RadioButton radioMass;
    public RadioButton radioHunting;
    public RadioButton radio15;
    public RadioButton radio30;
    public RadioButton radioIndividual;
    public TextField txtName;
    public TextField txtOffset;
    public TableColumn clmOffset;
    @FXML TableView<Competitor> tblTable = new TableView<>();
    @FXML TableColumn clmName = new TableColumn();
    @FXML TableColumn clmStartTime = new TableColumn();
    @FXML TableColumn clmEndTime = new TableColumn();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private NumberFormat secondFormat = new DecimalFormat("00.00");
    private NumberFormat timeFormat = new DecimalFormat("00");
    private XMLHandler xml = new XMLHandler();

    private boolean started = false;
    private long startTime;
    private long elapsedTime;
    private int elapsedSeconds;
    private int elapsedMinutes;
    private int elapsedHours;
    int additionalStartTime = 0;
    private int skiingIndex = 0;

    //TODO add saving and loading through XML,
    // Hunting Start
    // ,

    public void initialize(){   //Init the table columns, and set the timer.
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmOffset.setCellValueFactory(new PropertyValueFactory<>("timeOffset"));
        clmStartTime.setCellValueFactory(new PropertyValueFactory<>("startClock"));
        clmEndTime.setCellValueFactory(new PropertyValueFactory<>("finishClock"));
        clmTime.setCellValueFactory(new PropertyValueFactory<>("elapsedTimeDisplay"));

        new AnimationTimer(){
            @Override
            public void handle(long l) {
                if(started) {
                    elapsedTime = System.currentTimeMillis() - startTime;
                    elapsedSeconds = (int) (elapsedTime / 1000);
                    elapsedMinutes = elapsedSeconds / 60;
                    elapsedHours = elapsedMinutes / 60;

                    lblTimer.setText(elapsedHours + ":" + timeFormat.format(elapsedMinutes) + ":" + secondFormat.format((Math.floor(elapsedTime/10.0) / 100) % 60).replace(',', '.'));

                    try{
                        if(skiingIndex <= xml.size() && (radioIndividual.isSelected() || radioHunting.isSelected()) && elapsedSeconds == xml.get(skiingIndex).getTimeOffset()){
                            delayedStart();
                        }
                    }catch (IndexOutOfBoundsException e){
                        //Do nothing the outOfBounds is from the last statement in the if case and it's just gonna be out of bounds after all skiiers have went.
                    }

                }
            }
        }.start();
    }

    public void btnAdd(){   //Adds a competitor from the text field.
        xml.add(new Competitor(txtName.getText(), additionalStartTime, true));
        btnStart.setDisable(false);

        if(radioIndividual.isSelected()){
            radioHunting.setDisable(true);
            radioMass.setDisable(true);
            if(radio15.isSelected()){
                additionalStartTime += 15;
                radio30.setDisable(true);
            }else{
                additionalStartTime += 30;
                radio15.setDisable(true);
            }
        }else if(radioHunting.isSelected()){
            radioIndividual.setDisable(true);
            radioMass.setDisable(true);
            radio15.setDisable(true);
            radio30.setDisable(true);
        }else{ //Radio Mass is selected
            radioHunting.setDisable(true);
            radioIndividual.setDisable(true);
            radio15.setDisable(true);
            radio30.setDisable(true);
        }

        tblTable.getItems().add(xml.get(xml.size()-1));
        System.out.println("Added competitor: " +xml.get(xml.size()-1).toString());
    }

    public void btnStart() {
        timerToggle("start");
    }

    public void btnStop() {
        Date finishClock = new Date();
        int selectedTableCell = tblTable.getSelectionModel().getFocusedIndex();

        xml.get(selectedTableCell).setSkiing(false);
        xml.get(selectedTableCell).setFinishClock(dateFormat.format(finishClock));
        xml.get(selectedTableCell).setElapsedTime(elapsedTime - xml.get(selectedTableCell).getTimeOffset()*1000);    //refers to the time it took to finish the race in milliseconds

        double tempSeconds = xml.get(selectedTableCell).getElapsedTime()/1000;
        int tempMinutes = (int)tempSeconds / 60;
        int tempHours = tempMinutes / 60;

        xml.get(selectedTableCell).setElapsedTimeDisplay(tempHours + ":" + timeFormat.format(tempMinutes) + ":" + secondFormat.format(tempSeconds).replace(',', '.'));
        tblTable.getItems().set(selectedTableCell, xml.get(selectedTableCell));

        tblTable.getSelectionModel().selectNext();
        boolean allStopped = false;

        for(int i = 0; i < xml.size(); i++){    //Checking all competitors to see if they've stopped skiing, if they have the allStopped will be true
            if(xml.get(i).isSkiing()){
                //Someone is still skiing
                allStopped = false;
                break;
            }else if(!xml.get(i).isSkiing()){
                allStopped = true;
            }
        }

        if(allStopped){
            System.out.println("Everyone has stopped skiing");
            timerToggle("stop");
            btnStop.setDisable(true);
        }
    }

    private void massStart(){
        Date startClock = new Date();
        for (int i = 0; i < xml.size(); i++) {
            xml.get(i).setStartClock(dateFormat.format(startClock));
            tblTable.getItems().set(i, xml.get(i));
        }
    }

    private void delayedStart(){
        System.out.println("Delayed start: " +skiingIndex);
        Date startClock = new Date();
        xml.get(skiingIndex).setStartClock((dateFormat.format(startClock)));
        tblTable.getItems().set(skiingIndex, xml.get(skiingIndex));
        skiingIndex++;
    }

    private void timerToggle(String toggle){ //Toggles between the timer being 'on' and not.
        switch(toggle){
            case "start":
                startTime = System.currentTimeMillis();
                System.out.println("startTime in millis: " +startTime);
                System.out.println("Timer On");
                started = true;

                btnAdd.setDisable(true);
                btnStart.setDisable(true);
                btnStop.setDisable(false);

                if(radioMass.isSelected()) {
                    massStart();
                }
            break;

            case "stop":
                started = false;
                System.out.println("stopTime in millis: " +System.currentTimeMillis());
                System.out.println("Milli time difference: " +(System.currentTimeMillis() - startTime) +" Second time difference: " +(System.currentTimeMillis() - startTime)/1000);
                System.out.println("Timer off");
                btnStop.setDisable(true);

                xml.sort();
                calculateOffset();
                xml.save();
            break;
        }
    }

    private void calculateOffset(){
        xml.get(0).setTimeOffset(0);
        for(int i = 1; i < xml.size(); i++){
            //Takes the elapsedTime (milliseconds) of the previous contestant and subtract it by the elapsedTime of the current contestant then divide that by 1000 to get a second value
            System.out.println(xml.get(i).toString() + " minus previous contestant: " +xml.get(i-1).toString());
            xml.get(i).setTimeOffset((int)(xml.get(i).getElapsedTime()-xml.get(i-1).getElapsedTime())/1000);
            System.out.println(xml.get(i).toString());
        }
    }

    public void btnLoad(){ //Load competitors
        xml.load();
        for(int i = 0; i < xml.size(); i++){
            tblTable.getItems().add(xml.get(i));
        }

        btnStart.setDisable(false);
        radioIndividual.setDisable(true);
        radioMass.setDisable(true);
        radio15.setDisable(true);
        radio30.setDisable(true);
        radioHunting.setSelected(true);

    }
}