package taohu;

import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import taohu.inject.BeanObjectCreatorImpl;
import taohu.inject.interfaces.BeanConfigurationResolver;
import taohu.inject.interfaces.BeanObjectCreator;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Ignore
public class TCKTest {

    private BeanObjectCreator beanObjectCreator;

    @Before
    public void setUp(){
        BeanConfigurationResolver beanConfigurationResolver = mock(BeanConfigurationResolver.class);
        when(beanConfigurationResolver.containsBean(any(Class.class))).thenReturn(true);
        this.beanObjectCreator = new BeanObjectCreatorImpl(beanConfigurationResolver);
    }

    @Test
    public void testCar() throws Exception {
        Tck.testsFor((Car) beanObjectCreator.createBeanObject(Class.forName("org.atinject.tck.auto.Convertible")),
                false, false);
    }
}
