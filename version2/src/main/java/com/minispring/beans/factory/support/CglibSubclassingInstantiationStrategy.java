package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanDefinition;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

/**
 * CGLIB子类化实例化策略
 * 使用CGLIB动态生成子类来实例化Bean，适用于没有默认构造函数的类
 */
public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> ctor, Object[] args) throws BeansException {
        try {
            // 创建CGLIB增强器
            Enhancer enhancer = new Enhancer();
            // 设置需要创建子类的类
            enhancer.setSuperclass(beanDefinition.getBeanClass());
            // 设置回调函数，这里使用NoOp.INSTANCE，表示不设置任何拦截逻辑
            // 注意：如果需要实现AOP等增强逻辑，应改用MethodInterceptor
            enhancer.setCallback(NoOp.INSTANCE);

            // 参数校验和处理
            if (ctor == null) {
                // 如果没有指定构造函数，则使用默认构造函数
                return enhancer.create();
            }
            
            // 参数为null时，转换为空数组，避免NPE
            if (args == null) {
                args = new Object[0];
            }
            
            // 检查参数个数是否匹配
            if (args.length != ctor.getParameterCount()) {
                throw new BeansException("构造函数参数个数不匹配: " + beanName + 
                        "，期望 " + ctor.getParameterCount() + " 个参数，但提供了 " + args.length + " 个参数");
            }
            
            // 使用指定构造函数实例化
            return enhancer.create(ctor.getParameterTypes(), args);
        } catch (Exception e) {
            throw new BeansException("使用CGLIB实例化Bean失败 [" + beanName + "]", e);
        }
    }
} 