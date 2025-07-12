package com.minispring.beans.factory.xml;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.support.BeanDefinitionRegistry;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽象命名空间处理器
 * 提供命名空间处理器的基本实现
 */
public abstract class AbstractNamespaceHandler implements NamespaceHandler {
    
    /**
     * 元素解析器映射表
     */
    private final Map<String, ElementParser> elementParsers = new HashMap<>();
    
    /**
     * 属性装饰器映射表
     */
    private final Map<String, AttributeDecorator> attributeDecorators = new HashMap<>();
    
    /**
     * 注册元素解析器
     * 
     * @param elementName 元素名称
     * @param parser 元素解析器
     */
    protected void registerElementParser(String elementName, ElementParser parser) {
        elementParsers.put(elementName, parser);
    }
    
    /**
     * 注册属性装饰器
     * 
     * @param attributeName 属性名称
     * @param decorator 属性装饰器
     */
    protected void registerAttributeDecorator(String attributeName, AttributeDecorator decorator) {
        attributeDecorators.put(attributeName, decorator);
    }
    
    @Override
    public void parse(Element element, BeanDefinitionRegistry registry) throws BeansException {
        String localName = element.getName();
        ElementParser parser = elementParsers.get(localName);
        if (parser != null) {
            parser.parse(element, registry);
        } else {
            throw new XmlBeanDefinitionStoreException("未知的元素 [" + localName + "] 在命名空间 [" + element.getNamespaceURI() + "]");
        }
    }
    
    @Override
    public void decorate(Element element, String attributeName, BeanDefinitionRegistry registry) throws BeansException {
        AttributeDecorator decorator = attributeDecorators.get(attributeName);
        if (decorator != null) {
            decorator.decorate(element, attributeName, registry);
        } else {
            throw new XmlBeanDefinitionStoreException("未知的属性 [" + attributeName + "] 在命名空间 [" + element.getNamespaceURI() + "]");
        }
    }
    
    /**
     * 元素解析器接口
     */
    public interface ElementParser {
        /**
         * 解析元素
         * 
         * @param element 要解析的元素
         * @param registry Bean定义注册表
         * @throws BeansException 如果解析过程中发生错误
         */
        void parse(Element element, BeanDefinitionRegistry registry) throws BeansException;
    }
    
    /**
     * 属性装饰器接口
     */
    public interface AttributeDecorator {
        /**
         * 装饰元素
         * 
         * @param element 包含属性的元素
         * @param attributeName 属性名
         * @param registry Bean定义注册表
         * @throws BeansException 如果装饰过程中发生错误
         */
        void decorate(Element element, String attributeName, BeanDefinitionRegistry registry) throws BeansException;
    }
}
