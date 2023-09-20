package cs211.project.utils;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeValidate {
    private String timeInput;

    public TimeValidate(String timeInput) {
        this.timeInput = timeInput;
    }

    public boolean validate() {
        String timePattern = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$";
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

    public Integer getSecond() {
        String[] time = this.timeInput.split(":");
        return Integer.parseInt(time[2]);
    }

    public void addHour(LocalDateTime refLocalDateTime, Integer hour) {
        refLocalDateTime.plusHours(hour);
    }

    public void addMinute(LocalDateTime refLocalDateTime, Integer minute) {
        refLocalDateTime.plusMinutes(minute);
    }

    public void addSecond(LocalDateTime refLocalDateTime, Integer second) {
        refLocalDateTime.plusSeconds(second);
    }
}
