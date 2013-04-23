package taohu.Resolver;

import taohu.inject.DependencyInjector;
import taohu.inject.exception.BeanInitializationException;
import taohu.model.BeanDescriptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanObjectFactory {

    private Map<String, BeanDescriptor> beanDescriptors;

    public BeanObjectFactory(List<BeanDescriptor> beanDescriptorList) {

        this.beanDescriptors = new HashMap<>();
        for (BeanDescriptor bd : beanDescriptorList) {
            beanDescriptors.put(bd.getBeanId(), bd);
        }
    }

    public Object getBean(String beanId) throws BeanInitializationException {
        BeanDescriptor beanDescriptor = beanDescriptors.get(beanId);

        try {
            Constructor constructor = beanDescriptor.getClazz().getConstructor();
            return constructor.newInstance();

        } catch (NoSuchMethodException e) {
            throw new BeanInitializationException("Could not find such method to create bean: " + e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new BeanInitializationException("Could not create bean: " + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new BeanInitializationException("Could access such method to create bean: " + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new BeanInitializationException("Could not create bean: " + e.getMessage(), e);
        }
    }
}