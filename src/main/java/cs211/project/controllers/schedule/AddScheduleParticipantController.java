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
import javafx.event.ActionEvent;
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

public class AddScheduleParticipantController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;

    @FXML
    private HBox NavBarHBox;
    @FXML
    private TextField TextFieldDetail;

    @FXML
    private TextField TextFieldEndTime;

    @FXML
    private TextField TextFieldName;

    @FXML
    private TextField TextFieldStartTime;
    @FXML
    private Label errorLabel;
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

        errorLabel.setText("");
    }

    @FXML
    public void onSave(){

            ActivitiesEvent newActivitiesEvent = new ActivitiesEvent(this.activitiesEventID,
                    TextFieldName.getText(),
                    TextFieldDetail.getText(),
                    TextFieldStartTime.getText(),
                    TextFieldEndTime.getText(),event);
            activitiesEventCollection.add(newActivitiesEvent);
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
