package lk.ijse.ikmanRental.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.util.Duration;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Random;

public class RegisterFormController {
    @FXML
    private AnchorPane registerPane;
    @FXML
    private ImageView imgHome;
    @FXML
    private Button btnRegister;
    @FXML
    private CubicCurve curve;
    @FXML
    private TextField txtFirstname;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtGmail;
    @FXML
    private PasswordField txtPasswor;
    @FXML
    private PasswordField txtConfiermPassword;
    @FXML
    private TextField txtNIC;

    private FXMLLoader fxmlLoader;
    private String otp;

    @FXML
    void initialize(){
        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), registerPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        btnRegister.setDisable(false);
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
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
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

    public void rootOnMouseExited(MouseEvent event) {
        curve.setControlX2(50.8468017578125);
        curve.setControlY2(-5);
    }

    public void rootOnMouseMove(MouseEvent event) {
        curve.setControlX2(event.getX());
    }

    public void imgHomeOnAction(MouseEvent event) throws IOException {
        setUi("Login_form.fxml");
    }

    public void btnRegisterOnAction(ActionEvent actionEvent) throws IOException, GeneralSecurityException, MessagingException {

        String OTP []= new String[4];// genarate OTP

        for (int i = 0; i < 4; i++) {
            Random r=new Random();
            OTP[i]=r.nextInt(9)+"";
        }
        otp=OTP[0]+OTP[1]+OTP[2]+OTP[3];

        String message="Hi ,congratulation "+txtFirstname.getText()+" you have a new OTP..!"+"\n"+"\n"+otp;
        String title="Ikman Rental(PVT)";
        String gmail=txtGmail.getText();

        if (txtGmail.getText().equals("") && txtLastName.getText().equals("") && txtFirstname.getText().equals("") && txtPasswor.getText().equals("")){

            txtGmail.setStyle("-fx-border-color: #ff0c0c");
            txtFirstname.setStyle("-fx-border-color: #ff0c0c");
            txtFirstname.setStyle("-fx-border-color: #ff0c0c");
            txtLastName.setStyle("-fx-border-color: #ff0c0c");
            txtPasswor.setStyle("-fx-border-color: #ff0c0c");
            txtConfiermPassword.setStyle("-fx-border-color: #ff0c0c");
            txtNIC.setStyle("-fx-border-color: #ff0c0c");
            return;
        }
        else {
            try {
                new SendText().sendMail(title,message,gmail);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                new Alert(Alert.AlertType.ERROR,"insert values !").show();
                return;
            } catch (GeneralSecurityException e) {

            }
        }

        setUi("otpverify_form.fxml");
        ((OtpverifyFormController)(fxmlLoader.getController())).getGmail(txtFirstname.getText(),txtLastName.getText(),txtGmail.getText(),txtPasswor.getText(),txtNIC.getText(),otp);

    }

    private void setUi(String fileName) throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getResource("/view/"+fileName));
        Pane root = fxmlLoader.load();
        try {
            registerPane.getChildren().clear();
            registerPane.getChildren().setAll(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTextAgin(String Fname,String Lname,String Gmail,String password,String NIC){
        txtLastName.setText(Lname);
        txtFirstname.setText(Fname);
        txtGmail.setText(Gmail);
        txtPasswor.setText(password);
        txtConfiermPassword.setText(password);
        txtNIC.setText(NIC);
        btnRegister.setDisable(false);
    }

    public void txtconfiermKerTyped(KeyEvent keyEvent) {

        if (txtConfiermPassword.getText().equals(txtPasswor.getText())){
            txtConfiermPassword.setStyle("-fx-border-color: green");
            btnRegister.setDisable(false);
        }
        else {
            btnRegister.setDisable(true);
            txtConfiermPassword.setStyle("-fx-border-color: red");
        }
    }
}
