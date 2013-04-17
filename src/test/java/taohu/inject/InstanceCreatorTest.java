package taohu.inject;

import org.junit.Test;
import taohu.inject.exception.IllegalAnnotationQuantityException;
import taohu.inject.exception.LackOfAnnotationException;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class InstanceCreatorTest {

    @Test
    public void shouldCreateInstanceOfAClassWithAParameterlessConstructorWithInjectAnnotation
            () throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        Object instance = instanceCreator.getInstanceOf("taohu.inject.AnnotatedNoParaCtor");

        assertThat((AnnotatedNoParaCtor) instance, isA(AnnotatedNoParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithOneParaConstructor
            () throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        Object instance = instanceCreator.getInstanceOf("taohu.inject.OneParaCtor");

        assertThat((OneParaCtor) instance, isA(OneParaCtor.class));
        assertThat(((OneParaCtor) instance).getAnnotatedNoParaCtor(), isA(AnnotatedNoParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithTwoParaConstructor
            () throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        Object instance = instanceCreator.getInstanceOf("taohu.inject.TwoParaCtor");

        assertThat((TwoParaCtor) instance, isA(TwoParaCtor.class));
        assertThat(((TwoParaCtor) instance).getAnnotatedNoParaCtor(), isA(AnnotatedNoParaCtor.class));
        assertThat(((TwoParaCtor) instance).getOneParaCtor(), isA(OneParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithDefaultConstructorWithoutInjectAnnotation() throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        Object instance = instanceCreator.getInstanceOf("taohu.inject.NoParaCtor");

        assertThat((NoParaCtor) instance, isA(NoParaCtor.class));
    }

    @Test(expected = LackOfAnnotationException.class)
    public void shouldThrowExceptionWhenOnlyCtorWithParameterIsNotAnnotated() throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        instanceCreator.getInstanceOf("taohu.inject.OneParaCtorWithoutAnnotation");
    }

    @Test(expected = LackOfAnnotationException.class)
    public void shouldThrowExceptionWhenBothCtorsAreNotAnnotated() throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        instanceCreator.getInstanceOf("taohu.inject.TwoCtorsWithoutAnnotation");
    }

    @Test
    public void shouldCallTheAnnotatedCtorWhenThereAreTwoCtors() throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        Object instance = instanceCreator.getInstanceOf("taohu.inject.TwoCtorsOneParaLessWithOneAnnotation");
        assertThat((TwoCtorsOneParaLessWithOneAnnotation) instance, isA(TwoCtorsOneParaLessWithOneAnnotation.class));
        assertThat(((TwoCtorsOneParaLessWithOneAnnotation) instance).getAnnotatedNoParaCtor(), isA(AnnotatedNoParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithTwoConstructorsOneAnnotation() throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        Object instance = instanceCreator.getInstanceOf("taohu.inject.TwoCtorsWithOneAnnotation");
        assertThat((TwoCtorsWithOneAnnotation) instance, isA(TwoCtorsWithOneAnnotation.class));
        assertThat(((TwoCtorsWithOneAnnotation) instance).getAnnotatedNoParaCtor(), nullValue());
        assertThat(((TwoCtorsWithOneAnnotation) instance).getOneParaCtor(), isA(OneParaCtor.class));
    }

    @Test(expected = IllegalAnnotationQuantityException.class)
    public void shouldThrowExceptionWhenTwoCtorsAreAnnotated() throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        instanceCreator.getInstanceOf("taohu.inject.TwoCtorsWithTwoAnnotations");
    }
}
