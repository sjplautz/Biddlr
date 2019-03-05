package classes;

import java.time.LocalDateTime;

public class LocalDateTimeWrapped {
    public String localDateTime;

    public LocalDateTimeWrapped() {

    }

    public LocalDateTimeWrapped(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTimeWrapped(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime.toString();
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.parse(this.localDateTime);
    }
}