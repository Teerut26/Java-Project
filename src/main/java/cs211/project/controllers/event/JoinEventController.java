package cs211.project.controllers.event;

import cs211.project.models.*;
import cs211.project.models.collections.ActivitiesEventCollection;
import cs211.project.models.collections.ManyToManyCollection;
import cs211.project.models.collections.UserCollection;

import cs211.project.services.FXRouter;
import cs211.project.services.ManyToManyManager;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.ActivitiesEventFileListDatesource;
import cs211.project.services.datasource.ManyToManyFileListDatasource;
import cs211.project.services.datasource.UserFileListDatasource;
import cs211.project.utils.ComponentRegister;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class JoinEventController extends ComponentRegister {
    @FXML
    private BorderPane parentBorderPane;
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    @FXML
    private Label currentUserAmount;
    @FXML
    private Label eventDescriptionLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Rectangle eventImage;
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label eventTime;
    @FXML
    private Label maxUserAmount;
    @FXML
    private TableView memberTableView;
    @FXML
    private TableView activitiesTableView;
    private Event event;
    private ActivitiesEvent activitiesEventSelect;
    private RouteProvider<Event> routeProvider;

    private Boolean isSuspending = false;
    @FXML
    private Label suspendLabel;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
        this.event = routeProvider.getData();

        ManyToManyManager manyToManyManager = new ManyToManyManager(
                new ManyToManyFileListDatasource().MTM_USER_EVENT_SUSPEND);
        Boolean isSuspend = manyToManyManager
                .checkIsExisted(new ManyToMany(routeProvider.getUserSession().getId(), event.getEventID()));
        if (isSuspend) {
            this.isSuspending = true;
            suspendLabel.setVisible(true);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("You are suspended from this event");
            alert.setContentText("You can't see any activity in this event");
            alert.showAndWait();

        } else {
            this.isSuspending = false;
            suspendLabel.setVisible(false);
        }

        this.setContent();

        activitiesTableView.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<ActivitiesEvent>() {
                    @Override
                    public void changed(ObservableValue<? extends ActivitiesEvent> observableValue,
                            ActivitiesEvent oldValue, ActivitiesEvent newValue) {
                        if (newValue != null) {
                            routeProvider.addHashMap("activity-event-select", newValue);
                            routeProvider.addHashMap("back-value", "event-detail-joined");
                            try {
                                FXRouter.goTo("comment-activity-event", routeProvider);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                });

        this.initializeThemeMode();
    }

    @FXML
    public void initializeThemeMode() {
        if (this.routeProvider.getUserSession().getThemeMode().equals("dark")) {
            parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/light-mode.css");
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/dark-mode.css");
        } else if (this.routeProvider.getUserSession().getThemeMode().equals("light")) {
            parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/dark-mode.css");
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/light-mode.css");
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

    public void setContent() {
        this.eventNameLabel.setText(event.getNameEvent());
        this.eventDescriptionLabel.setText(event.getDescriptionEvent());
        this.locationLabel.setText(event.getLocation());
        this.currentUserAmount.setText(String.valueOf(event.getCurrentMemberParticipatingAmount()));
        this.maxUserAmount.setText(String.valueOf(event.getQuantityEvent()));
        this.eventTime.setText(event.getStartDate().format(Event.DATE_FORMATTER) + " - "
                + event.getEndDate().format(Event.DATE_FORMATTER));
        Image img = new Image("file:" + event.getImageEvent());
        this.eventImage.setFill(new ImagePattern(img));
        this.showTableMember();

        this.showTableActivities();
    }

    private void showTableMember() {
        UserCollection userCollection = new UserFileListDatasource().readData();
        ManyToManyCollection manyToManyCollection = new ManyToManyFileListDatasource(
                new ManyToManyFileListDatasource().MTM_USER_EVENT_SUSPEND).readData();
        ManyToManyCollection manyToManyCollectionUserEvent = new ManyToManyFileListDatasource(
                new ManyToManyFileListDatasource().MTM_USER_EVENT).readData();

        UserCollection newUserCollection = new UserCollection();
        userCollection.getUsers().forEach((user) -> {
            boolean isJoin = manyToManyCollectionUserEvent
                    .checkIsExisted(new ManyToMany(user.getId(), event.getEventID()));
            if (isJoin) {
                newUserCollection.add(user);
            }
        });

        TableColumn<User, String> userNameColumn = new TableColumn<>("UserName");
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        memberTableView.getColumns().clear();
        memberTableView.getColumns().add(userNameColumn);

        memberTableView.getItems().clear();

        for (User user : newUserCollection.getUsers()) {
            memberTableView.getItems().add(user);
        }
    }

    private void showTableActivities() {
        ActivitiesEventCollection activitiesEventCollection = new ActivitiesEventFileListDatesource().readData()
                .finsdByEventId(event.getEventID());

        TableColumn<User, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<User, String> startDateColumn = new TableColumn<>("startDate");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateStart"));

        TableColumn<User, String> endDateColumn = new TableColumn<>("endDate");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));

        activitiesTableView.getColumns().clear();
        activitiesTableView.getColumns().add(titleColumn);
        activitiesTableView.getColumns().add(startDateColumn);
        activitiesTableView.getColumns().add(endDateColumn);
        activitiesTableView.getItems().clear();

        if (this.isSuspending) {
            return;
        }
        for (ActivitiesEvent activitiesEvent : activitiesEventCollection.getActivitiesArrayList()) {
            activitiesTableView.getItems().add(activitiesEvent);
        }

        activitiesTableView.setOnMouseClicked(param -> {
            if (param.getClickCount() == 1) {
                activitiesEventSelect = (ActivitiesEvent) activitiesTableView.getSelectionModel().getSelectedItem();
            }
        });

    }

    @FXML
    public void commentActivityEvent() {
        if (activitiesEventSelect == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "please select activity", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        try {
            this.routeProvider.addHashMap("activity-event-select", activitiesEventSelect);
            this.routeProvider.addHashMap("back-value", "event-detail-joined");
            FXRouter.goTo("comment-activity-event", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToTeamList() {
        try {
            this.routeProvider.addHashMap("oldRoute", "event-detail-joined");
            this.routeProvider.addHashMap("eventID", this.event.getEventID());
            FXRouter.goTo("event-team-list", this.routeProvider);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onBack() {
        try {
            FXRouter.goTo("event-history", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
