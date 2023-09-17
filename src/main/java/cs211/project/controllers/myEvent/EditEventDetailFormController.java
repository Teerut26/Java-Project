package cs211.project.controllers.myEvent;

import cs211.project.models.Event;
import cs211.project.models.Activities;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.models.collections.EventCollection;
import cs211.project.services.Authentication;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
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
    private Event event;
    private EventCollection eventCollection;
    private EventFileListDatesource eventFileListDatesource;

    private RouteProvider<Event> routeProvider;

    @FXML
    public void initialize() {

        routeProvider = (RouteProvider<Event>) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        eventFileListDatesource = new EventFileListDatesource();
        eventCollection = eventFileListDatesource.readData();

        event = routeProvider.getData();

        this.showCurrentData();
    }

    public void showCurrentData() {
        TextFieldName.setText(event.getNameEvent());
        TextAreaDescription.appendText(event.getDescriptionEvent());
        TextFieldQuantity.setText(String.valueOf(event.getQuantityEvent()));
        DataTimeStart.setValue(event.getStartDate().toLocalDate());
        DataTimeEnd.setValue(event.getStartDate().toLocalDate());
        Image image = new Image("file:" + event.getImageEvent());
        addImage.setImage(image);
    }

    @FXML
    public void importImage(ActionEvent event) {
        ImageSaver imageSaver = new ImageSaver(this.event.getEventID(), "event");
        imageSaver.selectFile(event);
        File selectedFile = imageSaver.file;
        if (imageSaver.file != null) {
            Image image = new Image(selectedFile.toURI().toString());
            addImage.setImage(image);
            addImage.setUserData(imageSaver);
        }
    }

    @FXML
    public void onSave() {

        ImageSaver imageSaver = (ImageSaver) addImage.getUserData();
        imageSaver.saveImage();

        event.setNameEvent(TextFieldName.getText());
        event.setDescriptionEvent(TextAreaDescription.getText());
        event.setImageEvent("data/images/event/"+ this.event.getEventID() + "."+ imageSaver.extention);
        event.setStartDate(DataTimeStart.getValue().atStartOfDay());
        event.setEndDate(DataTimeEnd.getValue().atStartOfDay());
        event.setQuantityEvent(Integer.parseInt(TextFieldQuantity.getText()));

        eventFileListDatesource.writeData(eventCollection);

        try{
            FXRouter.goTo("set-event-detail", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCancel() {
        try {
            FXRouter.goTo("set-event-detail",this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}