package cs211.project.services;
import cs211.project.models.User;
import cs211.project.services.datasource.UserFileListDatasource;
import javafx.application.HostServices;

public class SingletonStorage {
    private static SingletonStorage single_instance = null;
    public User userSession;
    private HostServices hostServices;

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
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public HostServices getHostServices() {
        return hostServices;
    }
}
