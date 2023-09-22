package cs211.project.models;


import java.time.LocalDateTime;
import java.util.Objects;

public class Comment {
    private String id;
    public String message;
    private User owner;
    private LocalDateTime timeStamps;

    public Comment(String id, String message, User owner, LocalDateTime timeStamps ) {
        this.id = id;
        this.message = message;
        this.owner = owner;
        this.timeStamps = timeStamps;
    }

    public String getMessage() {
        return message;
    }

    public User getOwner() {
        return owner;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getTimeStamps() {
        return timeStamps;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id.equals(comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
