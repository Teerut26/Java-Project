package cs211.project.controllers.admin;

import cs211.project.models.User;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.FXRouter;
import cs211.project.services.RouteProvider;
import cs211.project.services.datasource.UserFileListDatasource;
import cs211.project.utils.ComponentRegister;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ManageUserController extends ComponentRegister {
    @FXML
    private VBox SideBarVBox;
    @FXML
    private HBox NavBarHBox;
    @FXML
    private TableView userTableView;
    private RouteProvider routeProvider;
    private UserCollection userCollection;
    private User userSelect;
    @FXML
    private Label userId;

    @FXML
    private void initialize() {
        routeProvider = (RouteProvider) FXRouter.getData();
        this.loadSideBarComponentAdmin(SideBarVBox, "AdminSideBarComponent.fxml", this.routeProvider);
        this.loadNavBarComponentAdmin(NavBarHBox, "AdminNavbarComponent.fxml", this.routeProvider);

        userCollection = new UserFileListDatasource().readData();

        this.showTable();
        this.setUpUserTableViewOnSelect();
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
        TableColumn<User, String> idColumn = new TableColumn<>("#");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameUser"));

        TableColumn<User, String> lastLoginColumn = new TableColumn<>("lastLogin");
        lastLoginColumn.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));

        userTableView.getColumns().clear();
        userTableView.getColumns().add(idColumn);
        userTableView.getColumns().add(usernameColumn);
        userTableView.getColumns().add(nameColumn);
        userTableView.getColumns().add(lastLoginColumn);

        userTableView.getItems().clear();

        for (User user : userCollection.getUsers()) {
            userTableView.getItems().add(user);
        }
    }


    @FXML
    void onUserDelete(ActionEvent event) {
        if (userSelect.isAdmin()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You can't delete admin", ButtonType.OK);
            alert.show();
            return;
        }
        if (userSelect != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You want delete : " + userSelect.getUserName() + " ?", ButtonType.OK, ButtonType.CANCEL);
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
