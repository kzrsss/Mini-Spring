package com.minispring.core.env;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Environment的抽象基类
 * 提供Environment的基本实现
 */
public abstract class AbstractEnvironment implements ConfigurableEnvironment {
    
    /**
     * 活跃的profiles属性名
     */
    public static final String ACTIVE_PROFILES_PROPERTY_NAME = "spring.profiles.active";
    
    /**
     * 默认的profiles属性名
     */
    public static final String DEFAULT_PROFILES_PROPERTY_NAME = "spring.profiles.default";
    
    /**
     * 默认的profile名
     */
    protected static final String DEFAULT_PROFILE_NAME = "default";
    
    /**
     * 属性源集合
     */
    private final MutablePropertySources propertySources = new MutablePropertySources();
    
    /**
     * 活跃的profiles
     */
    private final Set<String> activeProfiles = new LinkedHashSet<>();
    
    /**
     * 默认的profiles
     */
    private final Set<String> defaultProfiles = new LinkedHashSet<>(Collections.singleton(DEFAULT_PROFILE_NAME));
    
    /**
     * 构造函数
     */
    public AbstractEnvironment() {
        customizePropertySources(this.propertySources);
        
        // 从系统属性和环境变量中加载profiles
        String[] activeProfiles = getProfilesFromProperty(ACTIVE_PROFILES_PROPERTY_NAME);
        if (activeProfiles != null) {
            setActiveProfiles(activeProfiles);
        }
        String[] defaultProfiles = getProfilesFromProperty(DEFAULT_PROFILES_PROPERTY_NAME);
        if (defaultProfiles != null) {
            setDefaultProfiles(defaultProfiles);
        }
    }
    
    /**
     * 自定义属性源
     * 子类可以重写此方法添加自定义属性源
     * 
     * @param propertySources 属性源集合
     */
    protected void customizePropertySources(MutablePropertySources propertySources) {
        // 默认为空，由子类实现
    }
    
    /**
     * 从属性中获取profiles
     * 
     * @param propertyName 属性名
     * @return profiles数组，如果不存在返回null
     */
    private String[] getProfilesFromProperty(String propertyName) {
        String profilesStr = getProperty(propertyName);
        if (profilesStr != null && !profilesStr.isEmpty()) {
            return profilesStr.split(",");
        }
        return null;
    }
    
    /**
     * 设置活跃的profiles
     * 
     * @param profiles 活跃的profiles数组
     */
    @Override
    public void setActiveProfiles(String... profiles) {
        if (profiles == null || profiles.length == 0) {
            this.activeProfiles.clear();
            return;
        }
        
        for (String profile : profiles) {
            validateProfile(profile);
        }
        
        this.activeProfiles.clear();
        Collections.addAll(this.activeProfiles, profiles);
    }
    
    /**
     * 添加活跃的profile
     * 
     * @param profile 活跃的profile
     */
    @Override
    public void addActiveProfile(String profile) {
        validateProfile(profile);
        this.activeProfiles.add(profile);
    }
    
    /**
     * 设置默认的profiles
     * 
     * @param profiles 默认的profiles数组
     */
    @Override
    public void setDefaultProfiles(String... profiles) {
        if (profiles == null || profiles.length == 0) {
            this.defaultProfiles.clear();
            return;
        }
        
        for (String profile : profiles) {
            validateProfile(profile);
        }
        
        this.defaultProfiles.clear();
        Collections.addAll(this.defaultProfiles, profiles);
    }
    
    /**
     * 获取活跃的profiles
     * 
     * @return 活跃的profiles数组
     */
    @Override
    public String[] getActiveProfiles() {
        if (this.activeProfiles.isEmpty()) {
            return new String[0];
        }
        return this.activeProfiles.toArray(new String[0]);
    }
    
    /**
     * 获取默认的profiles
     * 
     * @return 默认的profiles数组
     */
    @Override
    public String[] getDefaultProfiles() {
        if (this.defaultProfiles.isEmpty()) {
            return new String[0];
        }
        return this.defaultProfiles.toArray(new String[0]);
    }
    
    /**
     * 判断指定的profile是否激活
     * 
     * @param profile profile名称
     * @return 如果激活返回true，否则返回false
     */
    @Override
    public boolean acceptsProfiles(String profile) {
        validateProfile(profile);
        
        // 如果活跃的profiles为空，则使用默认的profiles
        if (this.activeProfiles.isEmpty()) {
            return this.defaultProfiles.contains(profile);
        }
        
        return this.activeProfiles.contains(profile);
    }
    
