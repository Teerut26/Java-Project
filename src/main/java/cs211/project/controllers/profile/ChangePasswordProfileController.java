package cs211.project.controllers.profile;

import cs211.project.Main;
import cs211.project.models.User;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.SingletonStorage;
import cs211.project.services.datasource.UserFileListDatasource;
import cs211.project.utils.ComponentRegister;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ChangePasswordProfileController {

    @FXML
    BorderPane parentBorderPane;
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    private RouteProvider routeProvider;
    @FXML
    private TextField oldPassword;
    @FXML
    private TextField newPassword;
    @FXML
    private TextField confirmPassword;
    @FXML
    private Label wrongPassword;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();

        boolean isAdmin = SingletonStorage.getInstance().userSession.isAdmin();
        ComponentRegister cr = new ComponentRegister();

        if (isAdmin) {
            cr.loadSideBarComponentAdmin(SideBarVBox, "AdminSideBarComponent.fxml", this.routeProvider);
            cr.loadNavBarComponentAdmin(NavBarHBox, "AdminNavbarComponent.fxml", this.routeProvider);
        } else {
            cr.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
            cr.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
        }

        this.initializeThemeMode();
        this.initializeFont();
    }

    @FXML
    public void initializeThemeMode(){
        String className = Main.class.getName().replace('.', '/');
        String classJar = Main.class.getResource("/" + className + ".class").toString();
        Boolean isJarFile = classJar.startsWith("jar:");
        String pathDarkMode;
        String pathLightMode;
        if(isJarFile) {
            pathDarkMode = "/cs211/project/style/dark-mode.css";
            pathLightMode = "/cs211/project/style/light-mode.css";
        }else{
            pathDarkMode = "file:src/main/resources/cs211/project/style/dark-mode.css";
            pathLightMode = "file:src/main/resources/cs211/project/style/light-mode.css";
        }
        if (this.routeProvider.getUserSession().getThemeMode().equals("dark")){
            parentBorderPane.getStylesheets().remove(pathLightMode);
            parentBorderPane.getStylesheets().add(pathDarkMode);
        }else if (this.routeProvider.getUserSession().getThemeMode().equals("light")) {
            parentBorderPane.getStylesheets().remove(pathDarkMode);
            parentBorderPane.getStylesheets().add(pathLightMode);
        }
    }
    @FXML
    public void initializeFont() {
        String currentFont = this.routeProvider.getUserSession().getFont();
        clearFontStyle();
        if (currentFont.equals("font-style1")) {
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style1.css");
        } else if (currentFont.equals("font-style2")) {
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style2.css");
        } else if (currentFont.equals("font-style3")) {
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style3.css");
        }

    }

    @FXML
    public void clearFontStyle() {
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style1.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style2.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style3.css");
    }

    @FXML
    public void onConfirmClick() {
        UserFileListDatasource userFileListDatasource = new UserFileListDatasource();
        UserCollection userCollection = userFileListDatasource.readData();
        User user = userCollection.findByUsername(routeProvider.getUserSession().getUserName());
        if (user.validatePassword(oldPassword.getText())) {
            if (newPassword.getText().equals(confirmPassword.getText())) {
                user.changePassword(oldPassword.getText(), newPassword.getText(), confirmPassword.getText());
                user.setPassword(newPassword.getText());
                userFileListDatasource.writeData(userCollection);
                try {
                    FXRouter.goTo("my-profile-page");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                wrongPassword.setText("New password do not match");
            }
        } else {
            wrongPassword.setText("Old password is incorrect");
        }
    }

    @FXML
    public void onGoBack() {
        try {
            FXRouter.goTo("my-profile-page", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
