package cs211.project.controllers.event.team;

import cs211.project.Main;
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

public class CommentActivityTeamController {
    @FXML
    private BorderPane parentBorderPane;
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

        this.initializeThemeMode();
        this.initializeFont();
    }
    @FXML
    public void initializeThemeMode(){
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
    public void initializeFont() {
        String currentFont = this.routeProvider.getUserSession().getFont();
        clearFontStyle();
        if (currentFont.equals("font-style1")) {
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style1.css");
        } else if (currentFont.equals("font-style2")) {
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style2.css");
        } else if (currentFont.equals("font-style3")) {
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style3.css");
        }

    }

    @FXML
    public void clearFontStyle() {
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style1.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style2.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style3.css");
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
    void onBack() {
        try {
            if (this.routeProvider.getDataHashMap().get("back-my-team") != null) {
                routeProvider.addHashMap("teamJoined",this.activitiesTeam.getTeam());
                FXRouter.goTo((String) this.routeProvider.getDataHashMap().get("back-my-team"));
            } else {
                FXRouter.goTo("event-team-detail");
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
        CommentActivitiesTeam commentActivitiesTeam1 = new CommentActivitiesTeam(commentId, commentInput.getText(),
                this.routeProvider.getUserSession(), this.activitiesTeam, LocalDateTime.now());
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
