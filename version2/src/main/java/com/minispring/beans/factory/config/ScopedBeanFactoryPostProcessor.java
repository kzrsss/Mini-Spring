package com.minispring.beans.factory.config;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ConfigurableListableBeanFactory;

import java.util.Arrays;

/**
 * 作用域Bean工厂后处理器
 * 负责处理Bean定义中的作用域信息并创建相应的代理
 */
public class ScopedBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 获取所有Bean定义的名称
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            String scope = beanDefinition.getScope();
            
            // 对非单例且非原型的Bean进行处理
            if (scope != null && !scope.equals(ConfigurableBeanFactory.SCOPE_SINGLETON) 
                    && !scope.equals(ConfigurableBeanFactory.SCOPE_PROTOTYPE)) {
                
                // 检查是否需要作用域代理
                if (beanDefinition.isScopedProxy()) {
                    // 这里处理的逻辑是：为作用域Bean创建代理，并将Bean定义中的实际对象替换为代理
                    // 这样，当其他Bean引用这个作用域Bean时，实际上引用的是代理
                    // 代理会在每次方法调用时从作用域中获取实际对象
                    beanDefinition.setScopedProxy(true);
                    
                    // 在这里，我们不需要立即创建代理，因为实际的Bean还没有被创建
                    // 我们只需标记它需要代理，然后在Bean创建过程中处理
                    
                    // 通知Bean定义它需要一个代理创建器
                    beanDefinition.setAttribute("scopedProxyBeanName", beanName);
                    beanDefinition.setAttribute("originalScope", scope);
                }
            }
        }
    }
} 