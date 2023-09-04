package cs211.project.controllers.components;

import cs211.project.services.Authentication;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;

public class NavBarComponentController {

    @FXML private Text username;
    @FXML private Circle userProfileCircle;

    @FXML
    public void initialize() {
        Image img = new Image(getClass().getResource("/cs211/project/assets/image/auth/user.png").toExternalForm());
        userProfileCircle.setFill(new ImagePattern(img));
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
