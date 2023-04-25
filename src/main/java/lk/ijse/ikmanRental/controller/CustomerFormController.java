package lk.ijse.ikmanRental.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.ikmanRental.dto.Admin;
import lk.ijse.ikmanRental.dto.Customer;
import lk.ijse.ikmanRental.dto.tm.CustomerTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.ikmanRental.model.AdminModel;
import lk.ijse.ikmanRental.model.CustomerModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class CustomerFormController {

    @FXML
    private AnchorPane contextCustomerPane;

    @FXML
    private TableView tbtCustomer;

    @FXML
    private ComboBox cmbCustomerNic;
    @FXML
    private Label lblNic;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colNIC;
    @FXML
    private TableColumn colContact;
    @FXML
    private TableColumn colGmail;
    @FXML
    private ComboBox cmbAdminNic;
    @FXML
    private JFXButton btnUpdate;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private TextField txteditName;
    @FXML
    private TextField txtEditContact;
    @FXML
    private TextField txtEditGmail;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtNic;
    @FXML
    private TextField txtContact;
    @FXML
    private TextField txtGmail;

    @FXML
    void initialize(){

        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), contextCustomerPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        HomeFormController.stop=false;
        setCellValueFactory();
//        getAdminIds();
        getAll();
        getAllCustomerNic();
    }

    private void addError(TextField textField){
            textField.getParent().setStyle("-fx-border-color: red");
            btnAdd.setDisable(true);
    }

    private void removeError(TextField textField){
        textField.getParent().setStyle("-fx-border-color: green");
        btnAdd.setDisable(false);
    }

    private void getAllCustomerNic() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> custIds = CustomerModel.getCustomerNic();
            for (String id:custIds) {
                obList.add(id);
            }
            cmbCustomerNic.setItems(obList);
        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }

    private void init(){
        txtContact.clear();
        txtEditContact.clear();
        txtEditGmail.clear();
        txtGmail.clear();
        txteditName.clear();
        txtName.clear();
        txtNic.clear();
    }

    @FXML
    public void btnAddOnActon(ActionEvent actionEvent) {
        Admin admin = null;
        String name=txtName.getText();
        String NIC=txtNic.getText();
        String contact=txtContact.getText();
        String gmail=txtGmail.getText();

        if (name.equals("")){
            txtName.setStyle("-fx-border-color: red");
            return;
        }else if (NIC.equals("")){
            txtNic.setStyle("-fx-border-color: red");
            return;
        }else if (contact.equals("")){
            txtContact.setStyle("-fx-border-color: red");
        }else if (gmail.equals("")){
            txtGmail.setStyle("-fx-border-color: red");
        }

        try {
            admin = AdminModel.getloginDetail(Detail.getGmail());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String adminNic=admin.getNIC();

        boolean isSave=false;

        Customer customer = new Customer(NIC, gmail, contact, name, adminNic);

        if (txtNic.getText().length()>1){
            try {
                isSave = CustomerModel.save(customer);
                if (isSave) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer Added Successful !").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "please enter values !").show();
                }

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Duplicate NIC number , SQL Error :(").show();
            }
        }
        else {
            new Alert(Alert.AlertType.WARNING,"please fill values").show();
        }
        init();
        getAll();
//        getAllCustomerNic();
//        FXMLLoader fxmlLoader=null;
//        ((BookingFormController)fxmlLoader.getController()).initialize();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String nic= (String) cmbCustomerNic.getValue();

        if (nic==null){
            lblNic.setStyle("-fx-text-fill: red");
            lblNic.setText("Choose NIC !");
            txtEditContact.setStyle("-fx-border-color: red");
            txtEditGmail.setStyle("-fx-border-color: red");
            txteditName.setStyle("-fx-border-color: red");
            return;
        }

        Customer customer = new Customer();
        customer.setNic(nic);
        customer.setName(txteditName.getText());
        customer.setGmail(txtEditGmail.getText());
        customer.setContact(txtEditContact.getText());


        try {
            boolean isUpdate= CustomerModel.update(customer);
            if (isUpdate){
                new Alert(Alert.AlertType.CONFIRMATION,"updated !").show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Something happened !").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getAll();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String nic= (String) cmbCustomerNic.getValue();

        if (nic==null){
            lblNic.setStyle("-fx-text-fill: red");
            lblNic.setText("Choose NIC !");
            return;
        }

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Delete ?", yes, no).showAndWait();

        if(result.orElse(no)==yes){
            try {
                boolean isDeleted=CustomerModel.delete(nic);
                if (isDeleted){
                    new Alert(Alert.AlertType.CONFIRMATION,"delete SuccessesFull").show();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Something happened !").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,"this customer is applied booking !").show();
//            e.printStackTrace();
            }
        }
        getAll();
        getAllCustomerNic();
    }

    private void getAdminIds(){
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> ids = AdminModel.loadIds();

            for (String id:ids) {
                obList.add(id);
            }
            cmbAdminNic.setItems(obList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setCellValueFactory() {
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("NIC"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        colGmail.setCellValueFactory(new PropertyValueFactory<>("Gmail"));
    }

    private void getAll(){

        ObservableList<CustomerTM> obList = FXCollections.observableArrayList();
        try {
            List<Customer> cusList = CustomerModel.getAll();

            for (Customer customer : cusList) {
                obList.add(new CustomerTM(
                        customer.getName(),
                        customer.getNic(),
                        customer.getContact(),
                        customer.getGmail()
                ));
            }
            tbtCustomer.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cmdCustomerOnAction(ActionEvent actionEvent) throws SQLException {
        txtEditContact.setStyle("-fx-border-color: green");
        txtEditGmail.setStyle("-fx-border-color: green");
        txteditName.setStyle("-fx-border-color: green");
        lblNic.setText("");
        String nic= (String) cmbCustomerNic.getValue();

        Customer customer=CustomerModel.getAllCustomer(nic);

        try {
            txteditName.setText(customer.getName());
            txtEditContact.setText(customer.getContact());
            txtEditGmail.setText(customer.getGmail());
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public void txtNicTypedOnAction(KeyEvent keyEvent) {
        if (txtNic.getText().length()>=12 && txtNic.getText().length()==12 || txtNic.getText().length()>=10 && txtNic.getText().length()==10){
            txtNic.setStyle("-fx-border-color: green");
        }else {
            txtNic.setStyle("-fx-border-color: red");
        }
    }

    public void txtGmailTypedOnAction(KeyEvent keyEvent) {
        String gmail = txtGmail.getText();

        for (int i = 0; i < gmail.length(); i++) {
            if (gmail.charAt(i)=='@') {
                String s = "gmail.com";
                gmail = gmail + s;

                txtGmail.setText(gmail);
                txtGmail.setStyle("-fx-border-color: green");
                break;
            }else {
                txtGmail.setStyle("-fx-border-color: red");
            }
        }

    }

    public void txtContactTypedOnAction(KeyEvent keyEvent) {
        if (Pattern.compile("^(071|072|077|076|078|075)[0-9]{7}$").matcher(txtContact.getText()).matches()){
            txtContact.setStyle("-fx-border-color: green");
        }else {
            txtContact.setStyle("-fx-border-color: red");
        }
    }
}
