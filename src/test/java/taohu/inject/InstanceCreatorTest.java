package taohu.inject;

import org.junit.Test;
import taohu.inject.exception.InitialInstanceException;

import static org.hamcrest.CoreMatchers.isA;
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

    @Test(expected=InitialInstanceException.class)
    public void shouldHaveOnlyOneContructorHavingInjectAnnotation() throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        instanceCreator.getInstanceOf("taohu.inject.TwoInjectAnnotation");
    }

    @Test
    public void DoNotHaveToHaveInjectAnnotationOnArgumentsFreeConstructor() throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        Object instance = instanceCreator.getInstanceOf("taohu.inject.NoAnnotationAndNoArgumentsOnCtor");
        assertThat((NoAnnotationAndNoArgumentsOnCtor) instance, isA(NoAnnotationAndNoArgumentsOnCtor.class));
    }
}
