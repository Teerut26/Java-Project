package cs211.project.controllers.auth;

import cs211.project.models.User;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.Authentication;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.SingletonStorage;
import cs211.project.services.datasource.UserFileListDatasource;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDateTime;

public class LoginPageController {

    @FXML
    private TextField TextFieldUsername;
    @FXML
    private PasswordField TextFieldPassword;
    @FXML
    private Text TextError;
    @FXML
    private HBox aboutUsHbox;
    @FXML
    private HBox documentHbox;

    @FXML
    public void initialize() {
        TextError.setVisible(false);

        aboutUsHbox.addEventHandler(MouseEvent.MOUSE_CLICKED,new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    FXRouter.goTo("about-us");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        documentHbox.addEventHandler(MouseEvent.MOUSE_CLICKED,new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    FXRouter.goTo("about-us");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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

            if (user.isAdmin()) {
                FXRouter.goTo("admin-manage-user", routeProvider);
                return;
            }
            this.updateLastLogin(user);
            FXRouter.goTo("event-list", routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateLastLogin(User user) {
        UserFileListDatasource userFileListDatasource = new UserFileListDatasource();
        UserCollection userCollection = userFileListDatasource.readData();

        UserCollection newUserCollection = new UserCollection();
        for (User userTemp : userCollection.getUsers()) {
            if (userTemp.getId().equals(user.getId())) {
                userTemp.setLastLogin(LocalDateTime.now());
            }
            newUserCollection.add(userTemp);
        }

        userFileListDatasource.writeData(newUserCollection);

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
