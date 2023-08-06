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

    exports cs211.project.controllers.mainEvent;
    opens cs211.project.controllers.mainEvent to javafx.fxml;
}