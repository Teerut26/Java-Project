package cs211.project.utils;

import cs211.project.controllers.components.NavBarComponentController;
import cs211.project.controllers.components.SideBarComponentController;
import cs211.project.models.User;
import cs211.project.services.RouteProvider;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ComponentRegister {
    public void loadSideBarComponent(VBox SideBarVBoxRefferance, String pathSourceComponent, RouteProvider routeProvider) {
        FXMLLoader sideBarComponentLoader = new FXMLLoader(getClass().getResource("/cs211/project/components/" + pathSourceComponent));
        try {
            VBox sidebarComponent = sideBarComponentLoader.load();
            SideBarComponentController sideBarComponentController = sideBarComponentLoader.getController();
            sideBarComponentController.setRouteProvider(routeProvider);
            SideBarVBoxRefferance.getChildren().add(sidebarComponent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadNavBarComponent(HBox NavBarHBoxRefferance, String pathSourceComponent, RouteProvider routeProvider) {
        FXMLLoader navBarComponentLoader = new FXMLLoader(getClass().getResource("/cs211/project/components/" + pathSourceComponent));
        try {
            HBox navbarComponent = navBarComponentLoader.load();
            NavBarComponentController navBarComponentController = navBarComponentLoader.getController();
            navBarComponentController.setRouteProvider(routeProvider);
            NavBarHBoxRefferance.getChildren().add(navbarComponent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
