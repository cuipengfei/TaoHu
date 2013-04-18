package taohu.parser;

import org.xml.sax.SAXException;
import taohu.model.BeanDescriptor;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class XmlBeanParser {

    private String fileName;

    public XmlBeanParser(String fileName) {
        this.fileName = fileName;
    }

    public List<BeanDescriptor> parseBeans() throws SAXException, IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL xsdUrl = classLoader.getResource("taohu/bean.xsd");
        URL xmlUrl = classLoader.getResource(fileName);

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Validator validator = schemaFactory.newSchema(xsdUrl).newValidator();
        validator.validate(new StreamSource(new File(xmlUrl.toURI())));

        return null;
    }
}

