package cs211.project.models.collections;

import cs211.project.models.Activities;
import cs211.project.models.ActivitiesEvent;
import cs211.project.models.ActivitiesTeam;
import cs211.project.models.Team;

import java.util.ArrayList;

public class ActivitiesTeamCollection {
    private ArrayList<ActivitiesTeam> activitiesArrayList;

    public ActivitiesTeamCollection() {
        this.activitiesArrayList = new ArrayList<>();
    }

    public void add(ActivitiesTeam activities) {
        this.activitiesArrayList.add(activities);
    }


    public ArrayList<ActivitiesTeam> getActivitiesArrayList() {
        return this.activitiesArrayList;
    }


    public void update(ActivitiesTeam activitiesTeam) {
        for (int i = 0; i < this.activitiesArrayList.size(); i++) {
            if (this.activitiesArrayList.get(i).getId().equals(activitiesTeam.getId())) {
                this.activitiesArrayList.set(i, activitiesTeam);
            }
        }
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

    public ActivitiesTeamCollection findByTeamId(String id) {
        ActivitiesTeamCollection activitiesTeamCollection = new ActivitiesTeamCollection();
        for (ActivitiesTeam activities : this.activitiesArrayList) {
            if (activities.getTeam().getId().equals(id)) {
                activitiesTeamCollection.add(activities);
            }
        }
        return activitiesTeamCollection;
    }

    public ActivitiesTeamCollection removeByTeam(Team team) {
        ActivitiesTeamCollection activitiesTeamCollection = new ActivitiesTeamCollection();
        for (ActivitiesTeam activities : this.activitiesArrayList) {
            if (!activities.getTeam().equals(team)) {
                activitiesTeamCollection.add(activities);
            }
        }
        this.activitiesArrayList = activitiesTeamCollection.getActivitiesArrayList();
        return activitiesTeamCollection;
    }

    public Activities[] getActivities() {
        Activities[] activities = new Activities[this.activitiesArrayList.size()];
        for (int i = 0; i < this.activitiesArrayList.size(); i++) {
            activities[i] = this.activitiesArrayList.get(i);
        }
        return activities;
    }
}
