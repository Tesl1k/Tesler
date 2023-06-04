package applications;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.springframework.stereotype.Component;
import resources.StageMovement;

@Component
public class Main extends Application {

    private static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {

        stage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/apps/main.fxml"));

        AnchorPane root = loader.load();

        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

        if(!stage.isFullScreen()){
            StageMovement.moveStage(stage, scene);
        }

        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setTitle("Main");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.setFullScreenExitHint("");
        stage.show();

        FadeTransition ft = new FadeTransition(Duration.millis(450), root);
        ft.setFromValue(0.5);
        ft.setToValue(1.0);
        ft.play();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getStage() {
        return stage;
    }
}
