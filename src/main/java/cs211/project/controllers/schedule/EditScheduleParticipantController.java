package cs211.project.controllers.schedule;

import cs211.project.models.ActivitiesEvent;
import cs211.project.models.Event;
import cs211.project.models.collections.ActivitiesEventCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.ActivitiesEventFileListDatesource;
import cs211.project.utils.ComponentRegister;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class EditScheduleParticipantController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    @FXML
    private TextField TextFieldDescription;
    @FXML
    private TextField TextFieldEventName;
    @FXML
    private DatePicker DataTimeEnd;
    @FXML
    private DatePicker DataTimeStart;
    private ActivitiesEventFileListDatesource activitiesEventFileListDatesource;
    private ActivitiesEventCollection activitiesEventCollection;
    private ActivitiesEvent activitiesEvent;
    private RouteProvider<Event> routeProvider;

    @FXML
    public void initialize(){
        routeProvider = (RouteProvider<Event>) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        activitiesEventFileListDatesource = new ActivitiesEventFileListDatesource();
        activitiesEventCollection = activitiesEventFileListDatesource.readData();
        activitiesEventCollection.getActivitiesArrayList().forEach((activitiesEvent1 -> {
            if (activitiesEvent1.getEvent().getEventID().equals(routeProvider.getData().getEventID())){
                activitiesEvent = activitiesEvent1;
            }
        }));

        TextFieldEventName.setText(activitiesEvent.getTitle());
        TextFieldDescription.setText(activitiesEvent.getDetail());
        DataTimeStart.setValue(activitiesEvent.getStartDate().toLocalDate());
        DataTimeEnd.setValue(activitiesEvent.getEndDate().toLocalDate());
    }

    @FXML
    public void onSave(){
        activitiesEvent.setTitle(TextFieldEventName.getText());
        activitiesEvent.setDetail(TextFieldDescription.getText());
        activitiesEvent.setStartDate(DataTimeStart.getValue().atStartOfDay());
        activitiesEvent.setEndDate(DataTimeEnd.getValue().atStartOfDay());

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
