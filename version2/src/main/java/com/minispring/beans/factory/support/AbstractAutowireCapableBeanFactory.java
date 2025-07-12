package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.BeanWrapper;
import com.minispring.beans.PropertyValue;
import com.minispring.beans.PropertyValues;
import com.minispring.beans.SimpleTypeConverter;
import com.minispring.beans.TypeConverter;
import com.minispring.beans.TypeMismatchException;
import com.minispring.beans.factory.BeanFactoryAware;
import com.minispring.beans.factory.BeanNameAware;
import com.minispring.beans.factory.DisposableBean;
import com.minispring.beans.factory.InitializingBean;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanPostProcessor;
import com.minispring.beans.factory.config.BeanReference;
import com.minispring.beans.factory.support.ConstructorResolver.BeanInstantiationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 抽象自动装配Bean工厂
 * 实现创建Bean的功能
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

    /**
     * 设置实例化策略
     * @param instantiationStrategy 实例化策略
     */
    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    /**
     * 获取实例化策略
     * @return 实例化策略
     */
    public InstantiationStrategy getInstantiationStrategy() {
        return this.instantiationStrategy;
    }

    /**
     * 创建Bean实例
     * @param beanName Bean名称
     * @param beanDefinition Bean定义
     * @param args 构造参数
     * @return Bean实例
     * @throws BeansException 如果创建Bean失败
     */
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean = null;
        try {
            // 创建Bean实例
            bean = createBeanInstance(beanDefinition, beanName, args);
            
            // 处理循环依赖，将实例化后的Bean对象提前放入三级缓存
            // 只有单例且允许循环依赖的Bean才会进行提前暴露
            if (beanDefinition.isSingleton()) {
                final Object finalBean = bean;
                addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, beanDefinition, finalBean));
                System.out.println("将Bean[" + beanName + "]提前曝光到三级缓存");
            }
            
            // 创建Bean包装器
            BeanWrapper beanWrapper = new BeanWrapper(bean);
            
            // 填充Bean属性
            applyPropertyValues(beanName, bean, beanDefinition, beanWrapper);
            
            // 执行Bean的初始化方法和BeanPostProcessor的前置和后置处理
            bean = initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("创建Bean失败: " + beanName, e);
        }
        
        // 注册销毁方法回调
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);
        
        // 注册单例Bean
        if (beanDefinition.isSingleton()) {
            // 处理FactoryBean和循环依赖后，最终加入到单例缓存
            // 如果这个Bean被提前暴露过（即解决了循环依赖），这一步会清除三级缓存中的工厂对象
            registerSingleton(beanName, bean);
        }
        
        return bean;
    }

    /**
     * 创建Bean实例
     * @param beanDefinition Bean定义
     * @param beanName Bean名称
     * @param args 构造参数
     * @return Bean实例
     */
    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) {
        System.out.println("创建Bean实例: " + beanName + ", 构造参数: " + (args != null ? args.length : 0));
        
        // 获取所有构造函数
        Constructor<?>[] declaredConstructors = beanDefinition.getBeanClass().getDeclaredConstructors();
        
        // 创建构造函数解析器
        ConstructorResolver constructorResolver = new ConstructorResolver(this);
        
        // 解析构造函数和参数
        BeanInstantiationContext instantiationContext = constructorResolver.autowireConstructor(
                beanName, beanDefinition, declaredConstructors, args);
        
        // 使用实例化策略创建Bean实例
        return getInstantiationStrategy().instantiate(
                beanDefinition, beanName, instantiationContext.getConstructor(), instantiationContext.getArgs());
    }

    /**
     * 填充Bean属性
     * @param beanName Bean名称
     * @param bean Bean实例
     * @param beanDefinition Bean定义
     * @param beanWrapper Bean包装器
     */
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition, BeanWrapper beanWrapper) {
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            if (propertyValues.isEmpty()) {
                return;
            }
            
            // 创建类型转换器
            TypeConverter typeConverter = new SimpleTypeConverter();
            
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                
                // 处理Bean引用
                if (value instanceof BeanReference) {
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }
                
                // 使用BeanWrapper设置属性值
                beanWrapper.setPropertyValue(name, value);
            }
        } catch (Exception e) {
            throw new BeansException("填充Bean属性失败: " + beanName, e);
        }
    }

    /**
     * 初始化Bean
     * @param beanName Bean名称
     * @param bean Bean实例
     * @param beanDefinition Bean定义
     * @return 初始化后的Bean实例
     */
    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        // 0. 处理Aware接口
        if (bean instanceof BeanNameAware) {
            ((BeanNameAware) bean).setBeanName(beanName);
        }
        
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
        
        // 1. 执行BeanPostProcessor的前置处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        
        // 2. 执行初始化方法
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("执行Bean初始化方法失败: " + beanName, e);
        }
        
        // 3. 执行BeanPostProcessor的后置处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        
        return wrappedBean;
    }

    /**
     * 执行Bean的初始化方法
     * @param beanName Bean名称
     * @param bean Bean实例
     * @param beanDefinition Bean定义
     * @throws Exception 初始化过程中可能抛出的异常
     */
    private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        // 1. 如果Bean实现了InitializingBean接口，则调用其afterPropertiesSet方法
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
            System.out.println("执行Bean[" + beanName + "]的InitializingBean接口的afterPropertiesSet方法");
        }
        
        // 2. 如果Bean定义了初始化方法，则执行
        String initMethodName = beanDefinition.getInitMethodName();
        if (initMethodName != null && !initMethodName.isEmpty() && 
                !(bean instanceof InitializingBean && "afterPropertiesSet".equals(initMethodName))) {
            try {
                // 通过反射执行初始化方法
                Method initMethod = bean.getClass().getMethod(initMethodName);
                initMethod.invoke(bean);
                System.out.println("执行Bean[" + beanName + "]的自定义初始化方法：" + initMethodName);
            } catch (NoSuchMethodException e) {
                throw new BeansException("找不到Bean[" + beanName + "]的初始化方法：" + initMethodName, e);
            }
        }
    }
    
    /**
     * 执行BeanPostProcessor的前置处理
     * @param existingBean 现有的Bean实例
     * @param beanName Bean名称
     * @return 处理后的Bean实例
     */
    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }
    
    /**
     * 执行BeanPostProcessor的后置处理
     * @param existingBean 现有的Bean实例
     * @param beanName Bean名称
     * @return 处理后的Bean实例
     */
    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }
    
    /**
     * 注册销毁方法回调
     * @param beanName Bean名称
     * @param bean Bean实例
     * @param beanDefinition Bean定义
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        // 只有单例Bean才需要注册销毁方法
        if (!beanDefinition.isSingleton()) {
            return;
        }
        
        if (bean instanceof DisposableBean || 
                (beanDefinition.getDestroyMethodName() != null && !beanDefinition.getDestroyMethodName().isEmpty())) {
            // 创建DisposableBeanAdapter并注册
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition.getDestroyMethodName()));
        }
    }

    /**
     * 获取早期Bean引用，用于解决循环依赖
     * 主要用于AOP场景，普通Bean直接返回原始对象，AOP则返回代理对象
     * 
     * @param beanName Bean名称
     * @param beanDefinition Bean定义
     * @param bean Bean实例
     * @return 早期引用
     */
    protected Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition, Object bean) {
        Object exposedObject = bean;
        // 这里可以对Bean进行后续处理，比如创建代理对象等
        // 暂时简单实现，直接返回原始对象
        System.out.println("获取Bean[" + beanName + "]的早期引用");
        return exposedObject;
    }
}