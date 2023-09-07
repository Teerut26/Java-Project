package cs211.project.controllers.components;

import cs211.project.models.Event;
import cs211.project.services.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class EventCardComponentController {

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

    private Event event;

    public void setData(Event event) {
        Image img = new Image("file:" + event.getImageEvent());
        image.setFill(new ImagePattern(img));
        this.title.setText(event.getNameEvent());
        this.descriptions.setText(event.getDescriptionEvent());
        this.eventTime.setText(event.getStartDate().format(Event.DATE_FORMATTER) + " - " + event.getEndDate().format(Event.DATE_FORMATTER));
        this.currentMemberParticipatingAmount.setText(String.valueOf(event.getCurrentMemberParticipatingAmount()));
        this.maxMemberParticipating.setText(String.valueOf(String.valueOf(event.getQuantityEvent())));
        this.event = event;
    }

    @FXML
    void goToDetail(ActionEvent event) {
        try {
            FXRouter.goTo("event-detail");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
