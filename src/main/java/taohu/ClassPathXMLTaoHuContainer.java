package taohu;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.xml.sax.SAXException;
import taohu.inject.BeanObjectCreator;
import taohu.model.BeanDescriptor;
import taohu.resolver.BeanConfigurationResolver;
import taohu.resolver.XmlBeanParser;

import javax.annotation.Nullable;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathXMLTaoHuContainer {

    private Map<String, BeanObjectCreator> creatorsWithScopeName;
    private BeanConfigurationResolver beanConfigurationResolver;

    public ClassPathXMLTaoHuContainer() {
        this.creatorsWithScopeName = new HashMap<>();
    }

    public void addXMLConfig(String xmlFilePath) throws URISyntaxException, ClassNotFoundException, IOException, SAXException, ParserConfigurationException {
        URI xmlURI = getClass().getClassLoader().getResource(xmlFilePath).toURI();
        XmlBeanParser xmlBeanParser = new XmlBeanParser(xmlURI);
        List<BeanDescriptor> beanDescriptors = xmlBeanParser.parseBeans();

        beanConfigurationResolver = new BeanConfigurationResolver(beanDescriptors);

        BeanObjectCreator creator = new BeanObjectCreator(beanConfigurationResolver);
        creatorsWithScopeName.put(getScopeName(xmlFilePath), creator);
    }

    private String getScopeName(String xmlFilePath) {
        return xmlFilePath + xmlFilePath.hashCode();
    }

    public Object getBeanByID(final String id) throws Exception {
        Iterable<Object> objects = getBeanFromAllCreators(id);

        Optional<Object> objectOptional = Iterables.tryFind(objects, new Predicate<Object>() {
            @Override
            public boolean apply(@Nullable Object input) {
                return input != null;
            }
        });

        return objectOptional.isPresent() ? objectOptional.get() : null;
    }

    private Iterable<Object> getBeanFromAllCreators(final String id) {
        return Iterables.transform(creatorsWithScopeName.values(), new Function<BeanObjectCreator, Object>() {
            @Override
            public Object apply(@Nullable BeanObjectCreator input) {
                Object bean = null;
                try {
                    bean = input.getBeanObject(beanConfigurationResolver.getBeanClass(id));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bean;
            }
        });
    }
}
