package cs211.project.models;

import java.time.LocalDateTime;

public class Activities {

    private String title;
    private String detail;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Activities(String title, String detail, LocalDateTime startDate, LocalDateTime endDate){
        this.title = title;
        this.detail = detail;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTitle(){
        return title;
    }
    public String getDetail(){
        return detail;
    }

    public LocalDateTime getStartDate(){
        return startDate;
    }

    public LocalDateTime getEndDate(){
        return endDate;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDetail(String detail){
        this.detail = detail;
    }

    public void setStartDate(LocalDateTime startDate){
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate){
        this.endDate = endDate;
    }

    @Override
    public String toString(){
        return "Activities{" + "title=" + title + '\''+ ", detail=" + detail + '\'' +
                ", startDate=" + startDate + '\'' + ", endDate=" + endDate + '}';
    }

}
