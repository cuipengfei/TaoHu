package taohu.inject;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import taohu.inject.exception.BeanNotRegisteredToCreateException;
import taohu.inject.exception.LackOfAnnotationException;
import taohu.inject.interfaces.BeanConfigurationResolver;
import taohu.inject.interfaces.BeanObjectCreator;
import taohu.inject.interfaces.BeanObjectStock;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.*;
import java.util.List;

public class BeanObjectCreatorImpl implements BeanObjectCreator{

    private BeanConfigurationResolver beanConfigurationResolver;

    public BeanObjectCreatorImpl(BeanConfigurationResolver beanConfigurationResolver) {
        this.beanConfigurationResolver = beanConfigurationResolver;
    }

    @Override
    public Object createBeanObject(Class<?> clazz) throws Exception{

        if(!beanConfigurationResolver.containsBean(clazz)){
            throw new BeanNotRegisteredToCreateException("This class is not registered to create instance, please config xml first.");
        }

        Constructor<?> constructor = getSuitableConstructor(clazz);

        Object instance = injectConstructor(constructor);
        injectFields(instance, clazz);
        injectSetters(instance, clazz);

        return instance;
    }

    private Object injectConstructor(Constructor<?> constructor) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> parameters = getInstances(parameterTypes);

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
        Iterable<Field> nonFinalfieldsWithInject = Iterables.filter(Lists.newArrayList(fields), new Predicate<Field>() {
            @Override
            public boolean apply(@Nullable Field input) {
                return input.getAnnotation(Inject.class) != null
                        && !Modifier.isFinal(input.getModifiers());
            }
        });
        for (Field field : nonFinalfieldsWithInject) {
            setField(instance, field);
        }
    }

    private void setField(Object instance, Field field) throws IllegalAccessException {
        field.setAccessible(true);

        Class<?> fieldType = field.getType();
        List<Object> fieldValue = getInstances(new Class<?>[]{fieldType});
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
            List<Object> parameters = getInstances(parameterTypes);
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

    private List<Object> getInstances(Class<?>[] instanceTypes) {
        return Lists.transform(Lists.newArrayList(instanceTypes), new Function<Class<?>, Object>() {
            @Override
            public Object apply(@Nullable Class<?> instanceType) {
                try {
                    return BeanObjectCreatorImpl.this.createBeanObject(instanceType);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
