package cs211.project.controllers.myEvent;

import cs211.project.models.*;
import cs211.project.models.collections.ActivitiesEventCollection;
import cs211.project.models.collections.TeamCollection;

import cs211.project.models.collections.UserCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.ManyToManyManager;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.ActivitiesEventFileListDatesource;
import cs211.project.services.datasource.ManyToManyFileListDatasource;
import cs211.project.services.datasource.TeamFileListDatasource;
import cs211.project.services.datasource.UserFileListDatasource;
import cs211.project.utils.ComponentRegister;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.time.LocalDateTime;

public class SetEventDetailController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    @FXML
    private TableView<Team> teamTableView;
    @FXML
    private TableColumn<Team, String> nameTeamColumn;
    @FXML
    private TableColumn<Team, Integer> quantityTeamColumn;
    @FXML
    private TableColumn<Team, LocalDateTime> startDateTeam;
    @FXML
    private TableColumn<Team, LocalDateTime> endDateTeam;
    @FXML
    private TableColumn<Team, String> statusTeam;
    @FXML
    private TableView<ActivitiesEvent> activityEventTableView;
    @FXML
    private TableColumn<ActivitiesEvent, String> actionAcitivityCol;
    @FXML
    private TableColumn<ActivitiesEvent, String> titleActivityCol;
    @FXML
    private TableColumn<ActivitiesEvent, String> detailActivityCol;

    @FXML
    private TableColumn<ActivitiesEvent, LocalDateTime> endDateCol;
    @FXML
    private TableColumn<ActivitiesEvent, LocalDateTime> startDateCol;

    @FXML
    private Label teamId;
    @FXML
    private Label activityId;
    @FXML
    private TableView<User> userJoinTableView;
    @FXML
    private TableColumn<User, String> userNameColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> lastLoginColumn;
    @FXML
    private TableColumn<User, LocalDateTime> statusUserColumn;

    @FXML
    private Label currentUser;
    @FXML
    private Label eventDescription;
    @FXML
    private Rectangle imageEvent;
    @FXML
    private Label nameEvent;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label timeLabel;

    private Event event;

    private UserCollection userCollection;
    private User userSelect;
    private TeamCollection teamCollection;
    private Team teamSelect;
    private TeamFileListDatasource teamFileListDatasource;
    private RouteProvider<Event> routeProvider;

    private ActivitiesEventFileListDatesource activitiesEventFileListDatesource;
    private ActivitiesEventCollection activitiesEventCollection;
    private ActivitiesEvent activitySelect;


    @FXML
    public void initialize() {
        initializeComponents();
        loadEventData();
        loadUserData();
        showDetail();
        showTableUserJoin(userCollection);
        selectUserOnTableView();
        showTableTeam(teamCollection);
        selectTeamOnTableView();
        showTableActivities(activitiesEventCollection);
        selectAcitivityOnTableView();
//        showTableActivities(activitiesCollection);
    }

    private void initializeComponents() {
        routeProvider = (RouteProvider<Event>) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
        event = routeProvider.getData();
    }

    private void loadUserData() {
        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_EVENT);
        UserCollection userCollectionAll = new UserFileListDatasource().readData();
        UserCollection userCollectionEvent = new UserCollection();
        manyToManyManager.findsByB(this.event.getEventID()).getManyToManies().forEach((mtm) -> {
            userCollectionEvent.add(userCollectionAll.findById(mtm.getA()));
        });
        userCollection = userCollectionEvent;
    }

    private void loadEventData() {
        // team in event
        teamFileListDatasource = new TeamFileListDatasource();
        TeamCollection newTeamCollection = new TeamCollection();

        teamFileListDatasource.readData().getTeams().forEach((team) -> {
            if (team.getEvent().getEventID().equals(routeProvider.getData().getEventID())) {
                newTeamCollection.add(team);
            }
        });

        teamCollection = newTeamCollection;

        // activity in event
        activitiesEventFileListDatesource = new ActivitiesEventFileListDatesource();
        ActivitiesEventCollection newActivityCollection = new ActivitiesEventCollection();

        activitiesEventFileListDatesource.readData().getActivitiesArrayList().forEach((activitiesEvent) -> {

            if (activitiesEvent.getEvent().getEventID().equals(routeProvider.getData().getEventID())) {
                newActivityCollection.add(activitiesEvent);
            }
        });

        activitiesEventCollection = newActivityCollection;

    }
        public void showDetail () {
            nameEvent.setText(this.event.getNameEvent());
            eventDescription.setText(event.getDescriptionEvent());
            currentUser.setText(String.valueOf(event.getCurrentMemberParticipatingAmount()));
            quantityLabel.setText(String.valueOf(event.getQuantityEvent()));
            timeLabel.setText(event.getStartDate().format(cs211.project.models.Event.DATE_FORMATTER) + " - " + event.getEndDate().format(cs211.project.models.Event.DATE_FORMATTER));
            Image img = new Image("file:" + event.getImageEvent());
            imageEvent.setFill(new ImagePattern(img));
        }

        @FXML
        public void onEdit () {
            try {
                FXRouter.goTo("edit-event-detail-form", this.routeProvider);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void selectUserOnTableView () {
            userJoinTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
                @Override
                public void changed(ObservableValue observable, User oldValue, User newValue) {
                    if (newValue != null) {
                        userSelect = newValue;
                    } else {
                        userSelect = null;
                    }
                }
            });
        }

        public void showTableUserJoin (UserCollection userCollection){

            userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
            userNameColumn.setMinWidth(240.251220703125);

            nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameUser"));
            nameColumn.setMinWidth(259.687744140625);

            lastLoginColumn.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));
            lastLoginColumn.setMinWidth(216.812255859375);

            statusUserColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            statusUserColumn.setMinWidth(186.406005859375);

            userJoinTableView.getColumns().clear();
            userJoinTableView.getColumns().addAll(userNameColumn, nameColumn, lastLoginColumn, statusUserColumn);

            userJoinTableView.getItems().clear();

            for (User user : userCollection.getUsers()) {
                userJoinTableView.getItems().add(user);
            }
        }


        @FXML
        public void onSuspendUser (ActionEvent event){
            if (userSelect != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "You want to suspend : " + userSelect.getUserName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.CANCEL) {
                    return;
                }

            }

        }
