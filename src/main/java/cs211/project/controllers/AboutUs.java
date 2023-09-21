package cs211.project.controllers;

import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.utils.ComponentRegister;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AboutUs {

    @FXML
    public void initialize() {

    }

    @FXML
    void onBack(ActionEvent event) {
        try {
            FXRouter.goTo("login-page");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
