package lk.ijse.ikmanRental.controller;

import com.jfoenix.controls.JFXButton;

import javafx.animation.FadeTransition;
import javafx.scene.layout.AnchorPane;
import lk.ijse.ikmanRental.dto.*;
import lk.ijse.ikmanRental.dto.tm.BookingTM;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import lk.ijse.ikmanRental.model.*;


import javax.mail.MessagingException;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class BookingFormController {

    @FXML
    private AnchorPane contextBookingPane;

    @FXML
    private TextField txtHours;

    @FXML
    private Label lblBooking;

    @FXML
    private ComboBox cmbBookingStatus;

    @FXML
    private Label lblBillID;

    @FXML
    private Label lblPaymentID;

    @FXML
    private Label lblDate;
    @FXML
    private JFXButton btnNewCustomer;

    @FXML
    private TableView tblBill;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnNew;

    @FXML
    private JFXButton btnSearchAndEdit;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private ComboBox cmbBookingID;

    @FXML
    private Label lblChooseBookingId;

    @FXML
    private JFXButton btnNext1;

    @FXML
    private JFXButton btnNext2;

    @FXML
    private JFXButton btnBack1;

    @FXML
    private JFXButton btnBack2;

    @FXML
    private Tab tabPane2;

    @FXML
    private Tab tabPane1;

    @FXML
    private Tab tabPane3;

    @FXML
    private ImageView imgbtnHome;

    @FXML
    private Label lblDriverName;

    @FXML
    private Label lblCustomerName;

    @FXML
    private ComboBox<String> cmbCustomerDriverNic;

    @FXML
    private ComboBox<String> cmbCustomerNic;

    @FXML
    private ComboBox<String> cmbVehicle;

    @FXML
    private ComboBox<String> cmbVehicleNumber;

    @FXML
    private TableColumn colBookingID;

    @FXML
    private TableColumn colCost;

    @FXML
    private TableColumn colCustomerNic;

    @FXML
    private TableColumn colDistance;

    @FXML
    private TableColumn colDriverNic;

    @FXML
    private TableColumn colRequiredDate;

    @FXML
    private TableColumn colRideTo;

    @FXML
    private TableColumn colStatus;

    @FXML
    private TableColumn colVehicleNumber;

    @FXML
    private DatePicker datePicer;

    @FXML
    private Label lblCos;

    @FXML
    private Label lblDistance;

    @FXML
    private Label lblDriverPayament;

    @FXML
    private Label lblFual;

    @FXML
    private Label lblTotelCost;

    @FXML
    private TextField txtDistance;

    @FXML
    private TextField txtPlace2;

    @FXML
    private TextField txtplace1;

    @FXML
    void initialize(){

        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), contextBookingPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        HomeFormController.stop=false;
        setCellValueFactory();
        fillTable();
        setBookingID();
        setCustomerNic();
        setDriverNic();
        setVehicleType();
        generateBookingID();
        setStatus();
        generatePaymentID();
        generateBillID();
        btnNext1.setDisable(true);
        btnNext2.setDisable(true);
        btnUpdate.setDisable(true);
        imgbtnHome.setDisable(true);
    }

    private void setBookingID() {
        ObservableList<String> ids=FXCollections.observableArrayList();

        try {
            List<String> bookingID= BookingModel.getBookingIds();
            for (String id : bookingID){
                ids.add(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cmbBookingID.setItems(ids);
    }

    private void setCellValueFactory() {
        colBookingID.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
        colDriverNic.setCellValueFactory(new PropertyValueFactory<>("driverNIC"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colRequiredDate.setCellValueFactory(new PropertyValueFactory<>("requiredDate"));
        colDistance.setCellValueFactory(new PropertyValueFactory<>("distance"));
        colVehicleNumber.setCellValueFactory(new PropertyValueFactory<>("vehicleNumber"));
        colRideTo.setCellValueFactory(new PropertyValueFactory<>("rideTo"));
        colCustomerNic.setCellValueFactory(new PropertyValueFactory<>("customerNIC"));
    }

    private void fillTable() {
        String bookingID=null;
        ObservableList<BookingTM> bookingTMS=FXCollections.observableArrayList();

        try {
            List<Booking> bookings=BookingModel.getAll();

            for (Booking booking : bookings){
                bookingID=booking.getBookingID();

                bookingTMS.add(new BookingTM(
                        booking.getBookingID(),
                        DriverScheduleModel.getDriverNic(bookingID),
                        booking.getStatus(),
                        booking.getAmountsCost(),
                        booking.getRequiredDate(),
                        booking.getDistance(),
                        BookingDetailModel.getVehicleNumber(bookingID),
                        booking.getRideTo(),
                        booking.getCustomerNic()
                ));
            }
            tblBill.setItems(bookingTMS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateBillID() {
        try {
            String billID= BillModel.getNextID();
            lblBillID.setText(billID);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Bill SQL Error !").show();
            e.printStackTrace();
        }
    }

    private void generatePaymentID() {
        try {
            String paymentID=DriverPaymentModel.getnextID();
            lblPaymentID.setText(paymentID);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Payment SQL Error !").show();
            e.printStackTrace();
        }
    }

    private void setStatus() {
        ObservableList<String> status=FXCollections.observableArrayList();
        status.add("PENDING");
        status.add("RUNNING");
        status.add("FINISHED");
        cmbBookingStatus.setItems(status);
    }

    private void generateBookingID() {

        try {
            String id= BookingModel.getNextBookingID();
            lblBooking.setText(id);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"SQL Error !").show();
            e.printStackTrace();
        }
    }

    private void setVehicleType() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        obList.add("BUS");
        obList.add("PRIME MOVER");
        obList.add("LORRY");
        cmbVehicle.setItems(obList);
    }

    private void setDriverNic() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> nic= DriverModel.loadNic();
            for (String data:nic){
                obList.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cmbCustomerDriverNic.setItems(obList);
    }

    private void setCustomerNic() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> custIds = CustomerModel.getCustomerNic();
            for (String id:custIds) {
                obList.add(id);
            }
            cmbCustomerNic.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof javafx.scene.image.ImageView) {
            javafx.scene.image.ImageView icon = (ImageView) event.getSource();

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            glow.setWidth(15);
            glow.setHeight(15);
            glow.setRadius(15);
            icon.setEffect(glow);
        }
    }

    public void playMouseExitAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();
            icon.setEffect(null);
        }
    }

    public void imgMapClicked(MouseEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.google.com/maps/dir///@5.9971358,80.5872501,12z/data=!4m2!4m1!3e0?authuser=0"));
    }

    public void addBookingClicked(MouseEvent event) {
        String bookingID = lblBooking.getText();
        String status = (String) cmbBookingStatus.getValue();
        Double amountsCost = null;
        try {
            amountsCost = Double.valueOf(lblTotelCost.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR,"Please insert Values !").show();
            return;
        }
        Date date = Date.valueOf(datePicer.getValue());
        String riteTO = txtplace1.getText()+" - "+txtPlace2.getText();
        String distance = txtDistance.getText();
        String customerNic = cmbCustomerNic.getValue();
        String driverNic = cmbCustomerDriverNic.getValue();
        String vehicleNumber = cmbVehicleNumber.getValue();
        Double fuel = Double.valueOf(lblFual.getText());
        Double driverPayment = Double.valueOf(lblDriverPayament.getText());
        Date localDate= Date.valueOf(LocalDate.now());

        Booking booking = new Booking(bookingID, status, amountsCost, date, riteTO, distance, customerNic);
        Bill bill = new Bill(lblBillID.getText(), bookingID, customerNic, driverNic, amountsCost, vehicleNumber, localDate);
        DriverPayment driverPay = new DriverPayment(lblPaymentID.getText(), "PENDING", driverPayment, driverNic);
        DriverSchedule driverSchedule = new DriverSchedule(bookingID, driverNic);
        BookingDetail bookingDetail = new BookingDetail(bookingID, vehicleNumber,fuel);

        boolean isBooked=false;

        try {
            isBooked=BookingModel.addBooking(booking,bill,driverPay,driverSchedule,bookingDetail);
            if (isBooked){
                new Alert(Alert.AlertType.CONFIRMATION,"Success !").show();
//                Customer allCustomer = CustomerModel.getAllCustomer(cmbCustomerNic.getValue());
//                String gmail=DriverModel.getGmail(cmbCustomerDriverNic.getValue());
//                sendMail(allCustomer,gmail);
//                    new SendText().sendMail("ikmanRental(PVT)","Hi, you have a ride to + date for prime mover","thantrige32@gmail.com");
            }else {
                new Alert(Alert.AlertType.WARNING,"Something Happened !").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Booking SQL Error !").show();
            e.printStackTrace();
        }
        init();
        fillTable();
        TabPane tabPane = tabPane1.getTabPane();
        tabPane.getSelectionModel().select(tabPane1);
        tabPane2.setDisable(true);
        tabPane3.setDisable(true);
        tabPane1.setDisable(false);
    }

    private void sendMail(Customer allCustomer, String gmail) {
        String massageDriver="Hi ,"+lblDriverName.getText()+" you have a new Ride ON :\n\t" +
                datePicer.getValue()+""+" Please come on this Date...\n" +
                "your booking id="+lblBooking.getText();

        String customerMassage="hi ,"+lblCustomerName.getText()+" your Booking Approved... !" +
                "your booking id="+lblBooking.getText();

        String customerGmail=allCustomer.getGmail();
        String DriverGmail=gmail;
        String title="Ikman Rental Service (PVT)";

        try {
            new SendText().sendMail(title,massageDriver,DriverGmail);
            System.out.println("------------------------------------------------------------");
            new SendText().sendMail(title,customerMassage,customerGmail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        cmbVehicleNumber.setValue("");
        cmbVehicle.setValue("");
        cmbBookingStatus.setValue("");
        cmbCustomerNic.setValue("");
        txtDistance.clear();
        txtPlace2.clear();
        txtplace1.clear();
        txtHours.clear();
        lblDriverPayament.setText("");
        lblDistance.setText("");
        lblTotelCost.setText("");
        lblFual.setText("");
        lblCustomerName.setText("");
        lblDriverName.setText("");
        lblCos.setText("");
        lblTotelCost.setText("");
        cmbCustomerDriverNic.setValue("");
        lblDate.setText("");

        generateBillID();
        generateBookingID();
        generatePaymentID();
    }

    public void txtDistanceOnAction(ActionEvent actionEvent) {
        if (!txtDistance.getText().equals("")){
            btnUpdate.setDisable(false);
            imgbtnHome.setDisable(false);
        }else {
            new Alert(Alert.AlertType.WARNING,"try Again !").show();
            return;
        }

        final double ml=1000;
        double km= 0;
        Double hours=0.0;

        try {
            km = Double.parseDouble(txtDistance.getText());
            hours= Double.parseDouble(txtHours.getText());

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            return;
        }

        try {
            Vehicle vehicle= VehicleModel.getFuelToKm(cmbVehicleNumber.getValue());
            double fuelTokm=vehicle.getFuelToKm();
            Double status= null;
            try {
                status = Double.parseDouble(vehicle.getStatus());
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR,"Please insert Values !").show();
            }
            double result=ml/fuelTokm;
            double finalResult=result/ml;
            double fuel=finalResult*km;
            double fuelCost=fuel*560;
            double vehicleCost=status*hours+fuelCost;
            lblFual.setText(String.valueOf(fuel));
            lblCos.setText(String.valueOf(vehicleCost));
            Double driverStatus=DriverModel.getStatus(cmbCustomerDriverNic.getValue());
            double driverCost=driverStatus*hours;
            lblDriverPayament.setText(String.valueOf(driverCost));
            double total=vehicleCost+driverCost;
            lblTotelCost.setText(String.valueOf(total));
            lblDistance.setText(String.valueOf(km));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    public void cmbVehicleOnAction(ActionEvent actionEvent) {
        ObservableList<String> numbers=FXCollections.observableArrayList();
        String type = cmbVehicle.getValue();

        try {
            List<String> vehicle= VehicleModel.getAvailbleType(type);
            for(String num : vehicle){
                numbers.add(num);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        cmbVehicleNumber.setItems(numbers);
    }

    public void cmbCustomerNicOnAction(ActionEvent actionEvent) {
        String customerNic = cmbCustomerNic.getValue();

        try {
            String name=CustomerModel.getName(customerNic);
            lblCustomerName.setText(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cmbDriverNicOnAction(ActionEvent actionEvent) {
        String driverNic = cmbCustomerDriverNic.getValue();

        try {
            String name=DriverModel.getName(driverNic);
            lblDriverName.setText(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnNext1Clicked(MouseEvent event) {
        tabPane2.setDisable(false);
        TabPane tabPane = tabPane2.getTabPane();
        tabPane.getSelectionModel().select(tabPane2);
        tabPane1.setDisable(true);
    }

    public void btnNext2Clicked(MouseEvent event) {
        tabPane3.setDisable(false);
        tabPane2.setDisable(true);
        TabPane tabPane = tabPane3.getTabPane();
        tabPane.getSelectionModel().select(tabPane3);
    }

    public void btnBack2Clicked(MouseEvent event) {
        tabPane3.setDisable(true);
        tabPane2.setDisable(false);
        TabPane tabPane = tabPane2.getTabPane();
        tabPane.getSelectionModel().select(tabPane2);
    }

    public void btnBackClicked(MouseEvent event) {
        TabPane tabPane = tabPane1.getTabPane();
        tabPane.getSelectionModel().select(tabPane1);
        tabPane2.setDisable(true);
        tabPane1.setDisable(false);
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {

        String bookingID = (String) cmbBookingID.getValue();

        String status = (String) cmbBookingStatus.getValue();
        Double amountsCost = null;
        try {
            amountsCost = Double.valueOf(lblTotelCost.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR,"Please insert Values !").show();
            return;
        }
        Date date = Date.valueOf(datePicer.getValue());
        String riteTO = txtplace1.getText()+" - "+txtPlace2.getText();
        String distance = txtDistance.getText();
        String customerNic = cmbCustomerNic.getValue();
        String driverNic = cmbCustomerDriverNic.getValue();
        String vehicleNumber = cmbVehicleNumber.getValue();
        Double fuel = Double.valueOf(lblFual.getText());
        Double driverPayment = Double.valueOf(lblDriverPayament.getText());
        Date localDate= Date.valueOf(LocalDate.now());

        boolean isUpdate=false;

        Booking booking = new Booking(bookingID, status, amountsCost, date, riteTO, distance, customerNic);
        Bill bill = new Bill(BillModel.getBillid(bookingID), bookingID, customerNic, driverNic, amountsCost, vehicleNumber, localDate);
        DriverPayment driverPay = new DriverPayment(DriverPaymentModel.getPaymentID(driverNic), "PENDING", driverPayment, driverNic);
        DriverSchedule driverSchedule = new DriverSchedule(bookingID, driverNic);
        BookingDetail bookingDetail = new BookingDetail(bookingID, vehicleNumber,fuel);

        try {
            isUpdate=BookingModel.update(booking,bill,driverPay,driverSchedule,bookingDetail);

            if (isUpdate){
                new Alert(Alert.AlertType.CONFIRMATION,"Success !").show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Something Happened !").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            e.printStackTrace();
        }
        btnUpdate.setDisable(true);
        fillTable();
        init();
    }

    public void btnNewOnAction(ActionEvent actionEvent) {
        tabPane1.setDisable(false);
        btnDelete.setVisible(false);
        imgbtnHome.setDisable(false);
        imgbtnHome.setVisible(true);
        btnUpdate.setVisible(false);
        cmbBookingID.setVisible(false);
        lblChooseBookingId.setVisible(false);
        init();
    }

    public void btnSearchAndEditONAction(ActionEvent actionEvent) {
        tabPane1.setDisable(false);
        btnDelete.setVisible(true);
        imgbtnHome.setDisable(true);
        imgbtnHome.setVisible(false);
        btnUpdate.setVisible(true);
        cmbBookingID.setVisible(true);
        lblChooseBookingId.setVisible(true);
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String id = (String) cmbBookingID.getValue();

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Delete ?", yes, no).showAndWait();

        if(result.orElse(no)==yes){
            try {

                boolean isDeleted=BookingModel.delete(id);

                if (isDeleted){
                    new Alert(Alert.AlertType.CONFIRMATION,"Deleted Successful !").show();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Something Happened !").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                e.printStackTrace();
            }
        }
        fillTable();
        init();
    }

    public void cmbBookingIDOnAction(ActionEvent actionEvent) {
        String id = (String) cmbBookingID.getValue();

        try {
            Booking booking=BookingModel.getAllFromID(id);

            BookingDetail bookingDetail=BookingDetailModel.getAll(id);

            DriverSchedule driverSchedule=DriverScheduleModel.getAll(id);

            Bill bill=BillModel.getAll(id);

            DriverPayment payment=DriverPaymentModel.getAll(driverSchedule.getDriverNic());

            cmbBookingStatus.setValue(booking.getStatus());
            cmbCustomerDriverNic.setValue(driverSchedule.getDriverNic());
            cmbVehicleNumber.setValue(bookingDetail.getVehicleNumber());
            cmbVehicle.setValue(DriverModel.getType(bookingDetail.getVehicleNumber()));
            cmbCustomerNic.setValue(booking.getCustomerNic());

            lblDate.setText(String.valueOf(booking.getRequiredDate()));

            String palaces=booking.getRideTo();
            String place1="";
            String place2="";
            int len=0;

            for (int i = 0; i < palaces.length(); i++){
                char x = palaces.charAt(i);
                if(x=='-'){
                    len=i;
                    break;
                }
            }

            for (int i = 0; i < len; i++) {
                place1+=palaces.charAt(i);
            }

            for (int i = 0; i < palaces.length(); i++){
                if (i>len+1){
                    place2+=palaces.charAt(i);
                }
            }

            txtplace1.setText(place1);
            txtPlace2.setText(place2);

            txtDistance.setText(booking.getDistance());
            try {
                txtHours.setText(String.valueOf(payment.getPaymentCost()/ DriverModel.getStatus(driverSchedule.getDriverNic())));
            } catch (SQLException e) {
//                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void datePicerOnAction(ActionEvent actionEvent) {
        btnNext1.setDisable(false);
    }

    public void txtPlace2OnAction(ActionEvent actionEvent) {
        btnNext2.setDisable(false);
    }

    public void btnNewCustomerOnAction(ActionEvent actionEvent) {
        Stage stage=new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/customer_form.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.getIcons().add(new Image("assets/ikmanicon.png"));
        stage.setResizable(false);
        stage.setTitle("ikman Rental System");
        stage.show();
        stage.centerOnScreen();
    }
}
