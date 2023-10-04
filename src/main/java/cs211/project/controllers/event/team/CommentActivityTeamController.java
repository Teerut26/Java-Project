package cs211.project.controllers.event.team;

import cs211.project.controllers.components.CommentCardComponentController;
import cs211.project.models.ActivitiesEvent;
import cs211.project.models.ActivitiesTeam;
import cs211.project.models.CommentActivitiesEvent;
import cs211.project.models.CommentActivitiesTeam;
import cs211.project.models.collections.CommentActivitiesEventCollection;
import cs211.project.models.collections.CommentActivitiesTeamCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.CommentActivitiesEventFileListDatasource;
import cs211.project.services.datasource.CommentActivitiesTeamFileListDatasource;
import cs211.project.utils.ComponentLoader;
import cs211.project.utils.ComponentRegister;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class CommentActivityTeamController {
    @FXML
    private VBox CommentList;

    @FXML
    private HBox NavBarHBox;

    @FXML
    private VBox SideBarVBox;

    @FXML
    private Text activityName;

    @FXML
    private TextField commentInput;

    @FXML
    private ScrollPane commentListScrollPane;

    @FXML
    private Text teamName;

    private int batchSize = 5;
    private int currentBatchStart = 0;
    private CommentActivitiesTeamFileListDatasource commentActivitiesTeamFileListDatasource;
    private CommentActivitiesTeamCollection commentActivitiesTeamCollection;
    private RouteProvider routeProvider;
    private ActivitiesTeam activitiesTeam;

    @FXML
    public void initialize() {
        this.routeProvider = (RouteProvider) FXRouter.getData();
        this.activitiesTeam = (ActivitiesTeam) routeProvider.getDataHashMap().get("activity-select");
        this.commentActivitiesTeamFileListDatasource = new CommentActivitiesTeamFileListDatasource();

        ComponentRegister componentRegister = new ComponentRegister();
        componentRegister.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        componentRegister.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        this.readData();

        this.commentInput.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                this.onSend();
            }
        });

        this.teamName.setText(this.activitiesTeam.getTeam().getName());
        this.activityName.setText(this.activitiesTeam.getTitle());
        this.loadNextBatch();
    }

    private void readData() {
        this.commentActivitiesTeamCollection = this.commentActivitiesTeamFileListDatasource.readData();
    }

    private void loadComment(CommentActivitiesTeam comment) {
        if (!(comment.equalsTeam(this.activitiesTeam))) {
            return;
        }
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

    private void loadNextBatch() {
        List<CommentActivitiesTeam> comments = commentActivitiesTeamCollection.getComments();
        int currentBatchEnd = Math.min(currentBatchStart + batchSize, comments.size());

        for (int i = currentBatchStart; i < currentBatchEnd; i++) {
            CommentActivitiesTeam comment = comments.get(i);
            loadComment(comment);
        }

        currentBatchStart = currentBatchEnd;

        if (currentBatchStart < comments.size()) {
            Platform.runLater(this::loadNextBatch);
        } else {
            Platform.runLater(() -> {
                commentListScrollPane.layout();
                commentListScrollPane.setVvalue(1.0);
            });
        }
    }

    @FXML
    void onBack(ActionEvent event) {
        try {
            FXRouter.goTo("event-team-detail");
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
        CommentActivitiesTeam commentActivitiesTeam1 = new CommentActivitiesTeam(commentId, commentInput.getText(), this.routeProvider.getUserSession(), this.activitiesTeam, LocalDateTime.now());
        System.out.println(commentActivitiesTeam1);
        commentActivitiesTeamCollection.add(commentActivitiesTeam1);
        this.commentActivitiesTeamFileListDatasource.writeData(commentActivitiesTeamCollection);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            Platform.runLater(() -> {
                loadComment(commentActivitiesTeam1);
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
