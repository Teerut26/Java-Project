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
        ArrayList<CommentActivitiesTeam> tempComments = new ArrayList<>();
        for (CommentActivitiesTeam comment : this.comments) {
            if (comment.getActivitiesTeam() != null) {
                tempComments.add(comment);
            }
        }
        return tempComments;
    }

    public void setComments(ArrayList<CommentActivitiesTeam> comments) {
        this.comments = comments;
    }
}
