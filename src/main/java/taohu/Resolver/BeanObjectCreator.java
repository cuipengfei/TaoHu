package taohu.Resolver;

import taohu.model.BeanDescriptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BeanObjectCreator {

    public Object createBean(BeanDescriptor beanDescriptor) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = beanDescriptor.getClazz().getConstructor();
        Object object = constructor.newInstance();
        return object;
    }
}