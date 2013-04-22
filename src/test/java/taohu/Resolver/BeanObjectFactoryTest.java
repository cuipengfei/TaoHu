package taohu.Resolver;

import org.junit.BeforeClass;
import org.junit.Test;
import taohu.inject.ctor.AnnotatedNoParaCtor;
import taohu.inject.ctor.OneParaCtor;
import taohu.inject.ctor.TwoParaCtor;
import taohu.model.BeanDescriptor;
import taohu.model.ConstructorArgumentDescriptor;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BeanObjectFactoryTest {

    private static BeanObjectFactory beanObjectFactory;

    @Test
    public void shouldGetBeanObjectWithoutContructorArgumentsAndProperties() throws Exception {
        Object object = beanObjectFactory.getBean("noParaCtor");
        assertThat(object.getClass().equals(AnnotatedNoParaCtor.class), is(true));
    }

    @Test
    public void shouldGetBeanObejctWithOneConstructorArguments() throws Exception {
        Object object = beanObjectFactory.getBean("oneParaCtor");
        assertThat(object.getClass().equals(OneParaCtor.class), is(true));
        assertThat(((OneParaCtor) object).getAnnotatedNoParaCtor().getClass().equals(AnnotatedNoParaCtor.class), is(true));
    }

    @Test
    public void shouldGetBeanObejctWithTwoConstructorArguments() throws Exception {
        Object object = beanObjectFactory.getBean("twoParaCtor");
        assertThat(object.getClass().equals(TwoParaCtor.class), is(true));
        assertThat(((TwoParaCtor) object).getAnnotatedNoParaCtor().getClass().equals(AnnotatedNoParaCtor.class), is(true));
        assertThat(((TwoParaCtor) object).getOneParaCtor().getClass().equals(OneParaCtor.class), is(true));
        assertThat(((TwoParaCtor) object).getOneParaCtor().getAnnotatedNoParaCtor().getClass()
                .equals(AnnotatedNoParaCtor.class), is(true));
    }

    @BeforeClass
    public static void setUp() {
        BeanDescriptor noParaDescriptor = new BeanDescriptor();
        noParaDescriptor.setId("noParaCtor");
        noParaDescriptor.setClazz(AnnotatedNoParaCtor.class);

        ConstructorArgumentDescriptor firstArgumentDescriptor = new ConstructorArgumentDescriptor();
        firstArgumentDescriptor.setBeanId("noParaCtor");

        BeanDescriptor oneParaDescriptor = new BeanDescriptor();
        oneParaDescriptor.setId("oneParaCtor");
        oneParaDescriptor.setClazz(OneParaCtor.class);
        oneParaDescriptor.getConstructorDependency().add(firstArgumentDescriptor);

        ConstructorArgumentDescriptor secondArgumentDescriptor = new ConstructorArgumentDescriptor();
        secondArgumentDescriptor.setBeanId("oneParaCtor");

        BeanDescriptor twoParaDescriptor = new BeanDescriptor();
        twoParaDescriptor.setId("twoParaCtor");
        twoParaDescriptor.setClazz(TwoParaCtor.class);
        twoParaDescriptor.getConstructorDependency().add(firstArgumentDescriptor);
        twoParaDescriptor.getConstructorDependency().add(secondArgumentDescriptor);


        List<BeanDescriptor> beanDescriptors = new ArrayList<>();
        beanDescriptors.add(noParaDescriptor);
        beanDescriptors.add(oneParaDescriptor);
        beanDescriptors.add(twoParaDescriptor);

        beanObjectFactory = new BeanObjectFactory(beanDescriptors);
    }
}
