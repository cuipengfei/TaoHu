package taohu.inject.exception;

public class BeanNotRegisteredToCreateException extends Exception {
    public BeanNotRegisteredToCreateException(String message) {
        super(message);
    }
}
