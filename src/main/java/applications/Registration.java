package applications;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.stereotype.Component;
import resources.StageMovement;

@Component
public class Registration extends Application {

    private static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/apps/registration.fxml"));

        AnchorPane root = loader.load();

        stage = primaryStage;

        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/registration.css").toExternalForm());

        StageMovement.moveStage(stage, scene);

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Registration");
        stage.setWidth(600);
        stage.setHeight(400);
        stage.setResizable(false);
        stage.show();

        FadeTransition ft = new FadeTransition(Duration.millis(450), root);
        ft.setFromValue(0.5);
        ft.setToValue(1.0);
        ft.play();



    }
    public static Stage getStage() {
        return stage;
    }
    public static void main(String[] args) {
        launch();
    }
}
