package taohu.inject.ctor;

import javax.inject.Inject;

public class NeedNoParaCtor {
    private NoParaCtor noParaCtor;

    @Inject
    public NeedNoParaCtor(NoParaCtor noParaCtor) {
        this.noParaCtor = noParaCtor;
    }

    public NoParaCtor getNoParaCtor() {
        return noParaCtor;
    }
}
