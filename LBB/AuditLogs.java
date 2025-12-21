
import java.time.LocalDateTime;


public class AuditLogs {
    public int id;
    public LocalDateTime timestamp;
    public String title;

    public AuditLogs(int id,LocalDateTime timestamp, String title) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
    }

}
