package taohu.inject;

import org.junit.Before;
import org.junit.Test;
import taohu.inject.ctor.NoParaCtor;
import taohu.inject.field.FinalField;
import taohu.inject.field.PublicField;
import taohu.inject.field.PvtField;
import taohu.inject.field.StaticField;
import taohu.inject.impl.BeanConfigurationResolver;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DependencyInjectorFieldsTest {

    private static BeanObjectCreator beanObjectCreator;

    @Before
    public void setUp() {

        BeanConfigurationResolver beanConfigurationResolver = mock(BeanConfigurationResolver.class);
        when(beanConfigurationResolver.containsBean(any(Class.class))).thenReturn(true);

        beanObjectCreator = new BeanObjectCreator(beanConfigurationResolver);
    }

    @Test
    public void shouldSetPublicField() throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.field.PublicField"));

        assertThat(((PublicField) instance).noParaCtor, isA(NoParaCtor.class));
    }

    @Test
    public void shouldSetPrivateField() throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.field.PvtField"));

        assertThat(((PvtField) instance).getNoParaCtor(), isA(NoParaCtor.class));
    }

    @Test
    public void shouldNotSetFinalField() throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.field.FinalField"));

        assertThat(((FinalField) instance).noParaCtor, nullValue());
    }

    @Test
    public void shouldSetStaticField() throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.field.StaticField"));

        assertThat(((StaticField) instance).noParaCtor, isA(NoParaCtor.class));
    }
}
