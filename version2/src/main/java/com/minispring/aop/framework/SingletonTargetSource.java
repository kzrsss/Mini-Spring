package com.minispring.aop.framework;

/**
 * 单例目标源实现
 * 每次调用返回相同的目标对象
 */
public class SingletonTargetSource implements TargetSource {
    
    private final Object target;
    
    /**
     * 创建一个新的SingletonTargetSource
     * @param target 目标对象
     */
    public SingletonTargetSource(Object target) {
        if (target == null) {
            throw new IllegalArgumentException("Target object must not be null");
        }
        this.target = target;
    }
    
    @Override
    public Class<?> getTargetClass() {
        return target.getClass();
    }
    
    @Override
    public boolean isStatic() {
        return true;
    }
    
    @Override
    public Object getTarget() {
        return this.target;
    }
    
    @Override
    public void releaseTarget(Object target) {
        // 单例不需要释放
    }
    
    /**
     * 比较此目标源是否与另一目标源相等
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SingletonTargetSource)) {
            return false;
        }
        SingletonTargetSource otherTargetSource = (SingletonTargetSource) other;
        return this.target.equals(otherTargetSource.target);
    }
    
    /**
     * 返回此目标源的哈希码
     */
    @Override
    public int hashCode() {
        return this.target.hashCode();
    }
} 