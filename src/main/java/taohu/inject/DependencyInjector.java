package taohu.inject;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import taohu.inject.exception.LackOfAnnotationException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class DependencyInjector {
    public Object createInstanceAndInjectDependencies(String className)
            throws Exception {
        Class<?> clazz = Class.forName(className);
        Constructor<?> constructor = getSuitableConstructor(clazz);

        Object instance = injectConstructor(constructor);
        injectSetters(instance, clazz);

        return instance;
    }

    private Object injectConstructor(Constructor<?> constructor) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> parameters = getParameters(parameterTypes);

        Object instance;
        if (parameters.size() > 0) {
            instance = constructor.newInstance(parameters.toArray());
        } else {
            instance = constructor.newInstance();
        }
        return instance;
    }

    private void injectSetters(Object instance, Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = clazz.getMethods();
        Iterable<Method> methodsWithInjectAndNoTypeParaOfItsOwn = Iterables.filter(Lists.newArrayList(methods), new Predicate<Method>() {
            @Override
            public boolean apply(@Nullable Method input) {
                return input.getAnnotation(Inject.class) != null
                        && input.getTypeParameters().length == 0;
            }
        });

        callSetters(instance, methodsWithInjectAndNoTypeParaOfItsOwn);
    }

    private void callSetters(Object instance, Iterable<Method> methodsWithInjectAndNoTypeParaOfItsOwn)
            throws IllegalAccessException, InvocationTargetException {
        for (Method method : methodsWithInjectAndNoTypeParaOfItsOwn) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 0) {
                method.invoke(instance);
            } else {
                List<Object> parameters = getParameters(parameterTypes);
                method.invoke(instance, parameters.toArray());
            }
        }
    }

    private Constructor<?> getSuitableConstructor(Class<?> clazz)
            throws Exception {
        Constructor suitableConstructor;
        Constructor<?>[] constructors = clazz.getConstructors();

        if (constructors.length == 1) {
            Constructor<?> onlyConstructor = constructors[0];
            if (ConstructorInspector.isOnlyConstructorSuitable(onlyConstructor)) {
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
        List<Constructor<?>> constructorsAnnotatedWithInject = ConstructorInspector.getConstructorsAnnotatedWithInject(constructors);
        ConstructorAnnotationStatus status = inspectStatus(constructorsAnnotatedWithInject);

        if (status == ConstructorAnnotationStatus.OneAnnotatedAsInject) {
            suitableConstructor = constructorsAnnotatedWithInject.get(0);
        } else {
            throw status.getException();
        }
        return suitableConstructor;
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
