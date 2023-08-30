package cs211.project.models;


import java.time.LocalDateTime;

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

}
