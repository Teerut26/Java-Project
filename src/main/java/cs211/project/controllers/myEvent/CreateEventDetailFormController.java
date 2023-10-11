package cs211.project.controllers.myEvent;

import cs211.project.models.Event;
import cs211.project.models.collections.EventCollection;
import cs211.project.services.Authentication;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.utils.ComponentRegister;
import cs211.project.utils.ImageSaver;
import cs211.project.utils.TimeValidate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class CreateEventDetailFormController extends ComponentRegister {
    @FXML
    private BorderPane parentBorderPane;
    @FXML
    private TextArea TextAreaDescription;
    @FXML
    private TextField textFieldLocation;
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
    private TextField timeStart;
    @FXML
    private TextField timeEnd;
    private String eventID;
    private RouteProvider routeProvider;
    private EventFileListDatesource eventFileListDatesource;
    private EventCollection eventCollection;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
        this.eventID = UUID.randomUUID().toString();

        eventFileListDatesource = new EventFileListDatesource();
        eventCollection = eventFileListDatesource.readData();

        this.initializeThemeMode();
        this.initializeFont();
    }

    @FXML
    public void initializeThemeMode() {
        if (this.routeProvider.getUserSession().getThemeMode().equals("dark")) {
            parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/light-mode.css");
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/dark-mode.css");
        } else if (this.routeProvider.getUserSession().getThemeMode().equals("light")) {
            parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/dark-mode.css");
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/light-mode.css");
        }
    }

    @FXML
    public void initializeFont() {
        String currentFont = this.routeProvider.getUserSession().getFont();
        clearFontStyle();
        if (currentFont.equals("font-style1")) {
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style1.css");
        } else if (currentFont.equals("font-style2")) {
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style2.css");
        } else if (currentFont.equals("font-style3")) {
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style3.css");
        }

    }

    @FXML
    public void clearFontStyle() {
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style1.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style2.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style3.css");
    }

    @FXML
    public void importImage(ActionEvent event) {
        ImageSaver imageSaver = new ImageSaver(this.eventID, "event");
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

        TimeValidate timeStartUtils = new TimeValidate(timeStart.getText(), DataTimeStart.getValue().atStartOfDay());
        TimeValidate timeEndUtils = new TimeValidate(timeEnd.getText(), DataTimeEnd.getValue().atStartOfDay());

        if (!timeEndUtils.validate() || !timeStartUtils.validate()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Time");
            alert.setContentText("Please enter a valid time according to pattern 00:00");
            alert.show();
            return;
        }

        timeStartUtils.addTime(timeStartUtils.getHour(), timeStartUtils.getMinute());
        timeEndUtils.addTime(timeEndUtils.getHour(), timeEndUtils.getMinute());

        Event newEvent = new Event(this.eventID,
                TextFieldName.getText(),
                "data/images/event/" + this.eventID + "." + imageSaver.extention,
                TextAreaDescription.getText(),
                textFieldLocation.getText(),
                timeStartUtils.getRefLocalDateTime(),
                timeEndUtils.getRefLocalDateTime(),
                Integer.parseInt(TextFieldQuantity.getText()),
                routeProvider.getUserSession());

        eventCollection.add(newEvent);
        eventFileListDatesource.writeData(eventCollection);

        clearField();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Event Created");
        alert.setContentText("Event has been created successfully");
        alert.showAndWait();
        try {
            FXRouter.goTo("my-event", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearField() {
        TextFieldName.clear();
        addImage.setImage(null);
        TextAreaDescription.clear();
        textFieldLocation.clear();
        DataTimeStart.setValue(null);
        DataTimeEnd.setValue(null);
        TextFieldQuantity.clear();
    }

    @FXML
    public void onCancel() {
        try {
            FXRouter.goTo("my-event", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
