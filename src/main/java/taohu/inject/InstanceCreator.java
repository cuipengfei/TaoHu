package taohu.inject;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import taohu.inject.exception.InitialInstanceException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class InstanceCreator {
    public Object getInstanceOf(String className)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, InitialInstanceException {
        Class<?> clazz = Class.forName(className);

        Constructor constructor = getSuitableConstructor(clazz);

        Class<?>[] parameterTypes = constructor.getParameterTypes();

        List<Object> parameters = getParameters(parameterTypes);

        if (parameters.size() > 0) {
            return constructor.newInstance(parameters.toArray());
        } else {
            return constructor.newInstance();
        }
    }

    private Constructor getSuitableConstructor(Class<?> clazz) throws InitialInstanceException {
        Constructor<?>[] constructors = clazz.getConstructors();

        Constructor suitableConstructor = null;
        int count = 0;
        for (Constructor constructor : constructors) {
            ArrayList<Annotation> annotations = Lists.newArrayList(constructor.getAnnotations());
            boolean hasParameter = constructor.getParameterTypes().length > 0;

            if(!hasParameter){
                suitableConstructor = constructor;
                count++;
            }else{
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType().equals(Inject.class)) {
                        suitableConstructor = constructor;
                        count++;
                    }
                }
            }
        }

        if(count == 0){
            throw new InitialInstanceException("There is at least one constructor width inject annotation");
        }

        if(count > 1){
            throw new InitialInstanceException("There is at most one constructor with inject annotation");
        }

        return suitableConstructor;
    }

    private List<Object> getParameters(Class<?>[] parameterTypes) {
        return Lists.transform(Lists.newArrayList(parameterTypes), new Function<Class<?>, Object>() {
            @Override
            public Object apply(@Nullable Class<?> paraType) {
                String paraTypeName = paraType.getName();
                try {
                    return getInstanceOf(paraTypeName);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
