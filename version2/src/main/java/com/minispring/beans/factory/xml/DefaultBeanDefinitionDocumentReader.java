package com.minispring.beans.factory.xml;

import com.minispring.beans.BeansException;
import com.minispring.beans.PropertyValue;
import com.minispring.beans.PropertyValues;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanReference;
import com.minispring.beans.factory.support.BeanDefinitionRegistry;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;

import java.util.List;

/**
 * 默认的Bean定义文档读取器实现
 * 用于从XML文档中读取Bean定义并注册到BeanDefinitionRegistry
 */
public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader {

    /**
     * XML标签和属性常量
     */
    public static final String BEAN_ELEMENT = "bean";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String INIT_METHOD_ATTRIBUTE = "init-method";
    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
    public static final String SCOPE_ATTRIBUTE = "scope";
    
    /**
     * 命名空间处理器解析器
     */
    private NamespaceHandlerResolver namespaceHandlerResolver;
    
    /**
     * 默认构造函数
     */
    public DefaultBeanDefinitionDocumentReader() {
        this.namespaceHandlerResolver = new DefaultNamespaceHandlerResolver();
    }
    
    /**
     * 构造函数
     * 
     * @param namespaceHandlerResolver 命名空间处理器解析器
     */
    public DefaultBeanDefinitionDocumentReader(NamespaceHandlerResolver namespaceHandlerResolver) {
        this.namespaceHandlerResolver = namespaceHandlerResolver;
    }
    
    /**
     * 设置命名空间处理器解析器
     * 
     * @param namespaceHandlerResolver 命名空间处理器解析器
     */
    public void setNamespaceHandlerResolver(NamespaceHandlerResolver namespaceHandlerResolver) {
        this.namespaceHandlerResolver = namespaceHandlerResolver;
    }

    /**
     * 从Document中读取Bean定义
     * 
     * @param document 要解析的XML文档
     * @param registry Bean定义注册表，用于注册解析出的Bean定义
     * @throws BeansException 如果解析过程中发生错误
     */
    @Override
    public void registerBeanDefinitions(Document document, BeanDefinitionRegistry registry) throws BeansException {
        // 获取根元素
        Element root = document.getRootElement();
        
        // 解析文档中的Bean定义
        doRegisterBeanDefinitions(root, registry);
    }

    /**
     * 从根元素开始解析Bean定义
     * 
     * @param root 根元素
     * @param registry Bean定义注册表
     * @throws BeansException 如果解析过程中发生错误
     */
    protected void doRegisterBeanDefinitions(Element root, BeanDefinitionRegistry registry) throws BeansException {
        // 处理根元素下的所有子元素
        List<Element> elements = root.elements();
        for (Element element : elements) {
            // 获取元素的命名空间
            String namespaceUri = element.getNamespaceURI();
            
            if (namespaceUri != null && !namespaceUri.isEmpty()) {
                // 处理自定义命名空间
                parseCustomElement(element, registry);
            } else if (element.getName().equals(BEAN_ELEMENT)) {
                // 处理默认的bean元素
                processBeanDefinition(element, registry);
            }
        }
    }
    
    /**
     * 解析自定义命名空间元素
     * 
     * @param element 要解析的元素
     * @param registry Bean定义注册表
     * @throws BeansException 如果解析过程中发生错误
     */
    protected void parseCustomElement(Element element, BeanDefinitionRegistry registry) throws BeansException {
        String namespaceUri = element.getNamespaceURI();
        NamespaceHandler handler = namespaceHandlerResolver.resolve(namespaceUri);
        
        if (handler == null) {
            throw new XmlBeanDefinitionStoreException("未找到命名空间 [" + namespaceUri + "] 的处理器");
        }
        
        handler.parse(element, registry);
    }

    /**
     * 解析单个Bean元素并注册到注册表
     * 
     * @param beanElement Bean元素
     * @param registry Bean定义注册表
     * @throws BeansException 如果解析过程中发生错误
     */
    protected void processBeanDefinition(Element beanElement, BeanDefinitionRegistry registry) throws BeansException {
        // 解析bean元素的属性
        String id = beanElement.attributeValue(ID_ATTRIBUTE);
        String name = beanElement.attributeValue(NAME_ATTRIBUTE);
        String className = beanElement.attributeValue(CLASS_ATTRIBUTE);
        String initMethodName = beanElement.attributeValue(INIT_METHOD_ATTRIBUTE);
        String destroyMethodName = beanElement.attributeValue(DESTROY_METHOD_ATTRIBUTE);
        String scope = beanElement.attributeValue(SCOPE_ATTRIBUTE);

        // 获取Class对象
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new XmlBeanDefinitionStoreException("找不到类 [" + className + "]", e);
        }

        // 确定bean的名称
        String beanName = id != null && !id.isEmpty() ? id : name;
        if (beanName == null || beanName.isEmpty()) {
            // 如果没有指定id和name，使用类名的首字母小写作为bean名称
            beanName = Character.toLowerCase(clazz.getSimpleName().charAt(0)) + clazz.getSimpleName().substring(1);
        }

        // 创建BeanDefinition
        BeanDefinition beanDefinition = new BeanDefinition(clazz);

        // 设置初始化和销毁方法
        if (initMethodName != null && !initMethodName.isEmpty()) {
            beanDefinition.setInitMethodName(initMethodName);
        }
        if (destroyMethodName != null && !destroyMethodName.isEmpty()) {
            beanDefinition.setDestroyMethodName(destroyMethodName);
        }

        // 设置作用域
        if (scope != null && !scope.isEmpty()) {
            beanDefinition.setScope(scope);
        }

        // 解析property元素
        parsePropertyElements(beanElement, beanDefinition);
        
        // 处理自定义属性
        parseCustomAttributes(beanElement, beanDefinition, registry);

        // 注册BeanDefinition
        registry.registerBeanDefinition(beanName, beanDefinition);
    }
    
    /**
     * 解析自定义属性
     * 
     * @param element 包含属性的元素
     * @param beanDefinition Bean定义
     * @param registry Bean定义注册表
     * @throws BeansException 如果解析过程中发生错误
     */
    protected void parseCustomAttributes(Element element, BeanDefinition beanDefinition, BeanDefinitionRegistry registry) throws BeansException {
        List<org.dom4j.Attribute> attributes = element.attributes();
        for (org.dom4j.Attribute attribute : attributes) {
            String namespaceUri = attribute.getNamespaceURI();
            if (namespaceUri != null && !namespaceUri.isEmpty()) {
                NamespaceHandler handler = namespaceHandlerResolver.resolve(namespaceUri);
                if (handler != null) {
                    handler.decorate(element, attribute.getName(), registry);
                }
            }
        }
    }

    /**
     * 解析Bean元素中的property元素
     * 
     * @param beanElement Bean元素
     * @param beanDefinition Bean定义
     * @throws BeansException 如果解析过程中发生错误
     */
    protected void parsePropertyElements(Element beanElement, BeanDefinition beanDefinition) throws BeansException {
        List<Element> propertyElements = beanElement.elements(PROPERTY_ELEMENT);
        PropertyValues propertyValues = new PropertyValues();
        
        for (Element propertyElement : propertyElements) {
            parsePropertyElement(propertyElement, propertyValues);
        }
        
        beanDefinition.setPropertyValues(propertyValues);
    }

    /**
     * 解析单个property元素
     * 
     * @param propertyElement property元素
     * @param propertyValues 属性值集合
     * @throws BeansException 如果解析过程中发生错误
     */
    protected void parsePropertyElement(Element propertyElement, PropertyValues propertyValues) throws BeansException {
        String propertyName = propertyElement.attributeValue(NAME_ATTRIBUTE);
        String propertyValue = propertyElement.attributeValue(VALUE_ATTRIBUTE);
        String propertyRef = propertyElement.attributeValue(REF_ATTRIBUTE);

        if (propertyName == null || propertyName.isEmpty()) {
            throw new XmlBeanDefinitionStoreException("Bean的property元素必须指定name属性");
        }

        Object value;
        if (propertyValue != null && !propertyValue.isEmpty()) {
            // 普通属性值
            value = propertyValue;
        } else if (propertyRef != null && !propertyRef.isEmpty()) {
            // 引用其他Bean
            value = new BeanReference(propertyRef);
        } else {
            throw new XmlBeanDefinitionStoreException("Bean的property元素必须指定value或ref属性之一");
        }

        PropertyValue pv = new PropertyValue(propertyName, value);
        propertyValues.addPropertyValue(pv);
    }
} 