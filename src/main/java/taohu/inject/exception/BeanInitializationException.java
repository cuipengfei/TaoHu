package taohu.inject.exception;

public class BeanInitializationException extends Exception{

    public BeanInitializationException(String message, Exception e) {
        super(message, e);
    }
}
