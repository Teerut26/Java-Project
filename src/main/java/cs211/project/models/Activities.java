package cs211.project.models;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Activities {
    private String id;
    private String title;
    private String detail;
    private String startTime;
    private String endTime;

    public Activities(String id, String title, String detail, String startTime, String endTime) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStatus() {
        LocalDateTime now = LocalDateTime.now();
        String[] starttime = startTime.split(":");
        LocalDateTime timeStart = now
                .plusHours(Integer.parseInt(starttime[0]))
                .plusMinutes(Integer.parseInt(starttime[2]))
                .plusSeconds(Integer.parseInt(starttime[3]));

        String[] endtime = endTime.split(":");
        LocalDateTime timeend = now
                .plusHours(Integer.parseInt(endtime[0]))
                .plusMinutes(Integer.parseInt(endtime[2]))
                .plusSeconds(Integer.parseInt(endtime[3]));
        if (now.isBefore(timeend)) {
            return "Upcoming";
        } else if (now.isAfter(timeStart) && now.isBefore(timeend)) {
            return "Ongoing";
        } else {
            return "Finished";
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Activities{" + "title=" + title + '\'' + ", detail=" + detail + '\'' +
                ", startDate=" + startTime + '\'' + ", endDate=" + endTime + '}';
    }

    @Override
    public boolean equals(Object o) {
        Activities that = (Activities) o;
        return this.id.equals(that.id);
    }

}
