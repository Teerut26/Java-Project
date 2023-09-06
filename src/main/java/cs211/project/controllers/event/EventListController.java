package cs211.project.controllers.event;

import cs211.project.controllers.components.EventCardComponentController;
import cs211.project.models.Event;
import cs211.project.services.FXRouter;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.utils.ComponentLoader;
import cs211.project.utils.ComponentRegister;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventListController extends ComponentRegister {
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
    private ExecutorService executor = Executors.newCachedThreadPool();

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml");

        EventFileListDatesource eventFileListDatesource = new EventFileListDatesource();

        eventListScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            boolean isLoadmore = currentBatch < eventFileListDatesource.readData().getEvents().size();
            if (newValue.doubleValue() == 1.0 && isLoadmore) {
                ExecutorService executorService = Executors.newCachedThreadPool();
                this.loading.setVisible(true);
                executorService.execute(() -> {
                    veryLongOperation();
                    Platform.runLater(() -> {
                        loadNextBatch(eventFileListDatesource.readData().getEvents());
                        loading.setVisible(false);
                    });
                });
                executorService.shutdown();
            }
        });

        this.executor.execute(() -> {
            veryLongOperation();
            Platform.runLater(() -> {
                loadNextBatch(eventFileListDatesource.readData().getEvents());
                loading.setVisible(false);
            });
        });

        this.executor.shutdown();
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

//        // Display a "Load More" button if there are more events to load
//        if (currentBatch < events.size()) {
//            Button loadMoreButton = new Button("Load More");
//            loadMoreButton.setOnAction(event -> loadNextBatch(events));
//            vBoxEventlist.getChildren().add(loadMoreButton);
//        }
    }


    public static void veryLongOperation() {
        try {
            Thread.sleep(500);
        } catch (Exception ex) {
        }

    }

    @FXML
    public void onGotoDetail() {
        try {
            FXRouter.goTo("event-detail");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
