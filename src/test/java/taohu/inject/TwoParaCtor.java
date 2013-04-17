package taohu.inject;

import javax.inject.Inject;

public class TwoParaCtor {
    private AnnotatedNoParaCtor annotatedNoParaCtor;
    private OneParaCtor oneParaCtor;

    @Inject
    public TwoParaCtor(AnnotatedNoParaCtor annotatedNoParaCtor, OneParaCtor oneParaCtor) {
        this.annotatedNoParaCtor = annotatedNoParaCtor;
        this.oneParaCtor = oneParaCtor;
    }

    public AnnotatedNoParaCtor getAnnotatedNoParaCtor() {
        return annotatedNoParaCtor;
    }

    public OneParaCtor getOneParaCtor() {
        return oneParaCtor;
    }
}
