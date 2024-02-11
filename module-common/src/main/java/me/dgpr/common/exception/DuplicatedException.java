package me.dgpr.common.exception;

public class DuplicatedException extends RuntimeException {

    private static final String LOG_FORMAT = "Duplicated: %s [param=%s]";
    private static final String MESSAGE_FORMAT = "중복된 %s입니다.";
    private final String target;
    private final String param;


    public DuplicatedException(
            final String target,
            final String param) {
        super(String.format(MESSAGE_FORMAT, target));
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
