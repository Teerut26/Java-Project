package cs211.project.models;

import at.favre.lib.crypto.bcrypt.BCrypt;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.TeamCollection;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private String id;
    private String nameUser;
    private String userName;
    private String password;
    private String imageProfile;
    private LocalDateTime lastLogin;
    private String role = "user";
    private EventCollection eventCollection;
    private TeamCollection teamCollection;

    public User(String id, String nameUser, String userName, String password, LocalDateTime lastLogin) {
        this.id = id;
        this.nameUser = nameUser;
        this.userName = userName;
        this.password = password;
        this.lastLogin = lastLogin;
    }

    public User(String id, String nameUser, String userName, String password, String role, String imagePath, LocalDateTime lastLogin) {
        this.id = id;
        this.nameUser = nameUser;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.imageProfile = imagePath;
        this.lastLogin = lastLogin;
    }

    public boolean isAdmin() {
        return this.role.equals("admin");
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
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
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

    public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
        if (validatePassword(oldPassword)) {
            if (newPassword.equals(confirmPassword)) {
                this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            }
        }
    }

    public boolean validatePassword(String password) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), this.password);
        return result.verified;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
