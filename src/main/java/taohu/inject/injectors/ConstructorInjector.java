package taohu.inject.injectors;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import taohu.inject.BeanObjectCreator;
import taohu.inject.enums.ConstructorAnnotationStatus;
import taohu.inject.exception.BeanCreateException;
import taohu.inject.exception.BeanNotRegisteredException;
import taohu.inject.exception.LackOfAnnotationException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ConstructorInjector implements Injector {

    private BeanObjectCreator beanObjectCreator;

    public ConstructorInjector(BeanObjectCreator beanObjectCreator) {
        this.beanObjectCreator = beanObjectCreator;
    }

    @Override
    public Object inject(Object instance, Class<?> clazz) throws Exception {
        Constructor suitableConstructor = getSuitableConstructor(clazz);

        return injectConstructor(suitableConstructor);
    }

    private static boolean isOnlyConstructorSuitable(Constructor<?> constructor)
            throws LackOfAnnotationException {
        Constructor suitableConstructor = null;
        boolean hasPara = constructor.getParameterTypes().length > 0;
        if (!hasPara) {
            suitableConstructor = constructor;

        } else if (isConstructorAnnotatedWithInject(constructor)) {
            suitableConstructor = constructor;
        }
        return suitableConstructor != null;
    }

    private static boolean isConstructorAnnotatedWithInject(Constructor<?> constructor) {
        ArrayList<Annotation> annotations = Lists.newArrayList(constructor.getAnnotations());
        boolean hasAnnotations = annotations.size() > 0;
        boolean injectExists = Iterables.any(annotations, new Predicate<Annotation>() {
            @Override
            public boolean apply(@Nullable Annotation input) {
                return input.annotationType().equals(Inject.class);
            }
        });

        return hasAnnotations && injectExists;
    }

    private static List<Constructor<?>> getConstructorsAnnotatedWithInject(Constructor<?>[] constructors) {
        return Lists.newArrayList(Iterables.filter(Lists.newArrayList(constructors),
                new Predicate<Constructor<?>>() {
                    @Override
                    public boolean apply(@Nullable Constructor<?> input) {
                        return isConstructorAnnotatedWithInject(input);
                    }
                }));
    }

    private Object injectConstructor(Constructor<?> constructor)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, BeanCreateException, BeanNotRegisteredException {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> parameters = beanObjectCreator.getInstances(parameterTypes);

        constructor.setAccessible(true);
        Object instance;
        if (parameters.size() > 0) {
            instance = constructor.newInstance(parameters.toArray());
        } else {
            instance = constructor.newInstance();
        }
        return instance;
    }

    private Constructor getSuitableConstructor(Class<?> clazz) throws Exception {
        Constructor suitableConstructor;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (constructors.length == 1) {
            Constructor<?> onlyConstructor = constructors[0];
            if (isOnlyConstructorSuitable(onlyConstructor)) {
                suitableConstructor = onlyConstructor;
            } else {
                throw new LackOfAnnotationException("Should have Inject annotation on Constructor with parameters");
            }
        } else {
            suitableConstructor = getSuitableConstructorFromMultipleConstructors(constructors);
        }
        return suitableConstructor;
    }

    private Constructor getSuitableConstructorFromMultipleConstructors(Constructor<?>[] constructors)
            throws Exception {
        Constructor<?> suitableConstructor;
        List<Constructor<?>> constructorsAnnotatedWithInject = getConstructorsAnnotatedWithInject(constructors);
        ConstructorAnnotationStatus status = inspectStatus(constructorsAnnotatedWithInject);

        if (status == ConstructorAnnotationStatus.OneWithInject) {
            suitableConstructor = constructorsAnnotatedWithInject.get(0);
        } else {
            throw status.getException();
        }
        return suitableConstructor;
    }

    private ConstructorAnnotationStatus inspectStatus(List<Constructor<?>> constructorsAnnotatedWithInject) {
        int annotationCount = constructorsAnnotatedWithInject.size();

        if (annotationCount == 1) {
            return ConstructorAnnotationStatus.OneWithInject;
        } else if (annotationCount == 0) {
            return ConstructorAnnotationStatus.NoOneWithInject;
        } else {
            return ConstructorAnnotationStatus.MoreThanOneWithInject;
        }
    }
}
