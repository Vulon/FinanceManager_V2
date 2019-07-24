package FinanceManager_V2;

import FinanceManager_V2.Database.Repositories.CategoryRepository;
import FinanceManager_V2.Database.Repositories.TransactionRepository;
import FinanceManager_V2.Interface_controllers.AuthController;
import FinanceManager_V2.Interface_controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class MainApplication extends Application {
    public static ConfigurableApplicationContext springContext;


    private AuthController authController;
    private MainController mainController;

    @Autowired public TransactionRepository transactionRepository;

    @Autowired public CategoryRepository categoryRepository;

    public static void main(String[] args) {
        launch(args);
    }

    Stage primaryStage;
    Stage lastStage;

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(MainApplication.class);


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Finance Manager");

        startNewScene(AuthController.class);

        //primaryStage.show();
    }

    public void startNewScene(Class controllerClass){

        try{
            if(controllerClass.equals(AuthController.class)){
                if(lastStage != null)
                    lastStage.close();
                Stage stage = new Stage();
                stage.setTitle("Authentication");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setControllerFactory(springContext::getBean);
                fxmlLoader.setLocation(getClass().getResource("/fxml/authscene.fxml"));
                Parent authParent = fxmlLoader.load();
                authController = fxmlLoader.getController();
                authController.setUpLanguage();
                authController.setUpMainApp(this);
                Scene scene = new Scene(authParent, 800, 600);
                stage.setScene(scene);
                stage.show();
                lastStage = stage;
            }else if(controllerClass.equals(MainController.class)){
                lastStage.close();
                Stage stage = new Stage();
                stage.setTitle("Finance Manager");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setControllerFactory(springContext::getBean);
                fxmlLoader.setLocation(getClass().getResource("/fxml/mainscene.fxml"));
                Parent mainParent = fxmlLoader.load();
                mainController = fxmlLoader.getController();
                Scene scene = new Scene(mainParent, 800, 600);
                stage.setScene(scene);
                mainController.manualSetUp();
                stage.show();
                lastStage = stage;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        springContext.stop();
    }
}
