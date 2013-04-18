package taohu.parser;

import org.junit.Test;
import org.xml.sax.SAXException;
import taohu.model.BeanDescriptor;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
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
        assertThat(beans.get(0).getId(), is("first"));
        assertThat(beans.get(0).getClazz().equals(String.class), is(true));
        assertThat(beans.get(1).getId(), is("second"));
        assertThat(beans.get(1).getClazz().equals(Integer.class), is(true));
    }
}
