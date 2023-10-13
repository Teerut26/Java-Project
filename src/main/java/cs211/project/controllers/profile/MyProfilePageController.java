package cs211.project.controllers.profile;

import cs211.project.Main;
import cs211.project.models.User;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.SingletonStorage;
import cs211.project.services.datasource.UserFileListDatasource;
import cs211.project.utils.ComponentRegister;
import cs211.project.utils.ImageSaver;
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
import java.util.UUID;




public class MyProfilePageController {
    @FXML
    private BorderPane parentBorderPane;
    @FXML
    private ComboBox fontComboBox;
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
    @FXML
    private CheckBox checkBoxThemeMode;
    @FXML
    private Label themeModeChangeLabel;
    @FXML
    private Label fontSelectChangeLabel;


    private String profileID;
    private UserFileListDatasource userFileListDatasource;
    private UserCollection userCollection;
    private User user;
    private Image image;
    private RouteProvider routeProvider;

    private String fontSelect = "font-style1";
    private String themeModeSelect = "light";



    @FXML
    public void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        boolean isAdmin = SingletonStorage.getInstance().userSession.isAdmin();
        ComponentRegister cr = new ComponentRegister();

        if (isAdmin) {
            cr.loadSideBarComponentAdmin(SideBarVBox, "AdminSideBarComponent.fxml", this.routeProvider);
            cr.loadNavBarComponentAdmin(NavBarHBox, "AdminNavbarComponent.fxml", this.routeProvider);
        } else {
            cr.loadSideBarComponent(SideBarVBox, "SideBarComponent.fxml", this.routeProvider);
            cr.loadNavBarComponent(NavBarHBox, "NavBarComponent.fxml", this.routeProvider);
        }
        this.profileID = UUID.randomUUID().toString();
        userFileListDatasource = new UserFileListDatasource();
        userCollection = userFileListDatasource.readData();
        user = userCollection.findByUsername(routeProvider.getUserSession().getUserName());
        TextFieldName.setText(user.getNameUser());
        TextFieldUserName.setText(user.getUserName());
        TextFieldUserName.setEditable(false);
        this.initProfile();
        this.initializeThemeMode();
        initializeFontOption();
        initializeFont();
        themeModeChangeLabel.setText((""));
        fontSelectChangeLabel.setText((""));
    }



    void initProfile() {
        if (user.getImageProfile() != null) {
            image = new Image("file:" + user.getImageProfile());
            addImage.setImage(image);
        }
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
            checkBoxThemeMode.setSelected(true);
            parentBorderPane.getStylesheets().remove(pathLightMode);
            parentBorderPane.getStylesheets().add(pathDarkMode);
            themeModeSelect = "dark";
        }else if (this.routeProvider.getUserSession().getThemeMode().equals("light")) {
            checkBoxThemeMode.setSelected(false);
            parentBorderPane.getStylesheets().remove(pathDarkMode);
            parentBorderPane.getStylesheets().add(pathLightMode);
            themeModeSelect = "light";
        }
    }

    @FXML
    public void initializeFontOption(){
        fontComboBox.getItems().clear();
        fontComboBox.getItems().addAll("Arial","Tahoma","Times New Roman");
        fontComboBox.setValue("Arial");
    }

    @FXML
    public void initializeFont(){
        String currentFont =this.routeProvider.getUserSession().getFont();
        clearFontStyle();
        if (currentFont.equals("font-style1")){
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style1.css");
            fontComboBox.setValue("Arial");
            this.fontSelect = "font-style1";
        }else if (currentFont.equals("font-style2")){
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style2.css");
            fontComboBox.setValue("Tahoma");
            this.fontSelect = "font-style2";
        }else if (currentFont.equals("font-style3")){
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style3.css");
            fontComboBox.setValue("Times New Roman");
            this.fontSelect = "font-style3";
        }

    }

    @FXML
    public void clearFontStyle(){
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style1.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style2.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style3.css");
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
                return;
            } else if (userCollection.findByName(TextFieldName.getText()) != null) {
                errorLabel.setText("Name already taken");
                return;
            } else {
                user.setNameUser(TextFieldName.getText());

            }
        }
        user.setThemeMode(themeModeSelect);
        this.routeProvider.getUserSession().setThemeMode(themeModeSelect);
        initializeThemeMode();
        user.setFont(fontSelect);
        this.routeProvider.getUserSession().setFont(fontSelect);
        initializeFont();

        ImageSaver imageSaver = (ImageSaver) addImage.getUserData();
        if (imageSaver != null) {
            imageSaver.saveImage();
            user.setImageProfile("data/images/user/" + userID + "." + imageSaver.extention);
        }
        errorLabel.setText("");
        userCollection.update(user);
        userFileListDatasource.writeData(userCollection);
        themeModeChangeLabel.setText((""));
        fontSelectChangeLabel.setText((""));
        Alert alert = new Alert(Alert.AlertType.INFORMATION , "Saved" , ButtonType.OK);
        alert.showAndWait();


    }

    @FXML
    public void onChangePasswordClick() {
        try {
            FXRouter.goTo("change-password-profile-page", this.routeProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onChangeThemeMode() {
        if (checkBoxThemeMode.isSelected()) {
            this.themeModeSelect = "dark";

        } else {
            this.themeModeSelect = "light";
        }

        if(themeModeSelect.equals(this.routeProvider.getUserSession().getThemeMode())) {
            themeModeChangeLabel.setText((""));
        }else {
            themeModeChangeLabel.setText(("Change will be applied after save"));
        }

    }

    @FXML
    public void onSelectFont(){
        String fontName = fontComboBox.getValue().toString();
        String font;
        if (fontName.equals("Arial")){
            font = "font-style1";
        }else if (fontName.equals("Tahoma")){
            font = "font-style2";
        }else {
            font = "font-style3";
        }
        this.fontSelect = font;
            if(font.equals(this.routeProvider.getUserSession().getFont())) {
             fontSelectChangeLabel.setText((""));
            }else {
                fontSelectChangeLabel.setText(("Change will be applied after save"));
            }
    }

}

