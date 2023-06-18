package controllers;

import configs.Config;
import DAO.UserDAO;
import applications.Login;
import applications.Main;
import applications.Start;
import configs.MainConfig;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import resources.BackFiguresMovement;
import resources.InternetConnection;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField textEmail, textPassword;
    @FXML
    private Label isMiss;
    @FXML
    private CheckBox cbSave;
    private Config config;
    @FXML
    private AnchorPane anc;
    private UserDAO userDAO;
    private AnnotationConfigApplicationContext context;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        config = new Config("src/main/resources/properties/saving.properties");

        context = new AnnotationConfigApplicationContext(MainConfig.class);

        userDAO = context.getBean("userDAO", UserDAO.class);

        isMiss.setVisible(false);
        textEmail.setText(config.get("userEmail"));
        textPassword.setText(config.get("userPassword"));
        cbSave.setSelected(Boolean.parseBoolean(config.get("isUserChecked")));

        BackFiguresMovement.drowing(anc, Color.web("#3e4ff5"));

    }

    public void login() throws Exception {

        if(InternetConnection.isInternetAvailable()){

            if(userDAO.isUserNotMissing(textEmail.getText(), textPassword.getText())){

                config.set("userEmail", textEmail.getText());
                config.set("isUserChecked", String.valueOf(cbSave.isSelected()));

                if (cbSave.isSelected()) {
                    config.set("userPassword", textPassword.getText());
                } else {
                    config.set("userPassword", "");
                }

                config.set("isGuest", "false");
                Main main = context.getBean("main", Main.class);
                Stage stage = new Stage();
                main.start(stage);
                Start.getStage().close();
                Login.getStage().close();
            }
            else {

                if(!isMiss.isVisible()){
                    isMiss.setVisible(true);
                }
                textEmail.setStyle("-fx-border-color: red");
                textPassword.clear();
                textPassword.setStyle("-fx-border-color: red");

            }

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Нет подключения к интернету");
            alert.showAndWait();
        }



    }

    public void loginLikeGuest() throws Exception {

        config.set("isGuest", "true");
        config.set("isUserChecked", "false");
        config.set("userPassword", "");
        config.set("userEmail", "");

        Main main = context.getBean("main", Main.class);
        Stage stage = new Stage();
        main.start(stage);
        Start.getStage().close();
        Login.getStage().close();




    }
}
