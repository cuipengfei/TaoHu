package taohu.inject;

import javax.inject.Inject;

public class TwoCtorsWithOneAnnotation {

    private NoParaCtor noParaCtor;
    private OneParaCtor oneParaCtor;

    public TwoCtorsWithOneAnnotation(NoParaCtor noParaCtor) {
        this.noParaCtor = noParaCtor;
    }

    @Inject
    public TwoCtorsWithOneAnnotation(OneParaCtor oneParaCtor) {
        this.oneParaCtor = oneParaCtor;
    }

    public NoParaCtor getNoParaCtor(){
        return this.noParaCtor;
    }

    public OneParaCtor getOneParaCtor(){
        return this.oneParaCtor;
    }
}
