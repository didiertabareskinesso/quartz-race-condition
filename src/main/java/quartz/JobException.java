package quartz;

public class JobException extends RuntimeException {

    public JobException(Throwable cause) {
        super(cause);
    }

    public JobException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobException(String message) {
        super(message);
    }
}
