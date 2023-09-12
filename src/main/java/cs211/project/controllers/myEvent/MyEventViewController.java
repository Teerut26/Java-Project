package cs211.project.controllers.myEvent;

import cs211.project.controllers.components.EventCardComponentController;
import cs211.project.models.Event;
import cs211.project.services.FXRouter;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.utils.ComponentLoader;
import cs211.project.utils.ComponentRegister;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyEventViewController extends ComponentRegister {

    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    @FXML
    private ScrollPane eventListScrollPane;

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml");
        eventFileListDatesource = new EventFileListDatesource();

    }

    @FXML public void goToCreateEvent() {
        try {
            FXRouter.goTo("create-event-detail-form");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
