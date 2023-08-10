package cs211.project.controllers.components;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

import java.io.IOException;

public class SideBarComponentController {
    @FXML
    public void goToEventList() {
        try {
            FXRouter.goTo("event-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML public void goToHistoryEvent() {
        try {
            FXRouter.goTo("event-history");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML public void goToMyEvent() {
        try {
            FXRouter.goTo("my-event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
