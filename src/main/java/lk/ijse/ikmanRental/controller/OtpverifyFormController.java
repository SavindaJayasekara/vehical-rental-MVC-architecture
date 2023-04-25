package lk.ijse.ikmanRental.controller;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lk.ijse.ikmanRental.dto.Admin;
import javafx.animation.ScaleTransition;
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
import javafx.util.Duration;
import lk.ijse.ikmanRental.model.AdminModel;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Random;

public class OtpverifyFormController {
    private  String NIC;
    public AnchorPane verifyPane;
    public ImageView imgHome;
    public Button btnContinue;
    public Label lblname;
    public TextField txtOtp;
    public Hyperlink hypResend;
    private FXMLLoader fxmlLoader;
    private String Fname;
    private String Lname;
    private String Gmail;
    private String password;
    private String otp;

    @FXML
    void initialize(){
        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), verifyPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        btnContinue.setDisable(true);
    }

    @FXML
    public void imgHomeOnAction(MouseEvent event) throws IOException {
        setUi("register_form.fxml");
        ((RegisterFormController)(fxmlLoader.getController())).setTextAgin(this.Fname,this.Lname,this.Gmail,this.password,this.NIC);
    }

    @FXML
    public void imgHomeMouseEnter(MouseEvent event) {
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
    public void imgHomeMouseExit(MouseEvent event) {
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
    public void btnContinueOnAction(ActionEvent actionEvent) throws IOException {
        Admin admin = new Admin(Fname,Lname,NIC,password,Gmail);

        if(txtOtp.getText().equals(otp)){
            try {
                boolean result= AdminModel.save(admin);
                if(result){
//                    System.exit(0);
                    setUi("Login_form.fxml");
                    new Alert(Alert.AlertType.CONFIRMATION,"Accunt Created :)").show();
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
                    Stage stage2= (Stage) btnContinue.getScene().getWindow();
                    stage2.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            txtOtp.setStyle("-fx-border-color: #3cff3c");
        }
        else {
            txtOtp.setStyle("-fx-border-color: #d70c0c");
            System.out.println("invalid OTP"+otp);
            new Alert(Alert.AlertType.WARNING,"Wrong OTP code Please Check This...:(").show();
        }

    }

    @FXML
    public void getGmail(String Fname,String Lname,String Gamil,String password,String NIC,String otp){
        this.Fname=Fname;
        this.Lname=Lname;
        this.Gmail=Gamil;
        this.password=password;
        this.otp=otp;
        this.NIC=NIC;

        lblname.setText("Hi , "+Fname);

    }

    @FXML
    private void setUi(String fileName) throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getResource("/view/"+fileName));
        Pane root = fxmlLoader.load();
        try {
            verifyPane.getChildren().clear();
            verifyPane.getChildren().setAll(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void txtOTPkeyTyperOnAction(KeyEvent keyEvent) {

        if (txtOtp.getText().length()>0 && txtOtp.getText().length()<=4){
            txtOtp.setStyle("-fx-border-color: green");
            btnContinue.setDisable(false);
        }
        else {
            txtOtp.setStyle("-fx-border-color: red");
            btnContinue.setDisable(true);
        }
    }

    @FXML
    public void hypResendOnAction(ActionEvent actionEvent) throws GeneralSecurityException, IOException, MessagingException {
        String OTP []= new String[4];// genarate OTP

        for (int i = 0; i < 4; i++) {
            Random r=new Random();
            OTP[i]=r.nextInt(9)+"";
        }
        otp=OTP[0]+OTP[1]+OTP[2]+OTP[3];

        String message="Hi ,congratulation "+this.Fname+" you have a new OTP..!"+"\n"+"\n"+otp;
        String title="Ikman Rental(PVT)";
        String gmail=this.Gmail;

        new SendText().sendMail(title,message,gmail);
    }
}
