package software.plusminus.admin.exception;

public class AdminException extends RuntimeException {

    public AdminException() {
    }

    public AdminException(String s) {
        super(s);
    }

    public AdminException(String s, Throwable cause) {
        super(s, cause);
    }
}
