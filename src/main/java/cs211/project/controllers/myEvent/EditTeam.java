package cs211.project.controllers.myEvent;

import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;

import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.TeamFileListDatasource;
import cs211.project.models.collections.TeamCollection;
import cs211.project.utils.ComponentRegister;
import cs211.project.utils.TimeValidate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

    @FXML
    private TextField timeStart;

    @FXML
    private TextField timeEnd;
    private RouteProvider<Event> routeProvider;
    private Team team;
    private TeamFileListDatasource teamFileListDatasource;
    private TeamCollection teamCollection;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        this.team = (Team) routeProvider.getDataHashMap().get("team-select");
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
        errorLabel.setText("");
        this.teamFileListDatasource = new TeamFileListDatasource();
        this.teamCollection = teamFileListDatasource.readData();
        this.showCurrentData();
    }

    public void showCurrentData() {
        teamNameEd.setText(team.getName());
        quantityEd.setText(String.valueOf(team.getQuantity()));
        DateOpeningDate.setValue(team.getStartRecruitDate().toLocalDate());
        DataDeadline.setValue(team.getEndRecruitDate().toLocalDate());
        timeStart.setText(team.getStartTimeTeam());
        timeEnd.setText(team.getEndTimeTeam());
    }

    @FXML
    void onSave(ActionEvent event) {
        try {
            Integer.parseInt(quantityEd.getText());
            editTeam();
            teamCollection.update(this.team);
            teamFileListDatasource.writeData(teamCollection);
            navigateToSetEvent();
        } catch (NumberFormatException e) {
            errorLabel.setText("quantity must be number");
        }
    }

    private void editTeam() {

        TimeValidate timeStartUtils = new TimeValidate(timeStart.getText(), DateOpeningDate.getValue().atStartOfDay());
        TimeValidate timeEndUtils = new TimeValidate(timeEnd.getText(), DataDeadline.getValue().atStartOfDay());

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

        team.setName(teamNameEd.getText());
        team.setQuantity(Integer.parseInt(quantityEd.getText()));
        team.setEndRecruitDate(timeEndUtils.getRefLocalDateTime());
        team.setStartRecruitDate(timeStartUtils.getRefLocalDateTime());
    }

    private void navigateToSetEvent() {
        try {
            FXRouter.goTo("set-event-detail", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCancel() {
        navigateToSetEvent();
    }

}
