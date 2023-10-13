package cs211.project.controllers.components.admin;

import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;

public class AdminNavbarComponentController {
    @FXML
    private Circle userProfileCircle;
    @FXML
    private Text username;
    RouteProvider<String> routeProvider;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
    }

    public void setRouteProvider(RouteProvider routeProvider) {
        this.routeProvider = routeProvider;
        this.setMetaData();
    }

    public void setMetaData() {
        this.username.setText(this.routeProvider.getUserSession().getUserName());
        if (routeProvider.getUserSession().getImageProfile().equals("null")) {
            Image img = new Image(getClass().getResource("/cs211/project/assets/image/auth/user.png").toExternalForm());
            userProfileCircle.setFill(new ImagePattern(img));
        } else {
            Image img = new Image("file:" + routeProvider.getUserSession().getImageProfile());
            userProfileCircle.setFill(new ImagePattern(img));
        }
    }

    @FXML
    void goToLogin() {
        try {
            FXRouter.goTo("login-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void goToProfile() {
        this.routeProvider.setData("admin");
        try {
            FXRouter.goTo("my-profile-page", routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
