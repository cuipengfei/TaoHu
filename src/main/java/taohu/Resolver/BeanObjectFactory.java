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

        try {
            Constructor constructor = null;
            if (hasConstructorArguments(beanDescriptor)) {

                List<Class> argumentTypes = new ArrayList<>();
                List<Object> argumentObjects = new ArrayList<>();
                setArgumentsTypeAndInstance(beanDescriptor, argumentTypes, argumentObjects);

                Class[] argTypes = new Class[argumentTypes.size()];
                constructor = beanDescriptor.getClazz().getConstructor(argumentTypes.toArray(argTypes));

                return constructor.newInstance(argumentObjects.toArray());
            } else {
                constructor = beanDescriptor.getClazz().getConstructor();
                return constructor.newInstance();
            }

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

    private boolean hasConstructorArguments(BeanDescriptor beanDescriptor) {
        return beanDescriptor.getConstructorDependency() != null && beanDescriptor.getConstructorDependency().size() > 0;
    }

    private void setArgumentsTypeAndInstance(BeanDescriptor beanDescriptor, List<Class> argumentTypes,
                                             List<Object> argumentObjects) throws BeanInitializationException {
        for (ConstructorArgumentDescriptor argumentDescriptor : beanDescriptor.getConstructorDependency()) {
            String argumentBeanId = argumentDescriptor.getBeanId();
            BeanDescriptor argumentBeanDescriptor = beanDescriptors.get(argumentBeanId);
            Object argumentObject = this.getBean(argumentBeanId);

            argumentTypes.add(argumentBeanDescriptor.getClazz());
            argumentObjects.add(argumentObject);
        }
    }
}