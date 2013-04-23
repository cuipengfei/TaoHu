package taohu.inject;

import org.junit.Test;
import taohu.inject.ctor.*;
import taohu.inject.exception.IllegalAnnotationQuantityException;
import taohu.inject.exception.LackOfAnnotationException;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class DependencyInjectorCtorTest {

    @Test
    public void shouldCreateInstanceOfAClassWithAParameterlessConstructorWithInjectAnnotation
            () throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies(Class.forName("taohu.inject.ctor.AnnotatedNoParaCtor"));

        assertThat((AnnotatedNoParaCtor) instance, isA(AnnotatedNoParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithOneParaConstructor
            () throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies(Class.forName("taohu.inject.ctor.OneParaCtor"));

        assertThat((OneParaCtor) instance, isA(OneParaCtor.class));
        assertThat(((OneParaCtor) instance).getAnnotatedNoParaCtor(), isA(AnnotatedNoParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithTwoParaConstructor
            () throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies(Class.forName("taohu.inject.ctor.TwoParaCtor"));

        assertThat((TwoParaCtor) instance, isA(TwoParaCtor.class));
        assertThat(((TwoParaCtor) instance).getAnnotatedNoParaCtor(), isA(AnnotatedNoParaCtor.class));
        assertThat(((TwoParaCtor) instance).getOneParaCtor(), isA(OneParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithDefaultConstructorWithoutInjectAnnotation() throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies(Class.forName("taohu.inject.ctor.NoParaCtor"));

        assertThat((NoParaCtor) instance, isA(NoParaCtor.class));
    }

    @Test(expected = LackOfAnnotationException.class)
    public void shouldThrowExceptionWhenOnlyCtorWithParameterIsNotAnnotated() throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        dependencyInjector.createInstanceAndInjectDependencies(Class.forName("taohu.inject.ctor.OneParaCtorWithoutAnnotation"));
    }

    @Test(expected = LackOfAnnotationException.class)
    public void shouldThrowExceptionWhenBothCtorsAreNotAnnotated() throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        dependencyInjector.createInstanceAndInjectDependencies(Class.forName("taohu.inject.ctor.TwoCtorsWithoutAnnotation"));
    }

    @Test
    public void shouldCallTheAnnotatedCtorWhenThereAreTwoCtors() throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies(Class.forName("taohu.inject.ctor.TwoCtorsOneParaLessWithOneAnnotation"));
        assertThat((TwoCtorsOneParaLessWithOneAnnotation) instance, isA(TwoCtorsOneParaLessWithOneAnnotation.class));
        assertThat(((TwoCtorsOneParaLessWithOneAnnotation) instance).getAnnotatedNoParaCtor(), isA(AnnotatedNoParaCtor.class));
    }

    @Test
    public void shouldCreateInstanceOfAClassWithTwoConstructorsOneAnnotation() throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies(Class.forName("taohu.inject.ctor.TwoCtorsWithOneAnnotation"));
        assertThat((TwoCtorsWithOneAnnotation) instance, isA(TwoCtorsWithOneAnnotation.class));
        assertThat(((TwoCtorsWithOneAnnotation) instance).getAnnotatedNoParaCtor(), nullValue());
        assertThat(((TwoCtorsWithOneAnnotation) instance).getOneParaCtor(), isA(OneParaCtor.class));
    }

    @Test(expected = IllegalAnnotationQuantityException.class)
    public void shouldThrowExceptionWhenTwoCtorsAreAnnotated() throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        dependencyInjector.createInstanceAndInjectDependencies(Class.forName("taohu.inject.ctor.TwoCtorsWithTwoAnnotations"));
    }

    @Test
    public void shouldCallPrivateCtor() throws Exception {
        DependencyInjector dependencyInjector = new DependencyInjector();
        Object instance = dependencyInjector.createInstanceAndInjectDependencies(Class.forName("taohu.inject.ctor.PvtCtor"));
        assertThat((PvtCtor) instance, isA(PvtCtor.class));
    }
}
