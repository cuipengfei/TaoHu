package taohu.inject.injectors;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import taohu.inject.BeanObjectCreator;
import taohu.inject.exception.BeanCreateException;
import taohu.inject.exception.BeanNotRegisteredException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.List;

public class SetterInjector implements Injector {

    private BeanObjectCreator beanObjectCreator;

    public SetterInjector(BeanObjectCreator beanObjectCreator) {
        this.beanObjectCreator = beanObjectCreator;
    }

    @Override
    public Object inject(Object instance, Class<?> clazz) throws BeanCreateException, BeanNotRegisteredException {
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

        return null;
    }

    private void callSetter(Object instance, Method method) throws BeanCreateException, BeanNotRegisteredException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        method.setAccessible(true);

        try {
            if (parameterTypes.length == 0) {
                method.invoke(instance);
            } else {
                List<Object> parameters = beanObjectCreator.getInstances(parameterTypes);
                method.invoke(instance, parameters.toArray());
            }
        } catch (Exception e) {
            throw new BeanCreateException("Fail to invoke method " + method.getName() + " caused by: ", e);
        }
    }
}
