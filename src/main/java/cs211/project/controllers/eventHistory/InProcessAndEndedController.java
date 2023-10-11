package cs211.project.controllers.eventHistory;

import cs211.project.controllers.components.EventCardComponentController;
import cs211.project.controllers.components.InProcessCardComponentController;
import cs211.project.models.Event;
import cs211.project.models.ManyToMany;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.ManyToManyCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.ManyToManyManager;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.services.datasource.ManyToManyFileListDatasource;
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

public class InProcessAndEndedController extends ComponentRegister {
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
    private TabPane tabPaneId;
    private boolean firstLoadTabPane = true;

    @FXML
    private Label loading;
    @FXML
    private ScrollPane inprocessEventListScrollPane;
    @FXML
    private ScrollPane endedEventListScrollPane;

    private int inprocessCurrentBatch = 0;
    private int endedCurrentBatch = 0;
    private int inprocessbatchSize = 5;
    private int endedbatchSize = 5;

    private EventCollection inProcessEventCollection;
    private EventCollection endedEventCollection;



    private RouteProvider routeProvider;

    @FXML
    public void initialize() {

        routeProvider = (RouteProvider) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
        this.InitializeThemeMode();
        this.initializeFont();
        EventFileListDatesource eventFileListDatesource = new EventFileListDatesource();

        this.inProcessEventCollection = new EventCollection();
        this.endedEventCollection = new EventCollection();

       EventCollection eventCollection = eventFileListDatesource.readData();

       EventCollection newEventCollection = new EventCollection();

        ManyToManyCollection manyToManyCollection = new ManyToManyFileListDatasource(new ManyToManyFileListDatasource().MTM_USER_EVENT).readData();
        manyToManyCollection.findsByA(this.routeProvider.getUserSession().getId()).forEach(manyToMany -> {

            Event event = eventCollection.findById(manyToMany.getB());
            if (event != null) {
                newEventCollection.add(event);
            }
        });

        if (newEventCollection.getEvents().size() == 0) {
            return;
        }
        newEventCollection.getEvents().forEach(event -> {
            if (event.checkEventEnded()) {
                this.endedEventCollection.add(event);
            } else {
                this.inProcessEventCollection.add(event);
            }
        });

    if (firstLoadTabPane) {
        System.out.println("First load tab pane");
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
                        System.out.println("Tab Selection changed" +endedEventCollection.getEvents().size());
                        initEndedEventListScrollPane();
                        endedEventListScrollPaneListener();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @FXML
    public void InitializeThemeMode(){
        System.out.println("InitializeThemeMode" + this.routeProvider.getUserSession().getThemeMode());
        if (this.routeProvider.getUserSession().getThemeMode().equals("dark")){
            parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/light-mode.css");
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/dark-mode.css");
        }else if (this.routeProvider.getUserSession().getThemeMode().equals("light")) {
            parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/dark-mode.css");
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/light-mode.css");
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





}
