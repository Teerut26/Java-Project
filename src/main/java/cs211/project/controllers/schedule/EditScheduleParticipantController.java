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
import javafx.scene.control.Label;
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
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        activitiesEventFileListDatesource = new ActivitiesEventFileListDatesource();
        activitiesEventCollection = activitiesEventFileListDatesource.readData();
        activitiesEventCollection.getActivitiesArrayList().forEach((activitiesEvent1 -> {
            if (activitiesEvent1.getEvent().getEventID().equals(routeProvider.getData().getEventID())){
                activitiesEvent = activitiesEvent1;
            }
        }));

        TextFieldName.setText(activitiesEvent.getTitle());
        TextFieldDetail.setText(activitiesEvent.getDetail());
        dateStart.setValue(activitiesEvent.getDateStart().toLocalDate());
        dateEnd.setValue(activitiesEvent.getDateEnd().toLocalDate());
        timeStart.setText(activitiesEvent.getStartTime());
        timeEnd.setText(activitiesEvent.getEndTime());
    }

    @FXML
    public void onSave(){
        activitiesEvent.setTitle(TextFieldName.getText());
        activitiesEvent.setDetail(TextFieldDetail.getText());
        activitiesEvent.setDateStart(dateStart.getValue().atStartOfDay());
        activitiesEvent.setDateEnd(dateEnd.getValue().atStartOfDay());
        activitiesEvent.setStartTime(timeStart.getText());
        activitiesEvent.setEndTime(timeEnd.getText());

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
