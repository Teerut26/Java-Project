package cs211.project.models.collections;

import cs211.project.models.Activities;
import cs211.project.models.Event;

import java.util.ArrayList;

public class ActivitiesCollection {
    private ArrayList<Activities> activitiesArrayList;

    public ActivitiesCollection() {
        this.activitiesArrayList = new ArrayList<>();
    }

    public void add(Activities activities) {
        this.activitiesArrayList.add(activities);
    }
    public void remove(Event event) {
        this.activitiesArrayList.remove(event);
    }

    public Activities findById(String id) {
        for (Activities activities : this.activitiesArrayList) {
            if (activities.getId().equals(id)) {
                return activities;
            }
        }
        return null;
    }
}
