# Mini-Spring 框架

## 项目介绍

Mini-Spring是一个简化版的Spring框架，旨在帮助开发者快速理解Spring的核心原理和实现机制。该项目抽取了Spring的核心逻辑，保留其基本功能，同时大幅简化了代码结构，使学习者能够更容易地掌握Spring的精髓。

## 核心功能

Mini-Spring实现了以下Spring核心功能：

1. **IoC容器**：依赖注入和控制反转 ✅
2. **AOP**：面向切面编程 ✅
3. **Bean生命周期管理**：实例化、初始化、销毁 ✅
4. **应用上下文**：配置和环境管理 ✅
5. **资源加载**：类路径和文件系统资源 ✅
6. **事件监听机制**：发布订阅模式 ✅
7. **类型转换**：基本类型和自定义类型转换 ✅
8. **Bean作用域**：单例、原型及Web作用域 ✅

## 项目结构

```
src/main/java/com/minispring/
├── beans
│   ├── factory
│   │   ├── config
│   │   │   ├── Scope相关接口和实现
│   │   │   └── ...
│   │   ├── support
│   │   └── xml
│   ├── BeansException.java
│   ├── PropertyValue.java
│   ├── PropertyValues.java
│   └── ...
├── context
│   ├── event
│   ├── support
│   ├── ApplicationContext.java
│   └── ...
├── core
│   ├── io
│   ├── convert
│   │   ├── converter
│   │   ├── support
│   │   └── ...
│   └── ...
├── aop
│   ├── framework
│   ├── aspectj
│   ├── Advisor.java
│   ├── PointcutAdvisor.java
│   └── ...
├── web
│   ├── context
│   │   ├── request
│   │   │   ├── RequestScope.java
│   │   │   ├── SessionScope.java
│   │   │   └── ...
│   │   └── ...
│   └── ...
└── util
    └── ...
```

## 开发顺序与计划

# Mini-Spring 框架开发

## 第一阶段：IoC容器基础实现 ✅

### 1. Bean容器的最基本实现 ✅
- **BeanDefinition** ✅
  - 定义Bean的基本信息（类类型、作用域等）
  - 初始化和销毁方法配置
  - 是否单例/原型标记
- **BeanFactory** ✅
  - 核心方法：`getBean(String name)`
  - 类型安全访问：`getBean(String name, Class<T> requiredType)`
  - 通过类型获取：`getBean(Class<T> requiredType)`
- **SingletonBeanRegistry** ✅
  - 单例Bean注册表接口
  - 管理单例Bean生命周期

### 2. Bean的定义、注册和获取 ✅
- **DefaultSingletonBeanRegistry** ✅
  - 单例对象存储容器(ConcurrentHashMap)
  - 实现单例注册和获取逻辑
- **AbstractBeanFactory** ✅
  - 定义Bean获取的模板方法
  - 抽象Bean创建逻辑
- **DefaultListableBeanFactory** ✅
  - 默认可列表Bean工厂实现
  - Bean定义注册和获取功能

### 3. 基础属性处理 ✅
- **PropertyValue** ✅
  - 不可变的名称-值对
  - 支持原始值和转换后的值
- **PropertyValues** ✅
  - 管理多个PropertyValue集合
  - 线程安全实现，支持替换和查询

## 第二阶段：Bean生命周期与属性填充 ✅

### 4. 实例化策略与依赖注入 ✅
- **InstantiationStrategy** ✅
  - 定义Bean实例化的策略接口
  - 提供构造函数实例化的实现
  - 提供简单工厂方法的实现
- **BeanWrapper** ✅
  - 封装Bean实例的中间层
  - 提供统一的属性访问接口
  - 支持属性类型转换
- **TypeConverter** ✅
  - 类型转换核心接口
  - 基本类型及其包装类转换
  - String与其他类型互转

### 5. 依赖关系管理 ✅
- **ConstructorResolver** ✅
  - 构造器参数解析（类型/索引匹配）
  - 支持构造函数自动装配
- **DependencyDescriptor** ✅
  - 描述字段/方法注入点
  - 准备支持`@Autowired`等元数据
