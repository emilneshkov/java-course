package bg.sofia.uni.fmi.mjt.laundry.exception;

public class BasketOverloadedException extends RuntimeException {
    public BasketOverloadedException(String message) {
        super(message);
    }
}
