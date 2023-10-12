package cs211.project.controllers.components;

import cs211.project.models.Event;
import cs211.project.models.ManyToMany;
import cs211.project.services.FXRouter;
import cs211.project.services.ManyToManyManager;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.ManyToManyFileListDatasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class InProcessCardComponentController {
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
        this.eventTime.setText(event.getStartDate().format(Event.DATE_FORMATTER) + " - "
                + event.getEndDate().format(Event.DATE_FORMATTER));
        this.eventLocation.setText(event.getLocation());
        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_EVENT);
        Integer currentMemberParticipatingAmount = manyToManyManager.countByB(event.getEventID());

        this.currentMemberParticipatingAmount.setText(String.valueOf(currentMemberParticipatingAmount));
        this.maxMemberParticipating.setText(String.valueOf(String.valueOf(event.getQuantityEvent())));
        this.event = event;
    }


    @FXML
    void goToDetail() {
        try {
            RouteProvider routeProviderWithEvent = new RouteProvider<Event>(this.event);
            routeProviderWithEvent.setUserSession(this.routeProvider.getUserSession());
            FXRouter.goTo(this.customPath == null ? "event-detail-joined" : this.customPath, routeProviderWithEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCanceledEvent() {
        Event eventSelected = (Event) this.event;
        ManyToManyManager manyToManyManager = new ManyToManyManager(new ManyToManyFileListDatasource().MTM_USER_EVENT);
        manyToManyManager
                .remove(new ManyToMany(this.routeProvider.getUserSession().getId(), eventSelected.getEventID()));

        // reload
        try {
            FXRouter.goTo("event-history", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
