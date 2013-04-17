package taohu.inject;

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

public class ConstructorInspector {

    public static boolean isOnlyConstructorSuitable(Constructor<?> constructor)
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

    public static boolean isConstructorAnnotatedWithInject(Constructor<?> constructor) {
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

    public static List<Constructor<?>> getConstructorsAnnotatedWithInject(Constructor<?>[] constructors) {
        return Lists.newArrayList(Iterables.filter(Lists.newArrayList(constructors),
                new Predicate<Constructor<?>>() {
                    @Override
                    public boolean apply(@Nullable Constructor<?> input) {
                        return isConstructorAnnotatedWithInject(input);
                    }
                }));
    }
}
