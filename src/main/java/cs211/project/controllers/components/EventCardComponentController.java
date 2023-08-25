package cs211.project.controllers.components;

import cs211.project.services.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    public void setData(String title, String descriptions, String eventTime, int currentMemberParticipatingAmount, int maxMemberParticipating) {
        Image img = new Image("https://p-u.popcdn.net/event_details/posters/000/015/478/medium/5d369c5375dfadd2e9297c8109ca3a698b928370.jpg?1688606774");
        image.setFill(new ImagePattern(img));
        this.title.setText(title);
        this.descriptions.setText(descriptions);
        this.eventTime.setText(eventTime);
        this.currentMemberParticipatingAmount.setText(String.valueOf(currentMemberParticipatingAmount));
        this.maxMemberParticipating.setText(String.valueOf(maxMemberParticipating));
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
