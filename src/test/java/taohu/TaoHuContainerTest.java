package taohu;

import org.junit.Test;
import org.xml.sax.SAXException;
import taohu.inject.ctor.NoParaCtor;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

public class TaoHuContainerTest {
    @Test
    public void shouldCreateObjectWhenBeanIsDefinedInXML() throws Exception, IOException, ClassNotFoundException, SAXException, ParserConfigurationException {
        TaoHuContainer taoHuContainer = new TaoHuContainer();
        taoHuContainer.addXMLConfig("taohu/legalXmlFile.xml");

        Object first = taoHuContainer.getBeanByID("first");

        assertThat((NoParaCtor) first, isA(NoParaCtor.class));
    }
}
