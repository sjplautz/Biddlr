package classes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class LocalDateTimeWrapped {
    public String localDateTime;
    public long milliseconds;

    public LocalDateTimeWrapped() {

    }

    public LocalDateTimeWrapped(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTimeWrapped(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime.toString();
        this.milliseconds = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTime toLocalDateTime() {
        return  LocalDateTime.parse(this.localDateTime);
    }
}