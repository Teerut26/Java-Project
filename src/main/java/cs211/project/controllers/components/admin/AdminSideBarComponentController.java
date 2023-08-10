package cs211.project.controllers.components.admin;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

import java.io.IOException;

public class AdminSideBarComponentController {
    @FXML public void goToManageUser() {
        try {
            FXRouter.goTo("admin-manage-user");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML public void goToChangePassword() {
        try {
            FXRouter.goTo("admin-change-password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}