package taohu.inject;

public class OneParaCtorWithoutAnnotation {
    private AnnotatedNoParaCtor annotatedNoParaCtor;

    public OneParaCtorWithoutAnnotation(AnnotatedNoParaCtor annotatedNoParaCtor) {
        this.annotatedNoParaCtor = annotatedNoParaCtor;
    }

    public AnnotatedNoParaCtor getAnnotatedNoParaCtor() {
        return annotatedNoParaCtor;
    }
}
