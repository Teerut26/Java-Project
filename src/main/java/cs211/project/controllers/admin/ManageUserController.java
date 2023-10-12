package cs211.project.controllers.admin;

import cs211.project.models.User;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.comparator.UserLastloginComparator;
import cs211.project.services.datasource.UserFileListDatasource;
import cs211.project.utils.ComponentRegister;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.util.Callback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class ManageUserController {
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    @FXML
    private TableView userTableView;
    @FXML
    private BorderPane parentBorderPane;
    private RouteProvider routeProvider;
    private UserCollection userCollection;
    private User userSelect;
    @FXML
    private Label userId;

    @FXML
    private void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        ComponentRegister componentRegister = new ComponentRegister();
        componentRegister.loadSideBarComponentAdmin(SideBarVBox, "AdminSideBarComponent.fxml", this.routeProvider);
        componentRegister.loadNavBarComponentAdmin(NavBarHBox, "AdminNavbarComponent.fxml", this.routeProvider);

        userCollection = new UserFileListDatasource().readData();

        this.showTable();
        this.setUpUserTableViewOnSelect();
        this.initializeFont();
        this.initializeThemeMode();
    }

    @FXML
    public void initializeThemeMode() {
        if (routeProvider.getUserSession().getThemeMode().equals("dark")) {
            parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/light-mode.css");
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/dark-mode.css");
        } else if (routeProvider.getUserSession().getThemeMode().equals("light")) {
            parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/dark-mode.css");
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/light-mode.css");
        }
    }

    @FXML
    public void initializeFont() {
        clearFontStyle();
        if (routeProvider.getUserSession().getFont().equals("font-style1")) {
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style1.css");
        } else if (routeProvider.getUserSession().getFont().equals("font-style2")) {
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style2.css");
        } else if (routeProvider.getUserSession().getFont().equals("font-style3")) {
            parentBorderPane.getStylesheets().add("file:src/main/resources/cs211/project/style/font-style3.css");
        }
    }

    public void clearFontStyle() {
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style1.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style2.css");
        parentBorderPane.getStylesheets().remove("file:src/main/resources/cs211/project/style/font-style3.css");
    }

    private void setUpUserTableViewOnSelect() {
        userTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue observable, User oldValue, User newValue) {
                if (newValue != null) {
                    userSelect = newValue;
                    userId.setText(userSelect.getId());
                } else {
                    userSelect = null;
                    userId.setText("");
                }
            }
        });
    }

    private void showTable() {
        this.userId.setText("");
        TableColumn<User, String> imageColumn = new TableColumn<>("Profile");
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageProfile"));

        imageColumn.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>() {
            @Override
            public TableCell<User, String> call(TableColumn<User, String> param) {
                return new TableCell<User, String>() {
                    private ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String imageUrl, boolean empty) {
                        super.updateItem(imageUrl, empty);
                        if (imageUrl == null || empty) {
                            setGraphic(null);
                        } else {
                            Image image = new Image("file:" + imageUrl);
                            imageView.setImage(image);
                            imageView.setFitWidth(50);
                            imageView.setFitHeight(50);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });

        TableColumn<User, String> idColumn = new TableColumn<>("#");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setMaxWidth(50);

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        TableColumn<User, String> lastLoginColumn = new TableColumn<>("lastLogin");
        lastLoginColumn.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));

        lastLoginColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> parameter) {
                        User user = parameter.getValue();
                        return Bindings.createStringBinding(
                                () -> user.getLastLogin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                    }
                });

        userTableView.getColumns().clear();
        userTableView.getColumns().addAll(imageColumn, idColumn, usernameColumn, lastLoginColumn);

        userTableView.getItems().clear();

        userCollection.sort(new UserLastloginComparator());

        for (User user : userCollection.getUsers()) {
            userTableView.getItems().add(user);
        }
    }

    @FXML
    void onUserDelete() {
        if (userSelect.isAdmin()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You can't delete admin", ButtonType.OK);
            alert.show();
            return;
        }
        if (userSelect != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You want delete : " + userSelect.getUserName() + " ?",
                    ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.CANCEL) {
                return;
            }
            UserCollection newUserCollection = new UserCollection();
            for (User user : userCollection.getUsers()) {
                if (!user.getId().equals(userSelect.getId())) {
                    newUserCollection.add(user);
                }
            }
            new UserFileListDatasource().writeData(newUserCollection);
            userCollection = newUserCollection;
            this.showTable();
        }
    }
}
