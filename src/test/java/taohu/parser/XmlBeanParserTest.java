package taohu.parser;

import org.junit.Test;
import org.xml.sax.SAXException;

public class XmlBeanParserTest {

    @Test(expected = SAXException.class)
    public void shouldThrowExceptionWhenXMLFileCouldNotPassXSDValidation() throws Exception {
        XmlBeanParser parser = new XmlBeanParser("taohu/illegalXmlFile.xml");
        parser.parseBeans();
    }
}
