package cs211.project.models.collections;

import cs211.project.models.Comment;
import cs211.project.models.Event;

import java.util.ArrayList;

public class CommentCollection {
    private ArrayList<Comment> comments;

    public CommentCollection() {
        this.comments = new ArrayList<>();
    }

    public void add(Comment comment) {
        this.comments.add(comment);
    }

    public ArrayList<Comment> getComments() {
        return this.comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
