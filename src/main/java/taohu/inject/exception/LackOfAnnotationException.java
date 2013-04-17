package taohu.inject.exception;

/**
 * Created with IntelliJ IDEA.
 * User: twer
 * Date: 13-4-17
 * Time: 上午6:49
 * To change this template use File | Settings | File Templates.
 */
public class LackOfAnnotationException extends Exception{
    public LackOfAnnotationException(String message) {
        super(message);
    }
}
