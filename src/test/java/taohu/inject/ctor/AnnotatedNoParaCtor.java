package taohu.inject.ctor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnnotatedNoParaCtor {
    @Inject
    public AnnotatedNoParaCtor() {
    }
}
