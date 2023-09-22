package cs211.project.controllers.admin;

import cs211.project.models.User;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.UserFileListDatasource;
import cs211.project.utils.ComponentRegister;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ChangePasswordAdminController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    private RouteProvider routeProvider;

    @FXML
    private TextField confirmPasswordTextField;

    @FXML
    private TextField newPasswordTextField;

    @FXML
    private TextField oldPasswordTextField;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        this.loadSideBarComponentAdmin(SideBarVBox, "AdminSideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponentAdmin(NavBarHBox, "AdminNavbarComponent.fxml", this.routeProvider);
    }
    @FXML
    void onComfirm(ActionEvent event) {
        UserFileListDatasource userFileListDatasource = new UserFileListDatasource();
        UserCollection userCollection = userFileListDatasource.readData();
        User user = userCollection.findByUsername(routeProvider.getUserSession().getUserName());
        if (user.validatePassword(oldPasswordTextField.getText())) {
            if (newPasswordTextField.getText().equals(confirmPasswordTextField.getText())) {
                user.changePassword(oldPasswordTextField.getText(), newPasswordTextField.getText(), confirmPasswordTextField.getText());
                user.setPassword(newPasswordTextField.getText());
                userFileListDatasource.writeData(userCollection);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password changed successfully.", ButtonType.OK);
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "New password do not match", ButtonType.OK);
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Old password is incorrect", ButtonType.OK);
            alert.show();
        }
    }

}
