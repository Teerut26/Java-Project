package cs211.project.services;
import cs211.project.models.User;
import cs211.project.services.datasource.UserFileListDatasource;

public class SingletonStorage {
    private static SingletonStorage single_instance = null;
    public User userSession;

    public User getUserSession() {
        return userSession;
    }

    private SingletonStorage() {
    }

    public static synchronized SingletonStorage getInstance() {
        if (single_instance == null)
            single_instance = new SingletonStorage();

        return single_instance;
    }

    private void updateUserSession() {
        User newData = new UserFileListDatasource().readData().findById(this.userSession.getId());
        this.userSession = userSession;
    }
}
