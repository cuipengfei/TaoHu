package taohu.inject;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import taohu.inject.exception.BeanNotRegisteredToCreateException;
import taohu.inject.impl.BeanConfigurationResolver;
import taohu.inject.injectors.ConstructorInjector;
import taohu.inject.injectors.FieldInjector;
import taohu.inject.injectors.SetterInjector;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanObjectCreator {

    private Map<Class, Object> objectCache;
    private BeanConfigurationResolver beanConfigurationResolver;

    private ConstructorInjector constructorInjector;
    private FieldInjector fieldInjector;
    private SetterInjector setterInjector;

    public BeanObjectCreator(BeanConfigurationResolver beanConfigurationResolver) {
        this.beanConfigurationResolver = beanConfigurationResolver;
        objectCache = new HashMap<>();

        constructorInjector = new ConstructorInjector();
        fieldInjector = new FieldInjector();
        setterInjector = new SetterInjector();
    }

    public Object createBeanObject(String beanId) throws Exception {
        Class beanClass = beanConfigurationResolver.getBeanClass(beanId);

        if (beanClass == null) {
            throw new BeanNotRegisteredToCreateException("This bean is not defined in xml: " + beanId);
        }

        return this.createBeanObject(beanClass);
    }

    public Object createBeanObject(Class<?> clazz) throws Exception {

        if (!beanConfigurationResolver.containsBean(clazz)) {
            throw new BeanNotRegisteredToCreateException("This class is not registered in xml: " + clazz);
        }

        if (isSingleton(clazz)) {
            return getFromCacheOrCreate(clazz);
        }

        return doCreateObject(clazz);
    }

    private Object doCreateObject(Class<?> clazz) throws Exception {
        Object instance;
        instance = constructorInjector.injectConstructor(clazz, this);
        fieldInjector.injectFields(instance, clazz, this);
        setterInjector.injectSetters(instance, clazz, this);
        return instance;
    }

    private Object getFromCacheOrCreate(Class<?> clazz) throws Exception {
        Object obj = objectCache.get(clazz);
        if (obj == null) {
            obj = this.doCreateObject(clazz);
            this.objectCache.put(clazz, obj);
        }
        return obj;
    }

    private boolean isSingleton(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(Singleton.class)) {
                return true;
            }
        }
        return false;
    }

    public List<Object> getInstances(Class<?>[] instanceTypes) {
        return Lists.transform(Lists.newArrayList(instanceTypes), new Function<Class<?>, Object>() {
            @Override
            public Object apply(@Nullable Class<?> instanceType) {
                try {
                    return createBeanObject(instanceType);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
