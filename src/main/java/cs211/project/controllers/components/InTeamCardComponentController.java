package cs211.project.controllers.components;

import cs211.project.models.Event;
import cs211.project.models.ManyToMany;
import cs211.project.services.FXRouter;
import cs211.project.services.ManyToManyManager;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.ManyToManyFileListDatasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class InTeamCardComponentController {
    @FXML
    private Label currentMemberParticipatingAmount;

    @FXML
    private Label descriptions;

    @FXML
    private Label eventTime;

    @FXML
    private Rectangle image;

    @FXML
    private Label maxMemberParticipating;

    @FXML
    private Label title;
    @FXML
    private Label eventLocation;

    private String customPath = null;

    private Event event;
    private RouteProvider routeProvider;

    public void setRouteProvider(RouteProvider routeProvider) {
        this.routeProvider = routeProvider;
    }

    public void setData(Event event) {
        Image img = new Image("file:" + event.getImageEvent());
        image.setFill(new ImagePattern(img));
        this.title.setText(event.getNameEvent());
        this.descriptions.setText(event.getDescriptionEvent());
        this.eventTime.setText(event.getStartDate().format(Event.DATE_FORMATTER) + " - " + event.getEndDate().format(Event.DATE_FORMATTER));
        this.eventLocation.setText(event.getLocation());
        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_EVENT);
        Integer currentMemberParticipatingAmount = manyToManyManager.countByB(event.getEventID());

        this.currentMemberParticipatingAmount.setText(String.valueOf(currentMemberParticipatingAmount));
        this.maxMemberParticipating.setText(String.valueOf(String.valueOf(event.getQuantityEvent())));
        this.event = event;
    }

    public void setCustomPath(String customPath) {
        this.customPath = customPath;
    }


    @FXML
    public void goToTeamList() {

        try {
            if (this.routeProvider.getUserSession().getId().equals(this.event.getOwner().getId())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("You can't join your own event");
                alert.setContentText("You can't join your own event");
                alert.show();
                return;
            }
            routeProvider.addHashMap("eventID", this.event.getEventID());
            routeProvider.addHashMap("oldRoute", "event-history");
            FXRouter.goTo("event-team-list", this.routeProvider);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
