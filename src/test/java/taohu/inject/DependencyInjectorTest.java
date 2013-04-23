package taohu.inject;

import org.junit.Test;
import taohu.inject.ctor.NoParaCtor;
import taohu.inject.exception.BeanNotRegisteredToCreateException;
import taohu.inject.impl.BeanConfigurationResolverImp;
import taohu.inject.interfaces.BeanConfigurationResolver;
import taohu.model.BeanDescriptor;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DependencyInjectorTest {

    @Test(expected = BeanNotRegisteredToCreateException.class)
    public void shouldNotCreateInstanceIfClassIsNotRegistered() throws Exception {
        BeanConfigurationResolver resolver = new BeanConfigurationResolverImp(new ArrayList<BeanDescriptor>());

        DependencyInjector dependencyInjector = new DependencyInjector(resolver);
        dependencyInjector.createBeanObject(String.class);
    }

    @Test
    public void shouldCreateInstanceIfClassIsNotRegistered() throws Exception {
        BeanDescriptor descriptor = new BeanDescriptor("noPara", NoParaCtor.class);
        List<BeanDescriptor> beanDescriptors = new ArrayList<BeanDescriptor>();
        beanDescriptors.add(descriptor);
        BeanConfigurationResolver resolver = new BeanConfigurationResolverImp(beanDescriptors);

        DependencyInjector dependencyInjector = new DependencyInjector(resolver);
        Object beanObject = dependencyInjector.createBeanObject(NoParaCtor.class);

        assertThat(beanObject.getClass().equals(NoParaCtor.class), is(true));
    }

}
