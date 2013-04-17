package taohu.inject;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import taohu.inject.exception.LackOfAnnotationException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class DependencyInjector {
    public Object createInstanceAndInjectDependencies(String className)
            throws Exception {
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

    private Constructor<?> getSuitableConstructor(Class<?> clazz)
            throws Exception {
        Constructor suitableConstructor;
        Constructor<?>[] constructors = clazz.getConstructors();

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

        if (status == ConstructorAnnotationStatus.OneAnnotatedAsInject) {
            suitableConstructor = constructorsAnnotatedWithInject.get(0);
        } else {
            throw status.getException();
        }
        return suitableConstructor;
    }

    private List<Constructor<?>> getConstructorsAnnotatedWithInject(Constructor<?>[] constructors) {
        return Lists.newArrayList(Iterables.filter(Lists.newArrayList(constructors),
                new Predicate<Constructor<?>>() {
                    @Override
                    public boolean apply(@Nullable Constructor<?> input) {
                        return isConstructorAnnotatedWithInject(input);
                    }
                }));
    }

    private ConstructorAnnotationStatus inspectStatus(List<Constructor<?>> constructorsAnnotatedWithInject) {
        int annotationCount = constructorsAnnotatedWithInject.size();

        if (annotationCount == 1) {
            return ConstructorAnnotationStatus.OneAnnotatedAsInject;
        } else if (annotationCount == 0) {
            return ConstructorAnnotationStatus.NoOneAnnotatedAsInject;
        } else {
            return ConstructorAnnotationStatus.MoreThanOneAnnotatedAsInject;
        }
    }

    private boolean isOnlyConstructorSuitable(Constructor<?> constructor) throws LackOfAnnotationException {
        Constructor suitableConstructor = null;
        boolean hasPara = constructor.getParameterTypes().length > 0;
        if (!hasPara) {
            suitableConstructor = constructor;

        } else if (isConstructorAnnotatedWithInject(constructor)) {
            suitableConstructor = constructor;
        }
        return suitableConstructor != null;
    }

    private boolean isConstructorAnnotatedWithInject(Constructor<?> constructor) {
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

    private List<Object> getParameters(Class<?>[] parameterTypes) {
        return Lists.transform(Lists.newArrayList(parameterTypes), new Function<Class<?>, Object>() {
            @Override
            public Object apply(@Nullable Class<?> paraType) {
                String paraTypeName = paraType.getName();
                try {
                    return createInstanceAndInjectDependencies(paraTypeName);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
