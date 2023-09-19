package cs211.project.models.collections;

import cs211.project.models.Activities;
import cs211.project.models.ActivitiesEvent;
import cs211.project.models.Event;

import java.util.ArrayList;

public class ActivitiesEventCollection {
    private ArrayList<ActivitiesEvent> activitiesArrayList;

    public ActivitiesEventCollection() {
        this.activitiesArrayList = new ArrayList<>();
    }

    public void add(ActivitiesEvent activities) {
        this.activitiesArrayList.add(activities);
    }

    public ActivitiesEvent findByTitle(String title) {
        for (ActivitiesEvent activities : this.activitiesArrayList) {
            if (activities.getTitle().equals(title)) {
                return activities;
            }
        }
        return null;
    }

    public ArrayList<ActivitiesEvent> getActivitiesArrayList() {
        return this.activitiesArrayList;
    }

    public void setActivitiesArrayList(ArrayList<ActivitiesEvent> activitiesArrayList) {
        this.activitiesArrayList = activitiesArrayList;
    }

    public void remove(ActivitiesEvent activitiesEvent) {
        this.activitiesArrayList.remove(activitiesEvent);
    }

    public ActivitiesEvent findById(String id) {
        for (ActivitiesEvent activities : this.activitiesArrayList) {
            if (activities.getId().equals(id)) {
                return activities;
            }
        }
        return null;
    }

    public ActivitiesEventCollection finsdByEventId(String id) {
        ActivitiesEventCollection activitiesEventCollection = new ActivitiesEventCollection();
        for (ActivitiesEvent activities : this.activitiesArrayList) {
            if (activities.getEvent().getEventID().equals(id)) {
                activitiesEventCollection.add(activities);
            }
        }
        return activitiesEventCollection;
    }
}
