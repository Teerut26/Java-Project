package cs211.project.controllers.myEvent;
import cs211.project.models.Event;
import cs211.project.models.collections.EventCollection;
import cs211.project.services.Authentication;
import cs211.project.services.FXRouter;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.utils.ComponentRegister;
import cs211.project.utils.ImageSaver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class CreateEventDetailFormController extends ComponentRegister {

    @FXML
    private TextArea TextAreaDescription;
    @FXML
    private TextField TextFieldName;
    @FXML
    private ImageView addImage;
    @FXML
    private DatePicker DataTimeEnd;
    @FXML
    private DatePicker DataTimeStart;
    @FXML
    private TextField TextFieldQuantity;

    @FXML
    private HBox NavBarHBox;
    @FXML
    private VBox SideBarVBox;
    private String eventID;
    private String imageFilePath;

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml");
        this.eventID = UUID.randomUUID().toString();
    }
    @FXML
    public void importImage(ActionEvent event) {
        ImageSaver imageSaver = new ImageSaver(this.eventID, "event");
        imageSaver.selectFile(event);
        File selectedFile = imageSaver.file;
        if(imageSaver.file != null){
            Image image = new Image(selectedFile.toURI().toString());
            addImage.setImage(image);
            addImage.setUserData(imageSaver);
        }
    }
    @FXML
    public void onSave() {
        EventFileListDatesource eventFileListDatesource = new EventFileListDatesource();
        ImageSaver imageSaver = (ImageSaver) addImage.getUserData();
        imageSaver.saveImage();

        Event newEvent = new Event(this.eventID,
                TextFieldName.getText(),
                "data/images/event/"+ this.eventID + "."+ imageSaver.extention,
                TextAreaDescription.getText(),
                DataTimeStart.getValue().atStartOfDay(),
                DataTimeEnd.getValue().atStartOfDay(),
                Integer.parseInt(TextFieldQuantity.getText()),
                Authentication.currentUser);

        EventCollection eventOldData = eventFileListDatesource.readData();
        eventOldData.add(newEvent);
        eventFileListDatesource.writeData(eventOldData);

        TextFieldName.clear();
        addImage.setImage(null);
        TextAreaDescription.clear();
        DataTimeStart.setValue(null);
        DataTimeEnd.setValue(null);
        TextFieldQuantity.clear();

        try{
            FXRouter.goTo("my-event");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    public void onCancel() {
        TextFieldName.clear();
        addImage.setImage(null);
        TextAreaDescription.clear();
        DataTimeStart.setValue(null);
        DataTimeEnd.setValue(null);
        TextFieldQuantity.clear();

    }
    @FXML public void onBack(){
        try{
            FXRouter.goTo("my-event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
