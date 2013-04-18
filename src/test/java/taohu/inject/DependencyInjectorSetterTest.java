package taohu.inject;

import org.junit.Test;
import taohu.inject.ctor.NoParaCtor;
import taohu.inject.setter.SetterWithInject;
import taohu.inject.setter.SetterWithInjectAndItsOwnTypePara;
import taohu.inject.setter.SetterWithInjectAndTypeParaFromItClass;
import taohu.inject.setter.SetterWithoutInject;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class DependencyInjectorSetterTest {
    @Test
    public void shouldCallSetterWithInjectAndProvidePara()
            throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies("taohu.inject.setter.SetterWithInject");

        assertThat((SetterWithInject) instance, isA(SetterWithInject.class));
        assertThat(((SetterWithInject) instance).getNoParaCtor(), isA(NoParaCtor.class));
    }

    @Test
    public void shouldCallMultiParameteredSetterWithInjectAndProvideParameters()
            throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies("taohu.inject.setter.SetterWithInject");

        assertThat((SetterWithInject) instance, isA(SetterWithInject.class));
        assertThat(((SetterWithInject) instance).getNoParaCtor2(), isA(NoParaCtor.class));
        assertThat(((SetterWithInject) instance).getNoParaCtor3(), isA(NoParaCtor.class));
    }

    @Test
    public void shouldNotCallSetterWithoutInject()
            throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies("taohu.inject.setter.SetterWithoutInject");

        assertThat((SetterWithoutInject) instance, isA(SetterWithoutInject.class));
        assertThat(((SetterWithoutInject) instance).getNoParaCtor(), nullValue());
    }

    @Test
    public void shouldNotCallMethodWithTypePara() throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies("taohu.inject.setter.SetterWithInjectAndItsOwnTypePara");

        assertThat(((SetterWithInjectAndItsOwnTypePara) instance).getStr(), nullValue());
    }

    @Test
    public void shouldCallMethodWithTypeParaOfItsClass() throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies("taohu.inject.setter.SetterWithInjectAndTypeParaFromItClass");

        assertThat(((SetterWithInjectAndTypeParaFromItClass) instance).getStr(), is("method called"));
    }
}
