package taohu;

import taohu.inject.BeanObjectCreator;
import taohu.inject.exception.BeanCreateException;
import taohu.inject.exception.BeanNotRegisteredException;
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

        this.beanObjectCreator = new BeanObjectCreator(beanConfigurationResolver);
    }

    public Object getBeanByID(String beanId) throws Exception {

        Class clazz = beanConfigurationResolver.getBeanClass(beanId);
        if (clazz == null) {
            clazz = parentContainer.getBeanConfigurationResolver().getBeanClass(beanId);
        }

        if (clazz == null) {
            throw new BeanNotRegisteredException("The required bean is not registered, bean id: " + beanId);
        }

        return beanObjectCreator.getBeanObject(clazz);
    }

    public BeanConfigurationResolver getBeanConfigurationResolver() {
        return beanConfigurationResolver;
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
