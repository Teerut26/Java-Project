package cs211.project.controllers.event;

import cs211.project.controllers.components.EventCardComponentController;
import cs211.project.services.FXRouter;
import cs211.project.utils.ComponentRegister;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
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
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("/cs211/project/components/EventCardComponent.fxml"));
                        Pane eventCardComponent = fxmlLoader.load();

                        EventCardComponentController eventCardComponentController = fxmlLoader.getController();
                        eventCardComponentController.setData("Event " + i, "Event " + i + " description", "17 July 23 6:00 - 18 July 23 23:00", 10, 200);
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