    /**
     * 判断指定的profiles中是否有任一激活
     * 
     * @param profiles profile名称数组
     * @return 如果有任一激活返回true，否则返回false
     */
    @Override
    public boolean acceptsProfiles(String... profiles) {
        if (profiles == null || profiles.length == 0) {
            return false;
        }
        
        for (String profile : profiles) {
            if (acceptsProfiles(profile)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 验证profile名称
     * 
     * @param profile profile名称
     * @throws IllegalArgumentException 如果profile名称不合法
     */
    private void validateProfile(String profile) {
        if (profile == null || profile.isEmpty()) {
            throw new IllegalArgumentException("Profile不能为空");
        }
    }
    
    /**
     * 获取系统属性
     * 
     * @return 系统属性
     */
    @Override
    public Map<String, Object> getSystemProperties() {
        // 将Properties转换为Map
        Map<String, Object> map = Collections.emptyMap();
        try {
            map = (Map) System.getProperties();
        } catch (Exception ex) {
            // 忽略安全异常
        }
        return map;
    }
    
    /**
     * 获取系统环境变量
     * 
     * @return 系统环境变量
     */
    @Override
    public Map<String, Object> getSystemEnvironment() {
        Map<String, Object> map = Collections.emptyMap();
        try {
            map = (Map) System.getenv();
        } catch (Exception ex) {
            // 忽略安全异常
        }
        return map;
    }
    
    /**
     * 获取属性源集合
     * 
     * @return 属性源集合
     */
    @Override
    public MutablePropertySources getPropertySources() {
        return this.propertySources;
    }
    
    /**
     * 合并另一个Environment的配置
     * 
     * @param parent 父Environment
     */
    @Override
    public void merge(ConfigurableEnvironment parent) {
        if (parent == null) {
            return;
        }
        
        // 合并属性源
        for (PropertySource<?> propertySource : parent.getPropertySources()) {
            if (!this.propertySources.contains(propertySource.getName())) {
                this.propertySources.addLast(propertySource);
            }
        }
        
        // 合并活跃的profiles
        Set<String> parentActiveProfiles = new LinkedHashSet<>(Arrays.asList(parent.getActiveProfiles()));
        if (!parentActiveProfiles.isEmpty()) {
            this.activeProfiles.addAll(parentActiveProfiles);
        }
        
        // 合并默认的profiles
        Set<String> parentDefaultProfiles = new LinkedHashSet<>(Arrays.asList(parent.getDefaultProfiles()));
        if (!parentDefaultProfiles.isEmpty() && !parentDefaultProfiles.contains(DEFAULT_PROFILE_NAME)) {
            this.defaultProfiles.addAll(parentDefaultProfiles);
        }
    }
    
    /**
     * 获取指定名称的属性值
     * 
     * @param key 属性名称
     * @return 属性值，如果不存在返回null
     */
    @Override
    public String getProperty(String key) {
        for (PropertySource<?> propertySource : this.propertySources) {
            Object value = propertySource.getProperty(key);
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }
    
    /**
     * 获取指定名称的属性值，如果不存在则使用默认值
     * 
     * @param key 属性名称
     * @param defaultValue 默认值
     * @return 属性值，如果不存在返回默认值
     */
    @Override
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * 获取指定名称的属性值，并转换为指定类型
     * 
     * @param key 属性名称
     * @param targetType 目标类型
     * @param <T> 目标类型泛型
     * @return 属性值，如果不存在返回null
     */
    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        String value = getProperty(key);
        if (value == null) {
            return null;
        }
        return convertValueToTargetType(value, targetType);
    }
    
    /**
     * 获取指定名称的属性值，并转换为指定类型，如果不存在则使用默认值
     * 
     * @param key 属性名称
     * @param targetType 目标类型
     * @param defaultValue 默认值
     * @param <T> 目标类型泛型
     * @return 属性值，如果不存在返回默认值
     */
    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        T value = getProperty(key, targetType);
        return value != null ? value : defaultValue;
    }
    
    /**
     * 判断指定名称的属性是否存在
     * 
     * @param key 属性名称
     * @return 如果存在返回true，否则返回false
     */
    @Override
    public boolean containsProperty(String key) {
        for (PropertySource<?> propertySource : this.propertySources) {
            if (propertySource.containsProperty(key)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 将字符串值转换为目标类型
     * 
     * @param value 字符串值
     * @param targetType 目标类型
     * @param <T> 目标类型泛型
     * @return 转换后的值
     */
    @SuppressWarnings("unchecked")
    private <T> T convertValueToTargetType(String value, Class<T> targetType) {
        if (targetType == String.class) {
            return (T) value;
        } else if (targetType == Integer.class || targetType == int.class) {
            return (T) Integer.valueOf(value);
        } else if (targetType == Long.class || targetType == long.class) {
            return (T) Long.valueOf(value);
        } else if (targetType == Double.class || targetType == double.class) {
            return (T) Double.valueOf(value);
        } else if (targetType == Float.class || targetType == float.class) {
            return (T) Float.valueOf(value);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return (T) Boolean.valueOf(value);
        } else if (targetType == Short.class || targetType == short.class) {
            return (T) Short.valueOf(value);
        } else if (targetType == Byte.class || targetType == byte.class) {
            return (T) Byte.valueOf(value);
        } else if (targetType == Character.class || targetType == char.class) {
            if (value.length() == 1) {
                return (T) Character.valueOf(value.charAt(0));
            }
            throw new IllegalArgumentException("Cannot convert String [" + value + "] to target type [" + targetType.getName() + "]");
        } else {
            throw new IllegalArgumentException("Unsupported target type: " + targetType.getName());
        }
    }
    
    /**
     * 重写toString方法
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " {activeProfiles=" + this.activeProfiles +
                ", defaultProfiles=" + this.defaultProfiles +
                ", propertySources=" + this.propertySources + "}";
    }
} 