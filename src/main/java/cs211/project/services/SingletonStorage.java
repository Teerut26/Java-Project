package cs211.project.services;
import cs211.project.models.User;
import cs211.project.services.datasource.UserFileListDatasource;

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

    public void setUserSession(User userSession) {
        this.userSession = userSession;
    }
}
