package controllers;

import DAO.ResultDAO;
import DAO.TestDAO;
import DAO.UserDAO;
import applications.Main;
import applications.Start;
import applications.TestPassing;
import applications.TestSelection;
import configs.Config;
import configs.MainConfig;
import entitys.Result;
import entitys.Test;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import resources.BackFiguresMovement;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TestSelectionController implements Initializable {

    @FXML
    private AnchorPane anc;
    @FXML
    private ComboBox selectTest;
    private TestDAO testDAO;
    private UserDAO userDAO;
    private ResultDAO resultDAO;
    private List<Result> results;
    private AnnotationConfigApplicationContext context;
    private Config config;
    private Test test;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        context = new AnnotationConfigApplicationContext(MainConfig.class);
        testDAO = context.getBean(TestDAO.class);
        userDAO = context.getBean(UserDAO.class);
        resultDAO = context.getBean(ResultDAO.class);
        results = new ArrayList<>();
        config = new Config("src/main/resources/properties/saving.properties");


        for(Test test : testDAO.getTests()){
            if(test.getUser().getId() == userDAO.getUser(config.get("userEmail")).getId()){
                selectTest.getItems().add(test.getTitle());
                this.test = test;
            }
        }


        for (Result result : resultDAO.getResults()){
            if(result.getTest().getId() == test.getId()){
                results.add(result);
            }
        }

        BackFiguresMovement.drowing(anc, Color.GRAY);

    }

    public void testPassed() throws Exception {

        if(selectTest.getValue() != null){

            config.set("testTitle", selectTest.getValue().toString());

            TestPassing testPassing = context.getBean(TestPassing.class);
            Stage stage = new Stage();
            testPassing.start(stage);
            TestSelection.getStage().close();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Выберите тест");
            alert.showAndWait();
        }
    }

    public void deleteTest(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText(null);
        alert.setContentText("Вы действительно хотите удалить тест?" + test.getTitle());

        Optional<ButtonType> alertResult = alert.showAndWait();

        if (alertResult.isPresent() && alertResult.get() == ButtonType.OK) {
            if(test != null){

                for (Result result : results){
                    resultDAO.deleteResult(result);
                }
                testDAO.deleteTest(test);
                selectTest.getItems().remove(test.getTitle());

                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Успех");
                alert1.setHeaderText(null);
                alert1.setContentText("Тест \"" + test.getTitle() + "\" удален");
                alert1.showAndWait();
            }
        }



    }

    public void back() throws Exception {
        Main main = context.getBean(Main.class);
        Stage stage = new Stage();
        main.start(stage);
        TestSelection.getStage().close();
    }
}
