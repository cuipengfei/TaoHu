package taohu.inject;

import javax.inject.Inject;

public class TwoParaCtor {
    private NoParaCtor noParaCtor;
    private OneParaCtor oneParaCtor;

    @Inject
    public TwoParaCtor(NoParaCtor noParaCtor, OneParaCtor oneParaCtor) {
        this.noParaCtor = noParaCtor;
        this.oneParaCtor = oneParaCtor;
    }

    public NoParaCtor getNoParaCtor() {
        return noParaCtor;
    }

    public OneParaCtor getOneParaCtor() {
        return oneParaCtor;
    }
}
