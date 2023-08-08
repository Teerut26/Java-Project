package cs211.project.controllers.admin;

import cs211.project.until.ComponentRegister;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ChangePasswordAdminController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "admin/AdminSideBarComponent.fxml");
    }

}
