module cs211.project.cs211661project {
    requires javafx.controls;
    requires javafx.fxml;

    opens cs211.project.Router to javafx.fxml;
    exports cs211.project.Router;
    exports cs211.project.controllers;
    opens cs211.project.controllers to javafx.fxml;

    exports cs211.project.controllers.auth;
    opens cs211.project.controllers.auth to javafx.fxml;

    exports cs211.project.controllers.admin;
    opens cs211.project.controllers.admin to javafx.fxml;

    exports cs211.project.controllers.myEvent;
    opens cs211.project.controllers.myEvent to javafx.fxml;
    exports cs211.project.controllers.event;
    opens cs211.project.controllers.event to javafx.fxml;

    exports cs211.project.controllers.event.team;
    opens cs211.project.controllers.event.team to javafx.fxml;

    exports cs211.project.controllers.components.admin;
    opens cs211.project.controllers.components.admin to javafx.fxml;

    exports cs211.project.controllers.components;
    opens cs211.project.controllers.components to javafx.fxml;

    exports cs211.project.controllers.eventHistory;
    opens cs211.project.controllers.eventHistory to javafx.fxml;

    exports cs211.project.controllers.profile;
    opens cs211.project.controllers.profile to javafx.fxml;

    exports cs211.project.controllers.schedule;
    opens cs211.project.controllers.schedule to javafx.fxml;


}