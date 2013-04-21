package taohu.inject.field;

import taohu.inject.ctor.NoParaCtor;

import javax.inject.Inject;

public class StaticField {
    @Inject
    public static NoParaCtor noParaCtor;
}
