package view;

import controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView extends VBox {
    public LoginView(Stage primaryStage) {
        setPadding(new Insets(30));
        setSpacing(15);
        setAlignment(Pos.CENTER);

        Label title = new Label("Login SIRuang");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: montserrat");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);

        TextField showPasswordField = new TextField();
        showPasswordField.setManaged(false);
        showPasswordField.setVisible(false);
        showPasswordField.setMaxWidth(250);

        CheckBox showPasswordCheck = new CheckBox("Tampilkan Password");

        // Bind password
        showPasswordField.textProperty().bindBidirectional(passwordField.textProperty());

        showPasswordCheck.setOnAction(e -> {
            boolean show = showPasswordCheck.isSelected();
            showPasswordField.setVisible(show);
            showPasswordField.setManaged(show);
            passwordField.setVisible(!show);
            passwordField.setManaged(!show);
        });

        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(100);
        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = showPasswordCheck.isSelected() ? showPasswordField.getText() : passwordField.getText();
            LoginController.login(username, password, primaryStage);
        });

        getChildren().addAll(
                title,
                usernameField,
                passwordField,
                showPasswordField,
                showPasswordCheck,
                loginBtn
        );
    }
}
