package cs211.project.controllers.admin;

import cs211.project.until.ComponentRegister;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ManageUserController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;

    @FXML
    private void initialize() {
        this.loadSideBarComponent(SideBarVBox, "admin/AdminSideBarComponent.fxml");
    }
}
