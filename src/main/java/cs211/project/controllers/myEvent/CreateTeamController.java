package cs211.project.controllers.myEvent;

import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.TeamCollection;
import cs211.project.services.Authentication;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.TeamFileListDatasource;
import cs211.project.utils.ComponentRegister;
import cs211.project.utils.ImageSaver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.UUID;

public class CreateTeamController extends ComponentRegister {
    @FXML
    private HBox NavBarHBox;
    @FXML
    private VBox SideBarVBox;
    @FXML
    private DatePicker DataDeadline;
    @FXML
    private DatePicker DateOpeningDate;
    @FXML
    private TextField TextFieldQuantity;
    @FXML
    private TextField TextFieldTeamName;
    @FXML
    private Label errorLabel;
    private String teamID;
    private Event event;
    private EventCollection eventCollection;
    private RouteProvider<Event> routeProvider;
    private TeamFileListDatasource teamFileListDatasource;
    private TeamCollection teamCollection;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        this.teamID = UUID.randomUUID().toString();
        teamFileListDatasource = new TeamFileListDatasource();
        teamCollection = teamFileListDatasource.readData();

        event = routeProvider.getData();
        errorLabel.setText("");

    }

    @FXML
    public void onSave() {

        try{
            Integer.parseInt(TextFieldQuantity.getText());
            Team newTeam = creatNewTeam();;
            teamCollection.add(newTeam);
            teamFileListDatasource.writeData(teamCollection);
            clearField();
            navigateToEventDetail();
        } catch (NumberFormatException e) {
            errorLabel.setText("quantity must be number");
        }
    }

    private Team creatNewTeam(){
        return new Team(this.teamID,
                TextFieldTeamName.getText(),
                Integer.parseInt(TextFieldQuantity.getText()),
                DateOpeningDate.getValue().atStartOfDay(),
                DataDeadline.getValue().atStartOfDay(),
                routeProvider.getUserSession(),
                event
        );
    }
    private void clearField(){
        TextFieldTeamName.clear();
        TextFieldQuantity.clear();
        DateOpeningDate.setValue(null);
        DataDeadline.setValue(null);
    }

    private void navigateToEventDetail(){
        try {
            FXRouter.goTo("set-event-detail",this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCancel() {
        try {
            FXRouter.goTo("set-event-detail",this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        };
    }
}
