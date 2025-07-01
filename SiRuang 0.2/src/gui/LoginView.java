package gui;

import controller.LoginController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView extends VBox {
    public LoginView(Stage primaryStage) {
        setPadding(new Insets(20));
        setSpacing(10);

        Label title = new Label("Login SIRuang");
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        CheckBox showPasswordCheck = new CheckBox("Show Password");
        TextField showPasswordField = new TextField();
        Button loginBtn = new Button("Login");

        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");
        showPasswordField.setManaged(false);
        showPasswordField.setVisible(false);

        // Bind show password
        showPasswordField.textProperty().bindBidirectional(passwordField.textProperty());

        showPasswordCheck.setOnAction(e -> {
            boolean show = showPasswordCheck.isSelected();
            showPasswordField.setManaged(show);
            showPasswordField.setVisible(show);
            passwordField.setManaged(!show);
            passwordField.setVisible(!show);
        });

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            LoginController.login(username, password, primaryStage);
        });

        getChildren().addAll(title, usernameField, passwordField, showPasswordField, showPasswordCheck, loginBtn);
    }
}
