package taohu.inject.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import taohu.model.BeanDescriptor;

import javax.annotation.Nullable;
import java.util.List;

public class BeanConfigurationResolver {

    private List<BeanDescriptor> beanDescriptors;

    public BeanConfigurationResolver(List<BeanDescriptor> beanDescriptors) {

        this.beanDescriptors = beanDescriptors;
    }

    public Class<?> getBeanClass(final String beanId) {
        BeanDescriptor beanDescriptor = Iterables.find(beanDescriptors, new Predicate<BeanDescriptor>() {
            @Override
            public boolean apply(@Nullable BeanDescriptor input) {
                return input.getBeanId().equals(beanId);
            }
        });

        return beanDescriptor == null ? null : beanDescriptor.getClazz();
    }

    public boolean containsBean(final Class<?> clazz) {
        boolean any = Iterables.any(beanDescriptors, new Predicate<BeanDescriptor>() {
            @Override
            public boolean apply(@Nullable BeanDescriptor input) {
                return input.getClazz().equals(clazz);
            }
        });
        return any;
    }
}
