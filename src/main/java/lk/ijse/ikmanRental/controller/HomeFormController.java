package lk.ijse.ikmanRental.controller;

import javafx.animation.FadeTransition;
import javafx.scene.layout.AnchorPane;
import lk.ijse.ikmanRental.db.DBConnection;
import lk.ijse.ikmanRental.dto.VehicleIn;
import lk.ijse.ikmanRental.dto.VehicleOut;
import lk.ijse.ikmanRental.dto.tm.VehicleInTM;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.ikmanRental.model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeFormController {


    @FXML
    private AnchorPane homeContext;

    @FXML
    private Circle circlTransition;

    @FXML
    private Label lblOut;

    @FXML
    private Label lblIn;
    @FXML
    private Label lbltime;

    public Thread thread;

    public static boolean stop=true;

    @FXML
    private ImageView imgLOgOut;

    @FXML
    private ImageView imgtransaction;

    @FXML
    private Label lblCurrentDate;

    @FXML

    private Label lblCurrentDate2;

    @FXML
    private Label lblRegisterCustomer;

    @FXML
    private Label lblAVehicle;

    @FXML
    private Label lblBooking;

    @FXML
    private Label lblDrivers;

    @FXML
    private Label lblRides;

    @FXML
    private ComboBox cmbBookingID;

    @FXML
    private Label lblDriverId;

    @FXML
    private Label lblVehicleNUmber;

    @FXML
    private Button btnAdd;

    @FXML
    private ComboBox cmbBookingId2;

    @FXML
    private Label lblDistance;

    @FXML
    private Button btnAdd2;

    @FXML
    private Label lblDriverId2;

    @FXML
    private Label lblVehicleNum2;

    @FXML
    private TableView tblVehicleIn;

    @FXML
    private TableColumn colBookingId;

    @FXML
    private TableColumn colVehicleNumber;

    @FXML
    private TableColumn colDate;

    @FXML
    private TableColumn colDriverNic;


    @FXML
    void initialize (){

        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), homeContext);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        stop=true;
        setCellValueFactory();
        imgtransaction.setVisible(true);
        TranslateTransition transition=new TranslateTransition();
        transition.setNode(imgtransaction);
        transition.setDuration(Duration.millis(8000));
        transition.setByX(1000);
        transition.play();
        timenow();
        setBookingIDs();
        setLocalDate();
        setbookinidInPendinng();
        countResult();
        filltable();
        setRotate(circlTransition,true,360,10);
    }

    private void setRotate(Circle circle,boolean reverse,int angle,int duration){
        RotateTransition rt=new RotateTransition(Duration.seconds(duration),circle);

        rt.setAutoReverse(reverse);
        rt.setByAngle(angle);
        rt.setDelay(Duration.seconds(0));
        rt.setRate(3);
        rt.setCycleCount(50);
        rt.play();
    }

    private void setCellValueFactory() {
        colVehicleNumber.setCellValueFactory(new PropertyValueFactory<>("vehicleNumber"));
        colDriverNic.setCellValueFactory(new PropertyValueFactory<>("driverNic"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("currentDate"));
        colBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
    }

    private void filltable() {

        ObservableList<VehicleInTM> vehicleInTMS=FXCollections.observableArrayList();

        try {
            List<VehicleIn> inList= VehicleInModel.getAll();
            for (VehicleIn in : inList){
                vehicleInTMS.add(new VehicleInTM(
                        in.getVehicleNumber(),
                        in.getDriverNic(),
                        in.getDate(),
                        in.getBookingId()
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tblVehicleIn.setItems(vehicleInTMS);
    }

    private void countResult() {
        try {
            int count= CustomerModel.countCustomer();
            lblRegisterCustomer.setText(String.valueOf(count));
            int countVehicle= VehicleModel.count();
            lblAVehicle.setText(String.valueOf(countVehicle));
            int countBooking= BookingModel.count();
            lblBooking.setText(String.valueOf(countBooking));
            int countDrivers= DriverModel.count();
            lblDrivers.setText(String.valueOf(countDrivers));
            int countToday=BookingModel.countRides();
            lblRides.setText(String.valueOf(countToday));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setbookinidInPendinng() {
        ObservableList<String> bookingIds= FXCollections.observableArrayList();
        List<String> validIds=new ArrayList<>();

        try {
            validIds=BookingModel.getPendinngIds();
            for (String ids : validIds){
                bookingIds.add(ids);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cmbBookingId2.setItems(bookingIds);
    }

    private void setLocalDate() {
        LocalDate now = LocalDate.now();
        lblCurrentDate.setText(String.valueOf(now));
        lblCurrentDate2.setText(String.valueOf(now));
    }

    private void setBookingIDs() {
        ObservableList<String> bookingIds= FXCollections.observableArrayList();
        List<String> validIds=new ArrayList<>();

        try {
            validIds= BookingModel.getRunningIds();
            for (String ids : validIds){
                bookingIds.add(ids);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cmbBookingID.setItems(bookingIds);
    }

    private void timenow(){
        thread=new Thread(()->{
            SimpleDateFormat format=new SimpleDateFormat("hh:mm:ss");
            while (stop){
                try {
                    Thread.sleep(1000);
                }catch (Exception ignored){
                }
                final String time=format.format(new Date());
                Platform.runLater(() -> lbltime.setText(time));
            }
        });
        thread.start();
    }

    @FXML
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

    @FXML
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

    @FXML
    public void logoutClicked(MouseEvent event) {
        stop=false;
//        thread.stop();
        Stage satge1=new Stage();
        try {
            satge1.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Login_form.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        satge1.getIcons().add(new Image("assets/ikmanicon.png"));
        satge1.setTitle("ikman rental");
        satge1.show();
        satge1.setResizable(false);
        Stage stage2= (Stage) imgLOgOut.getScene().getWindow();
        stage2.close();
//        System.exit(0);
        if (AdmineditFormController.open){
            DashBordController.click=0;
            DashBordController.primaryStage.close();
        }
    }

    @FXML
    public void cmbBookingIdOnAction(ActionEvent actionEvent) {
        lblIn.setText("");
        String id = (String) cmbBookingID.getValue();

        try {
            String vehicleId= BookingDetailModel.getVehicleNUmberInRunning(id);
            String driverId= DriverScheduleModel.getDriverNicInRunning(id);
            lblDriverId.setText(driverId);
            lblVehicleNUmber.setText(vehicleId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnAddOnAction(ActionEvent actionEvent) throws SQLException {
        String id = (String) cmbBookingID.getValue();
        if (id==null){
            lblIn.setText("Choose ID !");
            return;
        }
        Calendar calendar = Calendar.getInstance();

        java.util.Date currentDate = calendar.getTime();

        java.sql.Date date = new java.sql.Date(currentDate.getTime());

        VehicleIn vehicleIn = new VehicleIn(lblVehicleNUmber.getText(),lblDriverId.getText(),date,id);

        Connection connection=null;

        try {
            connection= DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isSave= VehicleInModel.save(vehicleIn);
            boolean isUpdate=BookingModel.setStatus(id);
            boolean isUpdateVehicle=VehicleModel.updateAvailabilityRunning(lblVehicleNUmber.getText(),"AVAILABLE");
            boolean isUpdatePayment=DriverPaymentModel.updatePayment(lblDriverId.getText());

            if (isSave && isUpdate && isUpdateVehicle && isUpdatePayment){
                connection.commit();
                new Alert(Alert.AlertType.CONFIRMATION,"Success !").show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Something Happened !").show();
            }

        } catch (SQLException e) {
            connection.rollback();
            new Alert(Alert.AlertType.ERROR,"SQL Error !").show();
        }finally {
            connection.setAutoCommit(true);
        }
    }

    @FXML
    public void cbmBookingId2OnAction(ActionEvent actionEvent) {
        lblOut.setText("");
        String id = (String) cmbBookingId2.getValue();

        try {
            String vehicleId= BookingDetailModel.getVehicleNUmberInRunning(id);
            String driverId= DriverScheduleModel.getDriverNicInRunning(id);
            Double distance=BookingModel.getDistance(id);
            lblDriverId2.setText(driverId);
            lblVehicleNum2.setText(vehicleId);
            lblDistance.setText(String.valueOf(distance));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnAdd2OnAction(ActionEvent actionEvent) throws SQLException {
        String id = (String) cmbBookingId2.getValue();
        if (id==null){
            lblOut.setText("Choose ID !");
            return;
        }
        Connection connection=null;
        try {
            connection= DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            VehicleOut vehicle=new VehicleOut(lblVehicleNum2.getText(),lblDriverId2.getText(),BookingModel.getDistance(id),id);
            boolean isSave= VehicleOutModel.save(vehicle);
            boolean isUpdate=BookingModel.updateVehicleOut(id);
            boolean isUpdateVehicle=VehicleModel.updateAvailabilityRunning(lblVehicleNum2.getText(),"RUNNING");

            if (isSave && isUpdate && isUpdateVehicle){
                new Alert(Alert.AlertType.CONFIRMATION,"Success !").show();
                connection.commit();
            }else {
                new Alert(Alert.AlertType.WARNING,"Something Happened !").show();
            }
        } catch (SQLException e) {
            connection.rollback();
            new Alert(Alert.AlertType.ERROR,"SQL Error !").show();
        }finally {
            connection.setAutoCommit(true);
        }
    }

    @FXML
    public void btnBillOnAction(ActionEvent actionEvent) {

        if (cmbBookingId2.getValue()==null){
            lblOut.setText("Choose ID !");
            return;
        }

        Connection connection=null;
        try {
            connection=DBConnection.getInstance().getConnection();
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("file/serviceBill.jrxml");
            JasperReport report= JasperCompileManager.compileReport(is);
            Map<String,Object> par=new HashMap<>();
            par.put("bookingId", (String)cmbBookingId2.getValue());
            JasperPrint print=JasperFillManager.fillReport(report,par,connection);
            JasperViewer.viewReport(print,false);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JRException e) {
            Logger.getLogger(HomeFormController.class.getName()).log(Level.SEVERE,null,connection);
        }
    }

}
