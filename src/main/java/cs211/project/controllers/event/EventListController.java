package cs211.project.controllers.event;

import cs211.project.Main;
import cs211.project.controllers.components.EventCardComponentController;
import cs211.project.models.Event;
import cs211.project.models.ManyToMany;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.ManyToManyCollection;
import cs211.project.models.collections.TeamCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.ManyToManyManager;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.services.datasource.ManyToManyFileListDatasource;
import cs211.project.services.datasource.TeamFileListDatasource;
import cs211.project.utils.ComponentLoader;
import cs211.project.utils.ComponentRegister;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class EventListController {
    @FXML
    private BorderPane parentBorderPane;
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    @FXML
    private VBox vBoxEventlist;
    @FXML
    private Label loading;
    @FXML
    private ScrollPane eventListScrollPane;
    @FXML
    private TextField searchBar;
    private int currentBatch = 0;
    private int batchSize = 5;
    private EventCollection eventCollection;
    private RouteProvider routeProvider;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        ComponentRegister componentRegister = new ComponentRegister();
        componentRegister.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        componentRegister.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        EventFileListDatesource eventFileListDatesource = new EventFileListDatesource();

        this.eventCollection = new EventCollection();
        ManyToManyCollection manyToManyCollectionUserJoinedEvent = new ManyToManyFileListDatasource(
                new ManyToManyFileListDatasource().MTM_USER_EVENT).readData();

        eventFileListDatesource.readData().getEvents().forEach(event -> {
            boolean isJoined = manyToManyCollectionUserJoinedEvent
                    .checkIsExisted(new ManyToMany(routeProvider.getUserSession().getId(), event.getEventID()));
            boolean isTeamMember = checkJoinTeam(event);
            if (event.isPublic() && !isJoined && !isTeamMember) {
                this.eventCollection.add(event);
            }
        });

        this.initEventListScrollPane();
        this.eventListScrollPaneListener();
        this.searchEngine();
        this.initializeFont();
        this.initializeThemeMode();
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

    private void loadNextBatch(List<Event> events) {
        for (int i = currentBatch; i < Math.min(currentBatch + batchSize, events.size()); i++) {
            try {
                FXMLLoader fxmlLoader = new ComponentLoader("EventCardComponent.fxml").getFxmlLoader();
                Pane eventCardComponent = fxmlLoader.load();
                EventCardComponentController eventCardComponentController = fxmlLoader.getController();
                eventCardComponentController.setData(events.get(i));
                eventCardComponentController.setRouteProvider(this.routeProvider);
                vBoxEventlist.getChildren().add(eventCardComponent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        currentBatch += batchSize;
    }

    public void eventListScrollPaneListener() {
        this.eventListScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            boolean isLoadmore = currentBatch < eventCollection.getEvents().size();
            if (newValue.doubleValue() == 1.0 && isLoadmore) {
                ExecutorService executorService = Executors.newCachedThreadPool();
                this.loading.setVisible(true);
                executorService.execute(() -> {
                    Platform.runLater(() -> {
                        loadNextBatch(eventCollection.getEvents());
                        loading.setVisible(false);
                    });
                });
                executorService.shutdown();
            }
        });
    }

    public void initEventListScrollPane() {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(() -> {
            Platform.runLater(() -> {
                loadNextBatch(eventCollection.getEvents());
                loading.setVisible(false);
            });
        });

        executor.shutdown();
    }

    public void searchEngine() {
        ObservableList<Event> observableEvents = FXCollections.observableArrayList(eventCollection.getEvents());
        FilteredList<Event> filteredEvents = new FilteredList<>(observableEvents);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredEvents.setPredicate(event -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCase = newValue.toLowerCase();
                if (event.getNameEvent().toLowerCase().indexOf(lowerCase) == -1) {
                    return false;
                } else {
                    return true;
                }
            });
            vBoxEventlist.getChildren().clear();
            currentBatch = 0;
            loadNextBatch(filteredEvents);
        });
    }

    public Boolean checkJoinTeam(Event event) {
        AtomicReference<Boolean> isJoined = new AtomicReference<>(false);
        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
        TeamFileListDatasource teamFileListDatasource = new TeamFileListDatasource();
        TeamCollection teamCollection = teamFileListDatasource.readData().findByEvent(event);
        teamCollection.getTeams().forEach(team -> {
            if (manyToManyManager
                    .checkIsExisted(new ManyToMany(this.routeProvider.getUserSession().getId(), team.getId()))) {
                isJoined.set(true);
            }
        });
        return isJoined.get();
    }



}
