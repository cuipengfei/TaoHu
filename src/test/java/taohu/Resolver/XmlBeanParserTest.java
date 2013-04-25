package taohu.resolver;

import org.junit.Test;
import org.xml.sax.SAXException;
import taohu.inject.ctor.NoParaCtor;
import taohu.inject.ctor.OneParaCtor;
import taohu.model.BeanDescriptor;

import java.net.URI;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class XmlBeanParserTest {

    @Test(expected = SAXException.class)
    public void shouldThrowExceptionWhenXMLFileCouldNotPassXSDValidation() throws Exception {
        URI xmlURI = getClass().getClassLoader().getResource("taohu/illegalXmlFile.xml").toURI();
        XmlBeanParser parser = new XmlBeanParser(xmlURI);
        parser.parseBeans();
    }

    @Test
    public void shouldCreateBeanDescriptorAccordingToXML() throws Exception {
        URI xmlURI = getClass().getClassLoader().getResource("taohu/legalXmlFile.xml").toURI();
        XmlBeanParser parser = new XmlBeanParser(xmlURI);
        List<BeanDescriptor> beans = parser.parseBeans();

        assertThat(beans, notNullValue());
        assertThat(beans.size(), is(2));

        BeanDescriptor firstBean = beans.get(0);
        assertThat(firstBean.getBeanId(), is("first"));
        assertThat(firstBean.getClazz().equals(NoParaCtor.class), is(true));

        BeanDescriptor secondBean = beans.get(1);
        assertThat(secondBean.getBeanId(), is("second"));
        assertThat(secondBean.getClazz().equals(OneParaCtor.class), is(true));
    }
}
