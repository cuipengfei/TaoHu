package taohu.inject.interfaces;

public interface BeanConfigurationResolver {

    Class<?> getBeanClass(String beanId);

    boolean containsBean(Class<?> clazz);
}
