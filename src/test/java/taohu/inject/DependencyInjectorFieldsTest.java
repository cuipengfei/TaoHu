package taohu.inject;

import org.junit.Before;
import org.junit.Test;
import taohu.inject.ctor.NoParaCtor;
import taohu.inject.field.FinalField;
import taohu.inject.field.PublicField;
import taohu.inject.field.PvtField;
import taohu.inject.field.StaticField;
import taohu.inject.interfaces.BeanConfigurationResolver;
import taohu.inject.interfaces.BeanObjectCreator;
import taohu.inject.interfaces.BeanObjectStock;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DependencyInjectorFieldsTest {

    private static BeanObjectCreator beanObjectCreator;

    private static BeanObjectStock beanObjectStock;

    @Before
    public void setUp() {

        BeanConfigurationResolver beanConfigurationResolver = mock(BeanConfigurationResolver.class);
        when(beanConfigurationResolver.containsBean(any(Class.class))).thenReturn(true);

        beanObjectStock = mock(BeanObjectStock.class);

        beanObjectCreator = new BeanObjectCreatorImpl(beanConfigurationResolver, beanObjectStock);
    }

    @Test
    public void shouldSetPublicField() throws Exception {
        NoParaCtor noParaCtor = new NoParaCtor();
        when(beanObjectStock.getBeanObject(NoParaCtor.class)).thenReturn(noParaCtor);

        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.field.PublicField"));

        assertThat(((PublicField) instance).noParaCtor, is(noParaCtor));
    }

    @Test
    public void shouldSetPrivateField() throws Exception {
        NoParaCtor noParaCtor = new NoParaCtor();
        when(beanObjectStock.getBeanObject(NoParaCtor.class)).thenReturn(noParaCtor);

        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.field.PvtField"));

        assertThat(((PvtField) instance).getNoParaCtor(), is(noParaCtor));
    }

    @Test
    public void shouldNotSetFinalField() throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.field.FinalField"));

        assertThat(((FinalField) instance).noParaCtor, nullValue());
    }

    @Test
    public void shouldSetStaticField() throws Exception {
        NoParaCtor noParaCtor = new NoParaCtor();
        when(beanObjectStock.getBeanObject(NoParaCtor.class)).thenReturn(noParaCtor);

        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.field.StaticField"));

        assertThat(((StaticField) instance).noParaCtor, is(noParaCtor));
    }
}
