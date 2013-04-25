package taohu.inject.injectors;

import taohu.inject.exception.BeanCreateException;
import taohu.inject.exception.BeanNotRegisteredException;
import taohu.inject.exception.LackOfAnnotationException;

public interface Injector {
    public Object inject(Object instance, Class<?> clazz)
            throws BeanCreateException, LackOfAnnotationException, BeanNotRegisteredException;
}
