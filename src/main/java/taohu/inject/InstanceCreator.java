package taohu.inject;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.sun.istack.internal.Nullable;
import taohu.inject.exception.IllegalAnnotationQuantityException;
import taohu.inject.exception.LackOfAnnotationException;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class InstanceCreator {
    public Object getInstanceOf(String className)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, LackOfAnnotationException, IllegalAnnotationQuantityException {
        Class<?> clazz = Class.forName(className);

        Constructor<?> constructor = getSuitableConstructor(clazz);

        Class<?>[] parameterTypes = constructor.getParameterTypes();

        List<Object> parameters = getParameters(parameterTypes);

        if (parameters.size() > 0) {
            return constructor.newInstance(parameters.toArray());
        } else {
            return constructor.newInstance();
        }
    }

    private Constructor<?> getSuitableConstructor(Class<?> clazz) throws LackOfAnnotationException, IllegalAnnotationQuantityException {
        Constructor<?>[] constructors = clazz.getConstructors();
        Constructor suitableConstructor = null;
        if (constructors.length == 1) {
            Constructor<?> constructor = constructors[0];
            boolean hasPara = constructor.getParameterTypes().length > 0;
            if (hasPara) {
                ArrayList<Annotation> annotations = Lists.newArrayList(constructor.getAnnotations());
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType().equals(Inject.class)) {
                        suitableConstructor = constructor;
                    }
                }
            } else {
                suitableConstructor = constructor;
            }
        } else {
            int annotationCount = 0;
            for (Constructor constructor : constructors) {
                ArrayList<Annotation> annotations = Lists.newArrayList(constructor.getAnnotations());
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType().equals(Inject.class)) {
                        suitableConstructor = constructor;
                        annotationCount++;
                    }
                }
            }

            if (annotationCount == 0) {
                throw new LackOfAnnotationException("Should have Inject annotation on one of Constructors");
            }

            if (annotationCount > 1) {
                throw new IllegalAnnotationQuantityException("Only allow one constructor to have Inject annotation");
            }
        }

        if (suitableConstructor == null) {
            throw new LackOfAnnotationException("Should have Inject annotation on Constructor with parameters");
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
