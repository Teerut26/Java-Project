package cs211.project.controllers.myEvent;

import cs211.project.services.FXRouter;
import cs211.project.utils.ComponentRegister;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SetEventDetailController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
    }

    @FXML public void onBack() {
        try {
            FXRouter.goTo("my-event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
