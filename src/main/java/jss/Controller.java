package jss;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Controller {
    private Date date;
    public Button btnStop;
    public Button btnStart;
    public Button btnAdd;
    public RadioButton radioMass;
    public RadioButton radioHunting;
    public RadioButton radio15;
    public RadioButton radio30;
    public RadioButton radioIndividual;
    public TextField txtName;
    @FXML TableView tblTimer = new TableView<>();
    @FXML TableColumn tblName = new TableColumn();
    @FXML TableColumn tblStartTime = new TableColumn();
    @FXML TableColumn tblEndTime = new TableColumn();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private NumberFormat numberFormat = new DecimalFormat("#0.00");

    private ArrayList<Timer> timerArray = new ArrayList<>();

    public void initialize(){
    }


    Competitor johnDoe = new Competitor("John Doe");


}