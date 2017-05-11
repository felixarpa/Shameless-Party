package felixarpa.shamelessapp.domain.data;

class AlreadyInitializedException extends Exception {
    public AlreadyInitializedException(String message) {
        super(message);
    }
}
