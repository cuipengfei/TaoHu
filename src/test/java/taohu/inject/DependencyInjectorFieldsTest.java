package taohu.inject;

import org.junit.Before;
import org.junit.Test;
import taohu.inject.ctor.NoParaCtor;
import taohu.inject.field.FinalField;
import taohu.inject.field.PublicField;
import taohu.inject.field.PvtField;
import taohu.inject.field.StaticField;
import taohu.inject.interfaces.BeanConfigurationResolver;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class DependencyInjectorFieldsTest {

    private DependencyInjector dependencyInjector;

    @Before
    public void setUp(){
        this.dependencyInjector = new DependencyInjector(new BeanConfigurationResolver() {
            @Override
            public Class<?> getBeanClass(String beanId) {
                return null;
            }

            @Override
            public boolean containsBean(Class<?> clazz) {
                return true;
            }
        });
    }

    @Test
    public void shouldSetPublicField() throws Exception {
        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.field.PublicField"));

        assertThat(((PublicField) instance).noParaCtor, isA(NoParaCtor.class));
    }

    @Test
    public void shouldSetPrivateField() throws Exception {
        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.field.PvtField"));

        assertThat(((PvtField) instance).getNoParaCtor(), isA(NoParaCtor.class));
    }

    @Test
    public void shouldNotSetFinalField() throws Exception {
        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.field.FinalField"));

        assertThat(((FinalField) instance).noParaCtor, nullValue());
    }

    @Test
    public void shouldSetStaticField() throws Exception {
        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.field.StaticField"));

        assertThat(((StaticField) instance).noParaCtor, isA(NoParaCtor.class));
    }
}
