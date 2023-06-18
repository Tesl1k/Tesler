package controllers;

import DAO.ResultDAO;
import DAO.TestDAO;
import DAO.UserDAO;
import applications.GuestResult;
import applications.Main;
import applications.TestPassing;
import com.fasterxml.jackson.core.JsonProcessingException;
import configs.Config;
import configs.MainConfig;
import entitys.Result;
import entitys.Test;
import entitys.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.URL;
import java.util.*;

public class TestPassingController implements Initializable {
    @FXML
    private Label title, description;
    @FXML
    private VBox vb;
    @FXML
    private ScrollPane sp;
    private AnnotationConfigApplicationContext context;
    private TestDAO testDAO;
    private ResultDAO resultDAO;
    private UserDAO userDAO;
    private Config config;
    private List<Test> tests;
    private Test test;
    private User user;
    private Map<String, List<String>> questions, answers;
    private static Map<String, List<String>> results;
    private String questionType;
    private List<String> questionsList, selectionAnswers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        questionsList = new ArrayList<>();
        selectionAnswers = new ArrayList<>();
        results = new HashMap<>();

        context = new AnnotationConfigApplicationContext(MainConfig.class);
        config = new Config("src/main/resources/properties/saving.properties");
        testDAO = context.getBean(TestDAO.class);
        userDAO = context.getBean(UserDAO.class);
        resultDAO = context.getBean(ResultDAO.class);


        TestPassing.getStage().setFullScreen(Boolean.parseBoolean(config.get("fullScreen")));

        vb.prefWidthProperty().bind(sp.widthProperty());
        vb.prefHeightProperty().bind(sp.heightProperty());

        if(!Boolean.parseBoolean(config.get("isGuest"))){
            tests = testDAO.getTests();
        }
        else {
            tests = testDAO.getGuestTests();

        }

        for (Test test : tests){
            if(test.getTitle().equals(config.get("testTitle"))){
                this.test = test;
                if (!Boolean.parseBoolean(config.get("isGuest"))){
                    user = userDAO.getUser(config.get("userEmail"));
                }
            }
        }

        try {
            questions = test.getQuestions();
            answers = test.getAnswers();
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        title.setText(test.getTitle());
        description.setText(test.getDescription());

        try {
            passing();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void passing() throws Exception {

        VBox vBox = new VBox();

        List<ToggleGroup> toggles = new ArrayList<>();
        List<TextArea> areas = new ArrayList<>();
        List<String> toggleQuestion = new ArrayList<>(), areaQuestion = new ArrayList<>();
        List<String> toggleRightAnswer = new ArrayList<>(), areaRightAnswer = new ArrayList<>();

        vBox.setPadding(new Insets(0, 10, 10, 10));

        List<Control> controlList = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : questions.entrySet()) {
            questionType = entry.getKey();
            questionsList = entry.getValue();

            if(questionType.equals("Множественный выбор")){

                for (String question : questionsList) {

                    selectionAnswers = answers.get(question);

                    Label label = new Label(question);
                    toggleQuestion.add(question);
                    HBox hBox1 = new HBox();
                    HBox hBox2 = new HBox();

                    ToggleGroup group = new ToggleGroup();

                    toggles.add(group);

                    RadioButton radioButton = new RadioButton();

                    for (int i = 0; i < selectionAnswers.size(); i++){

                        if(selectionAnswers.get(i).contains("+")){
                            toggleRightAnswer.add(selectionAnswers.get(i).replace("+", ""));
                        }

                        RadioButton rb = new RadioButton(selectionAnswers.get(i).replace("+", ""));
                        rb.setToggleGroup(group);

                        radioButton = rb;

                        if(i < 2){
                            hBox1.getChildren().add(rb);
                        }
                        else{
                            hBox2.getChildren().add(rb);
                        }

                        HBox.setMargin(rb, new Insets(0, 10, 10, 10));
                    }

                    vBox.getChildren().add(label);
                    vBox.getChildren().add(hBox1);
                    vBox.getChildren().add(hBox2);

                    VBox.setMargin(label, new Insets(0, 10, 10, 10));

                    controlList.add(radioButton);

                }

            }
            else if(questionType.equals("Ввод свободного ответа")){

                for (String question : questionsList) {

                    Label label = new Label(question);
                    areaQuestion.add(question);

                    TextArea textArea = new TextArea();

                    areas.add(textArea);

                    areaRightAnswer.add(answers.get(question).get(0));

                    vBox.getChildren().add(label);
                    vBox.getChildren().add(textArea);

                    VBox.setMargin(label, new Insets(0, 10, 10, 10));
                    VBox.setMargin(textArea, new Insets(0, 10, 10, 10));

                    controlList.add(textArea);

                }

            }

        }

        vb.getChildren().add(vBox);
        Button save = new Button("Завершить");
        vb.getChildren().add(save);
        save.setOnAction(event -> {

            for (Control control : controlList) {
                if (control instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) control;
                    if (radioButton.getToggleGroup().getSelectedToggle() == null) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText(null);
                        alert.setContentText("Вы ответили не на все вопросы");
                        alert.showAndWait();
                        return;
                    }

                } else if (control instanceof TextArea) {
                    TextArea textAreaControl = (TextArea) control;
                    if (textAreaControl.getText().trim().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText(null);
                        alert.setContentText("Вы ответили не на все вопросы");
                        alert.showAndWait();
                        return;
                    }

                }
            }

            for (int i = 0; i < toggleQuestion.size(); i++){

                List<String> list = new ArrayList<>();

                RadioButton selectedRadioButton = (RadioButton) toggles.get(i).getSelectedToggle();
                if (selectedRadioButton != null) {
                    list.add(selectedRadioButton.getText());
                    list.add(toggleRightAnswer.get(i));

                    results.put(toggleQuestion.get(i), list);
                }

            }

            for (int i = 0; i < areaQuestion.size(); i++){

                List<String> list = new ArrayList<>();

                TextArea textArea = areas.get(i);
                if (textArea != null) {
                    list.add(textArea.getText());
                    list.add(areaRightAnswer.get(i));

                    results.put(areaQuestion.get(i), list);
                }

            }


            if(!Boolean.parseBoolean(config.get("isGuest"))){

                try {
                    Result result = new Result(user, test, results);
                    resultDAO.addResult(result);
                }
                catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                Main main = context.getBean(Main.class);
                Stage stage = new Stage();

                try {
                    main.start(stage);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                GuestResult guestResult = context.getBean(GuestResult.class);
                Stage stage = new Stage();

                try {
                    guestResult.start(stage);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Спасибо");
            alert.setHeaderText(null);
            alert.setContentText("Тест \"" + title.getText() + "\" пройден");
            alert.showAndWait();

            TestPassing.getStage().close();

        });

    }

    public void onCancel() throws Exception{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText(null);
        alert.setContentText("Вы действительно хотите закрыть окно?");

        quitTestEditor(alert);
    }

    private void quitTestEditor(Alert alert) throws Exception {
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Main main = context.getBean(Main.class);
            Stage stage = new Stage();
            main.start(stage);
            TestPassing.getStage().close();
        }
    }

    public static Map<String, List<String>> getResults(){
        return results;
    }

}
