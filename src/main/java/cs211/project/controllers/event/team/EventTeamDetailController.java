package cs211.project.controllers.event.team;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

public class EventTeamDetailController extends ComponentRegister {
    @FXML
    private BorderPane parentBorderPane;
    @FXML
    private HBox NavBarHBox;

    @FXML
    private VBox SideBarVBox;

    @FXML
    private TableView<ActivitiesTeam> activityTeamTableView;

    @FXML
    private TableView<User> memberTeamTableView;

    @FXML
    private Label teamName;

    @FXML
    private Button headOfTeamButton;

    @FXML
    private Button extraButton;

    @FXML
    private Button suspendButton;

    private ActivitiesTeamFileListDatesource activitiesTeamFileListDatesource;
    private ActivitiesTeamCollection activitiesTeamCollection;
    private ActivitiesTeam selectActivitiesTeam;
    private UserCollection userInTeamCollection;
    private User selectMemberInTeam;
    private Team team;
    private RouteProvider<Event> routeProvider;
    private CommentActivitiesTeamFileListDatasource commentActivitiesTeamFileListDatasource;

    private Boolean isHeadOfTeam = false;




    @FXML
    public void initialize() {
        initializeComponents();
        loadTeamDetailData();
        selectActivityTeamOnTableView();
        showTableActivityTeam(activitiesTeamCollection);
        selectUserTeamOnTableView();
        this.checkIsHeadOfTeam();
        this.initializeThemeMode();
        this.initializeFont();
        suspendButton.setVisible(false);
        extraButton.setVisible(false);
        headOfTeamButton.setVisible(false);
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

    private void initializeComponents() {
        routeProvider = (RouteProvider) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
    }

    private void loadTeamDetailData() {

        activitiesTeamFileListDatesource = new ActivitiesTeamFileListDatesource();
        activitiesTeamCollection = activitiesTeamFileListDatesource.readData();

        this.team = (Team) this.routeProvider.getDataHashMap().get("team-select");
        this.teamName.setText(this.team.getName());
        ActivitiesTeamCollection newActivityTeamCollection = new ActivitiesTeamCollection();
        activitiesTeamCollection.getActivitiesArrayList().forEach((activitiesTeam -> {
            if (activitiesTeam.getTeam().getId().equals(this.team.getId())) {
                newActivityTeamCollection.add(activitiesTeam);
            }
        }));
        activitiesTeamCollection = newActivityTeamCollection;

        ManyToManyCollection manyToManyCollection = new ManyToManyManager(
                new ManyToManyFileListDatasource().MTM_USER_TEAM).findsByB(this.team.getId());
        UserCollection userCollection = new UserFileListDatasource().readData();

        UserCollection newUserColletion = new UserCollection();
        manyToManyCollection.getManyToManies().forEach((manyToMany -> {
            newUserColletion.add(userCollection.findById(manyToMany.getA()));
        }));

        userInTeamCollection = newUserColletion;

        showTableMemberInTeam(this.userInTeamCollection);
    }

    public void selectActivityTeamOnTableView() {
        activityTeamTableView.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<ActivitiesTeam>() {
                    @Override
                    public void changed(ObservableValue observable, ActivitiesTeam oldValue, ActivitiesTeam newValue) {
                        if (newValue != null) {
                            selectActivitiesTeam = newValue;
                        } else {
                            selectActivitiesTeam = null;
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
        startDateColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ActivitiesTeam, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<ActivitiesTeam, String> parameter) {
                        ActivitiesTeam activitiesTeam = parameter.getValue();
                        return Bindings.createStringBinding(() -> activitiesTeam.getDateStart()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                    }
                });

        TableColumn<ActivitiesTeam, String> endDateColumn = new TableColumn<>("endDate");
        endDateColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ActivitiesTeam, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<ActivitiesTeam, String> parameter) {
                        ActivitiesTeam activitiesTeam = parameter.getValue();
                        return Bindings.createStringBinding(() -> activitiesTeam.getDateEnd()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                    }
                });

        statusColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ActivitiesTeam, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<ActivitiesTeam, String> parameter) {
                        ActivitiesTeam activitiesTeam = parameter.getValue();
                        return Bindings.createStringBinding(() -> activitiesTeam.getStatus());

                    }
                });

        activityTeamTableView.getColumns().clear();
        activityTeamTableView.getColumns().addAll(titleColumn, detailColumn, startDateColumn, endDateColumn,
                statusColumn);
        activityTeamTableView.getItems().clear();

        for (ActivitiesTeam activitiesTeam : activitiesTeamCollection.getActivitiesArrayList()) {
            activityTeamTableView.getItems().add(activitiesTeam);
        }
    }

    @FXML
    public void onAddActivityTeam(ActionEvent event) {
        try {
            FXRouter.goTo("add-schedule-team", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onBack(ActionEvent event) {
        try {
            if(isHeadOfTeam){
                routeProvider.addHashMap("teamJoined", this.team);
                FXRouter.goTo("my-team", this.routeProvider);
            }else {
                FXRouter.goTo("set-event-detail", this.routeProvider);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onViewActivityTeam(ActionEvent event) {
        try {
            if (selectActivitiesTeam == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "please select activity", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            this.routeProvider.addHashMap("activity-select", this.selectActivitiesTeam);
            FXRouter.goTo("comment-activity-team", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onEditActivityTeam(ActionEvent event) {
        try {
            if (selectActivitiesTeam == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "please select activity", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            this.routeProvider.addHashMap("activity-team-select", this.selectActivitiesTeam);
            FXRouter.goTo("edit-schedule-team", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onActivityEventTeamDelete() {
        if (selectActivitiesTeam != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "You want to delete : " + selectActivitiesTeam.getTitle() + " ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.NO) {
                return;
            }
            ActivitiesTeamCollection newActivityTeamCollection = new ActivitiesTeamCollection();
            for (ActivitiesTeam activitiesTeam : activitiesTeamCollection.getActivitiesArrayList()) {
                if (!activitiesTeam.getId().equals(selectActivitiesTeam.getId())) {
                    newActivityTeamCollection.add(activitiesTeam);
                }
                if (activitiesTeam.getId().equals(selectActivitiesTeam.getId())) {

                    commentActivitiesTeamFileListDatasource = new CommentActivitiesTeamFileListDatasource();

                    CommentActivitiesTeamCollection newCommentActivitiesTeamCollection = new CommentActivitiesTeamCollection();
                    commentActivitiesTeamFileListDatasource.readData().getComments().forEach(commentActivitiesTeam -> {
                        if (!commentActivitiesTeam.getActivitiesTeam().getId().equals(selectActivitiesTeam.getId())) {
                            newCommentActivitiesTeamCollection.add(commentActivitiesTeam);
                        }
                    });
                    commentActivitiesTeamFileListDatasource.writeData(newCommentActivitiesTeamCollection);
                }
            }
            activitiesTeamFileListDatesource.writeData(newActivityTeamCollection);
            activitiesTeamCollection = newActivityTeamCollection;
            showTableActivityTeam(activitiesTeamCollection);
        }
    }

    public void selectUserTeamOnTableView() {
        memberTeamTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue observableValue, User oldValue, User newValue) {
                if (newValue != null) {
                    selectMemberInTeam = newValue;
                    suspendButton.setVisible(true);
                    if(!isHeadOfTeam){
                        extraButton.setVisible(true);
                        headOfTeamButton.setVisible(true);
                    }
                    setSuspendButton();
                    setExtraButton();
                    setHeadOfTeamButton();
                } else {
                    selectMemberInTeam = null;
                }
            }
        });
    }




    public void showTableMemberInTeam(UserCollection userInTeamCollection) {
        ManyToManyManager manyToManyManagerSuspend = new ManyToManyManager(
                new ManyToManyFileListDatasource().MTM_USER_TEAM_SUSPEND);

        TableColumn<User, String> imageColumn = new TableColumn<>("Profile");
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageProfile"));
        imageColumn.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>() {
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

        TableColumn<User, String> userNameColumn = new TableColumn<>("UserName");
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        TableColumn<User, String> nameUserColumn = new TableColumn<>("Name");
        nameUserColumn.setCellValueFactory(new PropertyValueFactory<>("nameUser"));

        TableColumn<User, String> statusColumn = new TableColumn<>("status");
        statusColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> parameter) {
                        User user = parameter.getValue();
                        boolean isSuspend = manyToManyManagerSuspend
                                .checkIsExisted(new ManyToMany(user.getId(), team.getId()));
                        return Bindings.createStringBinding(() -> isSuspend ? "Suspend" : "Active");
                    }
                });

        TableColumn<User, String> typeColumn = new TableColumn<>("Type of member");
        typeColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> parameter) {
                        User user = parameter.getValue();
                        AtomicReference<Boolean> isExtra = new AtomicReference<>(false);
                        ManyToManyManager manyToManyManager = new ManyToManyManager(
                                new ManyToManyFileListDatasource().MTM_USER_TEAM_EXTRA);
                        TeamCollection teamCollection = new TeamFileListDatasource().readData()
                                .findByEvent(team.getEvent());
                        teamCollection.getTeams().forEach(teamInEvent -> {
                            if (manyToManyManager.checkIsExisted(new ManyToMany(user.getId(), teamInEvent.getId()))) {
                                isExtra.set(true);
                            }

                        });
                        return Bindings.createStringBinding(() -> isExtra.get() ? "Extra" : "General");
                    }
                });
        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> parameter) {
            User user = parameter.getValue();
                AtomicReference<Boolean> isHead = new AtomicReference<>(false);
            ManyToManyManager manyToManyManager = new ManyToManyManager(
                    new ManyToManyFileListDatasource().MTM_USER_TEAM_HEAD);
            TeamCollection teamCollection = new TeamFileListDatasource().readData()
                    .findByEvent(team.getEvent());
            teamCollection.getTeams().forEach(teamInEvent -> {
                if (manyToManyManager.checkIsExisted(new ManyToMany(user.getId(), teamInEvent.getId()))) {
                    isHead.set(true);
                }
            });
            return Bindings.createStringBinding(() -> isHead.get() ? "Head" : "Member");
            }
        });



                memberTeamTableView.getColumns().clear();
        memberTeamTableView.getItems().clear();
        memberTeamTableView.getColumns().addAll(imageColumn, userNameColumn, nameUserColumn, statusColumn,typeColumn ,roleColumn);

        for (User user : userInTeamCollection.getUsers()) {
            memberTeamTableView.getItems().add(user);
        }

    }

    @FXML
    void onSetExtraUser() {
        if (selectMemberInTeam != null) {
            ManyToManyManager manyToManyManager = new ManyToManyManager(
                    new ManyToManyFileListDatasource().MTM_USER_TEAM_EXTRA);
            if( manyToManyManager.checkIsExisted(new ManyToMany(selectMemberInTeam.getId(), team.getId()))) {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "You want to unset extra of team : " + selectMemberInTeam.getUserName() + " ?", ButtonType.OK,
                        ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    onUnSetExtraUser();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "You want to set head of team : " + selectMemberInTeam.getUserName() + " ?", ButtonType.OK,
                        ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {

                    manyToManyManager.add(new ManyToMany(selectMemberInTeam.getId(), team.getId()));
                    selectMemberInTeam = null;
                    hideAllButton();
                    this.showTableMemberInTeam(this.userInTeamCollection);
                }
            }
        }

    }

    @FXML
    void onUnSetExtraUser() {
        if (selectMemberInTeam != null) {

            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "You want to unset head of team : " + selectMemberInTeam.getUserName() + " ?", ButtonType.OK,
                    ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                ManyToManyManager manyToManyManager = new ManyToManyManager(
                        new ManyToManyFileListDatasource().MTM_USER_TEAM_EXTRA);
                manyToManyManager.remove(new ManyToMany(selectMemberInTeam.getId(), team.getId()));
                selectMemberInTeam = null;
                hideAllButton();
                this.showTableMemberInTeam(this.userInTeamCollection);
            }
        }
    }

    @FXML
    void onSetHeadOfTeam(){
        if (selectMemberInTeam != null) {
            ManyToManyManager manyToManyManager = new ManyToManyManager(
                    new ManyToManyFileListDatasource().MTM_USER_TEAM_HEAD);
            if( manyToManyManager.checkIsExisted(new ManyToMany(selectMemberInTeam.getId(), team.getId()))) {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "You want to unset head of team : " + selectMemberInTeam.getUserName() + " ?", ButtonType.OK,
                        ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    onUnSetHeadOfTeam();
                }

            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "You want to set head of team : " + selectMemberInTeam.getUserName() + " ?", ButtonType.OK,
                        ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {

                    if (manyToManyManager.findsByB(team.getId()).size() > 0) {
                        manyToManyManager.removeByB(team.getId());
                    }
                    manyToManyManager.add(new ManyToMany(selectMemberInTeam.getId(), team.getId()));
                    selectMemberInTeam = null;
                    hideAllButton();
                    this.showTableMemberInTeam(this.userInTeamCollection);
                }
            }
        }
}


        @FXML
        public void onUnSetHeadOfTeam() {
            if (selectMemberInTeam != null) {

                    Alert alert = new Alert(Alert.AlertType.WARNING,
                            "You want to unset head of team : " + selectMemberInTeam.getUserName() + " ?", ButtonType.OK,
                            ButtonType.CANCEL);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.OK) {
                        ManyToManyManager manyToManyManager = new ManyToManyManager(
                                new ManyToManyFileListDatasource().MTM_USER_TEAM_HEAD);
                        manyToManyManager.remove(new ManyToMany(selectMemberInTeam.getId(), team.getId()));
                        selectMemberInTeam = null;
                        hideAllButton();
                        this.showTableMemberInTeam(this.userInTeamCollection);
                    }
            }
        }

    @FXML
    void onSuspendUser(ActionEvent event) {
        ManyToManyManager manyToManyManager = new ManyToManyManager(
                new ManyToManyFileListDatasource().MTM_USER_TEAM_SUSPEND);
        if(manyToManyManager.checkIsExisted(new ManyToMany(selectMemberInTeam.getId(), team.getId()))) {
            onUsCancelSuspend(event);
            }else {
            if (selectMemberInTeam != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "You want to suspend : " + selectMemberInTeam.getUserName() + " ?", ButtonType.OK,
                        ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {

                    manyToManyManager.add(new ManyToMany(selectMemberInTeam.getId(), team.getId()));
                    selectMemberInTeam = null;
                    hideAllButton();
                    this.showTableMemberInTeam(this.userInTeamCollection);
                }
            }
        }
    }

    @FXML
    void onUsCancelSuspend(ActionEvent event) {
        if (selectMemberInTeam != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "You want to unSuspend : " + selectMemberInTeam.getUserName() + " ?", ButtonType.OK,
                    ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                ManyToManyManager manyToManyManager = new ManyToManyManager(
                        new ManyToManyFileListDatasource().MTM_USER_TEAM_SUSPEND);
                manyToManyManager.remove(new ManyToMany(selectMemberInTeam.getId(), team.getId()));
                selectMemberInTeam = null;
                hideAllButton();
                this.showTableMemberInTeam(this.userInTeamCollection);
            }
        }
    }

    public void checkIsHeadOfTeam(){
        ManyToManyManager manyToManyManager = new ManyToManyManager(
                new ManyToManyFileListDatasource().MTM_USER_TEAM_HEAD);
        if (manyToManyManager.checkIsExisted(new ManyToMany(routeProvider.getUserSession().getId(),team.getId()))) {
            headOfTeamButton.setVisible(false);
            extraButton.setVisible(false);
            isHeadOfTeam = true;
        } else {
            headOfTeamButton.setVisible(true);
            extraButton.setVisible(true);
            isHeadOfTeam = false;


        }
    }



    public void setSuspendButton(){
        ManyToManyManager manyToManyManager = new ManyToManyManager(
                new ManyToManyFileListDatasource().MTM_USER_TEAM_SUSPEND);
        if(manyToManyManager.checkIsExisted(new ManyToMany(selectMemberInTeam.getId(), team.getId()))) {
            suspendButton.setText("UnSuspend");
            suspendButton.getStyleClass().remove("btn-primary-active");
            suspendButton.getStyleClass().add("btn-error");
         }else{
            suspendButton.setText("Suspend");
            suspendButton.getStyleClass().remove("btn-error");
            suspendButton.getStyleClass().add("btn-primary-active");

        }
        if(routeProvider.getUserSession().getId().equals(selectMemberInTeam.getId())) {
            suspendButton.setDisable(true);
        }else{
            suspendButton.setDisable(false);
        }
    }

    public void setExtraButton(){
            ManyToManyManager manyToManyManager = new ManyToManyManager(
                    new ManyToManyFileListDatasource().MTM_USER_TEAM_EXTRA);
            if( manyToManyManager.checkIsExisted(new ManyToMany(selectMemberInTeam.getId(), team.getId()))) {
                extraButton.setText("UnSet Extra");
                extraButton.getStyleClass().remove("btn-primary-active");
                extraButton.getStyleClass().add("btn-error");
            }else{
                extraButton.setText("Set Extra");
                extraButton.getStyleClass().remove("btn-error");
                extraButton.getStyleClass().add("btn-primary-active");
            }
    }

    public void setHeadOfTeamButton(){

            ManyToManyManager manyToManyManager = new ManyToManyManager(
                    new ManyToManyFileListDatasource().MTM_USER_TEAM_HEAD);
            if (manyToManyManager.checkIsExisted(new ManyToMany(selectMemberInTeam.getId(), team.getId()))) {
                headOfTeamButton.setText("UnSet Head");
                headOfTeamButton.getStyleClass().remove("btn-primary-active");
                headOfTeamButton.getStyleClass().add("btn-error");
            } else {
                headOfTeamButton.setText("Set Head");
                headOfTeamButton.getStyleClass().remove("btn-error");
                headOfTeamButton.getStyleClass().add("btn-primary-active");
            }


    }

    public void hideAllButton() {
        headOfTeamButton.setVisible(false);
        extraButton.setVisible(false);
        suspendButton.setVisible(false);
    }
}
