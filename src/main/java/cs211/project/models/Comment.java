package cs211.project.models;


public class Comment {

    public String message;
    private String owner;

    public Comment(String message, String owner) {
        this.message = message;
        this.owner = owner;
    }

    public String getMessage() {
        return message;
    }

    public String getOwner() {
        return owner;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
