package cs211.project.controllers.myEvent;

import cs211.project.until.ComponentRegister;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class EditEventDetailFormController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
    }

}