- **BeanReference** ✅
  - 延迟依赖引用包装器
  - 为解决循环依赖提供基础

### 6. 资源加载系统 ✅
- **Resource** ✅
  - 统一资源描述接口
  - 关键方法：`exists()`, `isReadable()`, `getInputStream()`
- **ResourceLoader** ✅
  - 资源加载策略接口
  - 支持类路径、文件系统等资源加载
- **DefaultResourceLoader** ✅
  - 默认资源加载实现
  - 根据路径前缀选择资源类型

### 7. Bean定义读取 ✅
- **BeanDefinitionReader** ✅
  - Bean定义读取核心接口
  - 支持从不同来源读取Bean定义
- **AbstractBeanDefinitionReader** ✅
  - 抽象Bean定义读取器
  - 封装通用的读取逻辑
- **XmlBeanDefinitionReader** ✅
  - XML Bean定义读取器
  - 解析XML文件中的Bean定义

## 第三阶段：Bean生命周期回调与扩展点 ✅

### 8. 初始化和销毁 ✅
- **InitializingBean** ✅
  - Bean初始化接口`afterPropertiesSet()`
  - 提供初始化扩展点
- **DisposableBean** ✅
  - Bean销毁接口`destroy()`
  - 支持自定义销毁方法
- **BeanPostProcessor** ✅
  - 初始化前回调`postProcessBeforeInitialization`
  - 初始化后回调`postProcessAfterInitialization`

### 9. Aware接口族 ✅
- **BeanNameAware** ✅
  - 注入Bean名称
- **BeanFactoryAware** ✅
  - 注入BeanFactory
- **ApplicationContextAware** ✅
  - 注入ApplicationContext

## 第四阶段：XML配置文件解析 ✅

### 10. XML解析基础 ✅
- **DocumentLoader** ✅
  - XML文档加载器
  - 解析XML文件为Document对象
- **XmlBeanDefinitionReader** ✅
  - 从XML读取Bean定义
  - 解析`<bean>`标签及其属性
- **BeanDefinitionDocumentReader** ✅
  - 从Document读取Bean定义
  - 处理命名空间

### 11. XML配置特性 ✅
- **命名空间处理器** ✅
  - 支持自定义命名空间
  - 处理如`<context:...>`, `<aop:...>`等标签
- **PlaceholderResolver** ✅
  - 属性占位符解析器（支持`${...}`语法）
  - 实现配置属性替换

## 第五阶段：应用上下文与事件机制 ✅

### 12. 应用上下文 ✅
- **ApplicationContext** ✅
  - 应用上下文接口，继承BeanFactory
  - 提供更多企业级功能
- **AbstractApplicationContext** ✅
  - 实现上下文刷新流程`refresh()`
  - 管理上下文生命周期
- **ClassPathXmlApplicationContext** ✅
  - 从类路径XML加载上下文
  - 便于应用程序使用的门面

### 13. Environment及配置抽象 ✅
- **Environment** ✅
  - 环境配置抽象
  - 包含properties和profiles
- **PropertySource** ✅
  - 属性源抽象
  - 层级化属性查找

### 14. 事件机制 ✅
- **ApplicationEvent** ✅
  - 应用事件基类
  - 定义上下文事件
- **ApplicationListener** ✅
  - 事件监听器接口
  - 观察者模式实现
- **ApplicationEventMulticaster** ✅
  - 事件广播器
  - 管理监听器注册和事件发布

## 第六阶段：AOP实现 ✅

### 15. AOP核心概念 ✅
- **Pointcut** ✅
  - 切点接口，定义拦截规则
  - 方法匹配和类匹配
- **Advice** ✅
  - 通知接口，定义增强逻辑
  - Before/After/Around等类型
- **Advisor** ✅
  - 组合Pointcut与Advice
  - 切面完整定义

### 16. AOP代理 ✅
- **AopProxy** ✅
  - AOP代理接口
  - 定义获取代理对象的方法
- **JdkDynamicAopProxy** ✅
  - 基于JDK动态代理的实现
  - 处理实现接口的类
