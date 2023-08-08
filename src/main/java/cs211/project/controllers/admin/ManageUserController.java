package cs211.project.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ManageUserController {
    @FXML
    private VBox SideBarVBox;

    @FXML
    private void initialize() {
        loadSideBarComponent();
    }

    private void loadSideBarComponent() {
        FXMLLoader sideBarComponentLoader = new FXMLLoader(getClass().getResource("/cs211/project/components/admin/AdminSideBarComponent.fxml"));
        try {
            VBox navbarComponent = sideBarComponentLoader.load();
            SideBarVBox.getChildren().add(navbarComponent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
