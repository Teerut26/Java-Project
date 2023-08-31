package cs211.project.controllers.components;

import cs211.project.services.Authentication;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;

public class NavBarComponentController {

    @FXML private Text username;

    @FXML
    public void initialize() {
        username.setText(Authentication.currentUser.getUserName());
    }

    @FXML public void goToProfile(){
        try {
            FXRouter.goTo("my-profile-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML public void goToLogin(){
        try {
            FXRouter.goTo("login-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
