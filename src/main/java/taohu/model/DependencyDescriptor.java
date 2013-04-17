package taohu.model;

import java.util.HashMap;

public class DependencyDescriptor {

    private String id;

    private Class clazz;

    private DependencyType dependencyType;

    private Object value;

    private HashMap<String, DependencyDescriptor> constructorDependency = new HashMap();

    private HashMap<String, DependencyDescriptor> propertyDependency = new HashMap<>();

    public DependencyDescriptor(String id, Class clazz, DependencyType dependencyType, Object value,
                                HashMap<String, DependencyDescriptor> constructorDependency,
                                HashMap<String, DependencyDescriptor> propertyDependency) {
        this.id = id;
        this.clazz = clazz;
        this.dependencyType = dependencyType;
        this.value = value;
        this.constructorDependency = constructorDependency;
        this.propertyDependency = propertyDependency;
    }
}
