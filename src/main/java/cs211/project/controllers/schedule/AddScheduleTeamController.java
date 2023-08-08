package cs211.project.controllers.schedule;

import cs211.project.utils.ComponentRegister;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class AddScheduleTeamController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
    }
}
