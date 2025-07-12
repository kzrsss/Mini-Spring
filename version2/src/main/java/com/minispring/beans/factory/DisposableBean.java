package com.minispring.beans.factory;

/**
 * Bean销毁接口
 * 实现此接口的Bean会在容器关闭时执行destroy方法
 * 这是Spring生命周期中的一个重要扩展点，用于释放资源
 */
public interface DisposableBean {
    
    /**
     * 在Bean被销毁时调用
     * 可以在此方法中执行资源释放、连接关闭等清理工作
     * 
     * @throws Exception 销毁过程中可能抛出的异常
     */
    void destroy() throws Exception;
} 