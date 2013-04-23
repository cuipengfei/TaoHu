package taohu;

import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import taohu.inject.BeanObjectCreatorImpl;
import taohu.inject.interfaces.BeanConfigurationResolver;
import taohu.inject.interfaces.BeanObjectCreator;

@Ignore
public class TCKTest {

    private BeanObjectCreator beanObjectCreator;

    @Before
    public void setUp(){
        this.beanObjectCreator = new BeanObjectCreatorImpl(new BeanConfigurationResolver() {
            @Override
            public Class<?> getBeanClass(String beanId) {
                return null;
            }

            @Override
            public boolean containsBean(Class<?> clazz) {
                return true;
            }
        }, null);
    }

    @Test
    public void testCar() throws Exception {
        Tck.testsFor((Car) beanObjectCreator.createBeanObject(Class.forName("org.atinject.tck.auto.Convertible")),
                false, false);
    }
}
