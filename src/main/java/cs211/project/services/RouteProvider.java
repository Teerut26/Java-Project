package cs211.project.services;

import cs211.project.models.User;

import java.util.ArrayList;

public class RouteProvider<T> {
    private T data;
    private ArrayList<T> dataArrayList;
    private User userSession;

    public RouteProvider(T data) {
        this.data = data;
    }

    public RouteProvider(ArrayList<T> dataArrayList) {
        this.dataArrayList = dataArrayList;
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
        return userSession;
    }

    public void setUserSession(User userSession) {
        this.userSession = userSession;
    }
}