//    @FXML
//    public void onUsCancelSuspend(){
//        if (userSelect != null) {
//            Alert alert = new Alert(Alert.AlertType.WARNING, "You want to cancel suspend : " + userSelect.getUserName() + " ?", ButtonType.OK, ButtonType.CANCEL);
//            alert.showAndWait();
//            if (alert.getResult() == ButtonType.CANCEL) {
//                return;
//            }
//        }
//    }

        public void selectTeamOnTableView () {
            teamTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Team>() {
                @Override
                public void changed(ObservableValue observable, Team oldValue, Team newValue) {
                    if (newValue != null) {
                        teamSelect = newValue;
                    } else {
                        teamSelect = null;
                    }
                }
            });
        }


        public void showTableTeam (TeamCollection teamCollection){

            nameTeamColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            nameTeamColumn.setMinWidth(263.9804992675781);

            quantityTeamColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            quantityTeamColumn.setMinWidth(134.91705322265625);

            startDateTeam.setCellValueFactory(new PropertyValueFactory<>("startRecruitDate"));
            startDateTeam.setMinWidth(161.58294677734375);

            endDateTeam.setCellValueFactory(new PropertyValueFactory<>("endRecruitDate"));
            endDateTeam.setMinWidth(167.81219482421875);

            statusTeam.setCellValueFactory(new PropertyValueFactory<>(""));
            statusTeam.setMinWidth(160);

            teamTableView.getColumns().clear();
            teamTableView.getColumns().addAll(nameTeamColumn, quantityTeamColumn, startDateTeam, endDateTeam, statusTeam);

            teamTableView.getItems().clear();

            for (Team team : teamCollection.getTeams()) {
                teamTableView.getItems().add(team);
            }
        }

        @FXML
        void onSuspendTeam (ActionEvent event){
            if (teamSelect != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "You want to suspend : " + teamSelect.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.CANCEL) {
                    return;
                }
            }
        }


        @FXML
        public void onTeamDelete (ActionEvent event){
            if (teamSelect != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "You want to delete : " + teamSelect.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.CANCEL) {
                    return;
                }
                TeamCollection newTeamCollection = new TeamCollection();
                for (Team team : teamCollection.getTeams()) {
                    if (!team.getId().equals(teamSelect.getId())) {
                        newTeamCollection.add(team);
                    }
                }
                teamFileListDatasource.writeData(newTeamCollection);
                teamCollection = newTeamCollection;
                showTableTeam(teamCollection);
            }
        }

        public void selectAcitivityOnTableView () {
            activityEventTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ActivitiesEvent>() {
                @Override
                public void changed(ObservableValue observable, ActivitiesEvent oldValue, ActivitiesEvent newValue) {
                    if (newValue != null) {
                        activitySelect = newValue;
                    } else {
                        activitySelect = null;
                    }
                }
            });
        }

        public void showTableActivities (ActivitiesEventCollection activitiesCollection){

            titleActivityCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            titleActivityCol.setMinWidth(197.86151123046875);

            detailActivityCol.setCellValueFactory(new PropertyValueFactory<>("detail"));
            detailActivityCol.setMinWidth(237.21307373046875);

            startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            detailActivityCol.setMinWidth(167.2869873046875);

            endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            endDateCol.setMinWidth(160.69671630859375);

            actionAcitivityCol.setCellValueFactory(new PropertyValueFactory<>(""));
            actionAcitivityCol.setMinWidth(131);

            activityEventTableView.getColumns().clear();
            activityEventTableView.getColumns().addAll(titleActivityCol, detailActivityCol, startDateCol, endDateCol);

            for (ActivitiesEvent activitiesEvent : activitiesEventCollection.getActivitiesArrayList()) {
                activityEventTableView.getItems().add(activitiesEvent);
            }
        }

        @FXML
        void onDone (ActionEvent event){
            if (activitySelect != null) {

            }
        }

        @FXML
        void onAddActivity(ActionEvent event) {
            try {
                FXRouter.goTo("add-schedule-participant", this.routeProvider);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @FXML
        public void onBack () {
            try {
                FXRouter.goTo("my-event", this.routeProvider);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @FXML
        void onAddTeam (ActionEvent event){
            try {
                FXRouter.goTo("create-team", this.routeProvider);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    @FXML
    void onTeamEdit(ActionEvent event) {
        try {
            FXRouter.goTo("edit-team", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
