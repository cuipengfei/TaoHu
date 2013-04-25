package taohu.inject.exception;

public class BeanCreateException extends Exception {
    public BeanCreateException(String message) {
        super(message);
    }

    public BeanCreateException(String message, Exception e) {
        super(message, e);
    }
}
