package cs211.project.controllers.event;

import cs211.project.models.Event;
import cs211.project.services.FXRouter;
import cs211.project.utils.ComponentRegister;
import cs211.project.services.RouteProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class EventDetailController extends ComponentRegister {
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

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml");

        RouteProvider<Event> routeProvider = (RouteProvider<Event>) FXRouter.getData();
        this.event = routeProvider.getData();

        this.setContent();
    }

    public void setContent() {
        this.eventNameLabel.setText(event.getNameEvent());
        this.eventDescriptionLabel.setText(event.getDescriptionEvent());
        this.currentUserAmount.setText(String.valueOf(event.getCurrentMemberParticipatingAmount()));
        this.maxUserAmount.setText(String.valueOf(event.getQuantityEvent()));
        this.eventTime.setText(event.getStartDate().format(Event.DATE_FORMATTER) + " - " + event.getEndDate().format(Event.DATE_FORMATTER));
        Image img = new Image("file:" + event.getImageEvent());
        this.eventImage.setFill(new ImagePattern(img));
    }

    @FXML
    public void onBack() {
        try {
            FXRouter.goTo("event-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
