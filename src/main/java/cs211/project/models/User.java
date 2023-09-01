package cs211.project.models;

import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.TeamCollection;

import java.time.LocalDateTime;

public class User {
    private String id;
    private String nameUser;
    private String userName;
    private String password;
    private String imageProfile;
    private LocalDateTime lastLogin;
    private String role;
    private EventCollection eventCollection;
    private TeamCollection teamCollection;

    public User(String id, String nameUser, String userName, String password, LocalDateTime lastLogin) {
        this.id = id;
        this.nameUser = nameUser;
        this.userName = userName;
        this.password = password;
        this.lastLogin = lastLogin;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setTeamCollection(TeamCollection teamCollection) {
        this.teamCollection = teamCollection;
    }

    public TeamCollection getTeamCollection() {
        return teamCollection;
    }

    public EventCollection getEventCollection() {
        return eventCollection;
    }

    public void setEventCollection(EventCollection eventCollection) {
        this.eventCollection = eventCollection;
    }

    public void setImageProfile(String imagePath) {
        this.imageProfile = imagePath;
    }

    public void setPassword(String oldPassword, String newPassword, String confirmPassword) {
        if (oldPassword.equals(password)) {
            if (newPassword.equals(confirmPassword)) {
                this.password = newPassword;
            }
        }
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getNameUser() {
        return nameUser;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public LocalDateTime getLastlogin() {
        return lastLogin;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID=" + id + '\'' +
                ", nameUser=" + nameUser + '\'' +
                ", userName=" + userName + '\'' +
                ", password=" + password + '\'' +
                ", lastLogin=" + lastLogin +
                '}';
    }

}
