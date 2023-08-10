package cs211.project.controllers.myEvent;

import cs211.project.services.FXRouter;
import cs211.project.utils.ComponentRegister;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MyEventViewController extends ComponentRegister {

    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml");
    }

    @FXML public void goToCreateEvent() {
        try {
            FXRouter.goTo("create-event-detail-form");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML public void onSetEventDetailForm() {
        try {
            FXRouter.goTo("set-event-detail");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
