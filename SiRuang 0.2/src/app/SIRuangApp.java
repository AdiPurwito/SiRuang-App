// app/SIRuangApp.java
package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.LoginView;

public class SIRuangApp extends Application {
    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(new LoginView(stage), 400, 300));
        stage.setTitle("SIRuang - Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
