package taohu.Resolver;

import org.junit.Test;
import taohu.inject.ctor.AnnotatedNoParaCtor;
import taohu.inject.ctor.OneParaCtor;
import taohu.model.BeanDescriptor;
import taohu.model.ConstructorArgumentDescriptor;

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

    @Test
    public void shouldGetbeanObejctWithConstructorArguments() throws Exception {
        BeanDescriptor noParaDescriptor = new BeanDescriptor();
        noParaDescriptor.setId("noParaCtor");
        noParaDescriptor.setClazz(AnnotatedNoParaCtor.class);

        ConstructorArgumentDescriptor argumentDescriptor = new ConstructorArgumentDescriptor();
        argumentDescriptor.setBeanId("noParaCtor");

        BeanDescriptor oneParaDescriptor = new BeanDescriptor();
        oneParaDescriptor.setId("oneParaCtor");
        oneParaDescriptor.setClazz(OneParaCtor.class);
        oneParaDescriptor.getConstructorDependency().add(argumentDescriptor);


        List<BeanDescriptor> beanDescriptors = new ArrayList<>();
        beanDescriptors.add(noParaDescriptor);
        beanDescriptors.add(oneParaDescriptor);

        BeanObjectFactory factory = new BeanObjectFactory(beanDescriptors);
        Object object = factory.getBean("oneParaCtor");
        assertThat(object.getClass().equals(OneParaCtor.class), is(true));
        assertThat(((OneParaCtor)object).getAnnotatedNoParaCtor().getClass().equals(AnnotatedNoParaCtor.class), is(true));
    }
}
