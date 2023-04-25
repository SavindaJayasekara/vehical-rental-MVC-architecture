import lk.ijse.ikmanRental.controller.AdmineditFormController;
import lk.ijse.ikmanRental.controller.DashBordController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppInitializer extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("view/Login_form.fxml"))));
        primaryStage.getIcons().add(new Image("assets/ikmanicon.png"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("ikman Rental System");
        primaryStage.show();
        primaryStage.centerOnScreen();

        primaryStage.setOnCloseRequest(wv->{  // closed use lambda exception
            if (AdmineditFormController.open){
                DashBordController.primaryStage.close();
            }
        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}
