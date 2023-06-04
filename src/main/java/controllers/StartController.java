package controllers;

import DAO.ResultDAO;
import DAO.TestDAO;
import configs.Config;
import configs.MainConfig;
import DAO.UserDAO;
import applications.Login;
import applications.Registration;
import applications.Start;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import resources.BackFiguresMovement;
import resources.StageMovement;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class StartController implements Initializable{

    @FXML
    private Button buttonFS, buttonOFS;
    private UserDAO userDAO;
    private TestDAO testDAO;
    private ResultDAO resultDAO;
    @FXML
    private AnchorPane anc;
    private Config config;
    @FXML
    private Label titleLabel;
    @FXML
    private ImageView img;
    private AnnotationConfigApplicationContext context;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        context = new AnnotationConfigApplicationContext(MainConfig.class);

        userDAO = context.getBean(UserDAO.class);
        testDAO = context.getBean(TestDAO.class);
        resultDAO = context.getBean(ResultDAO.class);

        userDAO.connection();
        userDAO.close();
        testDAO.connection();
        testDAO.close();
        resultDAO.connection();
        resultDAO.close();


        config = new Config("src/main/resources/properties/saving.properties");

        Start.getStage().setFullScreen(Boolean.parseBoolean(config.get("fullScreen")));

        BackFiguresMovement.drowing(anc, Color.web("#776be2"));

        if(Start.getStage().isFullScreen()){
            AnchorPane.setLeftAnchor(titleLabel, 1100.);
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            img.setTranslateX(270);
            img.setFitWidth(350);
            img.setFitHeight(400);
        }
        else {
            AnchorPane.setLeftAnchor(titleLabel, 500.);
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        }

    }

    public void fullScreen(){
        Stage stage = Start.getStage();
        stage.setFullScreen(true);
        buttonFS.setVisible(false);
        buttonOFS.setVisible(true);
        BackFiguresMovement.drowing(anc, Color.web("#776be2"));
        AnchorPane.setLeftAnchor(titleLabel, 1100.);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        img.setTranslateX(250);
        img.setTranslateY(-150);
        img.setFitWidth(350);
        img.setFitHeight(400);
        stage.getScene().setOnMouseDragged(null);

    }

    public void outFullScreen(){
        Stage stage = Start.getStage();
        stage.setFullScreen(false);
        buttonFS.setVisible(true);
        buttonOFS.setVisible(false);
        BackFiguresMovement.drowing(anc, Color.web("#776be2"));
        AnchorPane.setLeftAnchor(titleLabel, 500.);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        img.setTranslateX(0);
        img.setTranslateY(0);
        img.setFitWidth(220);
        img.setFitHeight(220);
        StageMovement.moveStage(stage, stage.getScene());
    }

    public void openMyVk(){
        try {
            new ProcessBuilder("C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe", "https://vk.com/tesl1k4k").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login() throws Exception {

        Login login = context.getBean("login", Login.class);
        Stage stage = new Stage();
        login.start(stage);

    }

    public void registration() throws Exception {

        Registration registration = context.getBean("registration", Registration.class);
        Stage stage = new Stage();
        registration.start(stage);

    }

    public void quit(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText(null);
        alert.setContentText("Вы действительно хотите закрыть прилжение?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Start.getStage().close();
        }

    }

}
