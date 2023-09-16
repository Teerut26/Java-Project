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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
        TableColumn<User, String> cardNumberColumn = new TableColumn<>("Username");
        cardNumberColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameUser"));

        userTableView.getColumns().clear();
        userTableView.getColumns().add(cardNumberColumn);
        userTableView.getColumns().add(nameColumn);

        userTableView.getItems().clear();

        // ใส่ข้อมูล Student ทั้งหมดจาก studentList ไปแสดงใน TableView
        for (User user : userCollection.getUsers()) {
            userTableView.getItems().add(user);
        }
    }


    @FXML
    void onUserDelete(ActionEvent event) {
        if (userSelect != null) {
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
