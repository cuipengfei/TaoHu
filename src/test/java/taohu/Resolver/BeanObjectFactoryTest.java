package taohu.Resolver;

import org.junit.Before;
import org.junit.Test;
import taohu.inject.ctor.AnnotatedNoParaCtor;
import taohu.inject.ctor.OneParaCtor;
import taohu.model.BeanDescriptor;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BeanObjectFactoryTest {

    private static BeanObjectFactory beanObjectFactory;

    @Before
    public void setUp() {
        BeanDescriptor annotatedNoPara = new BeanDescriptor("noPara", AnnotatedNoParaCtor.class);
        BeanDescriptor onePara = new BeanDescriptor("onePara", OneParaCtor.class);

        List<BeanDescriptor> beanDescriptors = new ArrayList<>();
        beanDescriptors.add(annotatedNoPara);
        beanDescriptors.add(onePara);

        beanObjectFactory = new BeanObjectFactory(beanDescriptors);
    }

    @Test
    public void shouldGetBeanObject() throws Exception {
        Object object = beanObjectFactory.getBean("noPara");
        assertThat(object.getClass().equals(AnnotatedNoParaCtor.class), is(true));
    }

    @Test
    public void shouldNotCreateUnregisteredBeanObject() throws Exception {

    }
}
