package cs211.project.services;
import cs211.project.models.User;

public class SingletonStorage {
    private static SingletonStorage single_instance = null;
    public User userSession;

    private SingletonStorage() {
    }

    public static synchronized SingletonStorage getInstance() {
        if (single_instance == null)
            single_instance = new SingletonStorage();

        return single_instance;
    }
}
