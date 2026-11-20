package bg.sofia.uni.fmi.mjt.laundry.exception;

public class AlreadyQueuedException extends RuntimeException {
    public AlreadyQueuedException(String message) {
        super(message);
    }
}
