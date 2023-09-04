package cs211.project.controllers.myEvent;
import cs211.project.models.Event;
import cs211.project.models.collections.EventCollection;
import cs211.project.services.Authentication;
import cs211.project.services.FXRouter;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.utils.ComponentRegister;
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



    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml");

    }
    @FXML
    public void importImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile != null){
            Image image = new Image( selectedFile.toURI().toString());
            addImage.setImage(image);
            addImage.setUserData(selectedFile.getAbsolutePath());
        }
    }
    @FXML
    public void onSave() {

        EventFileListDatesource eventFileListDatesource = new EventFileListDatesource();

        Event newEvent = new Event(UUID.randomUUID().toString(),
                TextFieldName.getText(),
                TextAreaDescription.getText(),
                addImage.getUserData().toString(),
                DataTimeStart.getValue().atStartOfDay(),
                DataTimeEnd.getValue().atStartOfDay(),
                Integer.parseInt(TextFieldQuantity.getText()),
                Authentication.currentUser);

        EventCollection eventOldData = eventFileListDatesource.readData();
        eventOldData.add(newEvent);
        eventFileListDatesource.writeData(eventOldData);

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
