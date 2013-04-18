package taohu.parser;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import taohu.model.BeanDescriptor;

import javax.xml.XMLConstants;
import javax.xml.bind.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XmlBeanParser {

    private URI xmlURI;

    public XmlBeanParser(URI xmlURI) {
        this.xmlURI = xmlURI;
    }

    public List<BeanDescriptor> parseBeans() throws SAXException, IOException, URISyntaxException, ParserConfigurationException, ClassNotFoundException {

        validateXML();

        return parseBeanDescriptors();
    }

    private List<BeanDescriptor> parseBeanDescriptors() throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(xmlURI.toString());
        NodeList nodeList = document.getElementsByTagName("bean");

        List<BeanDescriptor> beans = new ArrayList();

        for (int idx = 0; idx < nodeList.getLength(); idx++) {
            BeanDescriptor descriptor = new BeanDescriptor();
            Node node = nodeList.item(idx);

            NamedNodeMap attributeMap = node.getAttributes();
            descriptor.setId(attributeMap.getNamedItem("id").getNodeValue());
            descriptor.setClazz(Class.forName(attributeMap.getNamedItem("class").getNodeValue()));

            beans.add(descriptor);
        }

        return beans;
    }

    private void validateXML() throws SAXException, IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL xsdUrl = classLoader.getResource("taohu/bean.xsd");

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Validator validator = schemaFactory.newSchema(xsdUrl).newValidator();
        validator.validate(new StreamSource(new File(xmlURI)));
    }
}

