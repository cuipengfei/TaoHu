package taohu.inject;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import taohu.inject.exception.LackOfAnnotationException;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
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
