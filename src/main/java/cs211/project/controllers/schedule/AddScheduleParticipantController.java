package cs211.project.controllers.schedule;

import cs211.project.models.Activities;
import cs211.project.models.ActivitiesEvent;
import cs211.project.models.Event;
import cs211.project.models.collections.ActivitiesCollection;
import cs211.project.models.collections.ActivitiesEventCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.ActivitiesEventFileListDatesource;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.utils.ComponentRegister;
import cs211.project.utils.TimeValidate;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class AddScheduleParticipantController extends ComponentRegister {
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
    private ActivitiesEventFileListDatesource activitiesEventFileListDatesource;
    private ActivitiesEventCollection activitiesEventCollection;
    private Event event;
    private String activitiesEventID;
    private RouteProvider<Event> routeProvider;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider<Event>) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
        this.activitiesEventID = UUID.randomUUID().toString();

        activitiesEventFileListDatesource = new ActivitiesEventFileListDatesource();
        activitiesEventCollection = activitiesEventFileListDatesource.readData();
        event = routeProvider.getData();
    }

    @FXML
    public void onSave() {
        TimeValidate timeStartUtils = new TimeValidate(timeStart.getText(), dateStart.getValue().atStartOfDay());
        TimeValidate timeEndUtils = new TimeValidate(timeEnd.getText(), dateEnd.getValue().atStartOfDay());

        if (!timeEndUtils.validate() || !timeStartUtils.validate()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Time");
            alert.setContentText("Please enter a valid time according to pattern 00:00:00");
            alert.show();
            return;
        }

        timeStartUtils.addTime(timeStartUtils.getHour(), timeStartUtils.getMinute(), timeStartUtils.getSecond());
        timeEndUtils.addTime(timeEndUtils.getHour(), timeEndUtils.getMinute(), timeEndUtils.getSecond());

        ActivitiesEvent newActivitiesEvent = new ActivitiesEvent(this.activitiesEventID,
                TextFieldName.getText(),
                TextFieldDetail.getText(),
                timeStartUtils.getRefLocalDateTime(),
                timeEndUtils.getRefLocalDateTime(),
                event);

        activitiesEventCollection.add(newActivitiesEvent);
        activitiesEventFileListDatesource.writeData(activitiesEventCollection);

        TextFieldName.clear();
        TextFieldDetail.clear();
        timeStart.clear();
        timeEnd.clear();
        dateStart.setValue(null);
        dateEnd.setValue(null);

        try {
            FXRouter.goTo("set-event-detail", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCancel() {
        try {
            FXRouter.goTo("set-event-detail", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
