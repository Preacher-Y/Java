
import java.time.LocalDateTime;


public class AuditLogs {
    public int id;
    public LocalDateTime timestamp;

    public AuditLogs(int id,LocalDateTime timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

}
