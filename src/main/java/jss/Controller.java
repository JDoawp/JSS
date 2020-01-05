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

    //TODO add saving and loading through XML,
    // Add functionality for different types of starts
    // Add actual start and stop functionality for competitors (start the timer with the first competitor and end it with the last one)
    // ,

    public void initialize(){   //Init the table columns, and set the timer.
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmOffset.setCellValueFactory(new PropertyValueFactory<>("timeOffset"));
        clmStartTime.setCellValueFactory(new PropertyValueFactory<>("startClock"));
        clmEndTime.setCellValueFactory(new PropertyValueFactory<>("finishClock"));
        clmTime.setCellValueFactory(new PropertyValueFactory<>("elapsedTime"));

        new AnimationTimer(){
            @Override
            public void handle(long l) {
                if(started) {
                    elapsedTime = System.currentTimeMillis() - startTime;
                    elapsedSeconds = (int) (elapsedTime / 1000);
                    elapsedMinutes = elapsedSeconds / 60;
                    elapsedHours = elapsedMinutes / 60;

                    lblTimer.setText(elapsedHours + ":" + timeFormat.format(elapsedMinutes) + ":" + secondFormat.format((Math.floor(elapsedTime/10.0) / 100)%60).replace(',', '.'));
                }
            }
        }.start();
    }

    public void btnAdd(){   //Adds a competitor from the text field.
        xml.add(new Competitor(txtName.getText(), additionalStartTime));
        btnStart.setDisable(false);

        if(radioHunting.isSelected()){
            radioIndividual.setDisable(true);
            radioMass.setDisable(true);
            if(radio15.isSelected()){
                additionalStartTime += 15;
                radio30.setDisable(true);
            }else{
                additionalStartTime += 30;
                radio15.setDisable(true);
            }
        }else if(radioIndividual.isSelected()){
            radioHunting.setDisable(true);
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
        xml.get(selectedTableCell).setLastTime(elapsedTime);    //this might be a bit confusing right here. 'LastTime' refers to the time it took to finish the race in milliseconds, ElapsedTime is a string which just displays that number in a better fashion.
        xml.get(selectedTableCell).setElapsedTime(elapsedHours + ":" + timeFormat.format(elapsedMinutes) + ":" + secondFormat.format((Math.floor(elapsedTime/10.0) / 100)%60).replace(',', '.'));
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
            xml.get(i).setSkiing(true);
            xml.get(i).setStartClock(dateFormat.format(startClock));
            tblTable.getItems().set(i, xml.get(i));
        }
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

                btnAdd.setDisable(false);
                btnStart.setDisable(false);
                btnStop.setDisable(true);
            break;
        }
    }

    public void btnLoad(){ //Load competitors
    }
}