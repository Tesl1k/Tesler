package controllers;

import applications.GuestResult;
import applications.Main;
import com.fasterxml.jackson.core.JsonProcessingException;
import configs.MainConfig;
import entitys.Result;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class GuestResultController implements Initializable {

    @FXML
    private VBox vb;
    private AnnotationConfigApplicationContext context;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        vb.setPadding(new Insets(10, 10, 10, 10));

        context = new AnnotationConfigApplicationContext(MainConfig.class);

        Map<String, List<String>> res;

        res = TestPassingController.getResults();

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
            myAnswer.setPrefHeight(20);

            Label rightAnswer = new Label("Правильный ответ: " + entry.getValue().get(1));
            rightAnswer.setTextFill(Color.WHITE);

            VBox.setMargin(question, new Insets(0, 0, 8, 0));
            VBox.setMargin(myAnswer, new Insets(0, 0, 8, 0));
            VBox.setMargin(rightAnswer, new Insets(0, 0, 25, 0));

            vb.getChildren().add(question);
            vb.getChildren().add(myAnswer);
            vb.getChildren().add(rightAnswer);

        }

        Button close = new Button("Закрыть");
        vb.getChildren().add(close);
        VBox.setMargin(close, new Insets(0, 0, 0, 350));
        close.setOnAction(event -> {

            Main main = context.getBean(Main.class);
            Stage stage = new Stage();
            try {
                main.start(stage);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            GuestResult.getStage().close();

        });
    }
}
