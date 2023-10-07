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
import javafx.scene.layout.BorderPane;
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
    private BorderPane parentBorderPane;
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

        this.initializeThemeMode();
        this.initializeFont();
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
        } else {
            Platform.runLater(() -> {
                commentListScrollPane.layout();
                commentListScrollPane.setVvalue(1.0);
            });
        }
    }

    private void readData() {
        commentActivitiesEventCollection = this.commentActivitiesEventFileListDatasource.readData();
    }

    private void loadComment(CommentActivitiesEvent comment) {
        if (!(comment.equalsEvent(this.activitiesEvent))) {
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

    @FXML
    void onBack(ActionEvent event) {
        try {
            if (this.routeProvider.getDataHashMap().get("back-value") != null) {
                FXRouter.goTo((String) this.routeProvider.getDataHashMap().get("back-value"));
            } else {
                FXRouter.goTo("set-event-detail");
            }

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
