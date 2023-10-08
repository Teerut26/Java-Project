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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.UUID;

public class EditTeam extends ComponentRegister {
    @FXML
    private BorderPane parentBorderPane;
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

        this.initializeThemeMode();
        this.initializeFont();
    }


    @FXML
    public void initializeThemeMode(){
        System.out.println("InitializeThemeMode" + this.routeProvider.getUserSession().getThemeMode());
        if (this.routeProvider.getUserSession().getThemeMode().equals("dark")){
            parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/light-mode.css");
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/dark-mode.css");
        }else if (this.routeProvider.getUserSession().getThemeMode().equals("light")) {
            parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/dark-mode.css");
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/light-mode.css");
        }



    }

    @FXML
    public void initializeFont(){
        String currentFont =this.routeProvider.getUserSession().getFont();
        clearFontStyle();
        if (currentFont.equals("font-style1")){
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style1.css");
        }else if (currentFont.equals("font-style2")){
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style2.css");
        }else if (currentFont.equals("font-style3")){
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style3.css");
        }

    }

    @FXML
    public void clearFontStyle(){
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style1.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style2.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style3.css");
    }


    public void showCurrentData() {
        teamNameEd.setText(team.getName());
        quantityEd.setText(String.valueOf(team.getQuantity()));
        DateOpeningDate.setValue(team.getStartRecruitDate().toLocalDate());
        DataDeadline.setValue(team.getEndRecruitDate().toLocalDate());
        timeStart.setText(formatTime(team.getStartTimeTeam()));
        timeEnd.setText(formatTime(team.getEndTimeTeam()));
    }

    public String formatTime(String time) {
        String[] timeArr = time.split(":");
        String hour = timeArr[0];
        String minute = timeArr[1];
        String second = timeArr[2];
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }
        return hour + ":" + minute + ":" + second;
    }

    @FXML
    void onSave(ActionEvent event) {
        try {
            Integer.parseInt(quantityEd.getText());
            Boolean status = editTeam();
            if (!status) {
                return;
            }
            teamCollection.update(this.team);
            teamFileListDatasource.writeData(teamCollection);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Team Edited");
            alert.setContentText("Team has been edited successfully");
            alert.showAndWait();

            navigateToSetEvent();
        } catch (NumberFormatException e) {
            errorLabel.setText("quantity must be number");
        }
    }

    private Boolean editTeam() {

        TimeValidate timeStartUtils = new TimeValidate(timeStart.getText(), DateOpeningDate.getValue().atStartOfDay());
        TimeValidate timeEndUtils = new TimeValidate(timeEnd.getText(), DataDeadline.getValue().atStartOfDay());

        if (!timeEndUtils.validate() || !timeStartUtils.validate()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Time");
            alert.setContentText("Please enter a valid time according to pattern 00:00");
            alert.show();
            return false;
        }

        timeStartUtils.addTime(timeStartUtils.getHour(), timeStartUtils.getMinute());
        timeEndUtils.addTime(timeEndUtils.getHour(), timeEndUtils.getMinute());

        team.setName(teamNameEd.getText());
        team.setQuantity(Integer.parseInt(quantityEd.getText()));
        team.setEndRecruitDate(timeEndUtils.getRefLocalDateTime());
        team.setStartRecruitDate(timeStartUtils.getRefLocalDateTime());

        return true;
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
