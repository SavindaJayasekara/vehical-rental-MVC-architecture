package lk.ijse.ikmanRental.controller;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;

public class DashBordController {

    public static boolean open;

    static int click;

    public static Stage primaryStage;

    @FXML
    private ImageView imgbtnHome;
    @FXML
    private AnchorPane manePane;
    @FXML
    private ImageView imgbtnHome1;
    @FXML
    private ImageView imgbtnHome2;
    @FXML
    private ImageView imgbtnHome21;
    @FXML
    private ImageView imgTranslte;
    @FXML
    private ImageView imgbtnHome3;
    @FXML
    private ImageView imgCustomer;

    private FXMLLoader fxmlLoader;

    @FXML
    void initialize(){

        open=true;
        try {
            setUi("home_form.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        imgTranslte.setVisible(true);
        TranslateTransition transition=new TranslateTransition();
        transition.setNode(imgTranslte);
        transition.setDuration(Duration.millis(8000));
        transition.setByX(1200);
        transition.play();
    }

    public void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof javafx.scene.image.ImageView) {
            javafx.scene.image.ImageView icon = (ImageView) event.getSource();

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
//            glow.setColor(Color.valueOf("#EF233C"));
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

    public void homeClicked() {
        try {
            setUi("home_form.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUi(String fileName) throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getResource("/view/"+fileName));
        Pane root = fxmlLoader.load();
        try {
            manePane.getChildren().clear();
            manePane.getChildren().setAll(root);
//            TranslateTransition tt = new TranslateTransition(Duration.millis(350), root);
//            tt.setFromX(-root.getWidth());
//            tt.setToX(0);
//            tt.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void driverklicked(MouseEvent event) {
        try {
            setUi("driver_form.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void vehicleclicked(MouseEvent event) {
        try {
            setUi("vehicle_form.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bookingclicked(MouseEvent event) {
        try {
            setUi("booking_form.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void detailClicked(MouseEvent event) {
        click++;
        if (click == 1){
            primaryStage=new Stage();
            try {
                primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/adminedit_form.fxml"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            primaryStage.getIcons().add(new Image("assets/ikmanicon.png"));
            primaryStage.setResizable(false);
            primaryStage.setTitle("Profile settings !");
            primaryStage.show();
            primaryStage.centerOnScreen();

            primaryStage.setOnCloseRequest(we -> click=0); // use lambda exception
        }
    }

    public void customerClicked(MouseEvent event) {
        try {
            setUi("customer_form.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
