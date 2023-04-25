package lk.ijse.ikmanRental.controller;

import javafx.animation.FadeTransition;
import lk.ijse.ikmanRental.dto.Admin;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.util.Duration;
import lk.ijse.ikmanRental.model.AdminModel;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

public class LoginFormController {
    @FXML
    private ImageView imgLogin;

    @FXML
    private Label lblGmailCheck;

    @FXML
    private Label lbltime;

    @FXML
    private CubicCurve curve;

    @FXML
    private Button btnNext;

    @FXML
    private Hyperlink hypCeateAccunt;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField txtGmail;

    @FXML
    private Label lblWernning;

    @FXML
    private PasswordField txtpasswordF;

    @FXML
    private Hyperlink hypPasswordForgrt;

    boolean stop =true;

    private Thread thread;

    private FXMLLoader fxmlLoader;

    Timeline timeline;

    @FXML
    public void initialize() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), mainPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

//        timenow();
        btnNext.setDisable(false);
        txtGmail.requestFocus();
//        imgLogin.setDisable(true);

//        timeline = new Timeline(new KeyFrame(Duration.seconds(0.8), event -> {
//            boolean isVisible = imgLogin.isVisible();
//            imgLogin.setVisible(!isVisible);
//        }));
//        timeline.setCycleCount(Animation.INDEFINITE);
//        timeline.play();
    }

//    private void timenow(){
//         thread=new Thread(()->{
//            SimpleDateFormat format=new SimpleDateFormat("hh:mm:ss");
//            while (stop){
//                try {
//                    Thread.sleep(1000);
//
//                }catch (Exception ignored){
//
//                }
//                final String time=format.format(new Date());
//                Platform.runLater(() -> lbltime.setText(time));
//            }
//        });
//        thread.start();
//    }

    @FXML
    public void rootOnMouseExited(MouseEvent mouseEvent) {
        curve.setControlX2(50.8468017578125);
        curve.setControlY2(-5);
    }

    @FXML
    public void rootOnMouseMove(MouseEvent mouseEvent) {
        curve.setControlX2(mouseEvent.getX());
    }

    @FXML
    public void btnNextOnAction(ActionEvent actionEvent) throws SQLException, IOException {
        Detail.setGmail(txtGmail.getText());
        Admin admin= AdminModel.getloginDetail(txtGmail.getText());

        try {
            if (admin.getPassword().equals(txtpasswordF.getText())) {
                setUi("dashBord_form.fxml");
                //new Alert(Alert.AlertType.CONFIRMATION, "Log In Successful :)").show();
            } else {
                txtGmail.setStyle("-fx-border-color: red");
                txtpasswordF.setStyle("-fx-border-color: red");
                txtpasswordF.clear();
                txtGmail.clear();
                lblWernning.setText("Doesn't match your detail.");
                return;
            }
        }catch (Exception e){
            txtGmail.setStyle("-fx-border-color: red");
            txtpasswordF.setStyle("-fx-border-color: red");
            txtpasswordF.clear();
            txtGmail.clear();
            lblWernning.setText("please insert values !");
            return;
        }
//        setUi("dashBord_form.fxml");
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
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
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
    public void hypCeateAccuntOnAction(ActionEvent actionEvent) throws IOException {
        setUi("register_form.fxml");
    }

    @FXML
    private void setUi(String fileName) throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getResource("/view/"+fileName));
        Pane root = fxmlLoader.load();
        try {
            mainPane.getChildren().clear();
            mainPane.getChildren().setAll(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void txtGmailKeyTyped(KeyEvent keyEvent) {
        lblWernning.setText("");

        String gmail=txtGmail.getText();

        for (int i = 0; i < txtGmail.getText().length(); i++) {
            if(gmail.charAt(i)=='@'){
//                String s = "gmail.com";
                txtGmail.setStyle("-fx-border-color: green");
//                gmail=gmail + s;
//                txtGmail.setText(gmail);
                btnNext.setDisable(false);
                break;
            }
            else {
                txtGmail.setStyle("-fx-border-color: red");
            }

        }
    }

    @FXML
    public void txtPasswordOnAction(ActionEvent actionEvent) {
        try {
            btnNextOnAction(actionEvent);
        } catch (SQLException e) {
            System.out.println("not created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hypPasswordForgrtOnAction(ActionEvent actionEvent) throws GeneralSecurityException, IOException {

        try {
            Admin admin = AdminModel.getloginDetail(txtGmail.getText());
//            System.out.println(admin);
            if (admin.getPassword() ==null){
                lblGmailCheck.setText("invalid Gmail Address !");
            }else {
                String message="Hi , your password is : "+admin.getPassword();
                String title="Ikman Rental(PVT)";
                String gmail=txtGmail.getText();
                new SendText().sendMail(title,message,gmail);
                lblGmailCheck.setStyle("-fx-text-fill: black");
                lblGmailCheck.setText("Please check your Gmail !");
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.WARNING,"Something Else !").show();
        } catch (MessagingException e) {
            lblGmailCheck.setText("please enter your registered Gmail !");
        }


    }
}
