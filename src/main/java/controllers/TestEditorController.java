package controllers;

import DAO.TestDAO;
import DAO.UserDAO;
import applications.Main;
import applications.TestEditor;
import configs.Config;
import configs.MainConfig;
import entitys.Test;
import entitys.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;

public class TestEditorController {
    @FXML
    private TextField titleField,  questionField, varA, varB, varC, varD;
    @FXML
    private TextArea descriptionField, freePut;
    @FXML
    private ComboBox<String> questionTypeBox, rightAnswer;
    @FXML
    private ListView<String> questionList;
    @FXML
    private Button deleteButton;
    @FXML
    private VBox setVar;
    @FXML
    private HBox hb;
    private Map<String, List<String>> questions, answers;
    private ObservableList<String> observableList;
    private AnnotationConfigApplicationContext context;
    private TestDAO testDAO;
    private UserDAO userDAO;
    private Config config;

    public void initialize() {

        questions = new HashMap<>();
        answers = new HashMap<>();
        observableList = FXCollections.observableArrayList();

        context = new AnnotationConfigApplicationContext(MainConfig.class);
        config = new Config("src/main/resources/properties/saving.properties");

        testDAO = context.getBean("testDAO", TestDAO.class);
        userDAO = context.getBean("userDAO", UserDAO.class);

        questionList.setItems(observableList);
        questionList.setEditable(true);
        questionList.setCellFactory(TextFieldListCell.forListView());
        deleteButton.setDisable(true);

        questionList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    deleteButton.setDisable(newValue == null);
                }
        );

        questionList.setOnEditCommit(
                event -> {
                    int index = event.getIndex();
                    observableList.set(index, event.getNewValue());
                }
        );

        questionTypeBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (questionTypeBox.getValue() != null){
                if (questionTypeBox.getValue().equals("Множественный выбор")){
                    freePut.setVisible(false);
                    varA.clear();
                    varB.clear();
                    varC.clear();
                    varD.clear();
                    setVar.setVisible(true);
                    hb.setVisible(true);
                }
                else if(questionTypeBox.getValue().equals("Ввод свободного ответа")){
                    setVar.setVisible(false);
                    hb.setVisible(false);
                    freePut.clear();
                    freePut.setVisible(true);
                }
            }
        });
    }

    @FXML
    private void addQuestion() {

        String questionType = questionTypeBox.getValue();
        String question = questionField.getText();

        List<String> answersL = new ArrayList<>();
        List<String> questionL = new ArrayList<>();

        if (questionType != null && !questionType.isEmpty() && question != null && !question.isEmpty()) {

            List<String> list = new ArrayList<>();

            list.add(varA.getText());
            list.add(varB.getText());
            list.add(varC.getText());
            list.add(varD.getText());

            if(setVar.isVisible()){

                if(rightAnswer.getValue() != null && !rightAnswer.getValue().isEmpty()){

                    if (rightAnswer.getValue().equals("А")){
                        list.set(0, list.get(0) + "+");
                    }
                    else if(rightAnswer.getValue().equals("Б")){
                        list.set(1, list.get(1) + "+");
                    }
                    else if(rightAnswer.getValue().equals("В")){
                        list.set(2, list.get(2) + "+");
                    }
                    else if(rightAnswer.getValue().equals("Г")){
                        list.set(3, list.get(3) + "+");
                    }


                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null);
                    alert.setContentText("Не выбран правильный вариант ответа");
                    alert.showAndWait();
                    return;
                }

                for(String ans : list){
                    if (ans != null && !ans.isEmpty()) {
                        answersL.add(ans);
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText(null);
                        alert.setContentText("Заполните все варианты ответов");
                        alert.showAndWait();
                        return;
                    }
                }

            }


            if (freePut.isVisible()){

                if (freePut != null && !freePut.getText().isEmpty()){
                    answersL.add(freePut.getText());
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null);
                    alert.setContentText("Введите ответ");
                    alert.showAndWait();
                    return;
                }
            }

            if(questions.get(questionType) != null && !questions.get(questionType).isEmpty()){
                questionL.addAll(questions.get(questionType));
            }

            questionL.add(question);
            questions.put(questionType, questionL);
            answers.put(question, answersL);
            observableList.add(questionType + ": " + question);

            questionTypeBox.getSelectionModel().clearSelection();
            questionField.clear();
            varA.clear();
            varB.clear();
            varC.clear();
            varD.clear();
            freePut.clear();

            questionList.scrollTo(observableList.size() - 1);
        }
    }

    @FXML
    private void deleteQuestion() {
        int index = questionList.getSelectionModel().getSelectedIndex();

        if (index >= 0) {
            questions.remove(questionTypeBox.getValue());
            observableList.remove(index);
        }
    }

    @FXML
    private void onSave() throws Exception {
        String title = titleField.getText();
        String description = descriptionField.getText();

        if (title.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Введите название теста");
            alert.showAndWait();
            return;
        }

        if (observableList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Добавьте хотя бы один вопрос");
            alert.showAndWait();
            return;
        }

        User user = userDAO.getUser(config.get("userEmail"));
        Test test = new Test(title, description, questions, answers, user);
        testDAO.addTest(test);


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText(null);
        alert.setContentText("Тест \"" + title + "\" сохранен");

        quitTestEditor(alert);
    }

    @FXML
    private void onCancel() throws Exception{
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
            TestEditor.getStage().close();
        }
    }

}
