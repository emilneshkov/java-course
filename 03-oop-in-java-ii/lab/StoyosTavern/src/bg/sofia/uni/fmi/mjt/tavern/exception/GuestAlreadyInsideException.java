package bg.sofia.uni.fmi.mjt.tavern.exception;

public class GuestAlreadyInsideException extends RuntimeException{
    public GuestAlreadyInsideException(String message){
        super(message);
    }
}
