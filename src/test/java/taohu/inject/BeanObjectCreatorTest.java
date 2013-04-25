package taohu.inject;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import taohu.inject.ctor.AnnotatedNoParaCtor;
import taohu.inject.ctor.NoParaCtor;
import taohu.inject.ctor.OneParaCtor;
import taohu.inject.ctor.TwoCtorsWithOneAnnotation;
import taohu.inject.exception.BeanCreateException;
import taohu.inject.exception.BeanNotRegisteredException;
import taohu.model.BeanDescriptor;
import taohu.resolver.BeanConfigurationResolver;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BeanObjectCreatorTest {

    private BeanConfigurationResolver beanConfigurationResolver;

    private BeanObjectCreator creator;

    @Before
    public void setUp() {
        this.beanConfigurationResolver = mock(BeanConfigurationResolver.class);
        when(beanConfigurationResolver.containsBean(any(Class.class))).thenReturn(true);
        this.creator = new BeanObjectCreator(beanConfigurationResolver);
    }

    @Test
    public void shouldReturnSameInstanceIfClassIsSingleton() throws Exception {
        Object firstObject = creator.getBeanObject(AnnotatedNoParaCtor.class);
        Object secondObject = creator.getBeanObject(AnnotatedNoParaCtor.class);

        assertThat(firstObject, is(secondObject));
    }

    @Test
    public void shouldReturnSameInstanceOfSingletonClassForParameter() throws Exception {
        Object noPara = creator.getBeanObject(AnnotatedNoParaCtor.class);
        OneParaCtor onePara = (OneParaCtor) creator.getBeanObject(OneParaCtor.class);

        assertThat(onePara, notNullValue());
        assertThat(onePara.getAnnotatedNoParaCtor(), is(noPara));
    }

    @Test(expected = BeanNotRegisteredException.class)
    public void shouldNotCreateInstanceIfClassIsNotRegistered() throws Exception {
        given(beanConfigurationResolver.containsBean(any(Class.class))).willReturn(false);

        creator.getBeanObject(AnnotatedNoParaCtor.class);
    }

    @Test
    public void shouldCreateBeanInParentCreator() throws Exception {
        BeanConfigurationResolver parentResolver = new BeanConfigurationResolver(Lists.newArrayList(new BeanDescriptor("noPara", AnnotatedNoParaCtor.class)));
        BeanConfigurationResolver childResolver = new BeanConfigurationResolver(Lists.newArrayList(new BeanDescriptor("onePara", OneParaCtor.class)));

        BeanObjectCreator parentCreator = new BeanObjectCreator(parentResolver);
        BeanObjectCreator childCreator = new BeanObjectCreator(childResolver, parentCreator);

        Object noPara = childCreator.getBeanObject(AnnotatedNoParaCtor.class);
        assertThat(noPara, notNullValue());
        assertThat((AnnotatedNoParaCtor) noPara, isA(AnnotatedNoParaCtor.class));
    }

    @Test
    public void shouldCreateBeanWithParaBeanDefinedInParent() throws Exception {
        BeanConfigurationResolver parentResolver = new BeanConfigurationResolver(Lists.newArrayList(new BeanDescriptor("noPara", AnnotatedNoParaCtor.class)));
        BeanConfigurationResolver childResolver = new BeanConfigurationResolver(Lists.newArrayList(new BeanDescriptor("onePara", OneParaCtor.class)));

        BeanObjectCreator parentCreator = new BeanObjectCreator(parentResolver);
        BeanObjectCreator childCreator = new BeanObjectCreator(childResolver, parentCreator);

        OneParaCtor onePara = (OneParaCtor)childCreator.getBeanObject(OneParaCtor.class);
        assertThat(onePara, notNullValue());
        assertThat(onePara, isA(OneParaCtor.class));

        assertThat(onePara.getAnnotatedNoParaCtor(), notNullValue());
        assertThat(onePara.getAnnotatedNoParaCtor(), isA(AnnotatedNoParaCtor.class));
    }

    @Test(expected = BeanNotRegisteredException.class)
    public void shouldNotCreateBeanNotDefinedInParentAndChild() throws Exception {
        BeanConfigurationResolver parentResolver = new BeanConfigurationResolver(Lists.newArrayList(new BeanDescriptor("noPara", AnnotatedNoParaCtor.class)));
        BeanConfigurationResolver childResolver = new BeanConfigurationResolver(Lists.newArrayList(new BeanDescriptor("onePara", OneParaCtor.class)));

        BeanObjectCreator parentCreator = new BeanObjectCreator(parentResolver);
        BeanObjectCreator childCreator = new BeanObjectCreator(childResolver, parentCreator);

        Object onePara = childCreator.getBeanObject(TwoCtorsWithOneAnnotation.class);
    }

    @Test(expected = BeanNotRegisteredException.class)
    public void shouldNotCreateBeanWithParaNotDefinedInParentAndChild() throws Exception {
        BeanConfigurationResolver parentResolver = new BeanConfigurationResolver(Lists.newArrayList(new BeanDescriptor("noPara", NoParaCtor.class)));
        BeanConfigurationResolver childResolver = new BeanConfigurationResolver(Lists.newArrayList(new BeanDescriptor("onePara", OneParaCtor.class)));

        BeanObjectCreator parentCreator = new BeanObjectCreator(parentResolver);
        BeanObjectCreator childCreator = new BeanObjectCreator(childResolver, parentCreator);

        Object onePara = childCreator.getBeanObject(OneParaCtor.class);
    }
}
