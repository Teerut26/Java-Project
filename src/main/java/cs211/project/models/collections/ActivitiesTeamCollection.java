package cs211.project.models.collections;

import cs211.project.models.ActivitiesEvent;
import cs211.project.models.ActivitiesTeam;

import java.util.ArrayList;

public class ActivitiesTeamCollection {
    private ArrayList<ActivitiesTeam> activitiesArrayList;

    public ActivitiesTeamCollection() {
        this.activitiesArrayList = new ArrayList<>();
    }

    public void add(ActivitiesTeam activities) {
        this.activitiesArrayList.add(activities);
    }

    public ActivitiesTeam findByTitle(String title) {
        for (ActivitiesTeam activities : this.activitiesArrayList) {
            if (activities.getTitle().equals(title)) {
                return activities;
            }
        }
        return null;
    }

    public ArrayList<ActivitiesTeam> getActivitiesArrayList() {
        return this.activitiesArrayList;
    }

    public void setActivitiesArrayList(ArrayList<ActivitiesTeam> activitiesArrayList) {
        this.activitiesArrayList = activitiesArrayList;
    }

    public void remove(ActivitiesEvent activitiesEvent) {
        this.activitiesArrayList.remove(activitiesEvent);
    }

    public ActivitiesTeam findById(String id) {
        for (ActivitiesTeam activities : this.activitiesArrayList) {
            if (activities.getId().equals(id)) {
                return activities;
            }
        }
        return null;
    }
}
