package cs211.project.controllers.schedule;

import cs211.project.models.ActivitiesTeam;
import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.collections.ActivitiesTeamCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.ActivitiesTeamFileListDatesource;
import cs211.project.utils.ComponentRegister;
import cs211.project.utils.TimeValidate;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;

public class EditScheduleTeamController extends ComponentRegister {
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
    private ActivitiesTeam activitiesTeam;
    private RouteProvider<Event> routeProvider;


    @FXML
    public void initialize(){
        routeProvider = (RouteProvider<Event>) FXRouter.getData();
        this.activitiesTeam = (ActivitiesTeam) routeProvider.getDataHashMap().get("select-activity-team");
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        activitiesTeamFileListDatesource = new ActivitiesTeamFileListDatesource();
        activitiesTeamCollection = activitiesTeamFileListDatesource.readData();

        TextFieldName.setText(activitiesTeam.getTitle());
        TextFieldDetail.setText(activitiesTeam.getDetail());
        dateStart.setValue(activitiesTeam.getDateStart().toLocalDate());
        dateEnd.setValue(activitiesTeam.getDateEnd().toLocalDate());
        timeStart.setText(activitiesTeam.getStartTime());
        timeEnd.setText(activitiesTeam.getEndTime());
    }

    @FXML
    public void onSave(){
        TimeValidate timeStartUtils = new TimeValidate(timeStart.getText(), dateStart.getValue().atStartOfDay());
        TimeValidate timeEndUtils = new TimeValidate(timeEnd.getText(), dateEnd.getValue().atStartOfDay());

        if (!timeEndUtils.validate() || !timeStartUtils.validate()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Time");
            alert.setContentText("Please enter a valid time");
            alert.show();
            return;
        }

        timeStartUtils.addTime(timeStartUtils.getHour(), timeStartUtils.getMinute(), timeStartUtils.getSecond());
        timeEndUtils.addTime(timeEndUtils.getHour(), timeEndUtils.getMinute(), timeEndUtils.getSecond());

        activitiesTeam.setTitle(TextFieldName.getText());
        activitiesTeam.setDetail(TextFieldDetail.getText());
        activitiesTeam.setDateStart(timeStartUtils.getRefLocalDateTime());
        activitiesTeam.setDateEnd(timeEndUtils.getRefLocalDateTime());

        activitiesTeamCollection.update(activitiesTeam);

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
