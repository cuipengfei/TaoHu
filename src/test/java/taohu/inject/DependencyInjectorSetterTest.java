package taohu.inject;

import org.junit.Test;
import taohu.inject.ctor.NoParaCtor;
import taohu.inject.setter.SetterWithInject;
import taohu.inject.setter.SetterWithoutInject;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.nullValue;
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
    public void shouldNotCallSetterWithInjectAndProvidePara()
            throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies("taohu.inject.setter.SetterWithoutInject");

        assertThat((SetterWithoutInject) instance, isA(SetterWithoutInject.class));
        assertThat(((SetterWithoutInject) instance).getNoParaCtor(), nullValue());
    }
}
