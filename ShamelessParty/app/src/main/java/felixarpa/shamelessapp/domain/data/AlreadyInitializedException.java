package felixarpa.shamelessapp.domain.data;

public class AlreadyInitializedException extends Exception {
    public AlreadyInitializedException(String message) {
        super(message);
    }
}
