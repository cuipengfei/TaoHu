package taohu.inject;

public class OneParaCtor {
    private NoParaCtor noParaCtor;

    public OneParaCtor(NoParaCtor noParaCtor) {
        this.noParaCtor = noParaCtor;
    }

    public NoParaCtor getNoParaCtor() {
        return noParaCtor;
    }
}
