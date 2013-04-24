package taohu.model;

public class BeanDescriptor {

    private String beanId;

    private Class clazz;

    public BeanDescriptor(String beanId, Class clazz) {
        this.beanId = beanId;
        this.clazz = clazz;
    }

    public String getBeanId() {
        return beanId;
    }

    public Class getClazz() {
        return clazz;
    }
}
