package cs211.project.controllers.components.admin;

import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import javafx.fxml.FXML;

import java.io.IOException;

public class AdminSideBarComponentController {
    RouteProvider routeProvider;

    public void setRouteProvider(RouteProvider routeProvider) {
        this.routeProvider = routeProvider;
    }
    @FXML public void goToManageUser() {
        try {
            FXRouter.goTo("admin-manage-user", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
