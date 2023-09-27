package cs211.project.controllers.event;

import cs211.project.controllers.components.CommentCardComponentController;
import cs211.project.models.ActivitiesEvent;
import cs211.project.models.CommentActivitiesEvent;
import cs211.project.models.Event;
import cs211.project.models.collections.CommentActivitiesEventCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.SingletonStorage;
import cs211.project.services.datasource.CommentActivitiesEventFileListDatasource;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.utils.ComponentLoader;
import cs211.project.utils.ComponentRegister;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommentActivityEventController {
    @FXML
    private HBox NavBarHBox;

    @FXML
    private VBox SideBarVBox;

    @FXML
    private VBox CommentList;

    @FXML
    private Text activityName;

    @FXML
    private TextField commentInput;

    @FXML
    private Text eventName;

    @FXML
    private ScrollPane commentListScrollPane;
    private int batchSize = 5;
    private int currentBatchStart = 0;
    private CommentActivitiesEventFileListDatasource commentActivitiesEventFileListDatasource;
    private CommentActivitiesEventCollection commentActivitiesEventCollection;
    private RouteProvider routeProvider;
    private ActivitiesEvent activitiesEvent;

    @FXML
    public void initialize() {
        this.routeProvider = (RouteProvider) FXRouter.getData();
        this.activitiesEvent = (ActivitiesEvent) routeProvider.getDataHashMap().get("activity-event-select");
        this.commentActivitiesEventFileListDatasource = new CommentActivitiesEventFileListDatasource();

        ComponentRegister componentRegister = new ComponentRegister();
        componentRegister.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        componentRegister.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        this.readData();

        this.commentInput.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                this.onSend();
            }
        });

        this.eventName.setText(this.activitiesEvent.getEvent().getNameEvent());
        this.activityName.setText(this.activitiesEvent.getTitle());
        this.loadNextBatch();
    }

    private void loadNextBatch() {
        List<CommentActivitiesEvent> comments = commentActivitiesEventCollection.getComments();
        int currentBatchEnd = Math.min(currentBatchStart + batchSize, comments.size());

        for (int i = currentBatchStart; i < currentBatchEnd; i++) {
            CommentActivitiesEvent comment = comments.get(i);
            loadComment(comment);
        }

        currentBatchStart = currentBatchEnd;

        if (currentBatchStart < comments.size()) {
            Platform.runLater(this::loadNextBatch);
        }else {
           Platform.runLater(()->{
                commentListScrollPane.layout();
                commentListScrollPane.setVvalue(1.0);
           });
        }
    }

    private void readData() {
        CommentActivitiesEventCollection newCommentActivitiesEventCollection = new CommentActivitiesEventCollection();
        this.commentActivitiesEventFileListDatasource.readData().getComments().forEach(commentActivitiesEvent -> {
            if (commentActivitiesEvent.getActivitiesEvent().getId().equals(this.activitiesEvent.getId())) {
                newCommentActivitiesEventCollection.add(commentActivitiesEvent);
            }
        });
        this.commentActivitiesEventCollection = newCommentActivitiesEventCollection;
    }

    private void loadComment(CommentActivitiesEvent comment) {
        try {
            FXMLLoader fxmlLoader = new ComponentLoader("CommentCardComponent.fxml").getFxmlLoader();
            Pane eventCardComponent = fxmlLoader.load();
            CommentCardComponentController commentCardComponentController = fxmlLoader.getController();
            commentCardComponentController.setContent(comment);
            CommentList.getChildren().add(eventCardComponent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onBack(ActionEvent event) {
        try {
            FXRouter.goTo("set-event-detail");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onSend() {
        if (commentInput.getText().isEmpty()) {
            return;
        }
        String commentId = UUID.randomUUID().toString();
        CommentActivitiesEvent commentActivitiesEvent1 = new CommentActivitiesEvent(commentId, commentInput.getText(), this.routeProvider.getUserSession(), this.activitiesEvent, LocalDateTime.now());
        commentActivitiesEventCollection.add(commentActivitiesEvent1);
        this.commentActivitiesEventFileListDatasource.writeData(commentActivitiesEventCollection);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            Platform.runLater(() -> {
                loadComment(commentActivitiesEvent1);
            });
            Platform.runLater(() -> {
                commentInput.setText("");
                commentListScrollPane.layout();
                commentListScrollPane.setVvalue(1.0);
            });
        });
        executorService.shutdown();
    }
}
