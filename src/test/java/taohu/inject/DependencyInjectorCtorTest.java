package taohu.inject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import taohu.inject.ctor.*;
import taohu.inject.exception.IllegalAnnotationQuantityException;
import taohu.inject.exception.LackOfAnnotationException;
import taohu.inject.interfaces.BeanConfigurationResolver;
import taohu.inject.interfaces.BeanObjectCreator;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DependencyInjectorCtorTest {


    private static BeanObjectCreator beanObjectCreator;

    @Before
    public void setUp() {

        BeanConfigurationResolver beanConfigurationResolver = mock(BeanConfigurationResolver.class);
        when(beanConfigurationResolver.containsBean(any(Class.class))).thenReturn(true);

        beanObjectCreator = new BeanObjectCreatorImpl(beanConfigurationResolver);
    }

    @Test
    public void shouldCreateInstanceOfAClassWithAParameterlessConstructorWithInjectAnnotation
            () throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.ctor.AnnotatedNoParaCtor"));

        assertThat((AnnotatedNoParaCtor) instance, isA(AnnotatedNoParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithOneParaConstructor
            () throws Exception {

        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.ctor.OneParaCtor"));

        assertThat((OneParaCtor) instance, isA(OneParaCtor.class));
        assertThat(((OneParaCtor) instance).getAnnotatedNoParaCtor(), isA(AnnotatedNoParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithTwoParaConstructor
            () throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.ctor.TwoParaCtor"));

        assertThat((TwoParaCtor) instance, isA(TwoParaCtor.class));
        assertThat(((TwoParaCtor) instance).getAnnotatedNoParaCtor(), isA(AnnotatedNoParaCtor.class));
        assertThat(((TwoParaCtor) instance).getOneParaCtor(), isA(OneParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithDefaultConstructorWithoutInjectAnnotation() throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.ctor.NoParaCtor"));

        assertThat((NoParaCtor) instance, isA(NoParaCtor.class));
    }

    @Test(expected = LackOfAnnotationException.class)
    public void shouldThrowExceptionWhenOnlyCtorWithParameterIsNotAnnotated() throws Exception {
        beanObjectCreator.createBeanObject(Class.forName("taohu.inject.ctor.OneParaCtorWithoutAnnotation"));
    }

    @Test(expected = LackOfAnnotationException.class)
    public void shouldThrowExceptionWhenBothCtorsAreNotAnnotated() throws Exception {
        beanObjectCreator.createBeanObject(Class.forName("taohu.inject.ctor.TwoCtorsWithoutAnnotation"));
    }

    @Test
    public void shouldCallTheAnnotatedCtorWhenThereAreTwoCtors() throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.ctor.TwoCtorsOneParaLessWithOneAnnotation"));
        assertThat((TwoCtorsOneParaLessWithOneAnnotation) instance, isA(TwoCtorsOneParaLessWithOneAnnotation.class));
        assertThat(((TwoCtorsOneParaLessWithOneAnnotation) instance).getAnnotatedNoParaCtor(), isA(AnnotatedNoParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithTwoConstructorsOneAnnotation() throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.ctor.TwoCtorsWithOneAnnotation"));

        assertThat((TwoCtorsWithOneAnnotation) instance, isA(TwoCtorsWithOneAnnotation.class));
        assertThat(((TwoCtorsWithOneAnnotation) instance).getAnnotatedNoParaCtor(), nullValue());
        assertThat(((TwoCtorsWithOneAnnotation) instance).getOneParaCtor(), is(OneParaCtor.class));
    }

    @Test(expected = IllegalAnnotationQuantityException.class)
    public void shouldThrowExceptionWhenTwoCtorsAreAnnotated() throws Exception {
        beanObjectCreator.createBeanObject(Class.forName("taohu.inject.ctor.TwoCtorsWithTwoAnnotations"));
    }

    @Test
    public void shouldCallPrivateCtor() throws Exception {
        Object instance = beanObjectCreator.createBeanObject(Class.forName("taohu.inject.ctor.PvtCtor"));
        assertThat((PvtCtor) instance, isA(PvtCtor.class));
    }
}
