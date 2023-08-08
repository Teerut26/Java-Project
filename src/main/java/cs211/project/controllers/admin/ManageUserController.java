package cs211.project.controllers.admin;

import cs211.project.utils.ComponentRegister;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ManageUserController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;

    @FXML
    private void initialize() {
        this.loadSideBarComponent(SideBarVBox, "admin/AdminSideBarComponent.fxml");
    }
}
