package taohu.inject.field;

import taohu.inject.ctor.NoParaCtor;

import javax.inject.Inject;

public class FinalField {
    @Inject
    public final NoParaCtor noParaCtor = null;
}
