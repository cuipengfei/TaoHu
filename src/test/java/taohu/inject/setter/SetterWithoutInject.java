package taohu.inject.setter;

import taohu.inject.ctor.NoParaCtor;

public class SetterWithoutInject {
    private NoParaCtor noParaCtor;

    public NoParaCtor getNoParaCtor() {
        return noParaCtor;
    }

    public void setNoParaCtor(NoParaCtor noParaCtor) {
        this.noParaCtor = noParaCtor;
    }
}
