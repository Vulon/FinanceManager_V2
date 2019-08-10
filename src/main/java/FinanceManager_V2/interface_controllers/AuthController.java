package FinanceManager_V2.Interface_controllers;

import FinanceManager_V2.MainApplication;
import FinanceManager_V2.Services.AuthenticationService;
import FinanceManager_V2.Services.CachedActionsManager;
import FinanceManager_V2.Services.Lang;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class AuthController {
    MainApplication mainApplication;

    @FXML    public BorderPane main_pane;
    @FXML    StackPane stack_pane;
    @FXML    AnchorPane register_pane;
    @FXML    AnchorPane login_pane;

    @FXML    Label register_label;
    @FXML    TextField register_email_textfield;
    @FXML    PasswordField register_password_textfield;
    @FXML    PasswordField register_repeatpassword_textfield;
    @FXML    Button register_button;

    @FXML    Label login_label;
    @FXML    TextField login_email_textfield;
    @FXML    PasswordField login_password_textfield;
    @FXML    Button login_button;

    @FXML    Button toggle_button;

    @FXML    ChoiceBox lang_picker;
    @FXML    Label info_label;

    public void setUpMainApp(MainApplication mainApp){
        mainApplication = mainApp;
//        setUpStylesheets();
    }
//
//    public void setUpStylesheets(){
//        login_button.setId("button-main");
//        register_button.setId("button-main");
//    }



    @FXML public void handle_login(){
        info_label.setVisible(true);
        if(!isCorrectEmail(login_email_textfield.getText())){
            info_label.setText(lang.getTextLine(Lang.TextLine.email_incorrect));
            return;
        }
        if(!isCorrectPassword(login_password_textfield.getText())){
            info_label.setText(lang.getTextLine(Lang.TextLine.password_incorrect));
            return;
        }
        info_label.setText(lang.getTextLine(Lang.TextLine.connecting_server));

        AuthenticationService.ServerResponseCode code = authenticationService.attemptLogin(login_email_textfield.getText(),
                login_password_textfield.getText());

        if(code == AuthenticationService.ServerResponseCode.USER_NOT_FOUND){
            info_label.setText(lang.getTextLine(Lang.TextLine.user_not_found));
            return;
        }
        if(code == AuthenticationService.ServerResponseCode.EMAIL_NOT_VERIFIED){ //Email not verified
            info_label.setText(lang.getTextLine(Lang.TextLine.email_not_verified));
            return;
        }
        if(code == AuthenticationService.ServerResponseCode.OK){
            //SUCCESS
            info_label.setText("SUCCESSFUL");
            cachedActionsManager.processUpdates();
            mainApplication.startNewScene(MainController.class);

            return;
        }
        info_label.setText(lang.getTextLine(Lang.TextLine.server_error));
    }
    @FXML public void handle_register(){
        info_label.setVisible(true);
        if(!isCorrectEmail(register_email_textfield.getText())){
            info_label.setVisible(true);
            info_label.setText(lang.getTextLine(Lang.TextLine.email_incorrect));
            return;
        }
        if(!isCorrectPassword(register_password_textfield.getText())){
            info_label.setText(lang.getTextLine(Lang.TextLine.password_incorrect));
            return;
        }
        if(!register_password_textfield.getText().equals(register_repeatpassword_textfield.getText())){
            info_label.setText(lang.getTextLine(Lang.TextLine.passwords_mismatch));
            return;
        }
        info_label.setText(lang.getTextLine(Lang.TextLine.connecting_server));
        AuthenticationService.ServerResponseCode code = authenticationService.attemptRegister(register_email_textfield.getText(), register_password_textfield.getText());
        if(code == AuthenticationService.ServerResponseCode.ALREADY_REGISTERED){
            info_label.setText(lang.getTextLine(Lang.TextLine.already_registered));
        }else if(code == AuthenticationService.ServerResponseCode.OK){
            info_label.setText(lang.getTextLine(Lang.TextLine.check_your_email));
            toggle_login();
        }else{
            info_label.setText(lang.getTextLine(Lang.TextLine.server_error));
        }
    }
    Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");
    Pattern passwordPattern = Pattern.compile("^[a-zA-Z0-9]+[0-9]+[a-zA-Z0-9]*$");

    private boolean isCorrectEmail(String email){
        return email.length() > 5 && emailPattern.matcher(email).find();
    }
    private boolean isCorrectPassword(String password){
        return password.length() > 5 && passwordPattern.matcher(password).find();
    }

    private Lang lang;
    private AuthenticationService authenticationService;
    private CachedActionsManager cachedActionsManager;

    public AuthController(Lang lang, AuthenticationService serverConnectionService, CachedActionsManager cachedActionsManager) {
        this.lang = lang;
        this.authenticationService = serverConnectionService;
        this.cachedActionsManager = cachedActionsManager;
    }

    ObservableList<Lang.Language> languageObservableList = null;

    public void setUpLanguage(){

        register_label.setText(lang.getTextLine(Lang.TextLine.register));
        register_email_textfield.setPromptText(lang.getTextLine(Lang.TextLine.email));
        register_password_textfield.setPromptText(lang.getTextLine(Lang.TextLine.password));
        register_repeatpassword_textfield.setPromptText(lang.getTextLine(Lang.TextLine.repeat_password));
        register_button.setText(lang.getTextLine(Lang.TextLine.register));

        login_label.setText(lang.getTextLine(Lang.TextLine.login));
        login_email_textfield.setPromptText(lang.getTextLine(Lang.TextLine.email));
        login_password_textfield.setPromptText(lang.getTextLine(Lang.TextLine.password));
        login_button.setText(lang.getTextLine(Lang.TextLine.login));

        toggle_button.setText(lang.getTextLine(Lang.TextLine.dont_have_account));
        register_pane.setVisible(false);
        login_pane.setVisible(true);

        if(languageObservableList == null){
            languageObservableList = FXCollections.observableArrayList();
            Lang.Language[] languages = Lang.Language.values();
            for(int i = 0; i < languages.length; i++){
                languageObservableList.add(languages[i]);
            }
            lang_picker.setItems(languageObservableList);
            lang_picker.getSelectionModel().select(Lang.Language.ENGLISH.ordinal());
            lang_picker.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("Selected new: " + newValue.toString());
                lang.setLanguage((Lang.Language)lang_picker.getItems().get(newValue.intValue()));
                setUpLanguage();
            });
        }
    }

    @FXML private void toggle_login(){
        info_label.setVisible(false);
        if(register_pane.isVisible()){
            register_pane.setVisible(false);
            login_pane.setVisible(true);
            toggle_button.setText(lang.getTextLine(Lang.TextLine.dont_have_account));
        }else{
            register_pane.setVisible(true);
            login_pane.setVisible(false);
            toggle_button.setText(lang.getTextLine(Lang.TextLine.have_account));
        }

    }


}
