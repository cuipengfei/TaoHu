package taohu.inject;

import org.junit.Before;
import org.junit.Test;
import taohu.inject.ctor.NoParaCtor;
import taohu.inject.interfaces.BeanConfigurationResolver;
import taohu.inject.interfaces.BeanObjectCreator;
import taohu.inject.setter.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DependencyInjectorSetterTest {

    private static BeanObjectCreator beanObjectCreator;

    @Before
    public void setUp() {

        BeanConfigurationResolver beanConfigurationResolver = mock(BeanConfigurationResolver.class);
        when(beanConfigurationResolver.containsBean(any(Class.class))).thenReturn(true);

        beanObjectCreator = new BeanObjectCreatorImpl();
    }

    @Test
    public void shouldCallSetterWithInjectAndProvidePara()
            throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.setter.SetterWithInject"));

        assertThat((SetterWithInject) instance, isA(SetterWithInject.class));
        assertThat(((SetterWithInject) instance).getNoParaCtor(), isA(NoParaCtor.class));
    }

    @Test
    public void shouldCallMultiParameteredSetterWithInjectAndProvideParameters()
            throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.setter.SetterWithInject"));

        assertThat((SetterWithInject) instance, isA(SetterWithInject.class));
        assertThat(((SetterWithInject) instance).getNoParaCtor2(), isA(NoParaCtor.class));
        assertThat(((SetterWithInject) instance).getNoParaCtor3(), isA(NoParaCtor.class));
    }

    @Test
    public void shouldNotCallSetterWithoutInject()
            throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.setter.SetterWithoutInject"));

        assertThat((SetterWithoutInject) instance, isA(SetterWithoutInject.class));
        assertThat(((SetterWithoutInject) instance).getNoParaCtor(), nullValue());
    }

    @Test
    public void shouldNotCallMethodWithTypePara() throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.setter.SetterWithInjectAndItsOwnTypePara"));

        assertThat(((SetterWithInjectAndItsOwnTypePara) instance).getStr(), nullValue());
    }

    @Test
    public void shouldCallMethodWithTypeParaOfItsClass() throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.setter.SetterWithInjectAndTypeParaFromItClass"));

        assertThat(((SetterWithInjectAndTypeParaFromItClass) instance).getStr(), is("method called"));
    }

    @Test
    public void shouldCallPrivateSetter() throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.setter.PvtSetter"));

        assertThat(((PvtSetter) instance).getNoParaCtor(), isA(NoParaCtor.class));
    }

    @Test
    public void shouldCallStaticSetter() throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.setter.StaticSetter"));


        assertThat(((StaticSetter) instance).getNoParaCtor(), isA(NoParaCtor.class));
    }
}
