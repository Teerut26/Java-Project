package cs211.project.controllers.myEvent;

import cs211.project.models.Event;
import cs211.project.models.Activities;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
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

import java.io.File;
import java.io.IOException;

public class EditEventDetailFormController extends ComponentRegister {
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
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml");

        EventFileListDatesource eventFileListDatesource = new EventFileListDatesource();
        originalEvent = eventFileListDatesource.readData().findById(eventID);
    }

    @FXML
    public void onSave() {

        EventFileListDatesource eventFileListDatesource = new EventFileListDatesource();
        Event eventToEdit = eventFileListDatesource.readData().findById(eventID);

        eventToEdit.setNameEvent(TextFieldName.getText());
        eventToEdit.setImageEvent(addImage.getUserData().toString());
        eventToEdit.setDescriptionEvent(TextAreaDescription.getText());
        eventToEdit.setStartDate(DataTimeStart.getValue().atStartOfDay());
        eventToEdit.setEndDate(DataTimeEnd.getValue().atStartOfDay());
        eventToEdit.setQuantityEvent(Integer.parseInt(TextFieldQuantity.getText()));

    }

    @FXML
    public void onCancel() {

        TextFieldName.setText(originalEvent.getNameEvent());
        addImage.setUserData(originalEvent.getImageEvent());
        TextAreaDescription.setText(originalEvent.getDescriptionEvent());
        DataTimeStart.setValue(originalEvent.getStartDate().toLocalDate());
        DataTimeEnd.setValue(originalEvent.getEndDate().toLocalDate());
        TextFieldQuantity.setText(String.valueOf(originalEvent.getQuantityEvent()));

    }
    @FXML public void onBack(){
        try{
            FXRouter.goTo("my-event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}