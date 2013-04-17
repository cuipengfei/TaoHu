package taohu.inject;

import javax.inject.Inject;

public class TwoCtorsOneParaLessWithOneAnnotation {

    private NoParaCtor noParaCtor;

    public TwoCtorsOneParaLessWithOneAnnotation() {
    }

    @Inject
    public TwoCtorsOneParaLessWithOneAnnotation(NoParaCtor noParaCtor) {
        this.noParaCtor = noParaCtor;
    }

    public NoParaCtor getNoParaCtor(){
        return this.noParaCtor;
    }
}
