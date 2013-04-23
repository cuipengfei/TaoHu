package taohu.inject;

import org.junit.Test;
import taohu.inject.exception.BeanNotRegisteredToCreateException;
import taohu.inject.impl.BeanConfigurationResolverImp;
import taohu.inject.interfaces.BeanConfigurationResolver;
import taohu.model.BeanDescriptor;

import java.util.ArrayList;

public class DependencyInjectorTest {

    @Test(expected = BeanNotRegisteredToCreateException.class)
    public void shouldNotCreateInstanceIfClassIsNotRegistered() throws Exception {
        BeanConfigurationResolver resolver = new BeanConfigurationResolverImp(new ArrayList<BeanDescriptor>());
        DependencyInjector dependencyInjector = new DependencyInjector(resolver);
        dependencyInjector.createBeanObject(String.class);
    }
}
