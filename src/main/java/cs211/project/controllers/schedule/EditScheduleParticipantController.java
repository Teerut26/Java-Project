package cs211.project.controllers.schedule;

import cs211.project.models.ActivitiesEvent;
import cs211.project.models.Event;
import cs211.project.models.collections.ActivitiesEventCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.ActivitiesEventFileListDatesource;
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

public class EditScheduleParticipantController extends ComponentRegister {
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
    private ActivitiesEvent activitiesEvent;
    private RouteProvider<Event> routeProvider;

    @FXML
    public void initialize(){
        routeProvider = (RouteProvider<Event>) FXRouter.getData();
        this.activitiesEvent = (ActivitiesEvent) routeProvider.getDataHashMap().get("activity-event-select");
        System.out.println(this.activitiesEvent.getTitle());
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        activitiesEventFileListDatesource = new ActivitiesEventFileListDatesource();
        activitiesEventCollection = activitiesEventFileListDatesource.readData();

        TextFieldName.setText(activitiesEvent.getTitle());
        TextFieldDetail.setText(activitiesEvent.getDetail());
        dateStart.setValue(activitiesEvent.getDateStart().toLocalDate());
        dateEnd.setValue(activitiesEvent.getDateEnd().toLocalDate());
        timeStart.setText(activitiesEvent.getStartTime());
        timeEnd.setText(activitiesEvent.getEndTime());
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

        activitiesEvent.setTitle(TextFieldName.getText());
        activitiesEvent.setDetail(TextFieldDetail.getText());
        activitiesEvent.setDateStart(timeStartUtils.getRefLocalDateTime());
        activitiesEvent.setDateEnd(timeEndUtils.getRefLocalDateTime());

        activitiesEventCollection.update(activitiesEvent);
        activitiesEventFileListDatesource.writeData(activitiesEventCollection);

        try {
            FXRouter.goTo("set-event-detail",this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCancel(){
        try {
            FXRouter.goTo("set-event-detail",this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
