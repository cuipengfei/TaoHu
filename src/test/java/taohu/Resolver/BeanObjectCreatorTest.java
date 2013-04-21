package taohu.Resolver;

import org.junit.Test;
import taohu.model.BeanDescriptor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BeanObjectCreatorTest {

    @Test
    public void shouldCreateBeanObjectWithoutContructorArgumentsAndProperties() throws Exception {
        BeanDescriptor beanDescriptor = new BeanDescriptor();
        beanDescriptor.setId("String");
        beanDescriptor.setClazz(String.class);

        BeanObjectCreator creator = new BeanObjectCreator();
        Object object = creator.createBean(beanDescriptor);
        assertThat(object.getClass().equals(String.class), is(true));
    }
}
