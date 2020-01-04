package jss;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Controller {
    public Label lblTimer;
    private Date date;
    public Button btnAdd;
    public Button btnStop;
    public Button btnStart;
    public RadioButton radioMass;
    public RadioButton radioHunting;
    public RadioButton radio15;
    public RadioButton radio30;
    public RadioButton radioIndividual;
    public TextField txtName;
    private ArrayList<Competitor> competitorArray = new ArrayList<>();
    @FXML TableView<Competitor> tblTable = new TableView<>();
    @FXML TableColumn clmName = new TableColumn();
    @FXML TableColumn clmStartTime = new TableColumn();
    @FXML TableColumn clmEndTime = new TableColumn();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private NumberFormat numberFormat = new DecimalFormat("#0.00");

    public void initialize(){
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        clmEndTime.setCellValueFactory(new PropertyValueFactory<>("finishTime"));


    }

    int additionalStartTime = 0;
    public void btnAdd(){
        competitorArray.add(new Competitor(txtName.getText(), String.valueOf(additionalStartTime), null));
        additionalStartTime += 30;

        tblTable.getItems().add(competitorArray.get(competitorArray.size()-1));
        System.out.println("Added dude");
    }

    public void btnStop(ActionEvent actionEvent) {
    }

    public void btnStart(ActionEvent actionEvent) {
    }
}