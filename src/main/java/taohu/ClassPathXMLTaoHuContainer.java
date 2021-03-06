package taohu;

import taohu.inject.BeanObjectCreator;
import taohu.inject.exception.BeanCreateException;
import taohu.inject.exception.BeanNotRegisteredException;
import taohu.inject.exception.LackOfAnnotationException;
import taohu.model.BeanDescriptor;
import taohu.resolver.BeanConfigurationResolver;
import taohu.resolver.XmlBeanParser;

import java.net.URI;
import java.util.List;

public class ClassPathXMLTaoHuContainer {

    private BeanConfigurationResolver beanConfigurationResolver;

    private ClassPathXMLTaoHuContainer parentContainer;

    private BeanObjectCreator beanObjectCreator;

    public ClassPathXMLTaoHuContainer(String xmlFilePath) throws BeanCreateException {
        this(xmlFilePath, null);
    }

    public ClassPathXMLTaoHuContainer(String xmlFilePath, ClassPathXMLTaoHuContainer parentContainer) throws BeanCreateException {
        this.parentContainer = parentContainer;

        createBeanResolver(xmlFilePath);

        if (parentContainer == null) {
            this.beanObjectCreator = new BeanObjectCreator(beanConfigurationResolver);
        } else {
            this.beanObjectCreator = new BeanObjectCreator(beanConfigurationResolver, parentContainer.getBeanObjectCreator());
        }

    }

    public Object getBeanByID(String beanId) throws LackOfAnnotationException, BeanCreateException, BeanNotRegisteredException {

        Class clazz = getBeanClass(beanId);

        if (clazz == null) {
            throw new BeanNotRegisteredException("The required bean is not registered, bean id: " + beanId);
        }

        return beanObjectCreator.getBeanObject(clazz);
    }

    public BeanConfigurationResolver getBeanConfigurationResolver() {
        return beanConfigurationResolver;
    }

    public BeanObjectCreator getBeanObjectCreator() {
        return beanObjectCreator;
    }

    private Class getBeanClass(String beanId) {
        Class clazz = beanConfigurationResolver.getBeanClass(beanId);
        if (clazz == null && parentContainer != null) {
            clazz = parentContainer.getBeanConfigurationResolver().getBeanClass(beanId);
        }
        return clazz;
    }

    private void createBeanResolver(String xmlFilePath) throws BeanCreateException {


        URI xmlURI = null;
        try {
            xmlURI = getClass().getClassLoader().getResource(xmlFilePath).toURI();
            XmlBeanParser xmlBeanParser = new XmlBeanParser(xmlURI);
            List<BeanDescriptor> beanDescriptors = xmlBeanParser.parseBeans();
            beanConfigurationResolver = new BeanConfigurationResolver(beanDescriptors);
        } catch (Exception e) {
            throw new BeanCreateException("Fail to parse xml file: " + xmlFilePath + ", caused by: ", e);
        }
    }
}
