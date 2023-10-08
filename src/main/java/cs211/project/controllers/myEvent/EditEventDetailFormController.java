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
import cs211.project.services.datasource.TeamFileListDatasource;
import cs211.project.services.deleterelated.DeleteyRelatedOfPrimaryKeyEvent;
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
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class EditEventDetailFormController extends ComponentRegister {
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
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        this.eventFileListDatesource = new EventFileListDatesource();
        this.eventCollection = eventFileListDatesource.readData();

        this.event = eventCollection.findById(((Event)routeProvider.getData()).getEventID());

        this.showCurrentData();
        this.initializeThemeMode();
        this.initializeFont();
    }


    @FXML
    public void initializeThemeMode(){
        System.out.println("InitializeThemeMode" + this.routeProvider.getUserSession().getThemeMode());
        if (this.routeProvider.getUserSession().getThemeMode().equals("dark")){
            parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/light-mode.css");
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/dark-mode.css");
        }else if (this.routeProvider.getUserSession().getThemeMode().equals("light")) {
            parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/dark-mode.css");
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/light-mode.css");
        }
    }

    @FXML
    public void initializeFont(){
        String currentFont =this.routeProvider.getUserSession().getFont();
        clearFontStyle();
        if (currentFont.equals("font-style1")){
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style1.css");
        }else if (currentFont.equals("font-style2")){
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style2.css");
        }else if (currentFont.equals("font-style3")){
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style3.css");
        }

    }

    @FXML
    public void clearFontStyle(){
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
        DataTimeEnd.setValue(event.getStartDate().toLocalDate());
        publicCheckBox.setSelected(event.isPublic());
        Image image = new Image("file:" + event.getImageEvent());
        addImage.setImage(image);
        timeStart.setText(event.getStartTimeEvent());
        timeEnd.setText(event.getEndTimeEvent());
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
            this.event.setImageEvent("data/images/event/"+ this.event.getEventID() + "."+ imageSaver.extention);
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

        this.event.setNameEvent(TextFieldName.getText());
        this.event.setDescriptionEvent(TextAreaDescription.getText());
        this.event.setLocation(locationEvent.getText());
        this.event.setStartDate(timeStartUtils.getRefLocalDateTime());
        this.event.setEndDate(timeEndUtils.getRefLocalDateTime());
        this.event.setQuantityEvent(Integer.parseInt(TextFieldQuantity.getText()));
        this.event.setPublic(publicCheckBox.isSelected());

        eventCollection.update(this.event);
        eventFileListDatesource.writeData(eventCollection);

        navigateToSetEvent();
    }
    public void navigateToSetEvent(){
        try{
            FXRouter.goTo("set-event-detail", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  void navigateToMyEvent(){
        try{
            FXRouter.goTo("my-event", this.routeProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCancel() {
        navigateToSetEvent();
    }

    @FXML
    private void onDelete(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Event");
        alert.setHeaderText("Are you sure you want to delete this event?");
        alert.setContentText("This action cannot be undone.");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            DeleteyRelatedOfPrimaryKeyEvent deleteyRelatedOfPrimaryKeyEvent = new DeleteyRelatedOfPrimaryKeyEvent(this.event);
            deleteyRelatedOfPrimaryKeyEvent.delete();

            eventCollection.remove(this.event);
            eventFileListDatesource.writeData(eventCollection);

            navigateToMyEvent();
        }

    }
}