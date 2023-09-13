package cs211.project.controllers.myEvent;

import cs211.project.controllers.components.EventCardComponentController;
import cs211.project.models.Event;
import cs211.project.models.collections.EventCollection;
import cs211.project.services.Authentication;
import cs211.project.services.FXRouter;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.utils.ComponentLoader;
import cs211.project.utils.ComponentRegister;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyEventViewController extends ComponentRegister {

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
    private int currentBatch = 0;
    private int batchSize = 5;
    private EventCollection eventCollection;

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml");

        EventFileListDatesource eventFileListDatesource = new EventFileListDatesource();
        this.eventCollection = new EventCollection();

        eventFileListDatesource.readData().getEvents().forEach(event -> {

            if (Authentication.currentUser.getId().equals(event.getOwner().getId())){
                this.eventCollection.add(event);
            }
        });

        this.initEventListScrollPane();
        this.eventListScrollPaneListener();

    }

    private void loadNextBatch(List<Event> events) {
        for (int i = currentBatch; i < Math.min(currentBatch + batchSize, events.size()); i++) {
            try {
                FXMLLoader fxmlLoader = new ComponentLoader("EventCardComponent.fxml").getFxmlLoader();
                Pane eventCardComponent = fxmlLoader.load();
                EventCardComponentController eventCardComponentController = fxmlLoader.getController();
                eventCardComponentController.setData(events.get(i));
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

    @FXML
    public void goToCreateEvent() {
        try {
            FXRouter.goTo("create-event-detail-form");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
