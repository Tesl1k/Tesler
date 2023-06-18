package controllers;

import DAO.ResultDAO;
import DAO.TestDAO;
import DAO.UserDAO;
import applications.Main;
import applications.Profile;
import applications.Start;
import com.fasterxml.jackson.core.JsonProcessingException;
import configs.Config;
import configs.MainConfig;
import entitys.Result;
import entitys.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import resources.BackFiguresMovement;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ProfileController implements Initializable {

    private AnnotationConfigApplicationContext context;
    private Config config;
    @FXML
    private AnchorPane anc;
    @FXML
    private ImageView avatar;
    @FXML
    private Label surname, name;
    @FXML
    private VBox vb, avatarVB, passTestVB, infoVB, resultVB;
    @FXML
    private ListView<String> passTests;
    @FXML
    private ScrollPane sc;
    private UserDAO userDAO;
    private ResultDAO resultDAO;
    private TestDAO testDAO;
    private List<Result> results;
    private User user;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        context = new AnnotationConfigApplicationContext(MainConfig.class);

        config = new Config("src/main/resources/properties/saving.properties");

        sc.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        BackFiguresMovement.drowing(anc, Color.web("#1cdc99"));
        Profile.getStage().setFullScreen(Boolean.parseBoolean(config.get("fullScreen")));

        if(Profile.getStage().isFullScreen()){
            resultVB.setPrefWidth(600);
            resultVB.setPrefHeight(700);
            passTestVB.setPrefWidth(400);
            passTestVB.setPrefHeight(450);
            AnchorPane.setLeftAnchor(passTestVB, 200.);
            AnchorPane.setBottomAnchor(passTestVB, 150.);
            sc.setPrefHeight(500);
            vb.setPrefHeight(500);
            vb.setPrefWidth(600);
        }
        else {
            resultVB.setPrefWidth(350);
            resultVB.setPrefHeight(320);
            AnchorPane.setLeftAnchor(passTestVB, 35.);
            AnchorPane.setBottomAnchor(passTestVB, 8.);
            sc.setPrefHeight(270);
            vb.setPrefHeight(270);
            vb.setPrefWidth(415);
        }

        userDAO = context.getBean(UserDAO.class);
        resultDAO = context.getBean(ResultDAO.class);
        testDAO = context.getBean(TestDAO.class);

        results = new ArrayList<>();

        for(Result result : resultDAO.getResults()){
            if(result.getUser().getEmail().equals(config.get("userEmail"))){
                results.add(result);
            }
        }


        user = userDAO.getUser(config.get("userEmail"));

        surname.setText(user.getSurname());
        name.setText(user.getName());
        if(user.getAvatar() != null){
            avatar.setImage(user.getAvatar());
        }

        List<Integer> ids = new ArrayList<>();

        for (Result result : results){

            if (!ids.contains(result.getTest().getId())) {
                ids.add(result.getTest().getId());
            }

        }

        for (int id : ids){

            passTests.getItems().add(testDAO.getTest(id).getTitle());

        }

        passTests.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {

                    vb.getChildren().clear();

                    int id = testDAO.getTest(newValue).getId();

                    int i = 1;

                    for (Result result : results){

                        if(result.getTest().getId() == id){

                            VBox vBox = new VBox();
                            vBox.setPadding(new Insets(10, 10, 10, 10));

                            Map<String, List<String>> res;

                            try {
                                res = result.getResult();
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }

                            Label attempt = new Label("Попытка №" + i);
                            attempt.setTextFill(Color.WHITE);
                            if(!Profile.getStage().isFullScreen()){
                                VBox.setMargin(attempt, new Insets(0, 10, 10, 130));
                            }
                            else {
                                VBox.setMargin(attempt, new Insets(0, 10, 10, 250));
                            }
                            vBox.getChildren().add(attempt);

                            for (Map.Entry<String, List<String>> entry : res.entrySet()){



                                Label question = new Label("Вопрос: " + entry.getKey());
                                question.setTextFill(Color.WHITE);

                                Label myAnswer = new Label();

                                Text answerPrefix = new Text("Ваш ответ: ");
                                Text answerText = new Text(entry.getValue().get(0));

                                answerPrefix.setFill(Color.WHITE);

                                if (entry.getValue().get(0).equals(entry.getValue().get(1))) {
                                    answerText.setFill(Color.LIGHTGREEN);
                                } else {
                                    answerText.setFill(Color.web("#e17575"));
                                }

                                TextFlow textFlow = new TextFlow(answerPrefix, answerText);
                                myAnswer.setGraphic(textFlow);

                                Label rightAnswer = new Label("Правильный ответ: " + entry.getValue().get(1));
                                rightAnswer.setTextFill(Color.WHITE);

                                VBox.setMargin(rightAnswer, new Insets(0, 0, 5, 0));

                                vBox.getChildren().add(question);
                                vBox.getChildren().add(myAnswer);
                                vBox.getChildren().add(rightAnswer);

                            }

                            i++;
                            vb.getChildren().add(vBox);

                        }

                    }

                }
        );



    }
    public void back() throws Exception {
        Main main = context.getBean(Main.class);
        Stage stage = new Stage();
        main.start(stage);
        Profile.getStage().close();
    }
    public void setAvatar() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Изображение (*.png, *.jpg, *.gif)", "*.png", "*.jpg", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        File selectedFile = fileChooser.showOpenDialog(Profile.getStage());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());

            avatar.setImage(image);
            User user = userDAO.getUser(config.get("userEmail"));
            user.setAvatar(image);
            userDAO.update(user);

        }

    }

}
