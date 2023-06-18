package applications;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.springframework.stereotype.Component;
import resources.StageMovement;

@Component
public class TestPassing extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        stage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/apps/test_passing.fxml"));

        ScrollPane root = loader.load();

        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/test_passing.css").toExternalForm());

        if(!stage.isFullScreen()){
            StageMovement.moveStage(stage, scene);
        }

        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Test passing");
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
        launch();
    }
}
