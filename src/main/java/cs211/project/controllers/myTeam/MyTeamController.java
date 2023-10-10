package cs211.project.controllers.myTeam;

import cs211.project.models.*;
import cs211.project.models.collections.*;
import cs211.project.services.FXRouter;
import cs211.project.services.ManyToManyManager;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.ActivitiesTeamFileListDatesource;
import cs211.project.services.datasource.ManyToManyFileListDatasource;
import cs211.project.services.datasource.TeamFileListDatasource;
import cs211.project.services.datasource.UserFileListDatasource;
import cs211.project.utils.ComponentRegister;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyTeamController {
    @FXML
    private BorderPane parentBorderPane;
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
    private TableView<ActivitiesTeam> activityTableView = new TableView<>();
    @FXML
    private ActivitiesTeamCollection activitiesCollection;

    @FXML
    private ActivitiesTeam currentActivitySelect;

    //selected activity
    @FXML
    private TableView<User> userTableView = new TableView<>();
    @FXML
    private UserCollection userCollection;
    @FXML
    private UserCollection userForTableView = new UserCollection();




    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        ComponentRegister componentRegister = new ComponentRegister();
        componentRegister.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        componentRegister.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        //get team by user
        setTeamInTableView();
        clearTeamInfo();

        if(routeProvider.getDataHashMap().get("teamJoined") != null) {
            Team teamJoined = (Team) routeProvider.getDataHashMap().get("teamJoined");
            currentTeamSelect = teamJoined;
            setTeamInfo();
            setActivityInTableView();
            setUserTableView();

            teamTableView.scrollTo(teamJoined);
            teamTableView.setRowFactory(tv -> new TableRow<Team>() {
                @Override
                protected void updateItem(Team item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || item.getId() == null) {
                        setStyle("");
                    } else if (item.getId().equals(teamJoined.getId())) {
                        setStyle("-fx-background-color: #2a9df4;");
                    } else {
                        setStyle("");
                    }
                }
            });

            routeProvider.getDataHashMap().remove("teamJoined");
        }

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
                setUserTableView();
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

         TableColumn<ActivitiesTeam, String> activityName = new TableColumn<>("Activity Name");
          activityName.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<ActivitiesTeam, String> activityDetail = new TableColumn<>("Detail");
        activityDetail.setCellValueFactory(new PropertyValueFactory<>("detail"));
        TableColumn<ActivitiesTeam, String> activityStartDate = new TableColumn<>("Start date");
        activityStartDate.setCellValueFactory(param -> {
            if (param.getValue() != null) {
                ActivitiesTeam activities = param.getValue();
                String startDate =formateDate(activities.getDateStart());
                return new ReadOnlyStringWrapper(startDate);
            } else {
                return new ReadOnlyStringWrapper("");
            }});
        TableColumn<ActivitiesTeam, String> activityEndDate = new TableColumn<>("End date");
        activityEndDate.setCellValueFactory(param -> {
            if (param.getValue() != null) {
                ActivitiesTeam activities = param.getValue();
                String endDate =formateDate(activities.getDateEnd());
                return new ReadOnlyStringWrapper(endDate);
            } else {
                return new ReadOnlyStringWrapper("");
            }});
        TableColumn<ActivitiesTeam, String> activityStartTime = new TableColumn<>("Start time");
        activityStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        TableColumn<ActivitiesTeam, String> activityEndTime = new TableColumn<>("End time");
        activityEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        activityTableView.getColumns().clear();
        activityTableView.getColumns().addAll(activityName, activityDetail, activityStartDate, activityEndDate, activityStartTime, activityEndTime);
        activityTableView.getItems().clear();

        for (ActivitiesTeam activitiesTeam : activitiesCollection.getActivitiesArrayList()) {
            activityTableView.getItems().add(activitiesTeam);
        }

        activityTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                   currentActivitySelect = activityTableView.getSelectionModel().getSelectedItem();
                   System.out.println(currentActivitySelect.getTitle());
            }
        });


    }

    public void setUserTableView(){
        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
        ManyToManyCollection manyToManyCollection = manyToManyManager.getAll();
        manyToManyCollection.findsByB(currentTeamSelect.getId()).forEach(manyToMany -> {
            UserFileListDatasource userFileListDatasource = new UserFileListDatasource();
            userCollection = userFileListDatasource.readData();
            User user = userCollection.findById(manyToMany.getA());
            System.out.println(user.getNameUser());
            userForTableView.add(user);
        });

        TableColumn<User, String> userName = new TableColumn<>("User Name");
        userName.setCellValueFactory(new PropertyValueFactory<>("nameUser"));


        userTableView.getColumns().clear();
        userTableView.getColumns().addAll(userName);
        userTableView.getItems().clear();

        for (User user : userForTableView.getUsers()) {
            userTableView.getItems().add(user);
        }

    }



    public void onCancelTeam() {
        if (currentTeamSelect == null) {
            return;
        }
        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
        manyToManyManager.remove(new ManyToMany(this.routeProvider.getUserSession().getId(), this.currentTeamSelect.getId()));
        clearTeamInfo();
        //reload this page
        try {
            FXRouter.goTo("my-team",this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void onViewActivityTeam(ActionEvent event) {
        try {
            if (currentActivitySelect == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "please select activity", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            this.routeProvider.addHashMap("activity-select", currentActivitySelect);
            FXRouter.goTo("comment-activity-team",this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String formateDate (LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }






}
