package me.dgpr.common.exception;

public class PermissionDeniedException extends RuntimeException {

    private static final String LOG_FORMAT = "Permission denied: [target=%s, param=%s]";
    private static final String MESSAGE = "권한이 없습니다.";
    private final String target;
    private final String param;

    public PermissionDeniedException(
            final String target,
            final String param) {
        super(MESSAGE);
        this.target = target;
        this.param = param;
    }

    public String getLogFormat() {
        return String.format(LOG_FORMAT, target, param);
    }
}
