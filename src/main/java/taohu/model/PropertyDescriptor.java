package taohu.model;

public class PropertyDescriptor {

    private String name;

    private BeanDescriptor reference;

    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BeanDescriptor getReference() {
        return reference;
    }

    public void setReference(BeanDescriptor reference) {
        this.reference = reference;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
