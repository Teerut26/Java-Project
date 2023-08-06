package cs211.project.controllers.auth;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.IOException;

public class LoginPageController {
    @FXML
    private Label welcomeText;

    @FXML
    public void onLoginButtonClick() {

        try{
            FXRouter.goTo("hello");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
