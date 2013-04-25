package taohu.inject.injectors;

import taohu.inject.BeanObjectCreator;
import taohu.inject.ConstructorAnnotationStatus;
import taohu.inject.ConstructorInspector;
import taohu.inject.exception.LackOfAnnotationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

    private Object injectConstructor(Constructor<?> constructor)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
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
}
