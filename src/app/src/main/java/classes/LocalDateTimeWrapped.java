package classes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Wrapper class for LocalDateTime
 * Firebase requires empty constructors for classes to be written/read from database (which LocalDateTime does not have)
 */
public class LocalDateTimeWrapped {
    public String localDateTime;
    public long milliseconds; //used for expiring the job on the backend

    public LocalDateTimeWrapped() { }

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