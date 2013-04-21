package taohu.inject.setter;

import taohu.inject.ctor.NoParaCtor;

import javax.inject.Inject;

public class StaticSetter {
    private static NoParaCtor noParaCtor;

    public static NoParaCtor getNoParaCtor() {
        return noParaCtor;
    }

    @Inject
    public static void setNoParaCtor(NoParaCtor noParaCtor) {
        StaticSetter.noParaCtor = noParaCtor;
    }
}
