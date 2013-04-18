package taohu.parser;

import org.xml.sax.SAXException;
import taohu.model.BeanDescriptor;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class XmlBeanParser {


    private InputStream xmlFileInputStream;

    public XmlBeanParser(InputStream xmlFileInputStream) {
        this.xmlFileInputStream = xmlFileInputStream;
    }

    public List<BeanDescriptor> parseBeans() throws SAXException, IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL xsdUrl = classLoader.getResource("taohu/bean.xsd");

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Validator validator = schemaFactory.newSchema(xsdUrl).newValidator();
        validator.validate(new StreamSource(xmlFileInputStream));

        return null;
    }
}

