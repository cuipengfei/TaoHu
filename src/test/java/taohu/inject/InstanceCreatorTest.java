package taohu.inject;

import org.junit.Test;
import taohu.inject.exception.IllegalAnnotationQuantityException;
import taohu.inject.exception.LackOfAnnotationException;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class InstanceCreatorTest {

    @Test
    public void shouldCreateInstanceOfAClassWithAParameterlessConstructor
            () throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        Object instance = instanceCreator.getInstanceOf("taohu.inject.NoParaCtor");

        assertThat((NoParaCtor) instance, isA(NoParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithOneParaConstructor
            () throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        Object instance = instanceCreator.getInstanceOf("taohu.inject.OneParaCtor");

        assertThat((OneParaCtor) instance, isA(OneParaCtor.class));
        assertThat(((OneParaCtor) instance).getNoParaCtor(), isA(NoParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithTwoParaConstructor
            () throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        Object instance = instanceCreator.getInstanceOf("taohu.inject.TwoParaCtor");

        assertThat((TwoParaCtor) instance, isA(TwoParaCtor.class));
        assertThat(((TwoParaCtor) instance).getNoParaCtor(), isA(NoParaCtor.class));
        assertThat(((TwoParaCtor) instance).getOneParaCtor(), isA(OneParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithDefaultConstructorWithoutInjectAnnotation() throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        Object instance = instanceCreator.getInstanceOf("taohu.inject.DefaultCtor");

        assertThat((DefaultCtor) instance, isA(DefaultCtor.class));
    }

    @Test(expected = LackOfAnnotationException.class)
    public void shouldRequireInjectAnnotationForContructorsWithPara() throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        instanceCreator.getInstanceOf("taohu.inject.OneParaCtorWithoutAnnotation");
    }

    @Test(expected = LackOfAnnotationException.class)
    public void shouldRequireInjectAnnotationIfThereAreMoreThanOneConstructors() throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        instanceCreator.getInstanceOf("taohu.inject.TwoCtorsWithoutAnnotation");
    }

    @Test
    public void shouldCreateInstanceOfAClassOneConstructorParaLessOneAnnotation() throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        Object instance = instanceCreator.getInstanceOf("taohu.inject.TwoCtorsOneParaLessWithOneAnnotation");
        assertThat((TwoCtorsOneParaLessWithOneAnnotation) instance, isA(TwoCtorsOneParaLessWithOneAnnotation.class));
        assertThat(((TwoCtorsOneParaLessWithOneAnnotation) instance).getNoParaCtor(), isA(NoParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithTwoConstructorsOneAnnotation() throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        Object instance = instanceCreator.getInstanceOf("taohu.inject.TwoCtorsWithOneAnnotation");
        assertThat((TwoCtorsWithOneAnnotation) instance, isA(TwoCtorsWithOneAnnotation.class));
        assertThat(((TwoCtorsWithOneAnnotation) instance).getNoParaCtor(), nullValue());
        assertThat(((TwoCtorsWithOneAnnotation) instance).getOneParaCtor(), isA(OneParaCtor.class));
    }

    @Test(expected = IllegalAnnotationQuantityException.class)
    public void shouldHaveOnlyOneAnnotationForConstructor() throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        instanceCreator.getInstanceOf("taohu.inject.TwoCtorsWithTwoAnnotations");
    }
}
