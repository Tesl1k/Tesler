package controllers;

import DAO.UserDAO;
import applications.Registration;
import configs.MainConfig;
import entitys.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import resources.BackFiguresMovement;
import resources.InternetConnection;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class RegistrationController implements Initializable {
    @FXML
    private TextField textName, textSurname, textEmail;
    @FXML
    private PasswordField textPassword;
    @FXML
    private Label l1, l2;
    @FXML
    private AnchorPane anc;
    private UserDAO userDAO;
    private AnnotationConfigApplicationContext context;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        context = new AnnotationConfigApplicationContext(MainConfig.class);

        userDAO = context.getBean("userDAO", UserDAO.class);

        Tooltip tooltip1 = new Tooltip("Пример почты: kolyaivanov123@mail.ru");
        Tooltip tooltip2 = new Tooltip("Пароль должен содержать: a-z A-Z 0-9");


        tooltip1.setShowDelay(Duration.millis(50));
        tooltip2.setShowDelay(Duration.millis(50));

        l1.setTooltip(tooltip1);
        l2.setTooltip(tooltip2);

        BackFiguresMovement.drowing(anc, Color.web("#776be2"));

    }

    public void registration(){

        if(InternetConnection.isInternetAvailable()){

            if(Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", textEmail.getText())
                    && PASSWORD_PATTERN.matcher(textPassword.getText()).matches()
                    && Pattern.matches("^[а-яёА-ЯЁ]+$", textName.getText())
                    && Pattern.matches("^[а-яёА-ЯЁ]+$", textSurname.getText()))
            {
                userDAO.addUser(new User(textName.getText(), textSurname.getText(), textEmail.getText(), textPassword.getText()));
                Registration.getStage().close();
            }
            else {

                TextField[] array = new TextField[]{textSurname, textName, textEmail, textPassword};

                for (int i = 0; i < array.length; i++){
                    if(array[i].getText().equals("")){
                        array[i].setStyle("-fx-border-color: red");
                    }
                }

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

}
