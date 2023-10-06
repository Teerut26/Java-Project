package cs211.project.controllers.myEvent;

import cs211.project.models.*;
import cs211.project.models.collections.*;

import cs211.project.services.FXRouter;
import cs211.project.services.ManyToManyManager;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.*;
import cs211.project.utils.ComponentRegister;
import javafx.beans.binding.Bindings;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

import java.io.IOException;
import java.time.format.DateTimeFormatter;


public class SetEventDetailController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    @FXML
    private Label teamId;
    @FXML
    private Label activityId;
    @FXML
    private TableView<User> userJoinTableView;
    @FXML
    private TableView<Team> teamTableView;
    @FXML
    private TableView<ActivitiesEvent> activityEventTableView;
    @FXML
    private Label currentUserJoinAmount;
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
    @FXML
    private Label maxMember;
    @FXML
    private Label memberAlive;
    @FXML
    private Label memberSupend;
    @FXML
    private Label currentMemberAmount;
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
    private ActivitiesTeamFileListDatesource activitiesTeamFileListDatesource;
    private CommentActivitiesEventFileListDatasource commentActivitiesEventFileListDatasource;


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
        selectActivityOnTableView();
        this.setMetaData();
    }

    private void initializeComponents() {
        routeProvider = (RouteProvider) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
    }

    private void loadUserData() {
        this.event = new EventFileListDatesource().readData().findById(routeProvider.getData().getEventID());

        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_EVENT);
        UserCollection userCollectionAll = new UserFileListDatasource().readData();
        UserCollection userCollectionEvent = new UserCollection();
        manyToManyManager.findsByB(this.event.getEventID()).getManyToManies().forEach((mtm) -> {
            userCollectionEvent.add(userCollectionAll.findById(mtm.getA()));
        });
        userCollection = userCollectionEvent;
    }

    private void setMetaData() {
        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_EVENT_SUSPEND);
        Integer suspend = manyToManyManager.countByB(this.event.getEventID());

        this.currentMemberAmount.setText(String.valueOf(this.userCollection.getUsers().size()));
        this.maxMember.setText(String.valueOf(this.event.getQuantityEvent()));
        this.memberSupend.setText(String.valueOf(suspend));
        this.memberAlive.setText(String.valueOf(this.userCollection.getUsers().size() - suspend));

        this.showTableUserJoin(this.userCollection);
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

    public void showDetail() {

        Integer currentMemberJoin = this.userCollection.getUsers().size();

        nameEvent.setText(this.event.getNameEvent());
        eventDescription.setText(event.getDescriptionEvent());
        currentUserJoinAmount.setText(String.valueOf(currentMemberJoin));
        quantityLabel.setText(String.valueOf(event.getQuantityEvent()));

        timeLabel.setText(event.getStartDate().format(Event.DATE_FORMATTER) + " - " + event.getEndDate().format(Event.DATE_FORMATTER));
        Image img = new Image("file:" + event.getImageEvent());
        imageEvent.setFill(new ImagePattern(img));
    }

    @FXML
    public void onEditDetail() {
        try {
            FXRouter.goTo("edit-event-detail-form", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectUserOnTableView() {
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

    public void showTableUserJoin(UserCollection userCollection) {

        ManyToManyManager manyToManyManagerSuspend = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_EVENT_SUSPEND);

        TableColumn<User, String> imageColumn = new TableColumn<>("Profile");
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageProfile"));

        imageColumn.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>(){
            @Override
            public TableCell<User, String> call(TableColumn<User, String> param) {
                return new TableCell<User, String>() {
                    private ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String imageUrl, boolean empty) {
                        super.updateItem(imageUrl, empty);
                        if (imageUrl == null || empty) {
                            setGraphic(null);
                        } else {
                            Image image = new Image("file:" + imageUrl);
                            imageView.setImage(image);
                            imageView.setFitWidth(50);
                            imageView.setFitHeight(50);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameUser"));

        TableColumn<User, String> lastLoginColumn = new TableColumn<>("lastLogin");
        lastLoginColumn.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));

        TableColumn<User, String> statusUserColumnColumn = new TableColumn<>("lastLogin");

        statusUserColumnColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                User user = param.getValue();
                boolean isSuspend = manyToManyManagerSuspend.checkIsExisted(new ManyToMany(user.getId(), event.getEventID()));
                return Bindings.createStringBinding(() -> isSuspend ? "Suspend" : "Active");
            }
        });

        userJoinTableView.getColumns().clear();
        userJoinTableView.getItems().clear();

        userJoinTableView.getColumns().addAll(imageColumn, usernameColumn, nameColumn, lastLoginColumn, statusUserColumnColumn);

        for (User user : userCollection.getUsers()) {
            userJoinTableView.getItems().add(user);
        }
    }

    @FXML
    public void onSuspendUser(ActionEvent event) {
        if (userSelect != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You want to suspend : " + userSelect.getUserName() + " ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                System.out.println(userSelect);
                ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_EVENT_SUSPEND);
                manyToManyManager.add(new ManyToMany(userSelect.getId(), this.event.getEventID()));
                this.teamSelect = null;
                this.setMetaData();
            }

        }

    }

    @FXML
    public void onUnCancelSuspend() {
        if (userSelect != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You want to cancel cancel suspend : " + userSelect.getUserName() + " ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_EVENT_SUSPEND);
                manyToManyManager.remove(new ManyToMany(userSelect.getId(), this.event.getEventID()));
                this.teamSelect = null;
                this.setMetaData();
            }
        }
    }

    public void selectTeamOnTableView() {
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


    public void showTableTeam(TeamCollection teamCollection) {
        TableColumn<Team, String> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Team, String> quantityTeamColumnColumn = new TableColumn<>("quantity");
        quantityTeamColumnColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Team, String> startDateTeamColumn = new TableColumn<>("startRecruitDate");
        startDateTeamColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Team, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Team, String> parameter) {
                Team team = parameter.getValue();
                return Bindings.createStringBinding(() -> team.getStartRecruitDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
        });

        TableColumn<Team, String> endRecruitDateColumn = new TableColumn<>("endRecruitDate");
        endRecruitDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Team, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Team, String> parameter) {
                Team team = parameter.getValue();
                return Bindings.createStringBinding(() -> team.getEndRecruitDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
        });


        teamTableView.getColumns().clear();
        teamTableView.getColumns().addAll(nameColumn, quantityTeamColumnColumn, startDateTeamColumn, endRecruitDateColumn);

        teamTableView.getItems().clear();

        for (Team team : teamCollection.getTeams()) {
            teamTableView.getItems().add(team);
        }
    }


    @FXML
    public void onTeamDelete(ActionEvent event) {
        if (teamSelect != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You want to delete : " + teamSelect.getName() + " ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.NO) {
                return;
            }
            TeamCollection newTeamCollection = new TeamCollection();
            for (Team team : teamCollection.getTeams()) {
                if (!team.getId().equals(teamSelect.getId())) {
                    newTeamCollection.add(team);
                }
                if(team.getId().equals(teamSelect.getId())){

                    activitiesTeamFileListDatesource = new ActivitiesTeamFileListDatesource();

                    ActivitiesTeamCollection newActivitiesTeamCollection = new ActivitiesTeamCollection();
                    activitiesTeamFileListDatesource.readData().getActivitiesArrayList().forEach(activitiesTeam -> {
                        if(!activitiesTeam.getTeam().getId().equals(teamSelect.getId())){
                            newActivitiesTeamCollection.add(activitiesTeam);
                        }
                    });
                    activitiesTeamFileListDatesource.writeData(newActivitiesTeamCollection);

                }
            }
            teamFileListDatasource.writeData(newTeamCollection);
            teamCollection = newTeamCollection;
            showTableTeam(teamCollection);
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

    @FXML
    void onTeamEdit(ActionEvent event) {
        try {
            if (teamSelect == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "please select Team", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            this.routeProvider.addHashMap("team-select", this.teamSelect);
            FXRouter.goTo("edit-team", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onManageTeam(ActionEvent event) {
        try {
            if (teamSelect == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "please select Team", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            this.routeProvider.addHashMap("team-select", this.teamSelect);
            FXRouter.goTo("event-team-detail", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void selectActivityOnTableView() {
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

    public void showTableActivities(ActivitiesEventCollection activitiesCollection) {
        TableColumn<ActivitiesEvent, String> titleColumn = new TableColumn<>("title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<ActivitiesEvent, String> detailColumn = new TableColumn<>("detail");
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));

        TableColumn<ActivitiesEvent, String> startDateColumn = new TableColumn<>("startDate");
        startDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ActivitiesEvent, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ActivitiesEvent, String> parameter) {
                ActivitiesEvent activitiesEvent = parameter.getValue();
                return Bindings.createStringBinding(() -> activitiesEvent.getDateStart().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            }
        });

        TableColumn<ActivitiesEvent, String> endDateColumn = new TableColumn<>("endDate");
        endDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ActivitiesEvent, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ActivitiesEvent, String> parameter) {
                ActivitiesEvent activitiesEvent = parameter.getValue();
                return Bindings.createStringBinding(() -> activitiesEvent.getDateEnd().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            }
        });

        TableColumn<ActivitiesEvent, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ActivitiesEvent, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ActivitiesEvent, String> parameter) {
                ActivitiesEvent activitiesEvent = parameter.getValue();
                return Bindings.createStringBinding(() -> activitiesEvent.getStatus());
            }
        });

        activityEventTableView.getColumns().clear();
        activityEventTableView.getColumns().addAll(titleColumn, detailColumn, startDateColumn, endDateColumn, statusColumn);
        activityEventTableView.getItems().clear();

        for (ActivitiesEvent activitiesEvent : activitiesEventCollection.getActivitiesArrayList()) {
            activityEventTableView.getItems().add(activitiesEvent);
        }
    }

    @FXML
    public void onViewActivityEvent(ActionEvent event) {
        try {
            if (activitySelect == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "please select activity", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            this.routeProvider.addHashMap("activity-event-select",this.activitySelect);
            FXRouter.goTo("comment-activity-event",this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onActivityEventDelete(ActionEvent event) {
        if(activitySelect != null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "You want to delete : " + activitySelect.getTitle() + " ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.NO) {
                return;
            }
            ActivitiesEventCollection newActivityEventCollection = new ActivitiesEventCollection();
            for(ActivitiesEvent activitiesEvent : activitiesEventCollection.getActivitiesArrayList()){
                if(!activitiesEvent.getId().equals(activitySelect.getId())){
                    newActivityEventCollection.add(activitiesEvent);
                }
                if(activitiesEvent.getId().equals(activitySelect.getId())){

                    commentActivitiesEventFileListDatasource = new CommentActivitiesEventFileListDatasource();

                    CommentActivitiesEventCollection newCommentActivitiesEventCollection = new CommentActivitiesEventCollection();
                    commentActivitiesEventFileListDatasource.readData().getComments().forEach(commentActivitiesEvent -> {
                        if(!commentActivitiesEvent.getActivitiesEvent().getId().equals(activitySelect.getId())){
                            newCommentActivitiesEventCollection.add(commentActivitiesEvent);
                        }
                    });
                    commentActivitiesEventFileListDatasource.writeData(newCommentActivitiesEventCollection);
                }
            }
            activitiesEventFileListDatesource.writeData(newActivityEventCollection);
            activitiesEventCollection = newActivityEventCollection;
            showTableActivities(activitiesEventCollection);
        }
    }

    @FXML
    public void onEditActivityEvent() {
        try {
            if (activitySelect == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "please select activity", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            this.routeProvider.addHashMap("activity-event-select", this.activitySelect);
            FXRouter.goTo("edit-schedule-participant", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
    public void onBack() {
        try {
            FXRouter.goTo("my-event", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
