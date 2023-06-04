package applications;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.springframework.stereotype.Component;
import resources.StageMovement;

import java.io.IOException;

@Component
public class TestSelection extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {

        stage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/apps/test_selection.fxml"));

        AnchorPane root = loader.load();

        stage = primaryStage;

        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/test_selection.css").toExternalForm());

        StageMovement.moveStage(stage, scene);

        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Test Selection");
        stage.setWidth(600);
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
