package cs211.project.router;

import javafx.application.Application;
import javafx.stage.Stage;
import cs211.project.services.FXRouter;

import java.io.IOException;

public class Router extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        configRoute();

        FXRouter.bind(this, stage, "CS211 661 Project");
        FXRouter.goTo("login-page");
    }

    private static void configRoute() {
        String resourcesPath = "cs211/project/views/";
        FXRouter.when("hello", resourcesPath + "hello-view.fxml");

        //auth routes
        FXRouter.when("login-page", resourcesPath + "auth/login-page.fxml");
        FXRouter.when("register-page", resourcesPath + "auth/register-page.fxml");

        //admin routes
        FXRouter.when("admin-change-password", resourcesPath + "admin/change-password-admin.fxml");
        FXRouter.when("admin-manage-user", resourcesPath + "admin/manage-user.fxml");

        //event routes
        FXRouter.when("event-list", resourcesPath + "event/event-list.fxml");
        FXRouter.when("event-detail", resourcesPath + "event/event-detail.fxml");
        FXRouter.when("event-detail-joined", resourcesPath + "event/join-event.fxml");
        FXRouter.when("event-team-manage", resourcesPath + "event/team/event-team-manage.fxml");
        FXRouter.when("event-team-activities-list", resourcesPath + "event/team/event-team-activities-list.fxml");
        FXRouter.when("event-team-detail", resourcesPath + "event/team/event-team-detail.fxml");
        FXRouter.when("event-team-list", resourcesPath + "event/team/event-team-list.fxml");
        FXRouter.when("event-history", resourcesPath + "eventHistory/in-process-and-ended.fxml");

        //my-event routes
        FXRouter.when("create-event-detail-form", resourcesPath + "myEvent/create-event-detail-form.fxml");
        FXRouter.when("create-team", resourcesPath + "myEvent/create-team.fxml");
        FXRouter.when("edit-team", resourcesPath + "myEvent/edit-team.fxml");
        FXRouter.when("edit-event-detail-form", resourcesPath + "myEvent/edit-event-detail-form.fxml");
        FXRouter.when("edit-participant", resourcesPath + "myEvent/edit-participant.fxml");
        FXRouter.when("my-event", resourcesPath + "myEvent/my-event-view.fxml");
        FXRouter.when("set-event-detail", resourcesPath + "myEvent/set-event-detail.fxml");


        //profile routes
        FXRouter.when("change-password-profile-page", resourcesPath + "profile/change-password-profile-page.fxml");
        FXRouter.when("my-profile-page", resourcesPath + "profile/my-profile-page.fxml");

        //schedule routes
        FXRouter.when("add-schedule-participant", resourcesPath + "Schedule/add-schedule-participant.fxml");
        FXRouter.when("add-schedule-team", resourcesPath + "Schedule/add-schedule-team.fxml");
        FXRouter.when("edit-schedule-participant", resourcesPath + "Schedule/edit-schedule-participant.fxml");
        FXRouter.when("edit-schedule-team", resourcesPath + "Schedule/edit-schedule-team.fxml");

        //about us routes
        FXRouter.when("about-us", resourcesPath + "about-us.fxml");

        //comment
        FXRouter.when("comment-activity-event", resourcesPath + "event/comment-activity-event.fxml");
        FXRouter.when("comment-activity-team", resourcesPath + "event/team/comment-activity-team.fxml");

        //setting
        FXRouter.when("setting-page", resourcesPath + "setting-page.fxml");

    }


    public static void main(String[] args) {
        launch();
    }
}