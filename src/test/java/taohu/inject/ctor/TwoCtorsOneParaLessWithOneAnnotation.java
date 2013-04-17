package taohu.inject.ctor;

import javax.inject.Inject;

public class TwoCtorsOneParaLessWithOneAnnotation {

    private AnnotatedNoParaCtor annotatedNoParaCtor;

    public TwoCtorsOneParaLessWithOneAnnotation() {
    }

    @Inject
    public TwoCtorsOneParaLessWithOneAnnotation(AnnotatedNoParaCtor annotatedNoParaCtor) {
        this.annotatedNoParaCtor = annotatedNoParaCtor;
    }

    public AnnotatedNoParaCtor getAnnotatedNoParaCtor(){
        return this.annotatedNoParaCtor;
    }
}
