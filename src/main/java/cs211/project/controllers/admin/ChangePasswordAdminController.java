package cs211.project.controllers.admin;

import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.utils.ComponentRegister;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ChangePasswordAdminController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;
    private RouteProvider routeProvider;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "admin/AdminSideBarComponent.fxml", this.routeProvider);
    }

}
