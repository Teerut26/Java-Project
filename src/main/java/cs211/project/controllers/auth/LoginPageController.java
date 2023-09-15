package cs211.project.controllers.auth;

import cs211.project.models.User;
import cs211.project.services.Authentication;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.UserFileListDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class LoginPageController {

    @FXML
    private TextField TextFieldUsername;
    @FXML
    private TextField TextFieldPassword;
    @FXML
    private Text TextError;

    @FXML
    public void initialize() {
        TextError.setVisible(false);
    }

    @FXML
    public void onLoginButtonClick() {
        UserFileListDatasource userFileListDatasource = new UserFileListDatasource();
        User user = userFileListDatasource.readData().findByUsername(TextFieldUsername.getText());

        if (user == null) {
            TextError.setVisible(true);
            TextError.setText("Username not found");
            return;
        }

        if (!user.validatePassword(TextFieldPassword.getText())) {
            TextError.setVisible(true);
            TextError.setText("Password is incorrect");
            return;
        }

        try {
            RouteProvider routeProvider = new RouteProvider();
            routeProvider.setUserSession(user);

            FXRouter.goTo("event-list", routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onSignUpButtonClick() {
        try {
            FXRouter.goTo("register-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
