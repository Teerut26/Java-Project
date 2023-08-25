package cs211.project.controllers.event;

import cs211.project.controllers.components.EventCardComponentController;
import cs211.project.models.Event;
import cs211.project.services.FXRouter;
import cs211.project.utils.ComponentLoader;
import cs211.project.utils.ComponentRegister;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
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

    private ExecutorService executor = Executors.newCachedThreadPool();

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml");

        this.executor.execute(() -> {
            veryLongOperation();
            Platform.runLater(() -> {
                for (int i = 0; i < 10; i++) {
                    try {
                        FXMLLoader fxmlLoader = new ComponentLoader("EventCardComponent.fxml").getFxmlLoader();
                        Pane eventCardComponent = fxmlLoader.load();
                        EventCardComponentController eventCardComponentController = fxmlLoader.getController();
                        Event event = new Event(UUID.randomUUID().toString(), "Event " + i, "https://p-u.popcdn.net/event_details/posters/000/015/702/medium/5c989f2b12ff1da960477bbbea5187a68eae5f96.jpg", "adsf",LocalDateTime.now(), LocalDateTime.now().plusDays(1), 100);
                        eventCardComponentController.setData(event);
                        vBoxEventlist.getChildren().add(eventCardComponent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                loading.setVisible(false);
            });
        });

        this.executor.shutdown();
    }

    @FXML


    public static void veryLongOperation() {
        try {
            Thread.sleep(5000);
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
