package taohu.inject.setter;

import javax.inject.Inject;

public class SetterWithInjectAndTypeParaFromItClass<T> {

    private String str;

    public String getStr() {
        return str;
    }

    @Inject
    public void setSomething(T t) {
        this.str = "method called";
    }
}
