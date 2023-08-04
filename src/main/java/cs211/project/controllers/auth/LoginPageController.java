package cs211.project.controllers.auth;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoginPageController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to Login Page!");
    }
}
