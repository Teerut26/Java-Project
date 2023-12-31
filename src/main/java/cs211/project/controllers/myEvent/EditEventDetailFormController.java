package cs211.project.controllers.myEvent;

import cs211.project.Main;
import cs211.project.models.Event;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.models.collections.EventCollection;
import cs211.project.services.datasource.EventFileListDatesource;
import cs211.project.services.deleterelated.DeleteRelatedOfPrimaryKeyEvent;
import cs211.project.utils.ComponentRegister;
import cs211.project.utils.ImageSaver;
import cs211.project.utils.TimeValidate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;

public class EditEventDetailFormController {
    @FXML
    private BorderPane parentBorderPane;
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
    private TextField locationEvent;
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    @FXML
    private CheckBox publicCheckBox;
    @FXML
    private TextField timeStart;
    @FXML
    private TextField timeEnd;

    private Event event;
    private EventCollection eventCollection;
    private EventFileListDatesource eventFileListDatesource;

    private RouteProvider<Event> routeProvider;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        ComponentRegister componentRegister = new ComponentRegister();
        componentRegister.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        componentRegister.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        this.eventFileListDatesource = new EventFileListDatesource();
        this.eventCollection = eventFileListDatesource.readData();

        this.event = eventCollection.findById(((Event) routeProvider.getData()).getEventID());

        this.showCurrentData();
        this.initializeThemeMode();
        this.initializeFont();
    }

    @FXML
    public void initializeThemeMode(){
        String className = Main.class.getName().replace('.', '/');
        String classJar = Main.class.getResource("/" + className + ".class").toString();
        Boolean isJarFile = classJar.startsWith("jar:");
        String pathDarkMode;
        String pathLightMode;
        if(isJarFile) {
            pathDarkMode = "/cs211/project/style/dark-mode.css";
            pathLightMode = "/cs211/project/style/light-mode.css";
        }else{
            pathDarkMode = "file:src/main/resources/cs211/project/style/dark-mode.css";
            pathLightMode = "file:src/main/resources/cs211/project/style/light-mode.css";
        }
        if (this.routeProvider.getUserSession().getThemeMode().equals("dark")){
            parentBorderPane.getStylesheets().remove(pathLightMode);
            parentBorderPane.getStylesheets().add(pathDarkMode);
        }else if (this.routeProvider.getUserSession().getThemeMode().equals("light")) {
            parentBorderPane.getStylesheets().remove(pathDarkMode);
            parentBorderPane.getStylesheets().add(pathLightMode);
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

    public void showCurrentData() {
        TextFieldName.setText(event.getNameEvent());
        TextAreaDescription.setText(event.getDescriptionEvent());
        locationEvent.setText(event.getLocation());
        TextFieldQuantity.setText(String.valueOf(event.getQuantityEvent()));
        DataTimeStart.setValue(event.getStartDate().toLocalDate());
        DataTimeEnd.setValue(event.getEndDate().toLocalDate());
        publicCheckBox.setSelected(event.isPublic());
        Image image = new Image("file:" + event.getImageEvent());
        addImage.setImage(image);
        timeStart.setText(formatTime(event.getStartTimeEvent()));
        timeEnd.setText(formatTime(event.getEndTimeEvent()));
    }

    public String formatTime(String time) {
        String[] timeArr = time.split(":");
        String hour = timeArr[0];
        String minute = timeArr[1];
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }

        return hour + ":" + minute;
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
        if (imageSaver != null) {
            imageSaver.saveImage();
            this.event.setImageEvent("data/images/event/" + this.event.getEventID() + "." + imageSaver.extention);
        }

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

        event.setNameEvent(TextFieldName.getText());
        event.setDescriptionEvent(TextAreaDescription.getText());
        event.setLocation(locationEvent.getText());
        event.setStartDate(timeStartUtils.getRefLocalDateTime());
        event.setEndDate(timeEndUtils.getRefLocalDateTime());
        event.setQuantityEvent(Integer.parseInt(TextFieldQuantity.getText()));
        event.setPublic(publicCheckBox.isSelected());

        eventCollection.update(event);
        eventFileListDatesource.writeData(eventCollection);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Event Updated");
        alert.setContentText("Event has been updated successfully");
        alert.showAndWait();
        navigateToSetEvent();

    }

    public void navigateToSetEvent() {
        try {
            FXRouter.goTo("set-event-detail", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCancel() {
        navigateToSetEvent();
    }

    @FXML
    private void onDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Event");
        alert.setHeaderText("Are you sure you want to delete this event?");
        alert.setContentText("This action cannot be undone.");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            DeleteRelatedOfPrimaryKeyEvent deleteyRelatedOfPrimaryKeyEvent = new DeleteRelatedOfPrimaryKeyEvent();
            deleteyRelatedOfPrimaryKeyEvent.delete(this.event);
            navigateToMyEvent();
        }

    }

    public void navigateToMyEvent() {
        try {
            FXRouter.goTo("my-event", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}