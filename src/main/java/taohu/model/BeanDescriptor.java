package taohu.model;

import java.util.HashMap;

public class BeanDescriptor {

    private String id;

    private Class clazz;

    private DependencyType dependencyType;

    private Object value;

    private HashMap<String, PropertyDescriptor> constructorDependency = new HashMap();

    private HashMap<String, PropertyDescriptor> propertyDependency = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public DependencyType getDependencyType() {
        return dependencyType;
    }

    public void setDependencyType(DependencyType dependencyType) {
        this.dependencyType = dependencyType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public HashMap<String, PropertyDescriptor> getConstructorDependency() {
        return constructorDependency;
    }

    public void setConstructorDependency(HashMap<String, PropertyDescriptor> constructorDependency) {
        this.constructorDependency = constructorDependency;
    }

    public HashMap<String, PropertyDescriptor> getPropertyDependency() {
        return propertyDependency;
    }

    public void setPropertyDependency(HashMap<String, PropertyDescriptor> propertyDependency) {
        this.propertyDependency = propertyDependency;
    }
}
