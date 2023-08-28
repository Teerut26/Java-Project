package cs211.project.models;

import java.time.LocalDateTime;

public class User {
    private String id;
    private String nameUser;
    private String userName;
    private String password;
    private String email;
    private String imageProfile;
    private LocalDateTime lastLogin;
    private String role;

    public User(String id, String nameUser, String userName,String email, String password, LocalDateTime lastLogin){
        this.id = id;
        this.nameUser = nameUser;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.lastLogin =lastLogin;
    }

    public void setImageProfile(String imagePath){
        this.imageProfile = imagePath;
    }

    public void setPassword(String oldPassword, String newPassword, String confirmPassword){
        if(oldPassword.equals(password)){
            if(newPassword.equals(confirmPassword)){
                this.password = newPassword;
            }
        }
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getId(){
        return id;
    }

    public String getNameUser(){
        return nameUser;
    }

    public String getUserName(){
        return userName;
    }

    public LocalDateTime getLastLogin(){
        return lastLogin;
    }

    public String getRole(){
        return role;
    }

    public String getPassword(){
        return password;
    }

    public String getImageProfile(){
        return imageProfile;
    }

    public String getEmail(){
        return email;
    }

    public LocalDateTime getLastlogin(){
        return lastLogin;
    }

    @Override
    public String toString(){
        return "User{" +
                "ID=" + id + '\'' +
                ", nameUser=" + nameUser + '\'' +
                ", userName=" + userName + '\'' +
                ", email=" + email + '\'' +
                ", password=" + password + '\'' +
                ", lastLogin=" + lastLogin +
                '}';
    }

}
