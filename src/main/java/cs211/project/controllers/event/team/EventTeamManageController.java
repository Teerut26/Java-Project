package cs211.project.controllers.event.team;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class EventTeamManageController {
    @FXML
    private VBox SideBarVBox;

    @FXML
    public void initialize() {
        this.loadSideBarComponent();
    }

    private void loadSideBarComponent() {
        FXMLLoader sideBarComponentLoader = new FXMLLoader(getClass().getResource("/cs211/project/components/SideBarComponent.fxml"));
        try {
            VBox navbarComponent = sideBarComponentLoader.load();
            SideBarVBox.getChildren().add(navbarComponent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
