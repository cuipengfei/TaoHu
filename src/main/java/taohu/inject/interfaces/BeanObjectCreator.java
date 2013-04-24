package taohu.inject.interfaces;

public interface BeanObjectCreator {

    Object createBeanObject(String beanId) throws Exception;

    Object createBeanObject(Class<?> clazz) throws Exception;
}
