package taohu.inject;

import javax.inject.Inject;

public class OneParaCtor {
    private NoParaCtor noParaCtor;

    public OneParaCtor(NoParaCtor noParaCtor) {
        this.noParaCtor = noParaCtor;
    }

    public NoParaCtor getNoParaCtor() {
        return noParaCtor;
    }
}
