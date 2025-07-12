package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.DisposableBean;
import com.minispring.beans.factory.ObjectFactory;
import com.minispring.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认单例Bean注册表实现
 * 实现SingletonBeanRegistry接口，提供单例Bean的注册和获取功能
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /** 一级缓存：完全初始化好的单例对象缓存 */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    
    /** 二级缓存：提前曝光的单例对象（未完全初始化）缓存 */
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);
    
    /** 三级缓存：单例工厂缓存，用于保存bean创建工厂，以便后面利用工厂为bean创建代理对象 */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
    
    /** 正在创建中的单例Bean名称集合 */
    private final Set<String> singletonsCurrentlyInCreation = ConcurrentHashMap.newKeySet();
    
    /**
     * 需要销毁的Bean容器
     * 使用LinkedHashMap保证销毁顺序与注册顺序相反
     */
    private final Map<String, DisposableBean> disposableBeans = new LinkedHashMap<>();

    /**
     * 获取单例Bean
     * 实现三级缓存查找
     * 
     * @param beanName Bean名称
     * @return 单例Bean对象，如果不存在返回null
     */
    @Override
    public Object getSingleton(String beanName) {
        // 首先从一级缓存中获取
        Object singletonObject = singletonObjects.get(beanName);
        
        // 如果一级缓存中没有，并且该Bean正在创建中（可能存在循环依赖）
        if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
            synchronized (this.singletonObjects) {
                // 从二级缓存中获取
                singletonObject = earlySingletonObjects.get(beanName);
                
                // 如果二级缓存也没有，则尝试从三级缓存获取
                if (singletonObject == null) {
                    ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                    if (singletonFactory != null) {
                        // 通过工厂获取对象
                        singletonObject = singletonFactory.getObject();
                        // 放入二级缓存，并从三级缓存移除
                        earlySingletonObjects.put(beanName, singletonObject);
                        singletonFactories.remove(beanName);
                    }
                }
            }
        }
        
        return singletonObject;
    }
    
    /**
     * 获取单例Bean
     * 如果不存在则通过提供的ObjectFactory创建
     * 
     * @param beanName Bean名称
     * @param singletonFactory Bean工厂
     * @return 单例Bean
     */
    public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
        synchronized (this.singletonObjects) {
            // 首先检查一级缓存
            Object singletonObject = this.singletonObjects.get(beanName);
            if (singletonObject == null) {
                
                // 标记该Bean正在创建中
                beforeSingletonCreation(beanName);
                
                boolean newSingleton = false;
                try {
                    // 使用工厂创建单例
                    singletonObject = singletonFactory.getObject();
                    newSingleton = true;
                } catch (Exception ex) {
                    throw new BeansException("创建单例Bean[" + beanName + "]失败", ex);
                } finally {
                    // 清除正在创建标记
                    afterSingletonCreation(beanName);
                }
                
                if (newSingleton) {
                    // 将创建好的单例加入一级缓存，并从二三级缓存移除
                    addSingleton(beanName, singletonObject);
                }
            }
            
            return singletonObject;
        }
    }
    
    /**
     * 添加单例Bean到一级缓存，并清除二三级缓存
     * 
     * @param beanName Bean名称
     * @param singletonObject 单例Bean
     */
    protected void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
        }
    }
    
    /**
     * 添加单例工厂到三级缓存
     * 
     * @param beanName Bean名称
     * @param singletonFactory 单例工厂
     */
    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        synchronized (this.singletonObjects) {
            if (!this.singletonObjects.containsKey(beanName)) {
                this.singletonFactories.put(beanName, singletonFactory);
                this.earlySingletonObjects.remove(beanName);
            }
        }
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
        }
    }
    
    /**
     * 当前Bean是否正在创建中
     * 
     * @param beanName Bean名称
     * @return 是否正在创建中
     */
    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }
    
    /**
     * 标记指定的Bean正在创建中
     * 
     * @param beanName Bean名称
     */
    protected void beforeSingletonCreation(String beanName) {
        if (!this.singletonsCurrentlyInCreation.add(beanName)) {
            throw new BeansException("Bean[" + beanName + "]已经在创建中");
        }
    }
    
    /**
     * 标记指定的Bean创建完成
     * 
     * @param beanName Bean名称
     */
    protected void afterSingletonCreation(String beanName) {
        if (!this.singletonsCurrentlyInCreation.remove(beanName)) {
            throw new BeansException("Bean[" + beanName + "]不在创建中，无法结束创建过程");
        }
    }

    /**
     * 注册需要销毁的Bean
     * 
     * @param beanName Bean名称
     * @param bean 需要销毁的Bean
     */
    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }
    
    /**
     * 销毁单例Bean
     * 按照注册的相反顺序销毁Bean
     */
    public void destroySingletons() {
        Set<String> beanNames = disposableBeans.keySet();
        String[] disposableBeanNames = beanNames.toArray(new String[0]);
        
        // 按照注册的相反顺序销毁Bean
        for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
            String beanName = disposableBeanNames[i];
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("销毁Bean[" + beanName + "]时发生异常", e);
            }
        }
        
        // 清空所有缓存
        this.singletonObjects.clear();
        this.earlySingletonObjects.clear();
        this.singletonFactories.clear();
        this.singletonsCurrentlyInCreation.clear();
    }

    /**
     * 判断是否包含指定名称的单例Bean
     * @param beanName Bean名称
     * @return 是否包含
     */
    protected boolean containsSingleton(String beanName) {
        return singletonObjects.containsKey(beanName);
    }

    /**
     * 获取所有单例Bean的名称
     * @return 单例Bean名称数组
     */
    protected String[] getSingletonNames() {
        return singletonObjects.keySet().toArray(new String[0]);
    }

    /**
     * 获取单例Bean的数量
     * @return 单例Bean数量
     */
    protected int getSingletonCount() {
        return singletonObjects.size();
    }
}