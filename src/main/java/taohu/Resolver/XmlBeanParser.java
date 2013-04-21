package taohu.Resolver;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import taohu.model.BeanDescriptor;
import taohu.model.ConstructorArgumentDescriptor;
import taohu.model.PropertyDescriptor;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Node node = nodeList.item(idx);

            BeanDescriptor descriptor = new BeanDescriptor();

            parseAttribute(node.getAttributes(), descriptor);

            parseSubElement(node.getChildNodes(), descriptor);

            beans.add(descriptor);
        }

        return beans;
    }

    private void parseSubElement(NodeList childNodeList, BeanDescriptor descriptor) {

        for (int subIdx = 0; subIdx < childNodeList.getLength(); subIdx++) {
            Node childNode = childNodeList.item(subIdx);
            if(childNode.getNodeName().equals("constructor-arg")){
                descriptor.getConstructorDependency().add(generateContructorArgument(childNode));
            }

            if(childNode.getNodeName().equals("properties")){
                descriptor.setPropertyDependency(generatePropertiesDescriptor(childNode));
            }
        }
    }

    private Map<String, PropertyDescriptor> generatePropertiesDescriptor(Node node) {
        NodeList childNodeList = node.getChildNodes();
        Map<String, PropertyDescriptor> propertiesDescriptor = new HashMap<>();

        for (int idx = 0; idx < childNodeList.getLength(); idx++) {
            Node item = childNodeList.item(idx);
            if(item.getNodeName().equals("property")){
                NamedNodeMap attributes = item.getAttributes();
                PropertyDescriptor pd = new PropertyDescriptor();
                pd.setName(attributes.getNamedItem("name").getNodeValue());
                pd.setBeanId(attributes.getNamedItem("ref").getNodeValue());
                propertiesDescriptor.put(pd.getName(), pd);
            }
        }
        return propertiesDescriptor;
    }

    private ConstructorArgumentDescriptor generateContructorArgument(Node childNode) {
        String beanId = childNode.getAttributes().getNamedItem("ref").getNodeValue();
        ConstructorArgumentDescriptor constructorArgumentDescriptor = new ConstructorArgumentDescriptor();
        constructorArgumentDescriptor.setBeanId(beanId);
        return constructorArgumentDescriptor;
    }

    private void parseAttribute(NamedNodeMap attributeMap, BeanDescriptor descriptor) throws ClassNotFoundException {
        descriptor.setId(attributeMap.getNamedItem("id").getNodeValue());
        descriptor.setClazz(Class.forName(attributeMap.getNamedItem("class").getNodeValue()));
    }

    private void validateXML() throws SAXException, IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL xsdUrl = classLoader.getResource("taohu/bean.xsd");

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Validator validator = schemaFactory.newSchema(xsdUrl).newValidator();
        validator.validate(new StreamSource(new File(xmlURI)));
    }
}

