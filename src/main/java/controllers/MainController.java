package controllers;

import applications.*;
import configs.Config;
import configs.MainConfig;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import resources.BackFiguresMovement;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    @FXML
    private AnchorPane anc;
    @FXML
    private VBox vBox;
    private Config config;
    private AnnotationConfigApplicationContext context;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        context = new AnnotationConfigApplicationContext(MainConfig.class);

        config = new Config("src/main/resources/properties/saving.properties");

        Main.getStage().setFullScreen(Boolean.parseBoolean(config.get("fullScreen")));

        BackFiguresMovement.drowing(anc, Color.web("#776be2"));

        if(Main.getStage().isFullScreen()){
            AnchorPane.setLeftAnchor(vBox, 200.);
            AnchorPane.setTopAnchor(vBox, 300.);
            vBox.setPrefWidth(220);
            vBox.setPrefHeight(400);
        }
        else {
            AnchorPane.setLeftAnchor(vBox, 100.);
            AnchorPane.setTopAnchor(vBox, 110.);
            vBox.setPrefWidth(200);
            vBox.setPrefHeight(350);
        }

    }

    public void logout() throws Exception {
        Start start = context.getBean("start", Start.class);
        Stage stage = new Stage();
        start.start(stage);
        Main.getStage().close();
    }

    public void settings() throws Exception {
        Settings settings = context.getBean("settings", Settings.class);;
        Stage stage = new Stage();
        settings.start(stage);
        Main.getStage().close();

    }

    public void testCreate() throws Exception {
        TestEditor testCreate = context.getBean("testEditor", TestEditor.class);
        Stage stage = new Stage();
        testCreate.start(stage);
        Main.getStage().close();
    }

    public void testPassing() throws Exception {
        TestSelection testSelection = context.getBean(TestSelection.class);
        Stage stage = new Stage();
        testSelection.start(stage);
        Main.getStage().close();
    }

    public void quit(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText(null);
        alert.setContentText("Вы действительно хотите закрыть прилжение?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Main.getStage().close();
        }
    }

    public void profile() throws IOException {
        Profile profile = context.getBean(Profile.class);
        Stage stage = new Stage();
        profile.start(stage);
        Main.getStage().close();
    }
}
