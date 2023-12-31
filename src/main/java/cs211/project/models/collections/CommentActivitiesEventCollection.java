package cs211.project.models.collections;

import cs211.project.models.ActivitiesEvent;
import cs211.project.models.Comment;
import cs211.project.models.CommentActivitiesEvent;

import java.util.ArrayList;

public class CommentActivitiesEventCollection {
    private ArrayList<CommentActivitiesEvent> comments;

    public CommentActivitiesEventCollection() {
        this.comments = new ArrayList<>();
    }

    public void add(CommentActivitiesEvent comment) {
        this.comments.add(comment);
    }

    public ArrayList<CommentActivitiesEvent> getComments() {
        ArrayList<CommentActivitiesEvent> tempComments = new ArrayList<>();
        for (CommentActivitiesEvent comment : this.comments) {
            if (comment.getActivitiesEvent() != null) {
                tempComments.add(comment);
            }
        }
        return tempComments;
    }


    public void remove(CommentActivitiesEvent comment) {
        this.comments.remove(comment);
    }


}
