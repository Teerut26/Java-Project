package cs211.project.controllers.event;

import cs211.project.models.Event;
import cs211.project.models.ManyToMany;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.ManyToManyCollection;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.Authentication;
import cs211.project.services.FXRouter;
import cs211.project.services.ManyToManyManager;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.services.datasource.ManyToManyFileListDatasource;
import cs211.project.utils.ComponentRegister;
import cs211.project.services.RouteProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class EventDetailController extends ComponentRegister {
    @FXML
    private BorderPane parentBorderPane;
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    @FXML
    private Label currentUserAmount;
    @FXML
    private Label eventDescriptionLabel;
    @FXML
    private Rectangle eventImage;
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label eventTime;
    @FXML
    private Label maxUserAmount;
    private Event event;
    private RouteProvider<Event> routeProvider;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider<Event>) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        this.event = routeProvider.getData();

        this.setContent();

//        this.initializeThemeMode();
//        this.initializeFont();
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

    public void setContent() {
        this.eventNameLabel.setText(event.getNameEvent());
        this.eventDescriptionLabel.setText(event.getDescriptionEvent());

        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_EVENT);
        Integer currentMemberParticipatingAmount = manyToManyManager.countByB(event.getEventID());

        this.currentUserAmount.setText(String.valueOf(currentMemberParticipatingAmount));
        this.maxUserAmount.setText(String.valueOf(event.getQuantityEvent()));
        this.eventTime.setText(event.getStartDate().format(Event.DATE_FORMATTER) + " - " + event.getEndDate().format(Event.DATE_FORMATTER));
        Image img = new Image("file:" + event.getImageEvent());
        this.eventImage.setFill(new ImagePattern(img));
    }

    @FXML
    public void onBack() {
        try {
            FXRouter.goTo("event-list", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onJoin() {
        try {
            if (this.routeProvider.getUserSession().getId().equals(this.event.getOwner().getId())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("You can't join your own event");
                alert.setContentText("You can't join your own event");
                alert.show();
                return;
            }

            // TODO: Add user to event
            ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_EVENT);
            manyToManyManager.add(new ManyToMany(this.routeProvider.getUserSession().getId(), this.event.getEventID()));

            FXRouter.goTo("event-list", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void goToTeamList () {
//        if (this.routeProvider.getUserSession().getId().equals(this.event.getOwner().getId())) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("You can't join your own event");
//            alert.setContentText("You can't join your own event");
//            alert.show();
//            return;
//        }
        try {
            FXRouter.goTo("event-team-list", this.routeProvider);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
