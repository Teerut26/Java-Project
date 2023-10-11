package cs211.project.controllers.event;

import cs211.project.controllers.components.EventCardComponentController;
import cs211.project.models.Event;
import cs211.project.models.ManyToMany;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.ManyToManyCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.services.datasource.ManyToManyFileListDatasource;
import cs211.project.utils.ComponentLoader;
import cs211.project.utils.ComponentRegister;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventListController extends ComponentRegister {
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

        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);




        EventFileListDatesource eventFileListDatesource = new EventFileListDatesource();

        this.eventCollection = new EventCollection();
        ManyToManyCollection manyToManyCollectionUserJoinedEvent = new ManyToManyFileListDatasource(new ManyToManyFileListDatasource().MTM_USER_EVENT).readData();

        eventFileListDatesource.readData().getEvents().forEach(event -> {
            boolean isJoined = manyToManyCollectionUserJoinedEvent.checkIsExisted(new ManyToMany(routeProvider.getUserSession().getId(), event.getEventID()));
            boolean isOwner = event.getOwner().getId().equals(routeProvider.getUserSession().getId());
            if (event.isPublic() && !isJoined) {
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

    public void searchEngine(){
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


}
