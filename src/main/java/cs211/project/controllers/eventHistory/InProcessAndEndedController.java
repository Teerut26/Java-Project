package cs211.project.controllers.eventHistory;

import cs211.project.Main;
import cs211.project.controllers.components.EventCardComponentController;
import cs211.project.controllers.components.InProcessCardComponentController;
import cs211.project.controllers.components.InTeamCardComponentController;
import cs211.project.models.Event;
import cs211.project.models.ManyToMany;
import cs211.project.models.Team;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InProcessAndEndedController {
    @FXML
    private BorderPane parentBorderPane;
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    @FXML
    private VBox vBoxEventlistInProcess;

    @FXML
    private VBox vBoxEventlistEnded;

    @FXML
    private VBox vBoxEventlistInTeam;

    @FXML
    private TabPane tabPaneId;
    private boolean firstLoadTabPane = true;

    @FXML
    private Label loading;
    @FXML
    private ScrollPane inprocessEventListScrollPane;
    @FXML
    private ScrollPane endedEventListScrollPane;
    @FXML
    private ScrollPane inTeamEventListScrollPane;

    private int inprocessCurrentBatch = 0;
    private int endedCurrentBatch = 0;
    private int inprocessbatchSize = 5;
    private int endedbatchSize = 5;

    private int inTeamCurrentBatch = 0;
    private int inTeambatchSize = 5;

    private EventCollection inProcessEventCollection;
    private EventCollection endedEventCollection;
    private EventCollection inTeamEventCollection;



    private RouteProvider routeProvider;

    @FXML
    public void initialize() {

        routeProvider = (RouteProvider) FXRouter.getData();
        ComponentRegister componentRegister = new ComponentRegister();
        componentRegister.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        componentRegister.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
        this.InitializeThemeMode();
        this.initializeFont();
        EventFileListDatesource eventFileListDatesource = new EventFileListDatesource();

        this.inProcessEventCollection = new EventCollection();
        this.endedEventCollection = new EventCollection();
        this.inTeamEventCollection = new EventCollection();

       EventCollection eventCollection = eventFileListDatesource.readData();

       EventCollection newEventCollection = new EventCollection();

        ManyToManyCollection manyToManyCollection = new ManyToManyFileListDatasource(new ManyToManyFileListDatasource().MTM_USER_EVENT).readData();
        manyToManyCollection.findsByA(this.routeProvider.getUserSession().getId()).forEach(manyToMany -> {

            Event event = eventCollection.findById(manyToMany.getB());
            if (event != null) {
                newEventCollection.add(event);
            }
        });

        if (newEventCollection.getEvents().size() != 0) {

            newEventCollection.getEvents().forEach(event -> {
                if (event.checkEventEnded()) {
                    this.endedEventCollection.add(event);
                } else {
                    this.inProcessEventCollection.add(event);
                }
            });
        }
        TeamCollection teamCollection = new TeamFileListDatasource().readData();

        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_TEAM);
         manyToManyManager.findsByA(this.routeProvider.getUserSession().getId()).getManyToManies().forEach(manyToMany -> {
             Team team = teamCollection.findById(manyToMany.getB());
                if (team != null) {
                    this.inTeamEventCollection.add(team.getEvent());
                }
         });


    if (firstLoadTabPane) {
        initInProcessEventListScrollPane();
        inprocessEventListScrollPaneListener();
            firstLoadTabPane = false;
        }

        tabPaneId.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            try {
                if (newTab != null) {
                    String tabId = newTab.getId();
                    if (tabId.equals("inProcessTab")) {
                        initInProcessEventListScrollPane();
                        inprocessEventListScrollPaneListener();
                    } else if (tabId.equals("endedTab")) {
                        initEndedEventListScrollPane();
                        endedEventListScrollPaneListener();
                    } else if (tabId.equals("inTeamTab")) {
                        initInTeamEventListScrollPane();
                        inTeamEventListScrollPaneListener();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @FXML
    public void InitializeThemeMode(){
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
    public void initializeFont(){
        String currentFont =this.routeProvider.getUserSession().getFont();
        clearFontStyle();
        if (currentFont.equals("font-style1")){
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style1.css");
        }else if (currentFont.equals("font-style2")){
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style2.css");
        }else if (currentFont.equals("font-style3")){
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style3.css");
        }

    }

    @FXML
    public void clearFontStyle(){
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style1.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style2.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style3.css");
    }


    private void loadNextBatchInProcess(List<Event> events) {
        for (int i = inprocessCurrentBatch; i < Math.min(inprocessCurrentBatch + inprocessbatchSize, events.size()); i++) {
            try {
                FXMLLoader fxmlLoader = new ComponentLoader("InProcessEventCardComponent.fxml").getFxmlLoader();
                Pane eventCardComponent = fxmlLoader.load();
                InProcessCardComponentController inProcessEventCardComponentController = fxmlLoader.getController();
                inProcessEventCardComponentController.setData(events.get(i));
                inProcessEventCardComponentController.setRouteProvider(this.routeProvider);
                vBoxEventlistInProcess.getChildren().add(eventCardComponent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        inprocessCurrentBatch += inprocessbatchSize;
    }

    private void loadNextBatchInEnded(List<Event> events) {
        for (int i = endedCurrentBatch; i < Math.min(endedCurrentBatch + endedbatchSize, events.size()); i++) {
            try {
                FXMLLoader fxmlLoader = new ComponentLoader("EndedEventCardComponent.fxml").getFxmlLoader();
                Pane eventCardComponent = fxmlLoader.load();
                EventCardComponentController eventCardComponentController = fxmlLoader.getController();
                eventCardComponentController.setData(events.get(i));
                eventCardComponentController.setRouteProvider(this.routeProvider);
                vBoxEventlistEnded.getChildren().add(eventCardComponent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        endedCurrentBatch += endedbatchSize;
    }

    private void loadNextBatchInTeam(List<Event> events){
        for (int i = inTeamCurrentBatch; i < Math.min(inTeamCurrentBatch + inTeambatchSize, events.size()); i++) {
            try {
                FXMLLoader fxmlLoader = new ComponentLoader("InTeamEventCardComponent.fxml").getFxmlLoader();
                Pane eventCardComponent = fxmlLoader.load();
                InTeamCardComponentController inTeamCardComponentController = fxmlLoader.getController();
                inTeamCardComponentController.setData(events.get(i));
                inTeamCardComponentController.setRouteProvider(this.routeProvider);
                vBoxEventlistInTeam.getChildren().add(eventCardComponent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        inTeamCurrentBatch += inTeambatchSize;
    }

    public void inprocessEventListScrollPaneListener(){
        this.inprocessEventListScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            boolean isLoadmore = inprocessCurrentBatch < inProcessEventCollection.getEvents().size();
            if (newValue.doubleValue() == 1.0 && isLoadmore) {
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.execute(() -> {
                    Platform.runLater(() -> {
                        loadNextBatchInProcess(inProcessEventCollection.getEvents());
                        loading.setVisible(false);
                    });
                });
                executorService.shutdown();
            }
        });

    }

    public void endedEventListScrollPaneListener(){
        this.endedEventListScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            boolean isLoadmore = endedCurrentBatch < endedEventCollection.getEvents().size();
            if (newValue.doubleValue() == 1.0 && isLoadmore) {
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.execute(() -> {
                    Platform.runLater(() -> {
                        loadNextBatchInEnded(endedEventCollection.getEvents());
                    });
                });
                executorService.shutdown();
            }
        });

    }

    public void inTeamEventListScrollPaneListener(){
        this.inTeamEventListScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            boolean isLoadmore = inTeamCurrentBatch < inTeamEventCollection.getEvents().size();
            if (newValue.doubleValue() == 1.0 && isLoadmore) {
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.execute(() -> {
                    Platform.runLater(() -> {
                        loadNextBatchInTeam(inTeamEventCollection.getEvents());
                    });
                });
                executorService.shutdown();
            }
        });

    }

    public void initInProcessEventListScrollPane() {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(() -> {
            Platform.runLater(() -> {
                loadNextBatchInProcess(inProcessEventCollection.getEvents());
            });
        });

        executor.shutdown();
    }

    public void initEndedEventListScrollPane() {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(() -> {
            Platform.runLater(() -> {
                loadNextBatchInEnded(endedEventCollection.getEvents());
            });
        });

        executor.shutdown();
    }

    public void initInTeamEventListScrollPane() {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(() -> {
            Platform.runLater(() -> {
                loadNextBatchInTeam(inTeamEventCollection.getEvents());
            });
        });

        executor.shutdown();
    }





}
