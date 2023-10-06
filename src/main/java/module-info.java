module cs211.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires bcrypt;
    requires org.apache.commons.csv;

    opens cs211.project.router to javafx.fxml;
    exports cs211.project.router;
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

    exports cs211.project.controllers.myTeam;
    opens cs211.project.controllers.myTeam to javafx.fxml;

    exports cs211.project.controllers.profile;
    opens cs211.project.controllers.profile to javafx.fxml;

    exports cs211.project.controllers.schedule;
    opens cs211.project.controllers.schedule to javafx.fxml;



    exports cs211.project.models;
    opens cs211.project.models to javafx.fxml;
    exports cs211.project.services.datasource;
    opens cs211.project.services.datasource to javafx.fxml;
    exports cs211.project.services.comparator;
    opens cs211.project.services.comparator to javafx.fxml;


}