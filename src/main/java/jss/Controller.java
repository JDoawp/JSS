package jss;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private boolean started = false;
    private long startTime;
    private long elapsedTime;
    private int elapsedSeconds;
    XMLHandler xml = new XMLHandler();

    //TODO add saving and loading through XML,
    // Add functionality for different types of starts
    // Add actual start and stop functionality for competitors (start the timer with the first competitor and end it with the last one)
    // ,

    public void initialize(){   //Init the table columns, and set the timer.
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmOffset.setCellValueFactory(new PropertyValueFactory<>("timeOffset"));
        clmStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        clmEndTime.setCellValueFactory(new PropertyValueFactory<>("finishTime"));

        new AnimationTimer(){
            @Override
            public void handle(long l) {
                if(started) {
                    elapsedTime = System.currentTimeMillis() - startTime;
                    elapsedSeconds = (int) (elapsedTime / 1000);
                    int elapsedMinutes = elapsedSeconds / 60;
                    int elapsedHours = elapsedMinutes / 60;

                    lblTimer.setText(elapsedHours + ":" + timeFormat.format(elapsedMinutes) + ":" + secondFormat.format((Math.floor(elapsedTime/10.0) / 100)%60).replace(',', '.'));
                }
            }
        }.start();
    }

    int additionalStartTime = 0;
    public void btnAdd(){   //Adds a competitor from the text field.
        xml.add(new Competitor(txtName.getText(), additionalStartTime));
        if(radioHunting.isSelected()){
            if(radio15.isSelected()){
                additionalStartTime += 15;
                radio30.setDisable(true);
            }else{
                additionalStartTime += 30;
                radio15.setDisable(true);
            }
        }

        tblTable.getItems().add(xml.get(xml.size()-1));
        System.out.println("Added competitor: " +xml.get(xml.size()-1).toString());
    }

    public void btnStart() {
        timerToggle();
    }

    public void btnStop() {
        timerToggle();;
    }

    private void timerToggle(){ //Toggles between the timer being 'on' and not.
        if(!started){
            startTime = System.currentTimeMillis();
            System.out.println("startTime in millis: " +startTime);
            System.out.println("Timer On");
            started = true;

            btnAdd.setDisable(true);
            btnStart.setDisable(true);
            btnStop.setDisable(false);
        }else{
            started = false;
            System.out.println("stopTime in millis: " +System.currentTimeMillis());
            System.out.println("Milli time difference: " +(System.currentTimeMillis() - startTime) +" Second time difference: " +(System.currentTimeMillis() - startTime)/1000);
            System.out.println("Timer off");

            btnAdd.setDisable(false);
            btnStart.setDisable(false);
            btnStop.setDisable(true);
        }
    }
}