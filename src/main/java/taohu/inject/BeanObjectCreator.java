package taohu.inject;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import taohu.inject.exception.BeanNotRegisteredException;
import taohu.inject.injectors.Injector;
import taohu.inject.injectors.InjectorFactory;
import taohu.resolver.BeanConfigurationResolver;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanObjectCreator {

    private Map<Class, Object> objectCache;
    private BeanConfigurationResolver beanConfigurationResolver;

    private Injector constructorInjector;
    private Injector fieldInjector;
    private Injector setterInjector;

    public BeanObjectCreator(BeanConfigurationResolver beanConfigurationResolver) {
        this.beanConfigurationResolver = beanConfigurationResolver;
        objectCache = new HashMap<>();

        constructorInjector = InjectorFactory.getConstructorInjector(this);
        fieldInjector = InjectorFactory.getFieldInjector(this);
        setterInjector = InjectorFactory.getSetterInjector(this);
    }

    public Object getBeanObject(String id) throws Exception {
        Class<?> beanClass = beanConfigurationResolver.getBeanClass(id);
        return getBeanObject(beanClass);
    }

    public Object getBeanObject(Class<?> clazz) throws Exception {
        throwIfNotRegistered(clazz);

        if (isSingleton(clazz)) {
            return getFromCacheOrCreate(clazz);
        }

        return doCreateObject(clazz);
    }

    private void throwIfNotRegistered(Class<?> clazz) throws BeanNotRegisteredException {
        if (!beanConfigurationResolver.containsBean(clazz)) {
            throw new BeanNotRegisteredException("This class is not registered in xml: " + clazz);
        }
    }

    private Object doCreateObject(Class<?> clazz) throws Exception {
        Object instance = null;
        instance = constructorInjector.inject(instance, clazz);
        fieldInjector.inject(instance, clazz);
        setterInjector.inject(instance, clazz);
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
        return Iterables.any(Lists.newArrayList(annotations), new Predicate<Annotation>() {
            @Override
            public boolean apply(@Nullable Annotation input) {
                return input.annotationType().equals(Singleton.class);
            }
        });
    }

    public List<Object> getInstances(Class<?>[] instanceTypes) {
        return Lists.transform(Lists.newArrayList(instanceTypes), new Function<Class<?>, Object>() {
            @Override
            public Object apply(@Nullable Class<?> instanceType) {
                try {
                    return getBeanObject(instanceType);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
