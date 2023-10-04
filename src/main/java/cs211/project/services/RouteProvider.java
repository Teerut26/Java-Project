package cs211.project.services;

import cs211.project.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class RouteProvider<T> {
    private T data;
    private HashMap dataHashMap;
    private ArrayList<T> dataArrayList;

    public RouteProvider(T data) {
        this.data = data;
        this.dataHashMap = new HashMap<String, Object>();
    }

    public RouteProvider(ArrayList<T> dataArrayList) {
        this.dataArrayList = dataArrayList;
        this.dataHashMap = new HashMap<String, Object>();
    }

    public RouteProvider() {
        this.data = null;
    }

    public T getData() {
        return data;
    }

    public ArrayList<T> getDataArrayList() {
        return dataArrayList;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setDataArrayList(ArrayList<T> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    public User getUserSession() {
        return SingletonStorage.getInstance().userSession;
    }

    public void setUserSession(User userSession) {
        SingletonStorage.getInstance().setUserSession(userSession);
    }

    public void addHashMap(String key, Object value) {
        this.dataHashMap.put(key, value);
    }

    public HashMap getDataHashMap() {
        return this.dataHashMap;
    }
}
