package taohu.inject;

import javax.inject.Inject;

public class TwoCtorsWithOneAnnotation {

    private AnnotatedNoParaCtor annotatedNoParaCtor;
    private OneParaCtor oneParaCtor;

    public TwoCtorsWithOneAnnotation(AnnotatedNoParaCtor annotatedNoParaCtor) {
        this.annotatedNoParaCtor = annotatedNoParaCtor;
    }

    @Inject
    public TwoCtorsWithOneAnnotation(OneParaCtor oneParaCtor) {
        this.oneParaCtor = oneParaCtor;
    }

    public AnnotatedNoParaCtor getAnnotatedNoParaCtor(){
        return this.annotatedNoParaCtor;
    }

    public OneParaCtor getOneParaCtor(){
        return this.oneParaCtor;
    }
}
