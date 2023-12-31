package cs211.project.controllers.event.team;

import cs211.project.Main;
import cs211.project.models.Event;
import cs211.project.models.ManyToMany;
import cs211.project.models.Team;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.TeamCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.ManyToManyManager;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.services.datasource.ManyToManyFileListDatasource;
import cs211.project.services.datasource.TeamFileListDatasource;
import cs211.project.utils.ComponentRegister;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

public class EventTeamListController {
    @FXML
    private BorderPane parentBorderPane;
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;

    @FXML
    private Button joinTeamButton;

    @FXML
    private Button viewTeamButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Label successLabel;
    private RouteProvider<Event> routeProvider;

    @FXML
    private TableView<Team> teamTableView = new TableView<>();

    @FXML
    TeamCollection teamCollection;

    @FXML
    private TeamFileListDatasource teamFileListDatasource;

    @FXML
    private TeamCollection teamForTableView = new TeamCollection();

    @FXML
    private Label teamNameLabel;

    @FXML
    private Label teamQuantityLabel;

    @FXML
    private Label startDateLabel;

    @FXML
    private Label endDateLabel;

    @FXML
    private Label extrauserLabel;

    @FXML
    private Team currentTeamSelect;

    private Boolean isJoinedTeam;

    private Boolean isExtraUser = false;

