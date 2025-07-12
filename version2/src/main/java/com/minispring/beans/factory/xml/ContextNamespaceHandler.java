package com.minispring.beans.factory.xml;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.support.BeanDefinitionRegistry;
import org.dom4j.Element;

/**
 * Context 命名空间处理器
 * 用于处理 context 命名空间下的元素和属性
 */
public class ContextNamespaceHandler extends AbstractNamespaceHandler {
    
    /**
     * 初始化命名空间处理器
     */
    @Override
    public void init() {
        // 注册元素解析器
        registerElementParser("property-placeholder", new PropertyPlaceholderElementParser());
        registerElementParser("component-scan", new ComponentScanElementParser());
        
        // 注册属性装饰器
        registerAttributeDecorator("default-lazy-init", new DefaultLazyInitAttributeDecorator());
    }
    
    /**
     * 属性占位符元素解析器
     */
    private static class PropertyPlaceholderElementParser implements ElementParser {
        @Override
        public void parse(Element element, BeanDefinitionRegistry registry) throws BeansException {
            String location = element.attributeValue("location");
            if (location != null && !location.isEmpty()) {
                System.out.println("解析 property-placeholder 元素，加载属性文件：" + location);
                // 实际实现中，这里应该加载属性文件并创建 PropertyPlaceholderConfigurer Bean
            }
        }
    }
    
    /**
     * 组件扫描元素解析器
     */
    private static class ComponentScanElementParser implements ElementParser {
        @Override
        public void parse(Element element, BeanDefinitionRegistry registry) throws BeansException {
            String basePackage = element.attributeValue("base-package");
            if (basePackage != null && !basePackage.isEmpty()) {
                System.out.println("解析 component-scan 元素，扫描包：" + basePackage);
                // 实际实现中，这里应该扫描指定包下的组件并注册为 Bean
            }
        }
    }
    
    /**
     * 默认延迟初始化属性装饰器
     */
    private static class DefaultLazyInitAttributeDecorator implements AttributeDecorator {
        @Override
        public void decorate(Element element, String attributeName, BeanDefinitionRegistry registry) throws BeansException {
            String value = element.attributeValue(attributeName);
            if ("true".equals(value)) {
                System.out.println("设置默认延迟初始化为 true");
                // 实际实现中，这里应该设置所有 Bean 的默认延迟初始化属性
            }
        }
    }
} 