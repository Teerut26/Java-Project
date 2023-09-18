package cs211.project.controllers.schedule;

import cs211.project.models.ActivitiesTeam;
import cs211.project.models.Team;
import cs211.project.models.collections.ActivitiesTeamCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.ActivitiesTeamFileListDatesource;
import cs211.project.utils.ComponentRegister;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class EditScheduleTeamController extends ComponentRegister {
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
    private ActivitiesTeamFileListDatesource activitiesTeamFileListDatesource;
    private ActivitiesTeamCollection activitiesTeamCollection;
    private ActivitiesTeam activitiesTeam;
    private RouteProvider<Team> routeProvider;


    @FXML
    public void initialize(){
        routeProvider = (RouteProvider<Team>) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        activitiesTeamFileListDatesource = new ActivitiesTeamFileListDatesource();
        activitiesTeamCollection = activitiesTeamFileListDatesource.readData();
        activitiesTeamCollection.getActivitiesArrayList().forEach((activitiesTeam1 -> {
            if (activitiesTeam1.getTeam().getId().equals(routeProvider.getData().getId())){
                activitiesTeam = activitiesTeam1;
            }
        }));

        TextFieldEventName.setText(activitiesTeam.getTitle());
        TextFieldDescription.setText(activitiesTeam.getDetail());
        DataTimeStart.setValue(activitiesTeam.getStartDate().toLocalDate());
        DataTimeEnd.setValue(activitiesTeam.getEndDate().toLocalDate());
    }

    @FXML
    public void onSave(){
        activitiesTeam.setTitle(TextFieldEventName.getText());
        activitiesTeam.setDetail(TextFieldDescription.getText());
        activitiesTeam.setStartDate(DataTimeStart.getValue().atStartOfDay());
        activitiesTeam.setEndDate(DataTimeEnd.getValue().atStartOfDay());

        activitiesTeamFileListDatesource.writeData(activitiesTeamCollection);

        try {
            FXRouter.goTo("event-team-manage",this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    @FXML
    public void onCancel(){
        try {
            FXRouter.goTo("event-team-manage",this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
