package taohu;

import org.junit.Before;
import org.junit.Test;
import taohu.inject.ctor.AnnotatedNoParaCtor;
import taohu.inject.ctor.NoParaCtor;
import taohu.inject.ctor.OneParaCtor;
import taohu.inject.ctor.TwoParaCtor;
import taohu.inject.exception.BeanCreateException;
import taohu.inject.exception.BeanNotRegisteredException;
import taohu.inject.exception.LackOfAnnotationException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

public class ClassPathXMLTaoHuContainerTest {

    private ClassPathXMLTaoHuContainer parentContainer;
    private ClassPathXMLTaoHuContainer childContainer;

    @Before
    public void setUp() throws BeanCreateException {
        parentContainer = new ClassPathXMLTaoHuContainer("taohu/parentXmlFile.xml");
        childContainer = new ClassPathXMLTaoHuContainer("taohu/childXmlFile.xml", parentContainer);
    }

    @Test
    public void shouldGetBeanWhenBeanIsDefinedInXML() throws BeanCreateException, BeanNotRegisteredException, LackOfAnnotationException {
        ClassPathXMLTaoHuContainer classPathXMLTaoHuContainer = new ClassPathXMLTaoHuContainer("taohu/legalXmlFile.xml");

        Object first = classPathXMLTaoHuContainer.getBeanByID("first");

        assertThat((NoParaCtor) first, isA(NoParaCtor.class));
    }

    @Test
    public void shouldGetBeanDefinedInParentContainer() throws BeanCreateException, BeanNotRegisteredException, LackOfAnnotationException {
        OneParaCtor oneParaCtor = (OneParaCtor) childContainer.getBeanByID("oneParaCtor");

        assertThat(oneParaCtor, notNullValue());
        assertThat(oneParaCtor, isA(OneParaCtor.class));
    }

    @Test
    public void shouldGetBeanWithParaClassInParentContainer() throws BeanCreateException, BeanNotRegisteredException, LackOfAnnotationException {
        TwoParaCtor twoParaCtor = (TwoParaCtor) childContainer.getBeanByID("twoParaCtor");

        assertThat(twoParaCtor, isA(TwoParaCtor.class));
        assertThat(twoParaCtor.getAnnotatedNoParaCtor(), notNullValue());
        assertThat(twoParaCtor.getAnnotatedNoParaCtor(), isA(AnnotatedNoParaCtor.class));
        assertThat(twoParaCtor.getOneParaCtor(), notNullValue());
        assertThat(twoParaCtor.getOneParaCtor(), isA(OneParaCtor.class));
    }

    @Test(expected = BeanNotRegisteredException.class)
    public void shouldNotGetBeanNotDefinedAtAll() throws LackOfAnnotationException, BeanNotRegisteredException, BeanCreateException {
        childContainer.getBeanByID("notDefined");
    }

    @Test(expected = BeanNotRegisteredException.class)
    public void shouldNotGetBeanWithParaNotDefinedAtAll() throws LackOfAnnotationException, BeanNotRegisteredException, BeanCreateException {
        childContainer.getBeanByID("needNoParaCtor");
    }
}
