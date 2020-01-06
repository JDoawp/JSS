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
    public Button btnLoad;
    public RadioButton radioMass;
    public RadioButton radioHunting;
    public RadioButton radio15;
    public RadioButton radio30;
    public RadioButton radioIndividual;
    public TextField txtName;
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

                    //Check if the delayedStart() should fire.
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

    //Adds a competitor from the text field, gives them proper offsetTime if that type of race is selected.
    //Also disables the other radioboxes if someone is added as to not have someone be setup for a mass start and someone else be setup for individual start
    //TODO Make so you can retroactively switch what mode to race in and have the program fix with the timeOffset automatically
    public void btnAdd(){
        xml.add(new Competitor(txtName.getText(), additionalStartTime));
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

    //Stops the selected skiier after all skiiers have been stopped call timerToggle with "stop" and disable the stop button
    //TODO Check if the selected skiier has already been stopped (skiing==false) then make so the user cannot select that cell
    //Currently if the skiier selected has already stopped, stopping them again will update their finish time.
    public void btnStop() {
        Date finishClock = new Date();
        int selectedTableCell = tblTable.getSelectionModel().getFocusedIndex();

        xml.get(selectedTableCell).setSkiing(false);
        xml.get(selectedTableCell).setFinishClock(dateFormat.format(finishClock));
        xml.get(selectedTableCell).setElapsedTime(elapsedTime - xml.get(selectedTableCell).getTimeOffset()*1000); //take the elapsedTime minus their time offset (in milliseconds) to get the actual time they took.

        double tempSeconds = xml.get(selectedTableCell).getElapsedTime()/1000;
        int tempMinutes = (int)tempSeconds / 60;
        int tempHours = tempMinutes / 60;

        xml.get(selectedTableCell).setElapsedTimeDisplay(tempHours + ":" + timeFormat.format(tempMinutes) + ":" + secondFormat.format(tempSeconds).replace(',', '.'));  //Displays their finish time in a neat fashion
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

    //Go through all Competitors and set their start time then update the table
    private void massStart(){
        Date startClock = new Date();
        for (int i = 0; i < xml.size(); i++) {
            xml.get(i).setStartClock(dateFormat.format(startClock));
            tblTable.getItems().set(i, xml.get(i));
        }
    }

    //Take current skiingIndex, update the startClock and table, then increase index.
    private void delayedStart(){
        System.out.println("Delayed start: " +skiingIndex);
        Date startClock = new Date();
        xml.get(skiingIndex).setStartClock((dateFormat.format(startClock)));
        tblTable.getItems().set(skiingIndex, xml.get(skiingIndex));
        skiingIndex++;
    }

    //If start is given set the startTime, toggle the started boolean, disable all buttons but the stop button
    //If stop is given, set the stopTime, toggle the started boolean, disable the stop button, sort the competitor list by winners (lowest elapsedTime) calculate everyones new offset and save to an xml file.
    private void timerToggle(String toggle){
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

    //Start at 1 (2nd place in list) take the elapsedTime (milliseconds) of the previous contestant and subtract it by the elapsedTime of the current contestant then divide that by 1000 to get a second value
    private void calculateOffset(){
        xml.get(0).setTimeOffset(0);
        for(int i = 1; i < xml.size(); i++){
            System.out.println(xml.get(i).toString() + " minus previous contestant: " +xml.get(i-1).toString());
            xml.get(i).setTimeOffset((int)(xml.get(i).getElapsedTime()-xml.get(i-1).getElapsedTime())/1000);
            System.out.println(xml.get(i).toString());
        }
    }

    //Load competitors and put them in an arrayList, update the table, then disable the proper buttons and such.
    public void btnLoad(){ //Load competitors
        xml.load();
        for(int i = 0; i < xml.size(); i++){
            tblTable.getItems().add(xml.get(i));
        }

        btnLoad.setDisable(true);
        btnStart.setDisable(false);
        radioIndividual.setDisable(true);
        radioMass.setDisable(true);
        radio15.setDisable(true);
        radio30.setDisable(true);
        radioHunting.setDisable(false);
        radioHunting.setSelected(true);
    }
}