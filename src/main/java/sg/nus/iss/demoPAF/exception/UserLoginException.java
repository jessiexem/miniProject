package sg.nus.iss.demoPAF.exception;

public class UserLoginException extends Exception {

    private String reason;

    public UserLoginException(String reason) {
        super();
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
