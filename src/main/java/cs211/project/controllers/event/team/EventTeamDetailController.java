package cs211.project.controllers.event.team;

import cs211.project.models.*;
import cs211.project.models.collections.ActivitiesTeamCollection;
import cs211.project.models.collections.ManyToManyCollection;
import cs211.project.models.collections.TeamCollection;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.ManyToManyManager;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.*;
import cs211.project.utils.ComponentRegister;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.time.LocalDateTime;

public class EventTeamDetailController extends ComponentRegister {
    @FXML
    private HBox NavBarHBox;

    @FXML
    private VBox SideBarVBox;
    @FXML
    private TableColumn<ActivitiesTeam, String> statusActivityTeamCol;

    @FXML
    private TableView<ActivitiesTeam> activityTeamTableView;

    @FXML
    private TableView<User> memberTeamTableView;

    private ActivitiesTeamFileListDatesource activitiesTeamFileListDatesource;
    private ActivitiesTeamCollection activitiesTeamCollection;
    private ActivitiesTeam selectActivitiesTeam;
    private UserCollection userInTeamCollection;
    private UserFileListDatasource userFileListDatasource;
    private User selectMemberInTeam;
    private Team team;
    private Event event;
    private EventFileListDatesource eventFileListDatesource;

    private RouteProvider<Event> routeProvider;

    @FXML
    public void initialize() {
        initializeComponents();
        loadTeamDetailData();
        selectActivityTeamOnTableView();
        showTableActivityTeam(activitiesTeamCollection);
        selectUserTeamOnTableView();

    }

    private void initializeComponents() {
        routeProvider = (RouteProvider<Event>) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
    }

    private void loadTeamDetailData() {

        // activity in team
        activitiesTeamCollection = new ActivitiesTeamFileListDatesource().readData();

        this.team = (Team) this.routeProvider.getDataHashMap().get("team-select");

        ActivitiesTeamCollection newActivityTeamCollection = new ActivitiesTeamCollection();
        activitiesTeamCollection.getActivitiesArrayList().forEach((activitiesTeam -> {
            if (activitiesTeam.getTeam().getId().equals(this.team.getId())) {
                newActivityTeamCollection.add(activitiesTeam);
            }
        }));
        activitiesTeamCollection = newActivityTeamCollection;

        // team member
        ManyToManyCollection manyToManyCollection = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM).findsByB(this.team.getId());
        UserCollection userCollection = new UserFileListDatasource().readData();

        UserCollection newUserColletion = new UserCollection();
        manyToManyCollection.getManyToManies().forEach((manyToMany -> {
            newUserColletion.add(userCollection.findById(manyToMany.getA()));
        }));

        userInTeamCollection = newUserColletion;

        showTableMemberInTeam(this.userInTeamCollection);
    }

    public void selectActivityTeamOnTableView() {
        activityTeamTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ActivitiesTeam>() {
            @Override
            public void changed(ObservableValue observable, ActivitiesTeam oldValue, ActivitiesTeam newValue) {
                if (newValue != null) {
                    selectActivitiesTeam = newValue;
                } else {
                    activityTeamTableView = null;
                }
            }
        });
    }

    public void showTableActivityTeam(ActivitiesTeamCollection activitiesTeamCollection) {
        TableColumn<ActivitiesTeam, String> titleColumn = new TableColumn<>("title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<ActivitiesTeam, String> detailColumn = new TableColumn<>("Detail");
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));

        TableColumn<ActivitiesTeam, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<ActivitiesTeam, String> startDateColumn = new TableColumn<>("startDate");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<ActivitiesTeam, String> endDateColumn = new TableColumn<>("endDate");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        statusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ActivitiesTeam, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ActivitiesTeam, String> parameter) {
                ActivitiesTeam activitiesTeam = parameter.getValue();
                return Bindings.createStringBinding(() -> activitiesTeam.getStatus());

            }
        });

        activityTeamTableView.getColumns().clear();
        activityTeamTableView.getColumns().addAll(titleColumn,detailColumn,statusColumn,startDateColumn,endDateColumn);

        for (ActivitiesTeam activitiesTeam : activitiesTeamCollection.getActivitiesArrayList()) {
            activityTeamTableView.getItems().add(activitiesTeam);
        }
    }

    @FXML
    void onAddActivityTeam(ActionEvent event) {
        try {
            FXRouter.goTo("add-schedule-team", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onBack(ActionEvent event) {
        try {
            FXRouter.goTo("set-event-detail", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onDone(ActionEvent event) {
        try {
            if (selectActivitiesTeam == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "please select activity", ButtonType.OK);
                alert.showAndWait();
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onEditActivityTeam(ActionEvent event) {
        try {
            if (selectActivitiesTeam == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "please select activity", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            this.routeProvider.addHashMap("select-activity-team", this.selectActivitiesTeam);
            FXRouter.goTo("edit-schedule-team", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void selectUserTeamOnTableView() {
        memberTeamTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue observableValue, User oldValue, User newValue) {
                if (newValue != null) {
                    selectMemberInTeam = newValue;
                } else {
                    selectMemberInTeam = null;
                }
            }
        });
    }

    public void showTableMemberInTeam(UserCollection userInTeamCollection) {
        ManyToManyManager manyToManyManagerSuspend = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM_SUSPEND);

        TableColumn<User, String> userNameColumn = new TableColumn<>("userName");
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        TableColumn<User, String> nameUserColumn = new TableColumn<>("nameUser");
        nameUserColumn.setCellValueFactory(new PropertyValueFactory<>("nameUser"));

        TableColumn<User, String> statusColumn = new TableColumn<>("nameUser");

        statusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> parameter) {
                User user = parameter.getValue();
                boolean isSuspend = manyToManyManagerSuspend.checkIsExisted(new ManyToMany(user.getId(), team.getId()));
                return Bindings.createStringBinding(() -> isSuspend ? "Suspend" : "Active");
            }
        });

        memberTeamTableView.getColumns().clear();
        memberTeamTableView.getItems().clear();
        memberTeamTableView.getColumns().addAll(userNameColumn, nameUserColumn, statusColumn);

        for (User user : userInTeamCollection.getUsers()) {
            memberTeamTableView.getItems().add(user);
        }

    }


    @FXML
    void onSuspendUser(ActionEvent event) {
        if (selectMemberInTeam != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You want to suspend : " + selectMemberInTeam.getUserName() + " ?", ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM_SUSPEND);
                manyToManyManager.add(new ManyToMany(selectMemberInTeam.getId(), team.getId()));
                selectMemberInTeam = null;
                this.showTableMemberInTeam(this.userInTeamCollection);
            }
        }
    }

    @FXML
    void onUsCancelSuspend(ActionEvent event) {
        if (selectMemberInTeam != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You want to unSuspend : " + selectMemberInTeam.getUserName() + " ?", ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM_SUSPEND);
                manyToManyManager.remove(new ManyToMany(selectMemberInTeam.getId(), team.getId()));
                selectMemberInTeam = null;
                this.showTableMemberInTeam(this.userInTeamCollection);
            }
        }
    }
}
