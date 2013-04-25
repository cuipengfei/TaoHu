package taohu.inject;

import org.junit.Before;
import org.junit.Test;
import taohu.inject.ctor.AnnotatedNoParaCtor;
import taohu.inject.ctor.OneParaCtor;
import taohu.inject.exception.BeanNotRegisteredException;
import taohu.resolver.BeanConfigurationResolver;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
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
        Object firstObject = creator.createBeanObject(AnnotatedNoParaCtor.class);
        Object secondObject = creator.createBeanObject(AnnotatedNoParaCtor.class);

        assertThat(firstObject, is(secondObject));
    }

    @Test
    public void shouldReturnSameInstanceOfSingletonClassForParameter() throws Exception {
        Object noPara = creator.createBeanObject(AnnotatedNoParaCtor.class);
        OneParaCtor onePara = (OneParaCtor) creator.createBeanObject(OneParaCtor.class);

        assertThat(onePara, notNullValue());
        assertThat(onePara.getAnnotatedNoParaCtor(), is(noPara));
    }

    @Test(expected = BeanNotRegisteredException.class)
    public void shouldNotCreateInstanceIfClassIsNotRegistered() throws Exception {
        given(beanConfigurationResolver.containsBean(any(Class.class))).willReturn(false);

        creator.createBeanObject(AnnotatedNoParaCtor.class);
    }
}
