package taohu.inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InstanceCreator {
    public Object getInstanceOf(String fullyQualifiedTypeName) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = Class.forName(fullyQualifiedTypeName);
        Constructor<?> constructor = clazz.getConstructor();
        return constructor.newInstance();
    }
}
