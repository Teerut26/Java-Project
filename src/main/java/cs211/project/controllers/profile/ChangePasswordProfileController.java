package cs211.project.controllers.profile;

import cs211.project.models.User;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.UserFileListDatasource;
import cs211.project.utils.ComponentRegister;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ChangePasswordProfileController extends ComponentRegister {
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
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
    }


    @FXML
    public void onConfirmClick(){
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
    public void onGoBack(){
        try {
            FXRouter.goTo("my-profile-page",this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
