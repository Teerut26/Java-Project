package cs211.project.controllers.schedule;

import cs211.project.models.Activities;
import cs211.project.models.ActivitiesTeam;
import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.collections.ActivitiesCollection;
import cs211.project.models.collections.ActivitiesTeamCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.ActivitiesTeamFileListDatesource;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.services.datasource.TeamFileListDatasource;
import cs211.project.utils.ComponentRegister;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddScheduleTeamController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;

    @FXML
    private HBox NavBarHBox;
    @FXML
    private TextField TextFieldDetail;
    @FXML
    private TextField TextFieldName;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private DatePicker dateStart;
    @FXML
    private TextField timeEnd;
    @FXML
    private TextField timeStart;
    private ActivitiesTeamFileListDatesource activitiesTeamFileListDatesource;
    private ActivitiesTeamCollection activitiesTeamCollection;
    private Team team;
    private String activitiesTeamID;
    private RouteProvider<Event> routeProvider;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider<Event>) FXRouter.getData();
        this.team = (Team) routeProvider.getDataHashMap().get("team-select");

        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
        this.activitiesTeamID = UUID.randomUUID().toString();

        activitiesTeamFileListDatesource = new ActivitiesTeamFileListDatesource();
        activitiesTeamCollection = activitiesTeamFileListDatesource.readData();

    }

    public void onSave(){

            ActivitiesTeam newActivitiesTeam = new ActivitiesTeam(this.activitiesTeamID,
                    TextFieldName.getText(),
                    TextFieldDetail.getText(),
                    dateStart.getValue().atStartOfDay(),
                    dateEnd.getValue().atStartOfDay(),
                    timeStart.getText(),
                    timeEnd.getText(), team);

            activitiesTeamCollection.add(newActivitiesTeam);
            activitiesTeamFileListDatesource.writeData(activitiesTeamCollection);


        try {
            FXRouter.goTo("event-team-detail",this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCancel(){
        try {
            FXRouter.goTo("event-team-detail",this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
