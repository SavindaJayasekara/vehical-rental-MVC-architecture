package lk.ijse.ikmanRental.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.ikmanRental.dto.Driver;
import lk.ijse.ikmanRental.dto.DriverPayment;
import lk.ijse.ikmanRental.dto.tm.DriverPaymentTM;
import lk.ijse.ikmanRental.dto.tm.DriverTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.ikmanRental.model.DriverModel;
import lk.ijse.ikmanRental.model.DriverPaymentModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DriverFormController {

    @FXML
    private AnchorPane contextDriverPane;
    @FXML
    private Label lblDelete;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colNic;
    @FXML
    private TableColumn colGender;
    @FXML
    private TableColumn colGmail;
    @FXML
    private TableColumn colStatus;
    @FXML
    private TableView tblDriver;
    @FXML
    private TableView tblPayment;
    @FXML
    private TableColumn colPaymentID;
    @FXML
    private TableColumn colStatusP;
    @FXML
    private TableColumn colTotalP;
    @FXML
    private TableColumn colDriverNicP;
    @FXML
    private ComboBox cmbEditGender;
    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private ComboBox<String> cmbNic;

    @FXML
    private ComboBox<String> cmdGender;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEditContact;

    @FXML
    private TextField txtEditGmail;

    @FXML
    private TextField txtEditStatus;

    @FXML
    private TextField txtGmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtStatus;

    @FXML
    private TextField txteditName;

    @FXML
    void initialize(){
        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), contextDriverPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        HomeFormController.stop=false;
        setGender();
        getAllNic();
        setCellValueFactory();
        getAll();
        fillPayment();
    }

    private void fillPayment() {
        ObservableList<DriverPaymentTM> paymentTMS=FXCollections.observableArrayList();
        try {
            List<DriverPayment> payments= DriverPaymentModel.getAll();
            for (DriverPayment payment : payments){
                paymentTMS.add(new DriverPaymentTM(
                        payment.getPaymentID(),
                        payment.getStatus(),
                        payment.getPaymentCost(),
                        payment.getDriverNic()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tblPayment.setItems(paymentTMS);
    }

    private void getAll() {
        ObservableList<DriverTM> obList = FXCollections.observableArrayList();

        try {
            List<Driver> drivers=DriverModel.getAll();

            for (Driver data:drivers){
                obList.add(new DriverTM(
                        data.getName(),
                        data.getGender(),
                        data.getGmail(),
                        data.getStatus(),
                        data.getNic()
                ));
            }
            tblDriver.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getAllNic() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> nic=DriverModel.loadNic();

            for (String data:nic){
                obList.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cmbNic.setItems(obList);
    }

    private void setGender() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        List<String> gender=new ArrayList<>();
        gender.add("Male");
        gender.add("Female");
        gender.add("other");

        for (String gen:gender) {
            obList.add(gen);
        }
        cmdGender.setItems(obList);
        cmbEditGender.setItems(obList);
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        String name=txtName.getText();
        String nic=txtNic.getText();
        String gmail=txtGmail.getText();
        String status=txtStatus.getText();
        String gender = (String) cmdGender.getValue();

        Driver driver = new Driver(nic,gmail,name,gender,status);

        if (txtNic.getText().length()>1){
            try {
                boolean isSave= DriverModel.save(driver);
                if (isSave){
                    new Alert(Alert.AlertType.CONFIRMATION,"Driver added !").show();
                }
                else {
                    new Alert(Alert.AlertType.WARNING,"Something Happened !").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,"SQL error").show();
                e.printStackTrace();
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"please fill values").show();
        }
        init();
        getAll();
    }

    private void init() {
//        txtContact.clear();
        txtGmail.clear();
        txtNic.clear();
        txtName.clear();

//        txtEditContact.clear();
        txteditName.clear();
        txtEditGmail.clear();
//        txtEditContact.clear();
        txtEditStatus.clear();
    }

    public void cmbNicOnAction(ActionEvent actionEvent) {
        lblDelete.setText("");
        String nic = (String) cmbNic.getValue();

        try {
            Driver driver=DriverModel.getAllDrivers(nic);

            assert driver != null;
            txtEditGmail.setText(driver.getGmail());
            txteditName.setText(driver.getName());
            txtEditStatus.setText(driver.getStatus());
            cmbEditGender.setValue(driver.getGender());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
//        String contact = txtEditContact.getText();
        String gmail = txtEditGmail.getText();
        String status = txtEditStatus.getText();
        String name = txteditName.getText();



        Driver driver = new Driver(
                cmbNic.getValue(),
                gmail,
                name,
                (String) cmbEditGender.getValue(),
                status
        );

        try {
            boolean isUpdate=DriverModel.update(driver);
            if (isUpdate){
                new Alert(Alert.AlertType.CONFIRMATION,"Driver Updated !").show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Something happened !").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"SQL error").show();
            e.printStackTrace();
        }
        getAll();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String nicValue = cmbNic.getValue();

        if (nicValue==null){
            lblDelete.setText("Choose NIC !");
            return;
        }

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Delete ?", yes, no).showAndWait();

        if(result.orElse(no)==yes){
            try {
                boolean isDelete=DriverModel.delete(nicValue);
                if (isDelete){
                    new Alert(Alert.AlertType.CONFIRMATION,"Driver Deleted !").show();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Something Happened !").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,"SQL error").show();
                e.printStackTrace();
            }
        }
        init();
        getAll();
    }

    private void setCellValueFactory() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colGmail.setCellValueFactory(new PropertyValueFactory<>("gmail"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        colPaymentID.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colStatusP.setCellValueFactory(new PropertyValueFactory<>("status"));
        colTotalP.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colDriverNicP.setCellValueFactory(new PropertyValueFactory<>("driverNic"));


    }
}
