package taohu.inject.ctor;

import javax.inject.Inject;

public class OneParaCtor {
    private AnnotatedNoParaCtor annotatedNoParaCtor;

    @Inject
    public OneParaCtor(AnnotatedNoParaCtor annotatedNoParaCtor) {
        this.annotatedNoParaCtor = annotatedNoParaCtor;
    }

    public AnnotatedNoParaCtor getAnnotatedNoParaCtor() {
        return annotatedNoParaCtor;
    }
}
