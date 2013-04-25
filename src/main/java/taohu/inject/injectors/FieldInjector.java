package taohu.inject.injectors;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import taohu.inject.BeanObjectCreator;
import taohu.inject.exception.BeanCreateException;
import taohu.inject.exception.BeanNotRegisteredException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class FieldInjector implements Injector {

    private BeanObjectCreator beanObjectCreator;

    public FieldInjector(BeanObjectCreator beanObjectCreator) {
        this.beanObjectCreator = beanObjectCreator;
    }

    @Override
    public Object inject(Object instance, Class<?> clazz) throws IllegalAccessException, BeanCreateException, BeanNotRegisteredException {
        Field[] fields = clazz.getDeclaredFields();
        Iterable<Field> nonFinalFieldsWithInject = Iterables.filter(Lists.newArrayList(fields), new Predicate<Field>() {
            @Override
            public boolean apply(@Nullable Field input) {
                return input.getAnnotation(Inject.class) != null
                        && !Modifier.isFinal(input.getModifiers());
            }
        });
        for (Field field : nonFinalFieldsWithInject) {
            setField(instance, field);
        }

        return null;
    }

    private void setField(Object instance, Field field) throws IllegalAccessException, BeanCreateException, BeanNotRegisteredException {
        field.setAccessible(true);

        Class<?> fieldType = field.getType();
        List<Object> fieldValue = beanObjectCreator.getInstances(new Class<?>[]{fieldType});
        field.set(instance, fieldValue.get(0));
    }
}
