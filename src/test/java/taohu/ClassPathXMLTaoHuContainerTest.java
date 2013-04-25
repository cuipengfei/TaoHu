package taohu;

import org.junit.Test;
import org.xml.sax.SAXException;
import taohu.inject.ctor.NoParaCtor;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

public class ClassPathXMLTaoHuContainerTest {
    @Test
    public void shouldCreateObjectWhenBeanIsDefinedInXML() throws Exception, IOException, ClassNotFoundException, SAXException, ParserConfigurationException {
        ClassPathXMLTaoHuContainer classPathXMLTaoHuContainer = new ClassPathXMLTaoHuContainer("taohu/legalXmlFile.xml");

        Object first = classPathXMLTaoHuContainer.getBeanByID("first");

        assertThat((NoParaCtor) first, isA(NoParaCtor.class));
    }
}
