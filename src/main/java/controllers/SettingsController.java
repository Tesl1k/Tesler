package controllers;

import configs.Config;
import applications.Main;
import applications.Settings;
import configs.MainConfig;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import resources.BackFiguresMovement;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private AnchorPane anc;
    @FXML
    private ComboBox comboBox;
    @FXML
    private CheckBox fullScreenCheck;
    private Config config, config2;
    private AnnotationConfigApplicationContext context;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        config = new Config("src/main/resources/properties/figureDrow.properties");

        config2 = new Config("src/main/resources/properties/saving.properties");

        context = new AnnotationConfigApplicationContext(MainConfig.class);

        fullScreenCheck.setSelected(Boolean.parseBoolean(config2.get("fullScreen")));

        Settings.getStage().setFullScreen(Boolean.parseBoolean(config2.get("fullScreen")));

        BackFiguresMovement.drowing(anc, Color.web("#3e4ff5"));

        comboBox.setValue(config.get("figureType"));


        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                String type = config.get("figureType");
                config.set("figureType", comboBox.getValue().toString());
                BackFiguresMovement.drowing(anc, Color.web("#776be2"));
                config.set("figureType", type);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    public  void save() throws Exception {
        if(fullScreenCheck.isSelected()){
            config2.set("fullScreen", "true");
        }
        else{
            config2.set("fullScreen", "false");
        }
        config.set("figureType", comboBox.getValue().toString());
        Main main = context.getBean("main", Main.class);
        Stage stage = new Stage();
        main.start(stage);
        Settings.getStage().close();
    }

}
