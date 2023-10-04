package cs211.project.controllers.myTeam;

import cs211.project.models.*;
import cs211.project.models.collections.ActivitiesCollection;
import cs211.project.models.collections.ActivitiesTeamCollection;
import cs211.project.models.collections.ManyToManyCollection;
import cs211.project.models.collections.TeamCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.ManyToManyManager;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.ActivitiesTeamFileListDatesource;
import cs211.project.services.datasource.ManyToManyFileListDatasource;
import cs211.project.services.datasource.TeamFileListDatasource;
import cs211.project.utils.ComponentRegister;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyTeamController {
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    private RouteProvider<Event> routeProvider;

    @FXML
    private Label teamNameLabel;
    @FXML
    private Label teamQuantityLabel;

    @FXML
    private Label teamStartDateLabel;

    @FXML
    private Label teamEndDateLabel;

    @FXML
    private Label eventTeamNameLabel;

    @FXML
    private Button cancelTeamButton;

    @FXML
    private  Button commentTeamButton;


    //table all team
    @FXML
    private TableView<Team> teamTableView = new TableView<>();

    @FXML
    private TeamFileListDatasource teamFileListDatasource;
    @FXML
    private TeamCollection teamForTableView = new TeamCollection();

//selected team
    @FXML
    private Team currentTeamSelect;

    @FXML
    private TableView<Activities> activityTableView = new TableView<>();
    @FXML
    ActivitiesTeamCollection activitiesCollection;
    @FXML
    private ActivitiesTeamFileListDatesource activitiesTeamFileListDatesource;



    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        ComponentRegister componentRegister = new ComponentRegister();
        componentRegister.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        componentRegister.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        //get team by user
        setTeamInTableView();
        clearTeamInfo();
    }

    public void setTeamInTableView() {
       TeamFileListDatasource teamFileListDatasource = new TeamFileListDatasource();
         TeamCollection teamCollection = teamFileListDatasource.readData();


        ManyToManyManager manyToManyManager= new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
        ManyToManyCollection manyToManyCollection = manyToManyManager.getAll();
        manyToManyCollection.findsByA(this.routeProvider.getUserSession().getId()).forEach(manyToMany -> {
            Team team = teamCollection.findById(manyToMany.getB());
            teamForTableView.add(team);
        });



        if (teamForTableView.getTeams().size() == 0) {
            teamNameLabel.setText("No team");
            teamQuantityLabel.setText("No team");
            teamStartDateLabel.setText("No team");
            teamEndDateLabel.setText("No team");
            cancelTeamButton.setDisable(true);
            commentTeamButton.setDisable(true);
            return;
        }


        TableColumn<Team, String> teamName = new TableColumn<>("Team Name");
        teamName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Team, String> teamStartDate = new TableColumn<>("Start date");
        teamStartDate.setCellValueFactory(param -> {
            if (param.getValue() != null) {
                Team team = param.getValue();
                String startDate =formateDate(team.getStartRecruitDate());
                return new ReadOnlyStringWrapper(startDate);
            } else {
                return new ReadOnlyStringWrapper("");


            }
        });
        TableColumn<Team, String> teamEndDate = new TableColumn<>("End date");
        teamEndDate.setCellValueFactory(param -> {
            if (param.getValue() != null) {
                Team team = param.getValue();
                String endDate = formateDate(team.getEndRecruitDate());
                return new ReadOnlyStringWrapper(endDate);
            } else {
                return new ReadOnlyStringWrapper("");
            }
        });


        teamTableView.getColumns().clear();
        teamTableView.getColumns().addAll(teamName, teamStartDate, teamEndDate);
        teamTableView.getItems().clear();

        for (Team team : teamForTableView.getTeams()) {
            teamTableView.getItems().add(team);
        }



        teamTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                currentTeamSelect = teamTableView.getSelectionModel().getSelectedItem();
                setTeamInfo();
                setActivityInTableView();
            }
        });



    }

    public void clearTeamInfo() {
        teamNameLabel.setText("Please select team");
        teamQuantityLabel.setText("Please select team");
        teamStartDateLabel.setText("Please select team");
        teamEndDateLabel.setText("Please select team");

        eventTeamNameLabel.setText("Please select team");
        cancelTeamButton.setDisable(true);
        commentTeamButton.setDisable(true);

    }

    public void setTeamInfo() {
        teamNameLabel.setText(currentTeamSelect.getName());
        teamQuantityLabel.setText(String.valueOf(currentTeamSelect.getQuantity()));
        teamStartDateLabel.setText(formateDate(currentTeamSelect.getStartRecruitDate()));
        teamEndDateLabel.setText(formateDate(currentTeamSelect.getEndRecruitDate()));
        cancelTeamButton.setDisable(false);
        commentTeamButton.setDisable(false);

    }

    public void setActivityInTableView() {
        ActivitiesTeamFileListDatesource activitiesTeamFileListDatesource = new ActivitiesTeamFileListDatesource();
        ActivitiesTeamCollection activitiesTeamCollection = activitiesTeamFileListDatesource.readData();

        activitiesCollection = new ActivitiesTeamCollection();
        activitiesCollection = activitiesTeamCollection.findByTeamId(currentTeamSelect.getId());

         TableColumn<Activities, String> activityName = new TableColumn<>("Activity Name");
          activityName.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Activities, String> activityDetail = new TableColumn<>("Detail");
        activityDetail.setCellValueFactory(new PropertyValueFactory<>("detail"));
        TableColumn<Activities, String> activityStartDate = new TableColumn<>("Start date");
        activityStartDate.setCellValueFactory(param -> {
            if (param.getValue() != null) {
                Activities activities = param.getValue();
                String startDate =formateDate(activities.getDateStart());
                return new ReadOnlyStringWrapper(startDate);
            } else {
                return new ReadOnlyStringWrapper("");
            }});
        TableColumn<Activities, String> activityEndDate = new TableColumn<>("End date");
        activityEndDate.setCellValueFactory(param -> {
            if (param.getValue() != null) {
                Activities activities = param.getValue();
                String endDate =formateDate(activities.getDateEnd());
                return new ReadOnlyStringWrapper(endDate);
            } else {
                return new ReadOnlyStringWrapper("");
            }});
        TableColumn<Activities, String> activityStartTime = new TableColumn<>("Start time");
        activityStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        TableColumn<Activities, String> activityEndTime = new TableColumn<>("End time");
        activityEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        activityTableView.getColumns().clear();
        activityTableView.getColumns().addAll(activityName, activityDetail, activityStartDate, activityEndDate, activityStartTime, activityEndTime);
        activityTableView.getItems().clear();

        for (Activities activities : activitiesCollection.getActivities()) {
            activityTableView.getItems().add(activities);
        }


    }

    public void onCancelTeam() {
        if (currentTeamSelect == null) {
            return;
        }
        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
        manyToManyManager.remove(new ManyToMany(this.routeProvider.getUserSession().getId(), this.currentTeamSelect.getId()));
        setTeamInTableView();
        clearTeamInfo();

    }





    public String formateDate (LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }






}
