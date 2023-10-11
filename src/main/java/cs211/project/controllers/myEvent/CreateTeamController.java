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
import cs211.project.utils.TimeValidate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.UUID;

public class CreateTeamController extends ComponentRegister {
    @FXML
    private BorderPane parentBorderPane;
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
    @FXML
    private TextField timeStart;
    @FXML
    private TextField timeEnd;
    private String teamID;
    private Event event;
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



    @FXML
    public void onSave() {

        try{
            Integer.parseInt(TextFieldQuantity.getText());
            Team newTeam = creatNewTeam();;
            teamCollection.add(newTeam);
            teamFileListDatasource.writeData(teamCollection);
            clearField();


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Team Created");
            alert.setContentText("Team Created Successfully");
            alert.showAndWait();
            navigateToEventDetail();
        } catch (NumberFormatException e) {
            errorLabel.setText("quantity must be number");

        }
    }

    private Team creatNewTeam(){

        TimeValidate timeStartUtils = new TimeValidate(timeStart.getText(), DateOpeningDate.getValue().atStartOfDay());
        TimeValidate timeEndUtils = new TimeValidate(timeEnd.getText(), DataDeadline.getValue().atStartOfDay());

        if (!timeEndUtils.validate() || !timeStartUtils.validate()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Time");
            alert.setContentText("Please enter a valid time according to pattern 00:00");
            alert.show();
            return null;
        }

        timeStartUtils.addTime(timeStartUtils.getHour(), timeStartUtils.getMinute());
        timeEndUtils.addTime(timeEndUtils.getHour(), timeEndUtils.getMinute());

        return new Team(this.teamID,
                TextFieldTeamName.getText(),
                Integer.parseInt(TextFieldQuantity.getText()),
                timeStartUtils.getRefLocalDateTime(),
                timeEndUtils.getRefLocalDateTime(),
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
