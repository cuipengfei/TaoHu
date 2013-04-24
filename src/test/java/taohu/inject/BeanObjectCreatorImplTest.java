package taohu.inject;

import org.junit.Test;
import taohu.inject.ctor.AnnotatedNoParaCtor;
import taohu.inject.ctor.OneParaCtor;
import taohu.inject.interfaces.BeanObjectCreator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class BeanObjectCreatorImplTest {

    @Test
    public void shouldReturnSameInstanceIfClassIsSingleton() throws Exception {
        BeanObjectCreator creator = new BeanObjectCreatorImpl();
        Object firstObject = creator.createBeanObject(AnnotatedNoParaCtor.class);
        Object secondObject = creator.createBeanObject(AnnotatedNoParaCtor.class);

        assertThat(firstObject, is(secondObject));
    }

    @Test
    public void shouldReturnSameInstanceOfSingletonClassForParameter() throws Exception {
        BeanObjectCreator creator = new BeanObjectCreatorImpl();
        Object noPara = creator.createBeanObject(AnnotatedNoParaCtor.class);
        OneParaCtor onePara = (OneParaCtor) creator.createBeanObject(OneParaCtor.class);

        assertThat(onePara, notNullValue());
        assertThat(onePara.getAnnotatedNoParaCtor(), is(noPara));
    }
}
