package cs211.project.controllers.event;

import cs211.project.services.FXRouter;
import cs211.project.utils.ComponentRegister;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class EventListController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml");
    }

    @FXML
    public void onGotoDetail() {
        try {
            FXRouter.goTo("event-detail");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
