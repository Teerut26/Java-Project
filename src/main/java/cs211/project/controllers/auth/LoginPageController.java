package cs211.project.controllers.auth;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginPageController {

    @FXML private TextField TextFieldUsername;
    @FXML private TextField TextFieldPassword;

    @FXML
    public void onLoginButtonClick() {
        try{
            FXRouter.goTo("event-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onSignUpButtonClick(){
        try{
            FXRouter.goTo("register-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
