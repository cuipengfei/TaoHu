package taohu.inject;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import taohu.inject.exception.BeanCreateException;
import taohu.inject.exception.BeanNotRegisteredException;
import taohu.inject.exception.LackOfAnnotationException;
import taohu.inject.injectors.Injector;
import taohu.inject.injectors.InjectorFactory;
import taohu.resolver.BeanConfigurationResolver;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanObjectCreator {

    private Map<Class, Object> objectCache;
    private BeanConfigurationResolver beanConfigurationResolver;

    private Injector constructorInjector;
    private Injector fieldInjector;
    private Injector setterInjector;
    private BeanObjectCreator parentCreator;

    public BeanObjectCreator(BeanConfigurationResolver beanConfigurationResolver) {
        this.beanConfigurationResolver = beanConfigurationResolver;
        objectCache = new HashMap<>();

        constructorInjector = InjectorFactory.getConstructorInjector(this);
        fieldInjector = InjectorFactory.getFieldInjector(this);
        setterInjector = InjectorFactory.getSetterInjector(this);
    }

    public BeanObjectCreator(BeanConfigurationResolver beanConfigurationResolver, BeanObjectCreator parentCreator) {
        this(beanConfigurationResolver);
        this.parentCreator = parentCreator;
    }

    public Object getBeanObject(Class<?> clazz) throws Exception {

        if (!beanConfigurationResolver.containsBean(clazz)) {
            if (parentCreator != null) {
                return parentCreator.getBeanObject(clazz);
            }
            throw new BeanNotRegisteredException("This class is not registered in xml: " + clazz);
        }

        if (isSingleton(clazz)) {
            return getFromCacheOrCreate(clazz);
        }

        return doCreateObject(clazz);
    }

    private Object doCreateObject(Class<?> clazz) throws LackOfAnnotationException, BeanNotRegisteredException, BeanCreateException {
        Object instance = null;
        instance = constructorInjector.inject(instance, clazz);
        fieldInjector.inject(instance, clazz);
        setterInjector.inject(instance, clazz);
        return instance;
    }

    private Object getFromCacheOrCreate(Class<?> clazz) throws LackOfAnnotationException, BeanCreateException, BeanNotRegisteredException {
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

    public List<Object> getInstances(Class<?>[] instanceTypes) throws BeanCreateException, BeanNotRegisteredException {

        List<Object> objs = new ArrayList<>();
        for (Class<?> clazz : instanceTypes) {
            try {
                objs.add(getBeanObject(clazz));
            } catch(BeanNotRegisteredException e){
                throw e;
            } catch (Exception e) {
                throw new BeanCreateException("Fail to create bean of class " + clazz + ", caused by: ", e);
            }
        }
        return objs;
    }
}
