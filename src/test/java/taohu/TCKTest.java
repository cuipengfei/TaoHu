package taohu;

import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.junit.Ignore;
import org.junit.Test;
import taohu.inject.DependencyInjector;

@Ignore
public class TCKTest {

    @Test
    public void testCar() throws Exception {
        Tck.testsFor((Car) new DependencyInjector().createBeanObject(Class.forName("org.atinject.tck.auto.Convertible")),
                false, false);
    }
}
