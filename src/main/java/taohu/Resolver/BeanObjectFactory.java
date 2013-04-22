package taohu.Resolver;

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
            beanDescriptors.put(bd.getId(), bd);
        }
    }

    public Object getBean(String beanId) throws BeanInitializationException {
        BeanDescriptor beanDescriptor = beanDescriptors.get(beanId);

        Object object = null;
        try {
            Constructor constructor = null;
            constructor = beanDescriptor.getClazz().getConstructor();
            object = constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new BeanInitializationException("Could not find such method to create bean", e);
        } catch (InstantiationException e) {
            throw new BeanInitializationException("Could not create bean", e);
        } catch (IllegalAccessException e) {
            throw new BeanInitializationException("Could access such method to create bean", e);
        } catch (InvocationTargetException e) {
            throw new BeanInitializationException("Could not create bean", e);
        }
        return object;
    }
}