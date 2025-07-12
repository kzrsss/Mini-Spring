package com.minispring.test;

import com.minispring.aop.MethodBeforeAdvice;
import com.minispring.aop.aspectj.AspectJExpressionPointcut;
import com.minispring.aop.framework.ProxyFactory;
import com.minispring.aop.support.DefaultPointcutAdvisor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * AOP功能测试类
 */
public class AopTest {
    
    /**
     * 测试基本的AOP功能
     */
    @Test
    public void testAopProxy() throws Exception {
        // 1. 创建目标对象
        TestService target = new TestService();
        System.out.println("Target class: " + target.getClass().getName());
        
        // 2. 创建切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.minispring.test.AopTest$ITestService.*(..))");
        System.out.println("Pointcut matches TestService: " + pointcut.matches(TestService.class));
        System.out.println("Pointcut matches sayHello method: " + pointcut.matches(TestService.class.getMethod("sayHello"), TestService.class));
        System.out.println("Pointcut matches ITestService sayHello method: " + pointcut.matches(ITestService.class.getMethod("sayHello"), ITestService.class));
        
        // 3. 创建通知
        TestBeforeAdvice beforeAdvice = new TestBeforeAdvice();
        
        // 4. 创建Advisor（切点和通知的组合）
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, beforeAdvice);
        
        // 5. 使用代理工厂创建代理
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvisor(advisor);
        ITestService proxy = (ITestService) proxyFactory.getProxy();
        
        // 6. 调用代理方法，应该触发通知
        Assertions.assertEquals("TestService.sayHello()", proxy.sayHello());
        Assertions.assertEquals(1, beforeAdvice.getCounter());
        
        // 7. 多次调用，计数器应该增加
        proxy.sayHello();
        Assertions.assertEquals(2, beforeAdvice.getCounter());
    }
    
    /**
     * 测试CGLIB代理
     */
    @Test
    public void testCglibProxy() throws Exception {
        // 1. 创建目标对象（不实现接口的类）
        NonInterfaceService target = new NonInterfaceService();
        
        // 2. 创建切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.minispring.test.AopTest$NonInterfaceService.*(..))");
        
        // 3. 创建通知
        TestBeforeAdvice beforeAdvice = new TestBeforeAdvice();
        
        // 4. 创建Advisor（切点和通知的组合）
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, beforeAdvice);
        
        // 5. 使用代理工厂创建代理
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvisor(advisor);
        NonInterfaceService proxy = (NonInterfaceService) proxyFactory.getProxy();
        
        // 6. 调用代理方法，应该触发通知
        Assertions.assertEquals("NonInterfaceService.doSomething()", proxy.doSomething());
        Assertions.assertEquals(1, beforeAdvice.getCounter());
    }
    
    /**
     * 用于测试的服务接口
     */
    public interface ITestService {
        String sayHello();
    }
    
    /**
     * 用于测试的服务实现
     */
    static class TestService implements ITestService {
        @Override
        public String sayHello() {
            return "TestService.sayHello()";
        }
    }
    
    /**
     * 不实现接口的服务类，用于测试CGLIB代理
     */
    static class NonInterfaceService {
        public String doSomething() {
            return "NonInterfaceService.doSomething()";
        }
    }
    
    /**
     * 测试用的前置通知
     */
    static class TestBeforeAdvice implements MethodBeforeAdvice {
        private int counter = 0;
        
        @Override
        public void before(Method method, Object[] args, Object target) throws Throwable {
            counter++;
            System.out.println("TestBeforeAdvice.before called, counter = " + counter);
            System.out.println("Before method [" + method.getName() + "] on target [" + target.getClass().getName() + "]");
        }
        
        public int getCounter() {
            return counter;
        }
    }
} 