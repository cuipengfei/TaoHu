package taohu.inject.setter;

import taohu.inject.ctor.NoParaCtor;

import javax.inject.Inject;

public class SetterWithInject {
    private NoParaCtor noParaCtor;
    private NoParaCtor noParaCtor2;
    private NoParaCtor noParaCtor3;

    public NoParaCtor getNoParaCtor() {
        return noParaCtor;
    }

    public NoParaCtor getNoParaCtor2() {
        return noParaCtor2;
    }

    public NoParaCtor getNoParaCtor3() {
        return noParaCtor3;
    }

    @Inject
    public void setNoParaCtor(NoParaCtor noParaCtor) {
        this.noParaCtor = noParaCtor;
    }

    @Inject
    public void setTwoNoParaCtors(NoParaCtor noParaCtor2, NoParaCtor noParaCtor3) {
        this.noParaCtor2 = noParaCtor2;
        this.noParaCtor3 = noParaCtor3;
    }
}
