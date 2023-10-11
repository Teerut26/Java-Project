package cs211.project.controllers.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import cs211.project.models.User;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.datasource.UserFileListDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class RegisterPageController {

    @FXML
    private TextField TextFieldName;
    @FXML
    private TextField TextFieldUsername;
    @FXML
    private PasswordField TextFieldPassword;
    @FXML
    private PasswordField TextFieldPasswordConfirm;
    @FXML
    private Text TextError;

    @FXML
    public void initialize() {
        TextError.setVisible(false);
    }

    @FXML
    private void BacktoLoginButton() {
        try {
            FXRouter.goTo("login-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void GetStartButton() {

        UserFileListDatasource userFileListDatasource = new UserFileListDatasource();
        User user = userFileListDatasource.readData().findByUsername(TextFieldUsername.getText());

        if (user != null) {
            TextError.setVisible(true);
            TextError.setText("Username already exist");
            return;
        }

        if (!TextFieldPassword.getText().equals(TextFieldPasswordConfirm.getText())) {
            TextError.setVisible(true);
            TextError.setText("Password is not match");
            return;
        }

        LocalDateTime localDateTime = LocalDateTime.now();

        User newUser = new User(
                UUID.randomUUID().toString(),
                TextFieldName.getText(),
                TextFieldUsername.getText(),
                BCrypt.withDefaults().hashToString(12,  TextFieldPassword.getText().toCharArray()),
                localDateTime
        );

        UserCollection userOldData = userFileListDatasource.readData();
        userOldData.add(newUser);
        userFileListDatasource.writeData(userOldData);

        try {
            FXRouter.goTo("login-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
