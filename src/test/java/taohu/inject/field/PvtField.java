package taohu.inject.field;

import taohu.inject.ctor.NoParaCtor;

import javax.inject.Inject;

public class PvtField {
    @Inject
    private NoParaCtor noParaCtor;

    public NoParaCtor getNoParaCtor() {
        return noParaCtor;
    }
}
