package cs211.project.controllers.components;

import cs211.project.models.Comment;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;

public class CommentCardComponentController {
    @FXML
    private Text content;
    @FXML
    private Text name;
    @FXML
    private Circle profileImage;
    @FXML
    private Text timestamp;

    public void setContent(Comment comment) {
        double desiredWrappingWidth = calculateDesiredWrappingWidth(comment.getMessage());
        this.content.setWrappingWidth(desiredWrappingWidth);

        this.content.setText(comment.message);
        this.name.setText(comment.getOwner().getUserName());
        this.timestamp.setText(comment.getTimeStamps().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        if (comment.getOwner().getImageProfile().equals("null")) {
            Image img = new Image(getClass().getResource("/cs211/project/assets/image/auth/user.png").toExternalForm());
            profileImage.setFill(new ImagePattern(img));
        } else {
            Image img = new Image("file:" + comment.getOwner().getImageProfile());
            profileImage.setFill(new ImagePattern(img));
        }
    }

    private double calculateDesiredWrappingWidth(String text) {
        double wrappingMultiplier = 5.0 ;
        int maxWidth = 500;
        int minWidth = 200;

        double result = (text.length() * wrappingMultiplier);

        if (result > maxWidth) {
            return maxWidth;
        } else if (result < minWidth) {
            return minWidth;
        } else {
            return result;
        }
    }
}
