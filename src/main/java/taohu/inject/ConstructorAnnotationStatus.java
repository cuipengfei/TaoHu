package taohu.inject;

import taohu.inject.exception.IllegalAnnotationQuantityException;
import taohu.inject.exception.LackOfAnnotationException;

public enum ConstructorAnnotationStatus {
    OneAnnotatedAsInject(null),
    NoOneAnnotatedAsInject(new LackOfAnnotationException("Should have Inject annotation on one of Constructors")),
    MoreThanOneAnnotatedAsInject(new IllegalAnnotationQuantityException("Only allow one constructor to have Inject annotation"));

    private Exception exception;

    ConstructorAnnotationStatus(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