- **CglibAopProxy** ✅
  - 基于CGLIB的实现
  - 处理没有实现接口的类

### 17. AspectJ集成 ✅
- **AspectJExpressionPointcut** ✅
  - 支持AspectJ表达式
  - 强大的切点定义方式
- **AspectJAdvisorFactory** ✅
  - 从AspectJ注解创建Advisor
  - 处理@Aspect注解类

## 第七阶段：完善与拓展 ✅

### 18. 类型转换 ✅
- **ConversionService** ✅
  - 类型转换服务接口
  - 统一的类型转换入口
- **Converter** ✅
  - 类型转换器接口
  - 实现各种类型间的转换

### 19. Bean作用域 ✅
- **Scope** ✅
  - 作用域接口
  - 定义获取和销毁Bean的方法
- **RequestScope/SessionScope** ✅
  - Web作用域实现
  - 与Web容器集成

### 20. 扩展特性 ✅
- **FactoryBean** ✅
  - 工厂Bean接口
  - 用于创建复杂对象
- **BeanFactoryPostProcessor** ✅
  - BeanFactory后处理器
  - 可修改Bean定义
- **ScopedProxyFactory** ✅
  - 作用域代理工厂
  - 处理不同作用域Bean的注入

## 附录：技术要点与挑战

### 循环依赖解决方案 ✅
- **三级缓存结构**
  ```java
  // 一级缓存：完全初始化的Bean
  Map<String, Object> singletonObjects = new ConcurrentHashMap<>();
  
  // 二级缓存：提前曝光的半成品Bean
  Map<String, Object> earlySingletonObjects = new HashMap<>();
  
  // 三级缓存：Bean工厂，用于生成半成品Bean的代理对象
  Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();
  ```

- **实现原理**
  - 三级缓存配合实例化和初始化分离，允许循环依赖的解决
  - 实例化完成后立即将Bean暴露到三级缓存中
  - 当发生循环依赖时，通过getSingleton方法从缓存中获取早期引用
  - 属性填充和初始化在获取早期引用后完成
  - 最终将完全初始化的Bean放入一级缓存

- **典型使用场景**
  - A依赖B，B依赖A的循环依赖场景
  - AOP代理对象的循环依赖
  - 构造器注入产生的循环依赖（有限支持）

### 类型转换体系
- 支持的类型转换：
  - 基本类型及其包装类
  - String类型
  - 集合类型
  - 自定义类型
- 处理策略：
  - 优先使用PropertyEditor
  - 其次使用Converter
  - 最后使用StringValueResolver

---

## 当前实现状态

✅ - 已完成
🔄 - 部分实现/进行中
⏱️ - 计划中

- 核心容器：✅ 基础功能已实现
- 属性处理：✅ 基本属性填充已实现
- 实例化策略：✅ 已实现
- 资源加载：✅ 已实现
- Bean定义读取：✅ 已完成
- 生命周期回调：✅ 已完成
- XML配置支持：✅ 已实现
- 应用上下文：✅ 已实现基本功能
- 事件系统：✅ 已实现基本功能
- AOP支持：✅ 已实现
- 类型转换：✅ 已实现
- Bean作用域：✅ 已实现
- 扩展特性：✅ 已实现
- 循环依赖支持：✅ 已实现

## 如何使用

Mini-Spring的使用方式与Spring框架类似，但API更简洁：

1. 定义Bean类
2. 创建XML配置文件（或使用注解）
3. 加载应用上下文
4. 获取Bean并使用

示例代码将在每个阶段完成后添加。

## 学习建议

1. 按照开发顺序逐步学习，理解每个模块的设计思路
2. 亲自动手实现每个模块，加深理解
3. 对比Spring源码，理解简化的部分和保留的核心逻辑
4. 尝试扩展功能，如添加新的Bean作用域或AOP切点表达式

## 项目依赖

- Java 17+
- Maven 3.8.1+

## 参考资料

- Spring Framework官方文档
- 《Spring揭秘》
- 《Spring源码深度解析》

