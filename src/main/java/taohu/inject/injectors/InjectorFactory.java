package taohu.inject.injectors;

import taohu.inject.BeanObjectCreator;

public final class InjectorFactory {

    private InjectorFactory() {
    }

    public static Injector getSetterInjector(BeanObjectCreator beanObjectCreator) {
        return new SetterInjector(beanObjectCreator);
    }

    public static Injector getFieldInjector(BeanObjectCreator beanObjectCreator) {
        return new FieldInjector(beanObjectCreator);
    }

    public static Injector getConstructorInjector(BeanObjectCreator beanObjectCreator) {
        return new ConstructorInjector(beanObjectCreator);
    }
}
