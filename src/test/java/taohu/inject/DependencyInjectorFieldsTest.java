package taohu.inject;

import org.junit.Test;
import taohu.inject.ctor.NoParaCtor;
import taohu.inject.field.FinalField;
import taohu.inject.field.PublicField;
import taohu.inject.field.PvtField;
import taohu.inject.field.StaticField;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class DependencyInjectorFieldsTest {
    @Test
    public void shouldSetPublicField() throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies("taohu.inject.field.PublicField");

        assertThat(((PublicField) instance).noParaCtor, isA(NoParaCtor.class));
    }

    @Test
    public void shouldSetPrivateField() throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies("taohu.inject.field.PvtField");

        assertThat(((PvtField) instance).getNoParaCtor(), isA(NoParaCtor.class));
    }

    @Test
    public void shouldNotSetFinalField() throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies("taohu.inject.field.FinalField");

        assertThat(((FinalField) instance).noParaCtor, nullValue());
    }

    @Test
    public void shouldSetStaticField() throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies("taohu.inject.field.StaticField");

        assertThat(((StaticField) instance).noParaCtor, isA(NoParaCtor.class));
    }
}
