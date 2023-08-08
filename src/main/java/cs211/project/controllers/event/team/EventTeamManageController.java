package cs211.project.controllers.event.team;

import cs211.project.until.ComponentRegister;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class EventTeamManageController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
    }
}
