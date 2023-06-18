package applications;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.springframework.stereotype.Component;
import resources.StageMovement;

import java.io.IOException;

@Component
public class GuestResult extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {

        stage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/apps/guest_result.fxml"));

        ScrollPane root = loader.load();

        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/guest_result.css").toExternalForm());


        StageMovement.moveStage(stage, scene);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setTitle("Guest result");
        stage.setWidth(800);
        stage.setHeight(600);
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
        launch(args);
    }
}
