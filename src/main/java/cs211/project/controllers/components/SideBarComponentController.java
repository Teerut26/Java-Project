package cs211.project.controllers.components;

import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import javafx.fxml.FXML;

import java.io.IOException;

public class SideBarComponentController {
    RouteProvider routeProvider;

    public void setRouteProvider(RouteProvider routeProvider) {
        this.routeProvider = routeProvider;
    }

    @FXML
    public void goToEventList() {
        try {
            FXRouter.goTo("event-list", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToHistoryEvent() {
        try {
            FXRouter.goTo("event-history", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToHistoryTeam() {
        try {
            FXRouter.goTo("my-team", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToMyEvent() {
        try {
            FXRouter.goTo("my-event", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
