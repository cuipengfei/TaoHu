package taohu.inject.ctor;

public class OneParaCtorWithoutAnnotation {
    private AnnotatedNoParaCtor annotatedNoParaCtor;

    public OneParaCtorWithoutAnnotation(AnnotatedNoParaCtor annotatedNoParaCtor) {
        this.annotatedNoParaCtor = annotatedNoParaCtor;
    }

    public AnnotatedNoParaCtor getAnnotatedNoParaCtor() {
        return annotatedNoParaCtor;
    }
}
