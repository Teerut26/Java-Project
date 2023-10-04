package cs211.project.controllers.event.team;

import cs211.project.models.Event;
import cs211.project.models.ManyToMany;
import cs211.project.models.Team;
import cs211.project.models.User;
import cs211.project.models.collections.TeamCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.ManyToManyManager;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.ManyToManyFileListDatasource;
import cs211.project.services.datasource.TeamFileListDatasource;
import cs211.project.utils.ComponentRegister;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventTeamListController extends ComponentRegister {
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
    private Team currentTeamSelect;


    @FXML
    public void initialize() {
        routeProvider = (RouteProvider<Event>) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        teamFileListDatasource = new TeamFileListDatasource();
        teamCollection = teamFileListDatasource.readData();

        String eventID = routeProvider.getData().getEventID();
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
    }

    public void setTeamTableView() {
        if (teamForTableView == null) {
            System.out.println("No teams");
        } else {


            TableColumn<Team, String> teamName = new TableColumn<>("Team name");
            teamName.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<Team, String> teamQuantity = new TableColumn<>("Team quantity");
            teamQuantity.setCellValueFactory(param -> {
                if (param.getValue() != null) {
                    ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
                    Team team = param.getValue();
                    String numberJoined = String.valueOf(manyToManyManager.countByB(team.getId()));
                    String quantity = String.valueOf(team.getQuantity());
                    String numberJoinedAndMax = numberJoined + "/" + quantity;
                    return new ReadOnlyStringWrapper(numberJoinedAndMax);
                } else {
                    return new ReadOnlyStringWrapper("");
                }
            });

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

            TableColumn<Team, String> teamStatus = new TableColumn<>("Status");
            teamStatus.setCellValueFactory(new PropertyValueFactory<>("joinStatus"));





            teamTableView.getColumns().clear();
            teamTableView.getColumns().addAll(teamName, teamQuantity, teamStartDate, teamEndDate, teamStatus);
            teamTableView.getItems().clear();

            for (Team team : teamForTableView.getTeams()) {
                ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
                Boolean isJoin = manyToManyManager.checkAHaveB(this.routeProvider.getUserSession().getId(), team.getId());
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
                    ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
                    String numberJoined = String.valueOf(manyToManyManager.countByB(selectedTeam.getId()));
                    String quantity = String.valueOf(selectedTeam.getQuantity());
                    String numberJoinedAndMax = numberJoined + "/" + quantity;
                    teamQuantityLabel.setText(numberJoinedAndMax);
                    String startDate  =formateDate(selectedTeam.getStartRecruitDate());
                    startDateLabel.setText(startDate);
                    String endDate = formateDate(selectedTeam.getEndRecruitDate());
                    endDateLabel.setText(endDate);
                }
                checkJoinTeamButtonStatus();
                successLabel.setVisible(false);
                errorLabel.setVisible(false);
                teamNameLabel.setVisible(true);
                teamQuantityLabel.setVisible(true);
                startDateLabel.setVisible(true);
                endDateLabel.setVisible(true);
                joinTeamButton.setDisable(false);


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
            ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
            manyToManyManager.remove(new ManyToMany(this.routeProvider.getUserSession().getId(), this.currentTeamSelect.getId()));
            viewTeamButton.setVisible(false);
            joinTeamButton.setText("Join");
            successLabel.setText("Canceled join team successfully");
            closeSuccessLabel();
            setStatusJoinInTable();
            checkJoinTeamButtonStatus();
        } else {
            ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
            manyToManyManager.add(new ManyToMany(this.routeProvider.getUserSession().getId(), this.currentTeamSelect.getId()));
            joinTeamButton.setText("cancel");
            viewTeamButton.setVisible(true);
            viewTeamButton.setDisable(false);
            successLabel.setVisible(true);
            successLabel.setText("Joined team successfully");
            closeSuccessLabel();
            setStatusJoinInTable();
            checkJoinTeamButtonStatus();
        }


    }

    private void closeSuccessLabel() {
        //when 3 seconds passed, close the success label
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        successLabel.setVisible(false);
                    }
                },
                3000
        );
    }

    //check if joined team already disabled button
    private Boolean checkJoinedTeam() {
        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
        if (manyToManyManager.checkAHaveB(this.routeProvider.getUserSession().getId(), this.currentTeamSelect.getId())) {
            System.out.println("Joined");
            return true;
        } else {
            System.out.println("Join");
            return false;
        }
    }

    private void checkJoinTeamButtonStatus() {
        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
            if (manyToManyManager.checkAHaveB(this.routeProvider.getUserSession().getId(), this.currentTeamSelect.getId())) {
                joinTeamButton.setText("cancel");
                viewTeamButton.setVisible(true);
                viewTeamButton.setDisable(false);
            } else {
                if (this.currentTeamSelect.getQuantity() == manyToManyManager.countByB(this.currentTeamSelect.getId())) {
                    joinTeamButton.setDisable(true);
                    joinTeamButton.setText("Full");
                    viewTeamButton.setVisible(true);
                } else {
                    joinTeamButton.setDisable(false);
                    joinTeamButton.setText("Join");
                    viewTeamButton.setVisible(false);
                }

            }


    }

    private void setStatusJoinInTable () {
        teamTableView.getItems().clear();
        for (Team team : teamForTableView.getTeams()) {
            ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
            Boolean isJoin = manyToManyManager.checkAHaveB(this.routeProvider.getUserSession().getId(), team.getId());
            if (isJoin) {
                team.setJoinStatus("Joined");
            } else {
                team.setJoinStatus("Not joined");
            }
            teamTableView.getItems().add(team);
        }

    }
    public String formateDate (LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }


    @FXML
    public void onBack() {
        try {
            FXRouter.goTo("event-detail-joined", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
