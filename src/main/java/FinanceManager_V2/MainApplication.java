package FinanceManager_V2;

import FinanceManager_V2.database.CategoryRepository;
import FinanceManager_V2.database.TransactionRepository;
import FinanceManager_V2.database.entity.Category;
import FinanceManager_V2.database.entity.Transaction;
import FinanceManager_V2.interface_controllers.AuthController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import tests.RandomDataGenerator;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class MainApplication extends Application {
    public static ConfigurableApplicationContext springContext;
    private Parent rootNode;
    private FXMLLoader fxmlLoader;

    @Autowired public TransactionRepository transactionRepository;

    @Autowired public CategoryRepository categoryRepository;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(MainApplication.class);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        fxmlLoader.setLocation(getClass().getResource("/fxml/authscene.fxml"));
        rootNode = fxmlLoader.load();
        ((AuthController)fxmlLoader.getController()).setUpLanguage();
        primaryStage.setTitle("Finance Manager");
        Scene scene = new Scene(rootNode, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        springContext.stop();
    }
}
