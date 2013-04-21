package taohu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanDescriptor {

    private String id;

    private Class clazz;

    private List<ConstructorArgumentDescriptor> constructorDependency = new ArrayList<>();

    private Map<String, PropertyDescriptor> propertyDependency = new HashMap<>();

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

    public List<ConstructorArgumentDescriptor> getConstructorDependency() {
        return constructorDependency;
    }

    public Map<String, PropertyDescriptor> getPropertyDependency() {
        return propertyDependency;
    }

    public void setPropertyDependency(Map<String, PropertyDescriptor> propertyDependency) {
        this.propertyDependency = propertyDependency;
    }
}
