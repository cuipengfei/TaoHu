package taohu.inject.setter;

import taohu.inject.ctor.NoParaCtor;

import javax.inject.Inject;

public class SetterWithInject {
    private NoParaCtor noParaCtor;

    public NoParaCtor getNoParaCtor() {
        return noParaCtor;
    }

    @Inject
    public void setNoParaCtor(NoParaCtor noParaCtor) {
        this.noParaCtor = noParaCtor;
    }
}
