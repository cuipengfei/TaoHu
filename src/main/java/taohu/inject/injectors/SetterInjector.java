package taohu.inject.injectors;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import taohu.inject.BeanObjectCreator;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class SetterInjector {
    public void injectSetters(Object instance, Class<?> clazz, BeanObjectCreator beanObjectCreator) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = clazz.getDeclaredMethods();
        Iterable<Method> methodsWithInjectAndNoTypeParaOfItsOwn = Iterables.filter(Lists.newArrayList(methods), new Predicate<Method>() {
            @Override
            public boolean apply(@Nullable Method input) {
                return input.getAnnotation(Inject.class) != null
                        && input.getTypeParameters().length == 0;
            }
        });

        for (Method method : methodsWithInjectAndNoTypeParaOfItsOwn) {
            callSetter(instance, method, beanObjectCreator);
        }
    }

    private void callSetter(Object instance, Method method, BeanObjectCreator beanObjectCreator) throws IllegalAccessException, InvocationTargetException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        method.setAccessible(true);
        if (parameterTypes.length == 0) {
            method.invoke(instance);
        } else {
            List<Object> parameters = beanObjectCreator.getInstances(parameterTypes);
            method.invoke(instance, parameters.toArray());
        }
    }
}
