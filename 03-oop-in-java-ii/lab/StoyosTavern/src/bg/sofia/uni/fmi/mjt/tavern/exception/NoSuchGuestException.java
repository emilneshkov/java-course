package bg.sofia.uni.fmi.mjt.tavern.exception;

public class NoSuchGuestException extends RuntimeException{
    public NoSuchGuestException(String message){
        super(message);
    }
}