    public String eventID;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider<Event>) FXRouter.getData();
        ComponentRegister componentRegister = new ComponentRegister();
        componentRegister.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        componentRegister.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        teamFileListDatasource = new TeamFileListDatasource();
        teamCollection = teamFileListDatasource.readData();
        eventID = (String) routeProvider.getDataHashMap().get("eventID");
        teamCollection.getTeams().forEach((team -> {
            if (team.getEvent().getEventID().equals(eventID)) {

                teamForTableView.add(team);

            }
        }));

        setTeamTableView();
        errorLabel.setVisible(false);
        successLabel.setVisible(false);
        joinTeamButton.setDisable(true);
        viewTeamButton.setDisable(true);
        teamNameLabel.setVisible(false);
        teamQuantityLabel.setVisible(false);
        startDateLabel.setVisible(false);
        endDateLabel.setVisible(false);
        extrauserLabel.setVisible(false);

        this.initializeThemeMode();
        this.initializeFont();
        checkIsExtraUser();
        if (isExtraUser) {
            extrauserLabel.setVisible(true);
        }

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

    public void setTeamTableView() {
        if (teamForTableView != null) {

            TableColumn<Team, String> teamName = new TableColumn<>("Team name");
            teamName.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<Team, String> teamQuantity = new TableColumn<>("Team quantity");
            teamQuantity.setCellValueFactory(param -> {
                if (param.getValue() != null) {
                    ManyToManyManager manyToManyManager = new ManyToManyManager(
                            new ManyToManyFileListDatasource().MTM_USER_TEAM);
                    Team team = param.getValue();
                    String numberJoined = String.valueOf(manyToManyManager.countByB(team.getId()));
                    String quantity = String.valueOf(team.getQuantity());
                    String numberJoinedAndMax = numberJoined + "/" + quantity;
                    return new ReadOnlyStringWrapper(numberJoinedAndMax);
                } else {
                    return new ReadOnlyStringWrapper("");
                }
            });

            TableColumn<Team, String> recruitmentStatus= new TableColumn<>("Status recruitment");
            recruitmentStatus.setCellValueFactory(param -> {
                if (param.getValue() != null) {
                    Team team = param.getValue();
                    if (team.getStartRecruitDate().isBefore(LocalDate.now().atStartOfDay()) && team.getEndRecruitDate().isAfter(LocalDate.now().atStartOfDay())) {
                        return new ReadOnlyStringWrapper("Open");
                    } else if (team.getStartRecruitDate().isAfter(LocalDate.now().atStartOfDay())) {
                        return new ReadOnlyStringWrapper("Not open");
                    } else {
                        return new ReadOnlyStringWrapper("Close");
                    }
                } else {
                    return new ReadOnlyStringWrapper("");
                }
            });

            TableColumn<Team, String> teamStartDate = new TableColumn<>("Open recruitment");
            teamStartDate.setCellValueFactory(param -> {
                if (param.getValue() != null) {
                    Team team = param.getValue();
                    String startDate = formateDate(team.getStartRecruitDate());
                    return new ReadOnlyStringWrapper(startDate);
                } else {
                    return new ReadOnlyStringWrapper("");

                }
            });

            TableColumn<Team, String> teamEndDate = new TableColumn<>("End recruitment");
            teamEndDate.setCellValueFactory(param -> {
                if (param.getValue() != null) {
                    Team team = param.getValue();
                    String endDate = formateDate(team.getEndRecruitDate());
                    return new ReadOnlyStringWrapper(endDate);
                } else {
                    return new ReadOnlyStringWrapper("");
                }
            });

            TableColumn<Team, String> teamStatus = new TableColumn<>("Status");
            teamStatus.setCellValueFactory(new PropertyValueFactory<>("joinStatus"));

            teamTableView.getColumns().clear();
            teamTableView.getColumns().addAll(teamName, teamQuantity,recruitmentStatus, teamStartDate, teamEndDate, teamStatus);
            teamTableView.getItems().clear();

            for (Team team : teamForTableView.getTeams()) {
                ManyToManyManager manyToManyManager = new ManyToManyManager(
                        new ManyToManyFileListDatasource().MTM_USER_TEAM);
                Boolean isJoin = manyToManyManager.checkAHaveB(this.routeProvider.getUserSession().getId(),
                        team.getId());
                if (isJoin) {
                    team.setJoinStatus("Joined");
                } else {
                    team.setJoinStatus("Not joined");
                }

                teamTableView.getItems().add(team);
            }

            teamTableView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {

                    Team selectedTeam = teamTableView.getSelectionModel().getSelectedItem();
                    this.currentTeamSelect = selectedTeam;
                    teamNameLabel.setText(selectedTeam.getName());
                    ManyToManyManager manyToManyManager = new ManyToManyManager(
                            new ManyToManyFileListDatasource().MTM_USER_TEAM);
                    String numberJoined = String.valueOf(manyToManyManager.countByB(selectedTeam.getId()));
                    String quantity = String.valueOf(selectedTeam.getQuantity());
                    String numberJoinedAndMax = numberJoined + "/" + quantity;
                    teamQuantityLabel.setText(numberJoinedAndMax);
                    String startDate = formateDate(selectedTeam.getStartRecruitDate());
                    startDateLabel.setText(startDate);
                    String endDate = formateDate(selectedTeam.getEndRecruitDate());
                    endDateLabel.setText(endDate);
                    successLabel.setVisible(false);
                    errorLabel.setVisible(false);
                    teamNameLabel.setVisible(true);
                    teamQuantityLabel.setVisible(true);
                    startDateLabel.setVisible(true);
                    endDateLabel.setVisible(true);
                    joinTeamButton.setDisable(false);
                    checkJoinTeamButtonStatus();
                }

            });

        }
    }

    public void onJoinTeam() {
        if (this.currentTeamSelect == null) {
            errorLabel.setText("Please select a team");
            return;
        }
        Boolean isJoined = checkJoinedTeam();
        if (isJoined) {
            ManyToManyManager manyToManyManager = new ManyToManyManager(
                    new ManyToManyFileListDatasource().MTM_USER_TEAM);
            manyToManyManager.remove(
                    new ManyToMany(this.routeProvider.getUserSession().getId(), this.currentTeamSelect.getId()));
            viewTeamButton.setVisible(false);
            joinTeamButton.setText("Join");
            successLabel.setText("Canceled join team successfully");
            closeSuccessLabel();
            setStatusJoinInTable();
            checkJoinTeamButtonStatus();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cancel join team");
            alert.setHeaderText("Cancel join team");
            alert.setContentText("Canceled join team successfully");
            alert.showAndWait();
        } else {
            ManyToManyManager manyToManyManager = new ManyToManyManager(
                    new ManyToManyFileListDatasource().MTM_USER_TEAM);
            if ((currentTeamSelect.getStartRecruitDate().isAfter(LocalDate.now().atStartOfDay()) )) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Join team");
                alert.setHeaderText("This team is not recruiting");
                alert.setContentText("Recruitment time has not started yet");
                alert.showAndWait();
                return;
            } else if (currentTeamSelect.getEndRecruitDate().isBefore(LocalDate.now().atStartOfDay())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Join team");
                alert.setHeaderText("This team is not recruiting");
                alert.setContentText("Recruitment time has ended");
                alert.showAndWait();
                return;
            }
            manyToManyManager.add(new ManyToMany(this.routeProvider.getUserSession().getId(), this.currentTeamSelect.getId()));
            joinTeamButton.setText("cancel");
            viewTeamButton.setVisible(true);
            viewTeamButton.setDisable(false);
            successLabel.setVisible(true);
            successLabel.setText("Joined team successfully");
            closeSuccessLabel();
            setStatusJoinInTable();
            checkJoinTeamButtonStatus();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Join team");
            alert.setHeaderText("Join team");
            alert.setContentText("Joined team successfully");
            alert.showAndWait();
        }

    }

    private void closeSuccessLabel() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        successLabel.setVisible(false);
                    }
                },
                3000);
    }

    private Boolean checkJoinedTeam() {
        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
        if (manyToManyManager.checkAHaveB(this.routeProvider.getUserSession().getId(),
                this.currentTeamSelect.getId())) {
            return true;
        } else {
            return false;
        }
    }

    private void checkJoinTeamButtonStatus() {
        this.checkJoinTeam();
        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
        if (manyToManyManager.checkAHaveB(this.routeProvider.getUserSession().getId(),
                this.currentTeamSelect.getId())) {
            joinTeamButton.setText("cancel");
            joinTeamButton.getStyleClass().remove("btn-done");
            joinTeamButton.getStyleClass().add("btn-error");
            viewTeamButton.setVisible(true);
            viewTeamButton.setDisable(false);
        } else {
            joinTeamButton.getStyleClass().remove("btn-error");
            joinTeamButton.getStyleClass().add("btn-done");
            if (this.currentTeamSelect.getQuantity() == manyToManyManager.countByB(this.currentTeamSelect.getId())) {
                joinTeamButton.setDisable(true);
                joinTeamButton.setText("Full");
                viewTeamButton.setVisible(true);

            } else {
                if (isExtraUser) {
                    joinTeamButton.setDisable(false);
                    joinTeamButton.setText("join");
                    viewTeamButton.setVisible(false);
                } else if (isJoinedTeam) {
                    joinTeamButton.setDisable(true);
                    viewTeamButton.setVisible(false);
                    joinTeamButton.setText("Join");

                } else {
                    joinTeamButton.setDisable(false);
                    joinTeamButton.setText("Join");
                    viewTeamButton.setVisible(false);
                }
            }

        }

    }

    private void checkIsExtraUser() {
        ManyToManyManager manyToManyManager = new ManyToManyManager(
                new ManyToManyFileListDatasource().MTM_USER_TEAM_EXTRA);
        manyToManyManager.findsByA(this.routeProvider.getUserSession().getId()).getManyToManies()
                .forEach(manyToMany -> {
                    if (teamForTableView.findById(manyToMany.getB()) != null) {
                        isExtraUser = true;
                    }
                });

    }

    private void setStatusJoinInTable() {
        teamTableView.getItems().clear();
        for (Team team : teamForTableView.getTeams()) {
            ManyToManyManager manyToManyManager = new ManyToManyManager(
                    new ManyToManyFileListDatasource().MTM_USER_TEAM);
            Boolean isJoin = manyToManyManager.checkAHaveB(this.routeProvider.getUserSession().getId(), team.getId());
            if (isJoin) {
                team.setJoinStatus("Joined");
            } else {
                team.setJoinStatus("Not joined");
            }
            teamTableView.getItems().add(team);
        }

    }

    public String formateDate(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    @FXML
    public void onClickButtonViewTeam() {
        try {
            routeProvider.addHashMap("teamJoined", this.currentTeamSelect);
            routeProvider.addHashMap("fromPage", "event-team-list");
            FXRouter.goTo("view-my-team", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkJoinTeam() {
        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
        EventFileListDatesource eventFileListDatesource = new EventFileListDatesource();
        EventCollection eventCollection = eventFileListDatesource.readData();
        Event event = eventCollection.findById(eventID);

        this.isJoinedTeam = false;
        TeamFileListDatasource teamFileListDatasource = new TeamFileListDatasource();
        TeamCollection teamCollection = teamFileListDatasource.readData().findByEvent(event);
        teamCollection.getTeams().forEach(team -> {
            if (manyToManyManager
                    .checkIsExisted(new ManyToMany(this.routeProvider.getUserSession().getId(), team.getId()))) {
                this.isJoinedTeam = true;

            }
        });

    }

    @FXML
    public void onBack() {
        try {
            if (this.routeProvider.getDataHashMap().get("oldRoute") != null) {
                FXRouter.goTo((String) this.routeProvider.getDataHashMap().get("oldRoute"), this.routeProvider);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
