package bg.sofia.uni.fmi.mjt.tavern.exception;

public class BannedGuestException extends RuntimeException{
    public BannedGuestException(String message){
        super(message);
    }
}
