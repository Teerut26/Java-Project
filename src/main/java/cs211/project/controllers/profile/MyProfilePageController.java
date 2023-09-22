package cs211.project.controllers.profile;

import cs211.project.models.User;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.UserFileListDatasource;
import cs211.project.utils.ComponentRegister;
import cs211.project.utils.ImageSaver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MyProfilePageController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    @FXML
    private ImageView addImage;
    @FXML
    private TextField TextFieldName;
    @FXML
    private TextField TextFieldUserName;
    @FXML
    private Label errorLabel;
    private String profileID;
    private UserFileListDatasource userFileListDatasource;
    private UserCollection userCollection;
    private User user;
    private Image image;
    private RouteProvider<String> routeProvider;


    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        if (this.routeProvider.getData() != null && this.routeProvider.getData().equals("admin")) {
            this.loadSideBarComponentAdmin(SideBarVBox, "AdminSideBarComponent.fxml", this.routeProvider);
            this.loadNavBarComponentAdmin(NavBarHBox, "AdminNavbarComponent.fxml", this.routeProvider);
        } else {
            this.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
            this.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
        }
        this.profileID = UUID.randomUUID().toString();
        userFileListDatasource = new UserFileListDatasource();
        userCollection = userFileListDatasource.readData();
        user = userCollection.findByUsername(routeProvider.getUserSession().getUserName());
        TextFieldName.setText(user.getNameUser());
        TextFieldUserName.setText(user.getUserName());
        TextFieldUserName.setEditable(false);
        this.initProfile();
    }

    void initProfile() {
        if (user.getImageProfile() != null) {
            image = new Image("file:" + user.getImageProfile());
            addImage.setImage(image);
        }
    }

    @FXML
    public void importImage(ActionEvent event) {
        String userID = this.routeProvider.getUserSession().getId();
        ImageSaver imageSaver = new ImageSaver(userID, "user");
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
        String userID = this.routeProvider.getUserSession().getId();

        if (!user.getNameUser().equals(TextFieldName.getText())) {
            if (TextFieldName.getText().isEmpty()) {
                errorLabel.setText("Name is empty");
            } else if (userCollection.findByName(TextFieldName.getText()) != null) {
                errorLabel.setText("Name already taken");
            } else {
                user.setNameUser(TextFieldName.getText());
            }
        }

        ImageSaver imageSaver = (ImageSaver) addImage.getUserData();
        if (imageSaver != null) {
            imageSaver.saveImage();
            user.setImageProfile("data/images/user/" + userID + "." + imageSaver.extention);
        }
        errorLabel.setText("");
        userCollection.add(user);
        userFileListDatasource.writeData(userCollection);
    }

    @FXML
    public void onChangePasswordClick() {
        try {
            FXRouter.goTo("change-password-profile-page", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

