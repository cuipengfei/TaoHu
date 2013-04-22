package taohu.Resolver;

import org.junit.Test;
import taohu.model.BeanDescriptor;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BeanObjectFactoryTest {

    @Test
    public void shouldGetBeanObjectWithoutContructorArgumentsAndProperties() throws Exception {
        BeanDescriptor beanDescriptor = new BeanDescriptor();
        beanDescriptor.setId("String");
        beanDescriptor.setClazz(String.class);

        List<BeanDescriptor> beanDescriptors = new ArrayList<>();
        beanDescriptors.add(beanDescriptor);

        BeanObjectFactory factory = new BeanObjectFactory(beanDescriptors);
        Object object = factory.getBean(beanDescriptor.getId());
        assertThat(object.getClass().equals(String.class), is(true));
    }
}
