package cs211.project.controllers.myEvent;

import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.TeamFileListDatasource;
import cs211.project.models.collections.TeamCollection;
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

public class EditTeam extends ComponentRegister {

    @FXML
    private DatePicker DataDeadline;
    @FXML
    private DatePicker DateOpeningDate;

    @FXML
    private HBox NavBarHBox;

    @FXML
    private VBox SideBarVBox;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField quantityEd;

    @FXML
    private TextField teamNameEd;
    private RouteProvider<Event> routeProvider;
    private Team team;
    private TeamFileListDatasource teamFileListDatasource;
    private TeamCollection teamCollection;

    @FXML
    public void initialize(){
        routeProvider = (RouteProvider) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        teamFileListDatasource = new TeamFileListDatasource();
        teamCollection = teamFileListDatasource.readData();
        teamCollection.getTeams().forEach((team1 -> {
            if(team1.getEvent().getEventID().equals(routeProvider.getData().getEventID())){
                team = team1;
            }
        }));

        errorLabel.setText("");
        this.showCurrentData();
    }

    public void showCurrentData(){
        teamNameEd.setText(team.getName());
        quantityEd.setText(String.valueOf(team.getQuantity()));
        DataDeadline.setValue(team.getStartRecruitDate().toLocalDate());
        DateOpeningDate.setValue(team.getEndRecruitDate().toLocalDate());
    }
    @FXML
    void onCancel(ActionEvent event) {

    }

    @FXML
    void onSave(ActionEvent event) {
        try{
            Integer.parseInt(quantityEd.getText());
            editTeam();
            teamFileListDatasource.writeData(teamCollection);
            onCancel();
        } catch (NumberFormatException e) {
            errorLabel.setText("quantity must be number");
        }
    }

    private void editTeam(){
        team.setName(teamNameEd.getText());
        team.setQuantity(Integer.parseInt(quantityEd.getText()));
        team.setEndRecruitDate(DataDeadline.getValue().atStartOfDay());
        team.setStartRecruitDate(DateOpeningDate.getValue().atStartOfDay());
    }

    @FXML
    public void onCancel() {
        try {
            FXRouter.goTo("set-event-detail",this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
