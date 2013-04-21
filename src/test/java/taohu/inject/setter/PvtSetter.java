package taohu.inject.setter;

import taohu.inject.ctor.NoParaCtor;

import javax.inject.Inject;

public class PvtSetter {
    private NoParaCtor noParaCtor;

    @Inject
    private void setNoParaCtor(NoParaCtor noParaCtor) {
        this.noParaCtor = noParaCtor;
    }

    public NoParaCtor getNoParaCtor() {
        return noParaCtor;
    }
}
