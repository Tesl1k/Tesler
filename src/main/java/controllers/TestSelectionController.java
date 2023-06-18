package controllers;

import DAO.ResultDAO;
import DAO.TestDAO;
import DAO.UserDAO;
import DAO.UserTestDAO;
import applications.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import configs.Config;
import configs.MainConfig;
import entitys.Result;
import entitys.Test;
import entitys.User;
import entitys.UserTest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import resources.BackFiguresMovement;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TestSelectionController implements Initializable {

    @FXML
    private AnchorPane anc;
    @FXML
    private ComboBox selectTest;
    private TestDAO testDAO;
    private UserDAO userDAO;
    private ResultDAO resultDAO;
    private UserTestDAO userTestDAO;
    private List<Result> results;
    private AnnotationConfigApplicationContext context;
    private Config config;
    private Test selectedTest, loaderTest;
    private List<Test> tests;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        context = new AnnotationConfigApplicationContext(MainConfig.class);
        results = new ArrayList<>();
        config = new Config("src/main/resources/properties/saving.properties");

        testDAO = context.getBean(TestDAO.class);

        tests = testDAO.getGuestTests();

        if(!Boolean.parseBoolean(config.get("isGuest"))){

            userDAO = context.getBean(UserDAO.class);
            resultDAO = context.getBean(ResultDAO.class);
            userTestDAO = context.getBean(UserTestDAO.class);

            for(Test test : userDAO.getUser(config.get("userEmail")).getTests()){

                selectTest.getItems().add(test.getTitle());

            }

            selectTest.setOnAction(event -> {

                for (Test test : userDAO.getUser(config.get("userEmail")).getTests()){
                    if(test.getTitle().equals(selectTest.getValue())){
                        selectedTest = test;
                    }
                }

                for (Result result : resultDAO.getResults()){
                    if(result.getTest().getId() == selectedTest.getId()){
                        results.add(result);
                    }
                }

            });

        }
        else {

            if(!tests.isEmpty()){

                for (Test test : tests){
                    selectTest.getItems().add(test.getTitle());
                }

            }

            selectTest.setOnAction(event -> {

                if(selectTest.getValue() != null){
                    selectedTest = testDAO.getGuestTest(selectTest.getValue().toString());
                }



            });

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

        if(!Boolean.parseBoolean(config.get("isGuest"))){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText(null);
            alert.setContentText("Вы действительно хотите удалить тест?");

            Optional<ButtonType> alertResult = alert.showAndWait();

            if (alertResult.isPresent() && alertResult.get() == ButtonType.OK) {
                if(selectedTest != null){

                    for (Result result : results){
                        resultDAO.deleteResult(result);
                    }

                    testDAO.deleteTest(selectedTest);

                    for (UserTest userTest : selectedTest.getUserTests()) {
                        userTest.setUser(null);
                    }

                    selectedTest.clearUsers();

                    userDAO.getUser(config.get("userEmail")).removeTest(selectedTest);

                    selectTest.getItems().remove(selectedTest.getTitle());

                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Успех");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Тест \"" + selectedTest.getTitle() + "\" удален");
                    alert1.showAndWait();
                }
            }

        }
        else if(Boolean.parseBoolean(config.get("isGuest"))){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText(null);
            alert.setContentText("Вы действительно хотите удалить тест?");

            Optional<ButtonType> alertResult = alert.showAndWait();

            if (alertResult.isPresent() && alertResult.get() == ButtonType.OK) {
                if(selectedTest != null){

                    tests.removeIf(test -> test.getTitle().equals(selectTest.getValue()));
                    selectTest.getItems().remove(selectedTest.getTitle());

                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Успех");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Тест \"" + selectedTest.getTitle() + "\" удален");
                    alert1.showAndWait();
                }
            }

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Вы не можете удалить тест");
            alert.showAndWait();
        }

    }

    public void saveTest() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(selectedTest);

        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";

        File folder = new File(desktopPath, "Tests");

        if (!folder.exists()) {
            try {
                folder.mkdirs();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

        }


        String fileName = selectedTest.getTitle() + ".json";
        String filePath = folder.getAbsolutePath() + File.separator + fileName;

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8)) {

            writer.write(json);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Успех");
            alert.setHeaderText(null);
            alert.setContentText("Тест \"" + selectedTest.getTitle() + "\" сохранён");
            alert.showAndWait();

        } catch (IOException e) {
            throw new Exception(e);
        }

    }

    public void loadTest() throws IOException {

        Gson gson = new Gson();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите тесты");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "*.json"));

        File selectedFile = fileChooser.showOpenDialog(TestSelection.getStage());

        if (selectedFile != null) {

            String json = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())), StandardCharsets.UTF_8);

            loaderTest = gson.fromJson(json, Test.class);

            if(Boolean.parseBoolean(config.get("isGuest"))){
                if (!tests.isEmpty()){

                    Test testToAdd;

                    List<String> titles = new ArrayList<>();

                    for(Test test : userDAO.getUser(config.get("userEmail")).getTests()){
                        titles.add(test.getTitle());
                    }

                    if (!titles.contains(loaderTest.getTitle())){

                        testToAdd = loaderTest;
                        selectTest.getItems().add(loaderTest.getTitle());

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Успех");
                        alert.setHeaderText(null);
                        alert.setContentText("Тест \"" + loaderTest.getTitle() + "\" добавлен");
                        alert.showAndWait();

                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText(null);
                        alert.setContentText("Тест \"" + loaderTest.getTitle() + "\" уже добавлен");
                        alert.showAndWait();
                        return;
                    }

                    if (testToAdd != null){
                        tests.add(testToAdd);
                    }


                }
                else {
                    tests.add(loaderTest);
                    selectTest.getItems().add(loaderTest.getTitle());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Успех");
                    alert.setHeaderText(null);
                    alert.setContentText("Тест \"" + loaderTest.getTitle() + "\" добавен");
                    alert.showAndWait();

                }

            }
            else {

                List<String> titlesTest = new ArrayList<>();

                for(Test test : testDAO.getTests()){
                    titlesTest.add(test.getTitle());
                }

                if (!userDAO.getUser(config.get("userEmail")).getTests().isEmpty()){

                    List<String> titlesUserTest = new ArrayList<>();

                    for(Test test : userDAO.getUser(config.get("userEmail")).getTests()){
                        titlesUserTest.add(test.getTitle());
                    }

                    if (!titlesUserTest.contains(loaderTest.getTitle())){

                        User user = userDAO.getUser(config.get("userEmail"));
                        if(!titlesTest.contains(loaderTest.getTitle())){
                            testDAO.addTest(loaderTest);
                        }
                        else {
                            loaderTest = testDAO.getTest(loaderTest.getTitle());
                        }

                        UserTest userTest = new UserTest(user, loaderTest);
                        userTestDAO.addUserTest(userTest);
                        user.setUserTests(new HashSet<>(userTestDAO.getUserTests()));
                        userDAO.update(user);
                        selectTest.getItems().add(loaderTest.getTitle());

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Успех");
                        alert.setHeaderText(null);
                        alert.setContentText("Тест \"" + loaderTest.getTitle() + "\" добавлен");
                        alert.showAndWait();

                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText(null);
                        alert.setContentText("Тест \"" + loaderTest.getTitle() + "\" уже добавлен");
                        alert.showAndWait();
                    }

                }
                else {
                    User user = userDAO.getUser(config.get("userEmail"));
                    if(!titlesTest.contains(loaderTest.getTitle())){
                        testDAO.addTest(loaderTest);
                    }
                    else {
                        loaderTest = testDAO.getTest(loaderTest.getTitle());
                    }

                    UserTest userTest = new UserTest(user, loaderTest);
                    userTestDAO.addUserTest(userTest);
                    user.setUserTests(new HashSet<>(userTestDAO.getUserTests()));
                    userDAO.update(user);
                    selectTest.getItems().add(loaderTest.getTitle());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Успех");
                    alert.setHeaderText(null);
                    alert.setContentText("Тест \"" + loaderTest.getTitle() + "\" добавен");
                    alert.showAndWait();

                }


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
