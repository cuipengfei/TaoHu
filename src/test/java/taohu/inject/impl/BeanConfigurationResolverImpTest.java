package taohu.inject.impl;

import org.junit.Before;
import org.junit.Test;
import taohu.inject.ctor.AnnotatedNoParaCtor;
import taohu.inject.ctor.OneParaCtor;
import taohu.inject.interfaces.BeanConfigurationResolver;
import taohu.model.BeanDescriptor;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BeanConfigurationResolverImpTest {

    private List<BeanDescriptor> beanDescriptors;

    @Before
    public void setUp() throws Exception {
        BeanDescriptor onePara = new BeanDescriptor("onePara", OneParaCtor.class);

        this.beanDescriptors = new ArrayList<>();
        beanDescriptors.add(onePara);
    }

    @Test
    public void shouldKnowContainsBeanOrNot() throws Exception {
        BeanConfigurationResolver resolver = new BeanConfigurationResolverImp(this.beanDescriptors);
        boolean shouldContain = resolver.containsBean(OneParaCtor.class);
        boolean shouldNotContain = resolver.containsBean(AnnotatedNoParaCtor.class);

        assertThat(shouldContain, is(true));
        assertThat(shouldNotContain, is(false));
    }

    @Test
    public void shouldReturnBeanClass(){
        BeanConfigurationResolver resolver = new BeanConfigurationResolverImp(this.beanDescriptors);
        Class clazz = resolver.getBeanClass("onePara");

        assertThat(clazz.equals(OneParaCtor.class), is(true));
    }
}