package cs211.project.controllers.schedule;

import cs211.project.Main;
import cs211.project.models.Activities;
import cs211.project.models.ActivitiesTeam;
import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.collections.ActivitiesCollection;
import cs211.project.models.collections.ActivitiesTeamCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.ActivitiesTeamFileListDatesource;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.services.datasource.TeamFileListDatasource;
import cs211.project.utils.ComponentRegister;
import cs211.project.utils.TimeValidate;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddScheduleTeamController {
    @FXML
    private BorderPane parentBorderPane;
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
    private ActivitiesTeamFileListDatesource activitiesTeamFileListDatesource;
    private ActivitiesTeamCollection activitiesTeamCollection;
    private Team team;
    private String activitiesTeamID;
    private RouteProvider<Event> routeProvider;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider<Event>) FXRouter.getData();
        this.team = (Team) routeProvider.getDataHashMap().get("team-select");
        ComponentRegister componentRegister = new ComponentRegister();
        componentRegister.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        componentRegister.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
        this.activitiesTeamID = UUID.randomUUID().toString();

        activitiesTeamFileListDatesource = new ActivitiesTeamFileListDatesource();
        activitiesTeamCollection = activitiesTeamFileListDatesource.readData();


        this.initializeThemeMode();
        this.initializeFont();
    }
    @FXML
    public void initializeThemeMode(){
        String className = Main.class.getName().replace('.', '/');
        String classJar = Main.class.getResource("/" + className + ".class").toString();
        Boolean isJarFile = classJar.startsWith("jar:");
        String pathDarkMode;
        String pathLightMode;
        if(isJarFile) {
            pathDarkMode = "/cs211/project/style/dark-mode.css";
            pathLightMode = "/cs211/project/style/light-mode.css";
        }else{
            pathDarkMode = "file:src/main/resources/cs211/project/style/dark-mode.css";
            pathLightMode = "file:src/main/resources/cs211/project/style/light-mode.css";
        }
        if (this.routeProvider.getUserSession().getThemeMode().equals("dark")){
            parentBorderPane.getStylesheets().remove(pathLightMode);
            parentBorderPane.getStylesheets().add(pathDarkMode);
        }else if (this.routeProvider.getUserSession().getThemeMode().equals("light")) {
            parentBorderPane.getStylesheets().remove(pathDarkMode);
            parentBorderPane.getStylesheets().add(pathLightMode);
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

    public void onSave() {
        TimeValidate timeStartUtils = new TimeValidate(timeStart.getText(), dateStart.getValue().atStartOfDay());
        TimeValidate timeEndUtils = new TimeValidate(timeEnd.getText(), dateEnd.getValue().atStartOfDay());

        if (!timeEndUtils.validate() || !timeStartUtils.validate()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Time");
            alert.setContentText("Please enter a valid time according to pattern 00:00");
            alert.show();
            return;
        }

        timeStartUtils.addTime(timeStartUtils.getHour(), timeStartUtils.getMinute());
        timeEndUtils.addTime(timeEndUtils.getHour(), timeEndUtils.getMinute());

        ActivitiesTeam newActivitiesTeam = new ActivitiesTeam(this.activitiesTeamID,
                TextFieldName.getText(),
                TextFieldDetail.getText(),
                timeStartUtils.getRefLocalDateTime(),
                timeEndUtils.getRefLocalDateTime(), team);

        activitiesTeamCollection.add(newActivitiesTeam);
        activitiesTeamFileListDatesource.writeData(activitiesTeamCollection);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Create new Activities Team");
        alert.setContentText("Create new Activities Team successfully");
        alert.showAndWait();

        try {
            FXRouter.goTo("event-team-detail",this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCancel() {
        try {
            FXRouter.goTo("event-team-detail",this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
