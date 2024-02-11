package me.dgpr.common.exception;

public class NotFoundException extends RuntimeException {

    private static final String LOG_FORMAT = "could not find %s [param=%s]";
    private static final String MESSAGE_FORMAT = "%s %s가 존재하지 않습니다.";
    private final String target;
    private final String param;


    public NotFoundException(
            final String target,
            final String param) {
        super(String.format(MESSAGE_FORMAT, target, param));
        this.target = target;
        this.param = param;
    }

    public String getTarget() {
        return target;
    }

    public String getParam() {
        return param;
    }

    public String getLogMessage() {
        return String.format(LOG_FORMAT, target, param);
    }
}
