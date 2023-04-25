package lk.ijse.ikmanRental.controller;

import javafx.animation.FadeTransition;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.ikmanRental.dto.Vehicle;
import lk.ijse.ikmanRental.dto.tm.VehicleTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.ikmanRental.model.VehicleModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleFormController {

    @FXML
    public TableView tblVehicle;

    @FXML
    private AnchorPane contextVehiclePane;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbAvailability;

    @FXML
    private ComboBox<String> cmbCondition;

    @FXML
    private ComboBox<String> cmbEditAvailability;

    @FXML
    private ComboBox<String> cmbEditCondition;

    @FXML
    private ComboBox<String> cmbEditType;

    @FXML
    private ComboBox<String> cmbType;

    @FXML
    private ComboBox cmbVehicleNumber;

    @FXML
    private TableColumn colAvailability;

    @FXML
    private TableColumn  colCondition;

    @FXML
    private TableColumn colFuelToKm;

    @FXML
    private TableColumn colKMH;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colStatus;

    @FXML
    private TableColumn colType;

    @FXML
    private TableColumn colVehicleNumber;

    @FXML
    private TextField txtEditFueltoKm;

    @FXML
    private TextField txtEditKMH;

    @FXML
    private TextField txtEditName;

    @FXML
    private TextField txtEditStatus;

    @FXML
    private TextField txtFuelToKm;

    @FXML
    private TextField txtKmh;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtStatus;

    @FXML
    private TextField txtVehicleNumber;

    @FXML
    void initialize(){
        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), contextVehiclePane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        HomeFormController.stop=false;
        setType();
        setAvailability();
        setCondition();
        setCellValueFactory();
        getAllVehicles();
        setVehicleNumbers();
    }

    private void setVehicleNumbers() {
        ObservableList<String> vehiclenumbers =FXCollections.observableArrayList();

        try {
            List<String> numbers= VehicleModel.loadNumbers();
            vehiclenumbers.addAll(numbers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cmbVehicleNumber.setItems(vehiclenumbers);
    }

    private void setCellValueFactory() {
        colVehicleNumber.setCellValueFactory(new PropertyValueFactory<>("vehicleNumber"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colFuelToKm.setCellValueFactory(new PropertyValueFactory<>("fuelToKm"));
        colKMH.setCellValueFactory(new PropertyValueFactory<>("kmh"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCondition.setCellValueFactory(new PropertyValueFactory<>("condition"));
    }

    private void getAllVehicles() {
        ObservableList<VehicleTM> obList = FXCollections.observableArrayList();

        try {
            List<Vehicle> vehicles=VehicleModel.getAll();
            for (Vehicle vehicle : vehicles){
                obList.add(new VehicleTM(
                        vehicle.getVehicleNumber(),
                        vehicle.getName(),
                        vehicle.getType(),
                        vehicle.getFuelToKm(),
                        vehicle.getKmh(),
                        vehicle.getAvailability(),
                        vehicle.getStatus(),
                        vehicle.getCondition()
                ));
            }
            tblVehicle.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setType() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        List<String> type=new ArrayList<>();
        type.add("BUS");
        type.add("PRIME MOVER");
        type.add("LORRY");

        obList.addAll(type);

        cmbType.setItems(obList);
        cmbEditType.setItems(obList);
    }

    private void setAvailability() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        List<String> availability=new ArrayList<>();
        availability.add("AVAILABLE");
        availability.add("NOT AVAILABLE");
        availability.add("RUNNING");

        obList.addAll(availability);
        cmbAvailability.setItems(obList);
        cmbEditAvailability.setItems(obList);

    }

    private void setCondition() {
        ObservableList<String> obList=FXCollections.observableArrayList();
        List<String> conditions=new ArrayList<>();
        conditions.add("GOOD");
        conditions.add("LOW");
        conditions.add("MIDDLE");

        obList.addAll(conditions);
        cmbCondition.setItems(obList);
        cmbEditCondition.setItems(obList);
    }

    private void init(){
        txtName.clear();
        txtKmh.clear();
        txtStatus.clear();
        txtFuelToKm.clear();
        txtVehicleNumber.clear();
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String name = txtName.getText();
        String number = txtVehicleNumber.getText();
        String status = txtStatus.getText();
        int kmh = 0;
        try {
            kmh = Integer.parseInt(txtKmh.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR,"Please insert Values").show();
            return;
        }
        String condition = cmbCondition.getValue();
        String type = cmbType.getValue();
        String availability = cmbAvailability.getValue();
        double fuelToKm = Double.parseDouble(txtFuelToKm.getText());

        Vehicle vehicle = new Vehicle(number, name, type, fuelToKm, kmh, availability, status, condition);

        try {
            boolean isSave= VehicleModel.save(vehicle);
            if (isSave){
                new Alert(Alert.AlertType.CONFIRMATION,"Success !").show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Something happened !").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"SQL error").show();
            e.printStackTrace();
        }
        init();
        initialize();
    }

    @FXML
    void btnDeleteONAction(ActionEvent event) {
        String number = (String) cmbVehicleNumber.getValue();

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Delete ?", yes, no).showAndWait();

        if(result.orElse(no)==yes){
            try {
                boolean isDelete=VehicleModel.delete(number);
                if (isDelete){
                    new Alert(Alert.AlertType.CONFIRMATION,"deleted !").show();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Something happened !").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,"SQL Error !").show();
                e.printStackTrace();
            }
        }

        initialize();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String number = String.valueOf(cmbVehicleNumber.getValue());

        String name = txtEditName.getText();
        int kmh = 0;
        try {
            kmh = Integer.parseInt(txtEditKMH.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR,"Please Insert Values !").show();
            return;
        }
        String status = txtEditStatus.getText();
        Double fuelToKm = null;
        try {
            fuelToKm = Double.valueOf(txtEditFueltoKm.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR,"Fill values").show();
            return;
        }
        String type = cmbEditType.getValue();
        String availability = cmbEditAvailability.getValue();
        String condition = cmbEditCondition.getValue();

        try {
            boolean isUpdate=VehicleModel.update(new Vehicle(
                    number,
                    name,
                    type,
                    fuelToKm,
                    kmh,
                    availability,
                    status,
                    condition
            ));
            if (isUpdate){
                new Alert(Alert.AlertType.CONFIRMATION,"Success !").show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Something Happened !").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"SQL error !").show();
            e.printStackTrace();
        }
        getAllVehicles();
    }

    @FXML
    void cmbVehicleOnAction(ActionEvent event) throws SQLException {
        String number = (String) cmbVehicleNumber.getValue();

            Vehicle vehicle=VehicleModel.getAll(number);

        assert vehicle != null;
        try {
            txtEditFueltoKm.setText(String.valueOf(vehicle.getFuelToKm()));
            txtEditKMH.setText(String.valueOf(vehicle.getKmh()));
            txtEditName.setText(vehicle.getName());
            txtEditStatus.setText(vehicle.getStatus());
            cmbEditCondition.setValue(vehicle.getCondition());
            cmbEditAvailability.setValue(vehicle.getAvailability());
            cmbEditType.setValue(vehicle.getType());
        }catch (Exception io){}


    }

}