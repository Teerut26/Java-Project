package cs211.project.utils;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeValidate {
    private String timeInput;
    private LocalDateTime refLocalDateTime;

    public TimeValidate(String timeInput) {
        this.timeInput = timeInput;
    }

    public TimeValidate(String timeInput, LocalDateTime refLocalDateTime) {
        this.timeInput = timeInput;
        this.refLocalDateTime = refLocalDateTime;
    }

    public boolean validate() {
        String timePattern = "^(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]|[0-9])$";
        Pattern pattern = Pattern.compile(timePattern);
        Matcher matcher = pattern.matcher(this.timeInput);
        return matcher.matches();
    }

    public Integer getHour() {
        String[] time = this.timeInput.split(":");
        return Integer.parseInt(time[0]);
    }

    public Integer getMinute() {
        String[] time = this.timeInput.split(":");
        return Integer.parseInt(time[1]);
    }

    public void addHour(Integer hour) {
        refLocalDateTime = refLocalDateTime.plusHours(hour);
    }

    public void addMinute(Integer minute) {
        refLocalDateTime = refLocalDateTime.plusMinutes(minute);
    }


    public void addTime(Integer hour, Integer minute) {
        refLocalDateTime = refLocalDateTime.plusHours(hour);
        refLocalDateTime = refLocalDateTime.plusMinutes(minute);
    }

    public String getTimeInput() {
        return timeInput;
    }

    public LocalDateTime getRefLocalDateTime() {
        return refLocalDateTime;
    }
}
