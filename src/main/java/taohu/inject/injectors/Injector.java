package taohu.inject.injectors;

public interface Injector {
    public Object inject(Object instance, Class<?> clazz) throws Exception;
}
