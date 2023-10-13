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

import java.io.IOException;
import java.time.LocalDate;
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
    private TableView<Team> teamTableView = new TableView<>();

    @FXML
    private TeamCollection teamForTableView = new TeamCollection();
    @FXML
    private Team currentTeamSelect;

    @FXML
    private CheckBox checkBoxIsInProgress;


    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        ComponentRegister componentRegister = new ComponentRegister();
        componentRegister.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        componentRegister.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
        setTeamInTableView();
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

    public void setTeamInTableView() {
        TeamFileListDatasource teamFileListDatasource = new TeamFileListDatasource();
        TeamCollection teamCollection = teamFileListDatasource.readData();

        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
        ManyToManyCollection manyToManyCollection = manyToManyManager.getAll();
        manyToManyCollection.findsByA(this.routeProvider.getUserSession().getId()).forEach(manyToMany -> {
            Team team = teamCollection.findById(manyToMany.getB());
            teamForTableView.add(team);
        });

        TableColumn<Team, String> eventNameColumn = new TableColumn<>("Event");
        eventNameColumn.setCellValueFactory(param -> {
            if (param.getValue() != null) {
                Team team = param.getValue();
                String eventName = team.getEvent().getNameEvent();
                return new ReadOnlyStringWrapper(eventName);
            } else {
                return new ReadOnlyStringWrapper("");
            }
        });

        TableColumn<Team, String> teamName = new TableColumn<>("Team Name");
        teamName.setCellValueFactory(new PropertyValueFactory<>("name"));

        //event status if event is display text ended
        TableColumn<Team, String> eventStatus = new TableColumn<>("Event Status");
        eventStatus.setCellValueFactory(param -> {
            if (param.getValue() != null) {
                Team team = param.getValue();
                if (team.getEvent().getEndDate().isBefore(LocalDate.now().atStartOfDay())) {
                    return new ReadOnlyStringWrapper("ended");

                }
                else {
                    return new ReadOnlyStringWrapper("in progress");
                }
            } else {
                return new ReadOnlyStringWrapper("");
            }
        });




        teamTableView.getColumns().clear();
        teamTableView.getColumns().addAll(eventNameColumn, teamName,eventStatus);
        teamTableView.getItems().clear();

        for (Team team : teamForTableView.getTeams()) {
            teamTableView.getItems().add(team);
        }

        teamTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                currentTeamSelect = teamTableView.getSelectionModel().getSelectedItem();

            }
        });
    }

    @FXML
    public void onCheckInProgress (){
        if(checkBoxIsInProgress.isSelected()){
            teamTableView.getItems().clear();
            for (Team team : teamForTableView.getTeams()) {
                if(team.getEvent().getEndDate().isAfter(LocalDate.now().atStartOfDay())){
                    teamTableView.getItems().add(team);
                }
            }
        }
        else {
            teamTableView.getItems().clear();
            for (Team team : teamForTableView.getTeams()) {
                teamTableView.getItems().add(team);
            }
        }
    }



    @FXML
    public void gotoViewTeam() {
        if(currentTeamSelect == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please select team");
            alert.showAndWait();
            return;
        }
        try {
            routeProvider.addHashMap("teamJoined", this.currentTeamSelect);
            routeProvider.addHashMap("fromPage", "my-team");
            FXRouter.goTo("view-my-team", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
