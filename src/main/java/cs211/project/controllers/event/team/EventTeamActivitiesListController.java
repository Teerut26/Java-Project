package cs211.project.controllers.event.team;

import cs211.project.utils.ComponentRegister;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class EventTeamActivitiesListController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
    }
}
