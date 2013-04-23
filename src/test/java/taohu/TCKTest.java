package taohu;

import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import taohu.inject.DependencyInjector;
import taohu.inject.interfaces.BeanConfigurationResolver;

@Ignore
public class TCKTest {

    private DependencyInjector dependencyInjector;

    @Before
    public void setUp(){
        this.dependencyInjector = new DependencyInjector(new BeanConfigurationResolver() {
            @Override
            public Class<?> getBeanClass(String beanId) {
                return null;
            }

            @Override
            public boolean containsBean(Class<?> clazz) {
                return true;
            }
        });
    }

    @Test
    public void testCar() throws Exception {
        Tck.testsFor((Car) dependencyInjector.createBeanObject(Class.forName("org.atinject.tck.auto.Convertible")),
                false, false);
    }
}
