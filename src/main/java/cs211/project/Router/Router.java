package cs211.project.Router;

import javafx.application.Application;
import javafx.stage.Stage;
import cs211.project.services.FXRouter;

import java.io.IOException;

public class Router extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        configRoute();

        FXRouter.bind(this, stage, "CS211 661 Project");
        FXRouter.goTo("dashborad-user");
    }

    private static void configRoute() {
        String resourcesPath = "cs211/project/views/";
        FXRouter.when("hello", resourcesPath + "hello-view.fxml");

        //auth routes
        FXRouter.when("login-page", resourcesPath + "auth/login-page.fxml");
        FXRouter.when("register-page", resourcesPath + "auth/register-page.fxml");

        //admin routes
        FXRouter.when("admin-change-password", resourcesPath + "admin/change-password.fxml");
        FXRouter.when("admin-manage-user", resourcesPath + "admin/manage-user.fxml");

        //user routes
        FXRouter.when("dashborad-user", resourcesPath + "dashborad-user.fxml");
    }


    public static void main(String[] args) {
        launch();
    }
}