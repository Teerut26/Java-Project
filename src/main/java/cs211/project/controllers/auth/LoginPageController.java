package cs211.project.controllers.auth;

import cs211.project.models.User;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.SingletonStorage;
import cs211.project.services.datasource.UserFileListDatasource;
import javafx.application.HostServices;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;
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
    private UserFileListDatasource userFileListDatasource;
    private UserCollection userCollection;

    @FXML
    public void initialize() {
        userFileListDatasource = new UserFileListDatasource();
        userCollection = userFileListDatasource.readData();

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
                openPDF();
            }
        });
    }

    public void openPDF() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            fileChooser.setInitialFileName("manual.pdf");

            File tempFile = fileChooser.showSaveDialog(null);

            if (tempFile != null) {
                InputStream is = getClass().getResourceAsStream("/cs211/project/assets/manual.pdf");
                if (is != null) {
                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }
                    is.close();
                    URI fileUri = Paths.get(tempFile.getAbsolutePath()).toUri();
                    HostServices hostServices = SingletonStorage.getInstance().getHostServices();
                    hostServices.showDocument(fileUri.toString());
                } else {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    @FXML
    public void onLoginButtonClick() {
        User user = userCollection.findByUsername(TextFieldUsername.getText());

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
                this.updateLastLogin(user);
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
        UserCollection newUserCollection = new UserCollection();
        for (User userTemp : this.userCollection.getUsers()) {
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
