package cs211.project.models.collections;

import cs211.project.models.Activities;
import cs211.project.models.Event;
import cs211.project.models.User;

import java.util.ArrayList;

public class UserCollection {
    private ArrayList<User> userArrayList;

    public UserCollection() {
        this.userArrayList = new ArrayList<>();
    }

    public void add(User user) {
        this.userArrayList.add(user);
    }

    public User findByName(String title) {
        for (User user : this.userArrayList) {
            if (user.getNameUser().equals(title)) {
                return user;
            }
        }
        return null;
    }

    public User findById(String id) {
        for (User user : this.userArrayList) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public User findByUsername(String username) {
        for (User user : this.userArrayList) {
            if (user.getUserName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> getUsers() {
        return this.userArrayList;
    }

    public void setUsers(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }

    public void remove(User user) {
        this.userArrayList.remove(user);
    }
}
