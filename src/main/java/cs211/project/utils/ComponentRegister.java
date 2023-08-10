package cs211.project.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ComponentRegister {
    public void loadSideBarComponent(VBox SideBarVBoxRefferance, String pathSourceComponent) {
        FXMLLoader sideBarComponentLoader = new FXMLLoader(getClass().getResource("/cs211/project/components/" + pathSourceComponent));
        try {
            VBox sidebarComponent = sideBarComponentLoader.load();
            SideBarVBoxRefferance.getChildren().add(sidebarComponent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadNavBarComponent(HBox NavBarHBoxRefferance, String pathSourceComponent) {
        FXMLLoader navBarComponentLoader = new FXMLLoader(getClass().getResource("/cs211/project/components/" + pathSourceComponent));
        try {
            HBox navbarComponent = navBarComponentLoader.load();
            NavBarHBoxRefferance.getChildren().add(navbarComponent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
