package taohu.Resolver;

import taohu.inject.exception.BeanInitializationException;
import taohu.model.BeanDescriptor;
import taohu.model.ConstructorArgumentDescriptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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

            if (beanDescriptor.getConstructorDependency() != null && beanDescriptor.getConstructorDependency().size() > 0) {
                List<Class> argumentTypes = new ArrayList<>();
                List<Object> argumentObjects = new ArrayList<>();
                for (ConstructorArgumentDescriptor argumentDescriptor : beanDescriptor.getConstructorDependency()) {
                    String argumentBeanId = argumentDescriptor.getBeanId();
                    BeanDescriptor argumentBeanDescriptor = beanDescriptors.get(argumentBeanId);
                    argumentTypes.add(argumentBeanDescriptor.getClazz());
                    Object argumentObject = this.getBean(argumentBeanId);
                    argumentObjects.add(argumentObject);
                }
                Class[] argTypes = new Class[argumentTypes.size()];
                constructor = beanDescriptor.getClazz().getConstructor(argumentTypes.toArray(argTypes));
                object = constructor.newInstance(argumentObjects.toArray());
            } else {
                constructor = beanDescriptor.getClazz().getConstructor();
                object = constructor.newInstance();
            }

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