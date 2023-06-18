package resources;

import DAO.ResultDAO;
import DAO.TestDAO;
import DAO.UserDAO;
import DAO.UserTestDAO;
import applications.Start;
import configs.MainConfig;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.net.InetAddress;

public class AppStarter {

    private static AnnotationConfigApplicationContext context;

    private static UserDAO userDAO;
    private static TestDAO testDAO;
    private static ResultDAO resultDAO;
    private static UserTestDAO userTestDAO;

    public static void main(String[] args) throws Exception {

        context = new AnnotationConfigApplicationContext(MainConfig.class);

        userDAO = context.getBean(UserDAO.class);
        testDAO = context.getBean(TestDAO.class);
        resultDAO = context.getBean(ResultDAO.class);
        userTestDAO = context.getBean(UserTestDAO.class);

        if(InternetConnection.isInternetAvailable()){
            userDAO.check();
            testDAO.check();
            resultDAO.check();
            userTestDAO.check();
        }

        Platform.startup(() -> {
            Start start = context.getBean("start", Start.class);
            Stage stage = new Stage();
            try {
                start.start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });



    }

}
