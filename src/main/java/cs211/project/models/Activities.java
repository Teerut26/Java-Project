package cs211.project.models;

import cs211.project.utils.TimeValidate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Activities {
    private String id;
    private String title;
    private String detail;
    protected LocalDateTime dateStart;
    protected LocalDateTime dateEnd;
    protected String startTime;
    protected String endTime;

    public Activities(String id, String title, String detail, LocalDateTime dateStart, LocalDateTime dateEnd) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.setTimeToLocalDateTime();
    }

    private void setTimeToLocalDateTime() {
        String hourStart = String.valueOf(this.dateStart.getHour());
        String minuteStart = String.valueOf(this.dateStart.getMinute());
        String secondStart = String.valueOf(this.dateStart.getSecond());

        String hourEnd = String.valueOf(this.dateEnd.getHour());
        String minuteEnd = String.valueOf(this.dateEnd.getMinute());
        String secondEnd = String.valueOf(this.dateEnd.getSecond());

        this.startTime = hourStart + ":" + minuteStart + ":" + secondStart;
        this.endTime = hourEnd + ":" + minuteEnd + ":" + secondEnd;
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

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public LocalDateTime getDateEnd() {
        return dateEnd;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(this.dateStart)) {
            return "Not started";
        } else if (now.isAfter(this.dateEnd)) {
            return "Finished";
        } else {
            return "In progress";
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(LocalDateTime dateEnd) {
        this.dateEnd = dateEnd;
    }

    @Override
    public String toString() {
        return "Activities{" + "title=" + title + '\'' + ", detail=" + detail + '\'' +
                ", startDate=" + startTime + '\'' + ", endDate=" + endTime + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activities that = (Activities) o;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
