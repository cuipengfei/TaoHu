package taohu.inject;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import taohu.inject.exception.LackOfAnnotationException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class DependencyInjector {
    public Object createInstanceAndInjectDependencies(String className)
            throws Exception {
        Class<?> clazz = Class.forName(className);
        Constructor<?> constructor = getSuitableConstructor(clazz);

        Object instance = injectConstructor(constructor);
        injectFields(instance, clazz);
        injectSetters(instance, clazz);

        return instance;
    }

    private Object injectConstructor(Constructor<?> constructor) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> parameters = getParameters(parameterTypes);

        constructor.setAccessible(true);
        Object instance;
        if (parameters.size() > 0) {
            instance = constructor.newInstance(parameters.toArray());
        } else {
            instance = constructor.newInstance();
        }
        return instance;
    }

    private void injectFields(Object instance, Class<?> clazz) throws IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        Iterable<Field> fieldsWithInject = Iterables.filter(Lists.newArrayList(fields), new Predicate<Field>() {
            @Override
            public boolean apply(@Nullable Field input) {
                return input.getAnnotation(Inject.class) != null;
            }
        });
        for (Field field : fieldsWithInject) {
            setField(instance, field);
        }
    }

    private void setField(Object instance, Field field) throws IllegalAccessException {
        field.setAccessible(true);

        Class<?> fieldType = field.getType();
        List<Object> fieldValue = getParameters(new Class<?>[]{fieldType});
        field.set(instance, fieldValue.get(0));
    }

    private void injectSetters(Object instance, Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = clazz.getDeclaredMethods();
        Iterable<Method> methodsWithInjectAndNoTypeParaOfItsOwn = Iterables.filter(Lists.newArrayList(methods), new Predicate<Method>() {
            @Override
            public boolean apply(@Nullable Method input) {
                return input.getAnnotation(Inject.class) != null
                        && input.getTypeParameters().length == 0;
            }
        });

        for (Method method : methodsWithInjectAndNoTypeParaOfItsOwn) {
            callSetter(instance, method);
        }
    }

    private void callSetter(Object instance, Method method) throws IllegalAccessException, InvocationTargetException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        method.setAccessible(true);
        if (parameterTypes.length == 0) {
            method.invoke(instance);
        } else {
            List<Object> parameters = getParameters(parameterTypes);
            method.invoke(instance, parameters.toArray());
        }
    }

    private Constructor<?> getSuitableConstructor(Class<?> clazz)
            throws Exception {
        Constructor suitableConstructor;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

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
