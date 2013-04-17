package taohu.inject;

import javax.inject.Inject;

public class TwoCtorsWithTwoAnnotations {

    @Inject
    public TwoCtorsWithTwoAnnotations() {
    }

    @Inject
    public TwoCtorsWithTwoAnnotations(AnnotatedNoParaCtor annotatedNoParaCtor) {
    }
}
