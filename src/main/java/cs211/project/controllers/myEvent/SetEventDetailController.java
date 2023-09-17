package cs211.project.controllers.myEvent;

import cs211.project.models.Activities;
import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.collections.ActivitiesCollection;
import cs211.project.models.collections.TeamCollection;

import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.TeamFileListDatasource;
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

public class SetEventDetailController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    @FXML
    private TableView<Team> teamTableView;
    @FXML
    private TableView<Activities> activityTableView;
    @FXML
    private Label teamId;
    @FXML
    private Label activityId;
    @FXML
    private Tab detailTab;
    @FXML
    private Tab participantTab;
    @FXML
    private Tab teamTab;
    @FXML
    private Tab activityTab;
    @FXML
    private TabPane tabPane;
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
    private TeamCollection teamCollection;
    private Team teamSelect;
    private TeamFileListDatasource teamFileListDatasource;
    private RouteProvider<Event> routeProvider;

//    private ActivitiesFileListDatesource activitiesFileListDatesource;
    private ActivitiesCollection activitiesCollection;
    private Activities activitySelect;


    @FXML
    public void initialize() {
        initializeComponents();
        loadEventData();
        showDetail();
        showTableTeam(teamCollection);
        setUpOnTableViewTeamOnSelect();
//        showTableActivities(activitiesCollection);
        setUpOnTableViewActivityOnSelect();

    }

    private void initializeComponents() {
        routeProvider = (RouteProvider) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
        event = routeProvider.getData();
    }

    private void loadEventData() {

        teamFileListDatasource = new TeamFileListDatasource();
        TeamCollection newTeamCollection = new TeamCollection();

        teamFileListDatasource.readData().getTeams().forEach((team) -> {
            if (team.getEvent().getEventID().equals(routeProvider.getData().getEventID())) {
                newTeamCollection.add(team);
            }
        });

        teamCollection = newTeamCollection;

//        activitiesFileListDatesource = new ActivitiesFileListDatesource();
//        activitiesCollection = activitiesFileListDatesource.readData();
    }

    public void showDetail() {
        nameEvent.setText(this.event.getNameEvent());
        eventDescription.setText(event.getDescriptionEvent());
        currentUser.setText(String.valueOf(event.getCurrentMemberParticipatingAmount()));
        quantityLabel.setText(String.valueOf(event.getQuantityEvent()));
        timeLabel.setText(event.getStartDate().format(cs211.project.models.Event.DATE_FORMATTER) + " - " + event.getEndDate().format(cs211.project.models.Event.DATE_FORMATTER));
        Image img = new Image("file:" + event.getImageEvent());
        imageEvent.setFill(new ImagePattern(img));
    }

    public void showTableTeam(TeamCollection teamCollection) {
        TableColumn<Team, String> nameColumn = new TableColumn<>("Team Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Team, String> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        teamTableView.getColumns().clear();
        teamTableView.getColumns().add(nameColumn);
        teamTableView.getColumns().add(quantityColumn);

        teamTableView.getItems().clear();

        for (Team team : teamCollection.getTeams()) {
            teamTableView.getItems().add(team);
        }
    }

    public void setUpOnTableViewTeamOnSelect() {
        teamTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Team>() {
            @Override
            public void changed(ObservableValue observable, Team oldValue, Team newValue) {
                if (newValue != null) {
                    teamSelect = newValue;
                    teamId.setText(teamSelect.getName());
                } else {
                    teamSelect = null;
                    teamId.setText("");
                }
            }
        });
    }

//    public void showTableActivities(ActivitiesCollection activitiesCollection) {
//        TableColumn<Activities, String> nameActivityColumn = new TableColumn<>("Activity Name");
//        nameActivityColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
//
//        TableColumn<Activities, String> descriptionColumn = new TableColumn<>("Description");
//        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));
//
//        TableColumn<Activities, String> startDateColumn = new TableColumn<>("Start Date");
//        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
//
//        TableColumn<Activities, String> endDateColumn = new TableColumn<>("End Date");
//        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
//
//        activityTableView.getColumns().clear();
//        activityTableView.getColumns().add(nameActivityColumn);
//        activityTableView.getColumns().add(descriptionColumn);
//        activityTableView.getColumns().add(startDateColumn);
//        activityTableView.getColumns().add(endDateColumn);
//        activityTableView.getItems().clear();
//
//        for (Activities activities : activitiesCollection.getActivitiesArrayList()) {
//            activityTableView.getItems().add(activities);
//        }
//    }

    public void setUpOnTableViewActivityOnSelect() {
        activityTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Activities>() {
            @Override
            public void changed(ObservableValue observable, Activities oldValue, Activities newValue) {
                if (newValue != null) {
                    activitySelect = newValue;
                    activityId.setText(activitySelect.getId());
                } else {
                    activitySelect = null;
                    activityId.setText("");
                }
            }
        });
    }

    @FXML
    public void onActivityDelete(ActionEvent event) {
        if (activitySelect != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You want to delete : " + activitySelect.getTitle() + " ?", ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.CANCEL) {
                return;
            }
            ActivitiesCollection newActivityCollection = new ActivitiesCollection();
            for (Activities activities : activitiesCollection.getActivitiesArrayList()) {
                if (!activities.getId().equals(activitySelect.getId())) {
                    newActivityCollection.add(activities);
                }
            }
//            activitiesFileListDatesource.writeData(newActivityCollection);
//            activitiesCollection = newActivityCollection;
//            showTableActivities(activitiesCollection);
        }
    }

    @FXML
    public void onTeamDelete(ActionEvent event) {
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


    @FXML
    public void onBack() {
        try {
            FXRouter.goTo("my-event", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onEdit() {
        try {
            FXRouter.goTo("edit-event-detail-form", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onAddTeam(ActionEvent event) {
        try {
            FXRouter.goTo("create-team", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
