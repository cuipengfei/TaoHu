package taohu.inject;

import org.junit.Before;
import org.junit.Test;
import taohu.inject.ctor.NoParaCtor;
import taohu.inject.interfaces.BeanConfigurationResolver;
import taohu.inject.interfaces.BeanObjectStock;
import taohu.inject.setter.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DependencyInjectorSetterTest {

    private static DependencyInjector dependencyInjector;

    private static BeanObjectStock beanObjectStock;

    @Before
    public void setUp() {

        BeanConfigurationResolver beanConfigurationResolver = mock(BeanConfigurationResolver.class);
        when(beanConfigurationResolver.containsBean(any(Class.class))).thenReturn(true);

        beanObjectStock = mock(BeanObjectStock.class);

        dependencyInjector = new DependencyInjector(beanConfigurationResolver, beanObjectStock);
    }

    @Test
    public void shouldCallSetterWithInjectAndProvidePara()
            throws Exception {
        NoParaCtor noParaCtor = new NoParaCtor();
        when(beanObjectStock.getBeanObject(NoParaCtor.class)).thenReturn(noParaCtor);

        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.setter.SetterWithInject"));

        assertThat((SetterWithInject) instance, isA(SetterWithInject.class));
        assertThat(((SetterWithInject) instance).getNoParaCtor(), is(noParaCtor));
    }

    @Test
    public void shouldCallMultiParameteredSetterWithInjectAndProvideParameters()
            throws Exception {
        NoParaCtor noParaCtor = new NoParaCtor();
        when(beanObjectStock.getBeanObject(NoParaCtor.class)).thenReturn(noParaCtor);

        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.setter.SetterWithInject"));

        assertThat((SetterWithInject) instance, isA(SetterWithInject.class));
        assertThat(((SetterWithInject) instance).getNoParaCtor2(), is(noParaCtor));
        assertThat(((SetterWithInject) instance).getNoParaCtor3(), is(noParaCtor));
    }

    @Test
    public void shouldNotCallSetterWithoutInject()
            throws Exception {
        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.setter.SetterWithoutInject"));

        assertThat((SetterWithoutInject) instance, isA(SetterWithoutInject.class));
        assertThat(((SetterWithoutInject) instance).getNoParaCtor(), nullValue());
    }

    @Test
    public void shouldNotCallMethodWithTypePara() throws Exception {
        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.setter.SetterWithInjectAndItsOwnTypePara"));

        assertThat(((SetterWithInjectAndItsOwnTypePara) instance).getStr(), nullValue());
    }

    @Test
    public void shouldCallMethodWithTypeParaOfItsClass() throws Exception {
        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.setter.SetterWithInjectAndTypeParaFromItClass"));

        assertThat(((SetterWithInjectAndTypeParaFromItClass) instance).getStr(), is("method called"));
    }

    @Test
    public void shouldCallPrivateSetter() throws Exception {
        NoParaCtor noParaCtor = new NoParaCtor();
        when(beanObjectStock.getBeanObject(NoParaCtor.class)).thenReturn(noParaCtor);

        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.setter.PvtSetter"));

        assertThat(((PvtSetter) instance).getNoParaCtor(), is(noParaCtor));
    }

    @Test
    public void shouldCallStaticSetter() throws Exception {
        NoParaCtor noParaCtor = new NoParaCtor();
        when(beanObjectStock.getBeanObject(NoParaCtor.class)).thenReturn(noParaCtor);

        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.setter.StaticSetter"));


        assertThat(((StaticSetter) instance).getNoParaCtor(), is(noParaCtor));
    }
}
