package taohu.inject;

import org.junit.Before;
import org.junit.Test;
import taohu.inject.ctor.*;
import taohu.inject.exception.IllegalAnnotationQuantityException;
import taohu.inject.exception.LackOfAnnotationException;
import taohu.inject.interfaces.BeanConfigurationResolver;
import taohu.inject.interfaces.BeanObjectStock;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DependencyInjectorCtorTest {


    private static DependencyInjector dependencyInjector;

    private static BeanObjectStock beanObjectStock;

    @Before
    public void setUp() {

        BeanConfigurationResolver beanConfigurationResolver = mock(BeanConfigurationResolver.class);
        beanObjectStock = mock(BeanObjectStock.class);

        when(beanConfigurationResolver.containsBean(any(Class.class))).thenReturn(true);

        dependencyInjector = new DependencyInjector(beanConfigurationResolver, beanObjectStock);
    }

    @Test
    public void shouldCreateInstanceOfAClassWithAParameterlessConstructorWithInjectAnnotation
            () throws Exception {
        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.ctor.AnnotatedNoParaCtor"));

        assertThat((AnnotatedNoParaCtor) instance, isA(AnnotatedNoParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithOneParaConstructor
            () throws Exception {

        AnnotatedNoParaCtor annotatedNoParaCtor = new AnnotatedNoParaCtor();
        when(beanObjectStock.getBeanObject(AnnotatedNoParaCtor.class)).thenReturn(annotatedNoParaCtor);

        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.ctor.OneParaCtor"));

        assertThat((OneParaCtor) instance, isA(OneParaCtor.class));
        assertThat(((OneParaCtor) instance).getAnnotatedNoParaCtor(), is(annotatedNoParaCtor));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithTwoParaConstructor
            () throws Exception {
        AnnotatedNoParaCtor annotatedNoParaCtor = new AnnotatedNoParaCtor();
        OneParaCtor oneParaCtor = new OneParaCtor(annotatedNoParaCtor);
        when(beanObjectStock.getBeanObject(OneParaCtor.class)).thenReturn(oneParaCtor);
        when(beanObjectStock.getBeanObject(AnnotatedNoParaCtor.class)).thenReturn(annotatedNoParaCtor);

        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.ctor.TwoParaCtor"));

        assertThat((TwoParaCtor) instance, isA(TwoParaCtor.class));
        assertThat(((TwoParaCtor) instance).getAnnotatedNoParaCtor(), is(annotatedNoParaCtor));
        assertThat(((TwoParaCtor) instance).getOneParaCtor(), is(oneParaCtor));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithDefaultConstructorWithoutInjectAnnotation() throws Exception {
        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.ctor.NoParaCtor"));

        assertThat((NoParaCtor) instance, isA(NoParaCtor.class));
    }

    @Test(expected = LackOfAnnotationException.class)
    public void shouldThrowExceptionWhenOnlyCtorWithParameterIsNotAnnotated() throws Exception {
        dependencyInjector.createBeanObject(Class.forName("taohu.inject.ctor.OneParaCtorWithoutAnnotation"));
    }

    @Test(expected = LackOfAnnotationException.class)
    public void shouldThrowExceptionWhenBothCtorsAreNotAnnotated() throws Exception {
        dependencyInjector.createBeanObject(Class.forName("taohu.inject.ctor.TwoCtorsWithoutAnnotation"));
    }

    @Test
    public void shouldCallTheAnnotatedCtorWhenThereAreTwoCtors() throws Exception {
        AnnotatedNoParaCtor annotatedNoParaCtor = new AnnotatedNoParaCtor();
        when(beanObjectStock.getBeanObject(AnnotatedNoParaCtor.class)).thenReturn(annotatedNoParaCtor);

        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.ctor.TwoCtorsOneParaLessWithOneAnnotation"));
        assertThat((TwoCtorsOneParaLessWithOneAnnotation) instance, isA(TwoCtorsOneParaLessWithOneAnnotation.class));
        assertThat(((TwoCtorsOneParaLessWithOneAnnotation) instance).getAnnotatedNoParaCtor(), is(annotatedNoParaCtor));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithTwoConstructorsOneAnnotation() throws Exception {
        AnnotatedNoParaCtor annotatedNoParaCtor = new AnnotatedNoParaCtor();
        OneParaCtor oneParaCtor = new OneParaCtor(annotatedNoParaCtor);
        when(beanObjectStock.getBeanObject(OneParaCtor.class)).thenReturn(oneParaCtor);

        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.ctor.TwoCtorsWithOneAnnotation"));

        assertThat((TwoCtorsWithOneAnnotation) instance, isA(TwoCtorsWithOneAnnotation.class));
        assertThat(((TwoCtorsWithOneAnnotation) instance).getAnnotatedNoParaCtor(), nullValue());
        assertThat(((TwoCtorsWithOneAnnotation) instance).getOneParaCtor(), is(oneParaCtor));
    }

    @Test(expected = IllegalAnnotationQuantityException.class)
    public void shouldThrowExceptionWhenTwoCtorsAreAnnotated() throws Exception {
        dependencyInjector.createBeanObject(Class.forName("taohu.inject.ctor.TwoCtorsWithTwoAnnotations"));
    }

    @Test
    public void shouldCallPrivateCtor() throws Exception {
        Object instance = dependencyInjector.createBeanObject(Class.forName("taohu.inject.ctor.PvtCtor"));
        assertThat((PvtCtor) instance, isA(PvtCtor.class));
    }
}
