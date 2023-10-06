package cs211.project.services.comparator;

import cs211.project.models.User;

import java.util.Comparator;

public class UserLastloginComparator implements Comparator<User> {
    @Override
    public int compare(User o1, User o2) {
        return o2.getLastLogin().compareTo(o1.getLastLogin());
    }
}
