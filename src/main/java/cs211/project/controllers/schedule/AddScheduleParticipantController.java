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
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.UUID;

public class AddScheduleParticipantController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    @FXML
    private TextField TextFieldEventName;
    @FXML
    private TextField TextFieldDescription;
    @FXML
    private DatePicker DataTimeEnd;
    @FXML
    private DatePicker DataTimeStart;
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
    public void onSave(){

        ActivitiesEvent newActivitiesEvent = new ActivitiesEvent(this.activitiesEventID,
                TextFieldEventName.getText(),
                TextFieldDescription.getText(),
                DataTimeStart.getValue().atStartOfDay(),
                DataTimeEnd.getValue().atStartOfDay(),event);

        activitiesEventCollection.add(newActivitiesEvent);
        activitiesEventFileListDatesource.writeData(activitiesEventCollection);

        TextFieldEventName.clear();
        TextFieldDescription.clear();
        DataTimeStart.setValue(null);
        DataTimeEnd.setValue(null);

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
