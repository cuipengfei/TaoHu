package taohu.parser;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.InputStream;

public class XmlBeanParserTest {

    @Test(expected = SAXException.class)
    public void shouldThrowExceptionWhenXMLFileCouldNotPassXSDValidation() throws Exception {
        InputStream xmlFileInputStream = getClass().getClassLoader().getResourceAsStream("taohu/illegalXmlFile.xml");
        XmlBeanParser parser = new XmlBeanParser(xmlFileInputStream);
        parser.parseBeans();
    }
}
