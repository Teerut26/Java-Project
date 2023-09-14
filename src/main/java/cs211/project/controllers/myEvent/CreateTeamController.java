package cs211.project.controllers.myEvent;

import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.collections.TeamCollection;
import cs211.project.services.Authentication;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.TeamFileListDatasource;
import cs211.project.utils.ComponentRegister;
import cs211.project.utils.ImageSaver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.UUID;

public class CreateTeamController extends ComponentRegister {
    @FXML
    private HBox NavBarHBox;
    @FXML
    private VBox SideBarVBox;
    @FXML
    private DatePicker DataDeadline;
    @FXML
    private DatePicker DateOpeningDate;
    @FXML
    private TextField TextFieldQuantity;
    @FXML
    private TextField TextFieldTeamName;
    @FXML
    private ImageView addImage;

    private String teamID;
    private Event event;

    @FXML
    public void initialize() {
        this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml");
        this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml");

        this.teamID = UUID.randomUUID().toString();

        RouteProvider<Event> routeProvider = (RouteProvider<Event>) FXRouter.getData();
        this.event = routeProvider.getData();
    }

    @FXML
    public void importImage(ActionEvent event) {
        ImageSaver imageSaver = new ImageSaver(this.teamID, "team");
        imageSaver.saveImage(event);
        File selectedFile = imageSaver.file;
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            addImage.setImage(image);
            addImage.setUserData("data/images/team/" + this.teamID + "." + imageSaver.extention);
        }
    }

    @FXML
    public void onSave() {

        Team newTeam = new Team(this.teamID,
                TextFieldTeamName.getText(),
                TextFieldQuantity.getText(),
                DateOpeningDate.getValue().atStartOfDay(),
                DataDeadline.getValue().atStartOfDay(),
                Authentication.currentUser,
                this.event
        );

        TeamFileListDatasource teamFileListDatasource = new TeamFileListDatasource();
        TeamCollection teamOldData = teamFileListDatasource.readData();
        teamOldData.add(newTeam);

        TextFieldTeamName.clear();
        TextFieldQuantity.clear();
        DateOpeningDate.setValue(null);
        DataDeadline.setValue(null);
    }

    @FXML
    public void onCancel() {
        TextFieldTeamName.clear();
        TextFieldQuantity.clear();
        DateOpeningDate.setValue(null);
        DataDeadline.setValue(null);
    }

    @FXML
    public void onBack() {
        try {
            FXRouter.goTo("set-event-detail");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
