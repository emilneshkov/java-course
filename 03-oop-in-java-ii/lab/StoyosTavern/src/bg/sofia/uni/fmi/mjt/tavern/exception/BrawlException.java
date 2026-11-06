package bg.sofia.uni.fmi.mjt.tavern.exception;

public class BrawlException extends RuntimeException {
    public BrawlException(ProvocationException cause) {
        super(cause);
    }
}
