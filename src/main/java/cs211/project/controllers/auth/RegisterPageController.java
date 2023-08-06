package cs211.project.controllers.auth;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class RegisterPageController {
    @FXML
    private Label welcomeText;

    @FXML
    private void BacktoLoginButton(){
        try{
            FXRouter.goTo("login-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void GetStartButton(){
        try{
            FXRouter.goTo("hello");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
