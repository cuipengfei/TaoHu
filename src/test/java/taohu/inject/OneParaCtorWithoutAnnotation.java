package taohu.inject;

import javax.inject.Inject;

public class OneParaCtorWithoutAnnotation {
    private NoParaCtor noParaCtor;

    public OneParaCtorWithoutAnnotation(NoParaCtor noParaCtor) {
        this.noParaCtor = noParaCtor;
    }

    public NoParaCtor getNoParaCtor() {
        return noParaCtor;
    }
}
