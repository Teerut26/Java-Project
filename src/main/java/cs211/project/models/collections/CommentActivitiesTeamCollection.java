package cs211.project.models.collections;

import cs211.project.models.CommentActivitiesEvent;
import cs211.project.models.CommentActivitiesTeam;

import java.util.ArrayList;

public class CommentActivitiesTeamCollection {
    private ArrayList<CommentActivitiesTeam> comments;

    public CommentActivitiesTeamCollection() {
        this.comments = new ArrayList<>();
    }

    public void add(CommentActivitiesTeam comment) {
        this.comments.add(comment);
    }

    public ArrayList<CommentActivitiesTeam> getComments() {
        return this.comments;
    }

    public void setComments(ArrayList<CommentActivitiesTeam> comments) {
        this.comments = comments;
    }
}
