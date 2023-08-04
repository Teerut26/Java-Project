package cs211.project.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ChangePasswordController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to Login Page!");
    }
}
