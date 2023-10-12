package cs211.project.controllers.schedule;

import cs211.project.models.ActivitiesEvent;
import cs211.project.models.Event;
import cs211.project.models.collections.ActivitiesEventCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.ActivitiesEventFileListDatesource;
import cs211.project.utils.ComponentRegister;
import cs211.project.utils.TimeValidate;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;

public class EditScheduleParticipantController {
    @FXML
    private BorderPane parentBorderPane;
    @FXML
    private VBox SideBarVBox;

    @FXML
    private HBox NavBarHBox;

    @FXML
    private TextField TextFieldDetail;
    @FXML
    private TextField TextFieldName;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private DatePicker dateStart;
    @FXML
    private TextField timeEnd;
    @FXML
    private TextField timeStart;
    private ActivitiesEventFileListDatesource activitiesEventFileListDatesource;
    private ActivitiesEventCollection activitiesEventCollection;
    private ActivitiesEvent activitiesEvent;
    private RouteProvider<Event> routeProvider;

    @FXML
    public void initialize() {
        routeProvider = (RouteProvider<Event>) FXRouter.getData();
        ComponentRegister componentRegister = new ComponentRegister();
        this.activitiesEvent = (ActivitiesEvent) routeProvider.getDataHashMap().get("activity-event-select");
        componentRegister.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
        componentRegister.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);

        activitiesEventFileListDatesource = new ActivitiesEventFileListDatesource();
        activitiesEventCollection = activitiesEventFileListDatesource.readData();

        TextFieldName.setText(activitiesEvent.getTitle());
        TextFieldDetail.setText(activitiesEvent.getDetail());
        dateStart.setValue(activitiesEvent.getDateStart().toLocalDate());
        dateEnd.setValue(activitiesEvent.getDateEnd().toLocalDate());
        timeStart.setText(formatTime(activitiesEvent.getStartTime()));
        timeEnd.setText(formatTime(activitiesEvent.getEndTime()));

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
    public void onSave() {
        TimeValidate timeStartUtils = new TimeValidate(timeStart.getText(), dateStart.getValue().atStartOfDay());
        TimeValidate timeEndUtils = new TimeValidate(timeEnd.getText(), dateEnd.getValue().atStartOfDay());

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

        activitiesEvent.setTitle(TextFieldName.getText());
        activitiesEvent.setDetail(TextFieldDetail.getText());
        activitiesEvent.setDateStart(timeStartUtils.getRefLocalDateTime());
        activitiesEvent.setDateEnd(timeEndUtils.getRefLocalDateTime());

        activitiesEventCollection.update(activitiesEvent);
        activitiesEventFileListDatesource.writeData(activitiesEventCollection);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Edit Activities Participant Success");
        alert.setContentText("Edit Activities Participant Success");
        alert.showAndWait();

        try {
            FXRouter.goTo("set-event-detail", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public void onCancel() {
        try {
            FXRouter.goTo("set-event-detail", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
