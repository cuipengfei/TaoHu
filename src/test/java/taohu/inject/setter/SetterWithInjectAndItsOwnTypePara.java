package taohu.inject.setter;

import javax.inject.Inject;

public class SetterWithInjectAndItsOwnTypePara {

    private String str;

    public String getStr() {
        return str;
    }

    @Inject
    public <T> void setSomething(T t) {
        this.str = "method called";
    }
}
