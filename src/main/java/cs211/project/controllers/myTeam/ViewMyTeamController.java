package cs211.project.controllers.myTeam;

import cs211.project.Main;
import cs211.project.models.*;
import cs211.project.models.collections.*;
import cs211.project.services.FXRouter;
import cs211.project.services.ManyToManyManager;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.*;
import cs211.project.utils.ComponentRegister;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ViewMyTeamController {
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
    private Label eventNameLabel;

    @FXML
    private Label eventStatusLabel;


    @FXML
    private Button cancelTeamButton;

    @FXML
    private Button commentTeamButton;

    @FXML
    private Button manageTeamButton;

    @FXML
    private Team currentTeamSelect;
    @FXML
    private Label currentMemberAmount;

    @FXML
    private TableView<ActivitiesTeam> activityTableView = new TableView<>();
    @FXML
    private ActivitiesTeamCollection activitiesCollection;

    @FXML
    private ActivitiesTeam currentActivitySelect;

    @FXML
    private TableView<User> userTableView = new TableView<>();
    @FXML
    private UserCollection userCollection;
    @FXML
    private UserCollection userForTableView = new UserCollection();

    @FXML
    private Label suspendAlertLabel;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        ComponentRegister componentRegister = new ComponentRegister();
        componentRegister.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        componentRegister.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
        manageTeamButton.setVisible(false);
        suspendAlertLabel.setVisible(false);
        if (routeProvider.getDataHashMap().get("teamJoined") != null) {
            Team teamJoined = (Team) routeProvider.getDataHashMap().get("teamJoined");
            currentTeamSelect = teamJoined;
            setTeamInfo();
            setActivityInTableView();
            setUserTableView();
            checkHeadOfTeam();
            checkSuspend();
            routeProvider.getDataHashMap().remove("teamJoined");
        }
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
    public void initializeFont() {
        String currentFont = this.routeProvider.getUserSession().getFont();
        clearFontStyle();
        if (currentFont.equals("font-style1")) {
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style1.css");
        } else if (currentFont.equals("font-style2")) {
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style2.css");
        } else if (currentFont.equals("font-style3")) {
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style3.css");
        }

    }

    @FXML
    public void clearFontStyle() {
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style1.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style2.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style3.css");
    }



    public void setTeamInfo() {

        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
        manyToManyManager.findsByB(currentTeamSelect.getId()).getManyToManies().size();

        teamNameLabel.setText(currentTeamSelect.getName());
        teamQuantityLabel.setText(String.valueOf(currentTeamSelect.getQuantity()));
        eventNameLabel.setText(currentTeamSelect.getEvent().getNameEvent());
        currentMemberAmount.setText(String.valueOf(manyToManyManager.findsByB(currentTeamSelect.getId()).getManyToManies().size()));
        if(currentTeamSelect.getEvent().getEndDate().isBefore(LocalDate.now().atStartOfDay())){
            eventStatusLabel.setText("Event is ended");
        }else if(currentTeamSelect.getEvent().getStartDate().isAfter(LocalDate.now().atStartOfDay())){
            eventStatusLabel.setText("Event is not start yet");
        }else{
            eventStatusLabel.setText("Event is in progress");
        }

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

        TableColumn<ActivitiesTeam, String> activityStatus= new TableColumn<>("Status");
        activityStatus.setCellValueFactory(param -> {
            if (param.getValue() != null) {
                ActivitiesTeam activities = param.getValue();
                if(activities.getDateStart().isAfter(LocalDate.now().atStartOfDay())){
                    return new ReadOnlyStringWrapper("Not start yet");
                }else if(activities.getDateEnd().isBefore(LocalDate.now().atStartOfDay())){
                    return new ReadOnlyStringWrapper("Ended");
                }else{
                    return new ReadOnlyStringWrapper("In progress");
                }
            } else {
                return new ReadOnlyStringWrapper("");
            }
        });



        TableColumn<ActivitiesTeam, String> activityStartDate = new TableColumn<>("Start date");
        activityStartDate.setCellValueFactory(param -> {
            if (param.getValue() != null) {
                ActivitiesTeam activities = param.getValue();
                String startDate = formateDate(activities.getDateStart());
                return new ReadOnlyStringWrapper(startDate);
            } else {
                return new ReadOnlyStringWrapper("");
            }
        });
        TableColumn<ActivitiesTeam, String> activityEndDate = new TableColumn<>("End date");
        activityEndDate.setCellValueFactory(param -> {
            if (param.getValue() != null) {
                ActivitiesTeam activities = param.getValue();
                String endDate = formateDate(activities.getDateEnd());
                return new ReadOnlyStringWrapper(endDate);
            } else {
                return new ReadOnlyStringWrapper("");
            }
        });

        activityTableView.getColumns().clear();
        activityTableView.getColumns().addAll(activityName, activityDetail,activityStatus, activityStartDate, activityEndDate);
        activityTableView.getItems().clear();

        for (ActivitiesTeam activitiesTeam : activitiesCollection.getActivitiesArrayList()) {
            activityTableView.getItems().add(activitiesTeam);
        }

        activityTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                currentActivitySelect = activityTableView.getSelectionModel().getSelectedItem();
            }
        });

    }

    public void setUserTableView() {
        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
        ManyToManyCollection manyToManyCollection = manyToManyManager.getAll();
        manyToManyCollection.findsByB(currentTeamSelect.getId()).forEach(manyToMany -> {
            UserFileListDatasource userFileListDatasource = new UserFileListDatasource();
            userCollection = userFileListDatasource.readData();
            User user = userCollection.findById(manyToMany.getA());
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
        manyToManyManager
                .remove(new ManyToMany(this.routeProvider.getUserSession().getId(), this.currentTeamSelect.getId()));

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cancel team success", ButtonType.OK);
        alert.showAndWait();

        try {
            FXRouter.goTo(routeProvider.getDataHashMap().get("fromPage").toString(), this.routeProvider);
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
            this.routeProvider.addHashMap("back-my-team", "view-my-team");
            FXRouter.goTo("comment-activity-team", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String formateDate(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(formatter);
    }

    public void checkHeadOfTeam(){
        manageTeamButton.setVisible(false);
        ManyToManyManager manyToManyManager = new ManyToManyManager(
                new ManyToManyFileListDatasource().MTM_USER_TEAM_HEAD);
        if(manyToManyManager.checkIsExisted(new ManyToMany(this.routeProvider.getUserSession().getId(), this.currentTeamSelect.getId())
        )){
            manageTeamButton.setVisible(true);
        }
    }

    public void checkSuspend(){
        ManyToManyManager manyToManyManager = new ManyToManyManager(
                new ManyToManyFileListDatasource().MTM_USER_TEAM_SUSPEND);
        Boolean isSuspend = manyToManyManager
                .checkIsExisted(new ManyToMany(routeProvider.getUserSession().getId(), currentTeamSelect.getId()));
        if (isSuspend) {
            activityTableView.getItems().clear();
            manageTeamButton.setDisable(true);
            suspendAlertLabel.setVisible(true);
        }else{
            manageTeamButton.setDisable(false);
            suspendAlertLabel.setVisible(false);
        }
    }


    @FXML
    public void goToManageTeam() {
        try {
            if (this.currentTeamSelect == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "please select Team", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            this.routeProvider.addHashMap("team-select", this.currentTeamSelect);
            FXRouter.goTo("event-team-detail", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onBack(){
        try {
            FXRouter.goTo(routeProvider.getDataHashMap().get("fromPage").toString(), this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
