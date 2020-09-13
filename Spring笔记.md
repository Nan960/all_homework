# Spring笔记

<!-- @import "[TOC]" {cmd="toc" depthFrom=1 depthTo=6 orderedList=false} -->

<!-- code_chunk_output -->

- [Spring笔记](#spring笔记)
  - [Spring核心思想](#spring核心思想)
    - [IOC](#ioc)
      - [什么是IoC](#什么是ioc)
      - [解决什么问题](#解决什么问题)
      - [IOC和DI区别](#ioc和di区别)
    - [AOP](#aop)
      - [什么是AOP](#什么是aop)
      - [AOP解决了什么问题](#aop解决了什么问题)
      - [为什么叫面向切面编程](#为什么叫面向切面编程)
      - [补充](#补充)
  - [自定义简单IOC/AOP实现](#自定义简单iocaop实现)
    - [IOC容器简单实现](#ioc容器简单实现)
    - [AOP简单实现](#aop简单实现)
  - [Spring-IOC](#spring-ioc)
    - [纯XML配置](#纯xml配置)
      - [程序整合](#程序整合)
        - [JavaSE项目](#javase项目)
        - [web项目](#web项目)
      - [Bean配置](#bean配置)
        - [Bean的三种管理方式](#bean的三种管理方式)
        - [Bean标签属性](#bean标签属性)
        - [DI配置](#di配置)
    - [XML和注解混合配置](#xml和注解混合配置)
      - [整合步骤](#整合步骤)
    - [纯注解配置](#纯注解配置)
      - [常用注解说明](#常用注解说明)
      - [配置](#配置)
        - [项目内类交给IOC容器管理和获取](#项目内类交给ioc容器管理和获取)
        - [配置文件迁移](#配置文件迁移)
      - [启动方式](#启动方式)
        - [JavaSE项目](#javase项目-1)
        - [JavaWeb项目](#javaweb项目)
  - [Spring-IOC高级特性](#spring-ioc高级特性)
    - [懒（延迟）加载（Lazy-Init）](#懒延迟加载lazy-init)
      - [使用场景](#使用场景)
      - [启用延迟加载](#启用延迟加载)
    - [后置处理器](#后置处理器)
      - [实现方式：](#实现方式)
      - [扩展](#扩展)
  - [Spring-IOC源码分析](#spring-ioc源码分析)
    - [Spring-IOC源码分析-整体执行流程](#spring-ioc源码分析-整体执行流程)
      - [核心加载方法`AbstractApplicationContext#refresh()`](#核心加载方法abstractapplicationcontextrefresh)
    - [Spring-如何解决循环依赖：](#spring-如何解决循环依赖)
      - [什么是循环依赖](#什么是循环依赖)
      - [spring循环依赖处理机制](#spring循环依赖处理机制)
      - [为什么使用三级缓存，而不是用二级缓存呢？](#为什么使用三级缓存而不是用二级缓存呢)
  - [Spring AOP讲解](#spring-aop讲解)
    - [Spring-AOP术语](#spring-aop术语)
    - [Spring配置AOP](#spring配置aop)
      - [纯XML配置AOP](#纯xml配置aop)
      - [XML和注解混合配置](#xml和注解混合配置-1)
      - [纯注解配置](#纯注解配置-1)
      - [Expression表达式简单说明](#expression表达式简单说明)
      - [SpringAOP实现流程](#springaop实现流程)
  - [Spring声明式事务的支持](#spring声明式事务的支持)
    - [事务介绍](#事务介绍)
      - [什么是事务？](#什么是事务)
      - [事务的四大特性](#事务的四大特性)
      - [事务的隔离级别](#事务的隔离级别)
        - [事务并发问题](#事务并发问题)
        - [数据库定义四种隔离级别](#数据库定义四种隔离级别)
      - [事务的传播行为](#事务的传播行为)
    - [Spring声明式事务配置](#spring声明式事务配置)
      - [纯XML方式配置](#纯xml方式配置)
      - [XML注解结合配置](#xml注解结合配置)
      - [纯注解配置](#纯注解配置-2)
      - [注解如何配置全局事务增强](#注解如何配置全局事务增强)
      - [注意：](#注意)
  - [作业](#作业)

<!-- /code_chunk_output -->

## Spring核心思想

### IOC

#### 什么是IoC
IoC:Inversion of Control(控制反转/反转控制)，注意它是一种技术思想，而不是一个技术实现

**描述**：由第三方容器(框架)来管理外部资源（不仅仅是对象包括比如文件等）的生命周期

**为什么叫控制反转？**
控制：指外部资源的获取销毁等权利
反转：本来需要依赖外部资源的对象自己去获取外部资源，转换为，被动的接受第三方容器给予外部资源；

**图解：**
![IOC图解](https://img-blog.csdnimg.cn/20200902141353326.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA2ODk0NDA=,size_16,color_FFFFFF,t_70#pic_center)

#### 解决什么问题

IOC解决了对象之间的耦合问题；
在未使用IOC前，当我们依赖某个接口时，而且接口实现有多个的时候，我们每切换一次实现，就需要将所有引入接口的类的代码都改一遍；
使用IOC后，我们只需要在IOC容器中更换接口实现类即可；

#### IOC和DI区别
DI：Dependence Injection（依赖注入）
IOC和DI是对同一种思想的两种叫法；
- 从对象的角度：对象的控制权（创建销毁）交给了容器，我们叫IOC
- 从容器角度：容器会把控制的对象，注入到需要依赖此对象的实例中，所以我们叫DI

### AOP

#### 什么是AOP
AOP：Aspect Oriented Programming 面向切面编程，是OOP的延伸和补充；

什么是OOP？
OOP，面向对象编程，它具有三大特性：封装，继承，多肽；他是针对垂直业务流程的抽象封装，已获得更加高效清晰的逻辑单元划分,本质上不会改变原有业务流程执行过程，只是将业务按照对象，属性，功能分组提取；

在OOP思想下，当我们需要对功能方法增加通用功能（权限校验）时，我们需要将所有需要添加权限校验功能的方法，全部修改一遍，这样很明显不方便，代码耦合度高，这个时候，就用到了AOP思想；

AOP思想，将通用功能（权限校验）从业务流程中抽离出来，作为横切逻辑代码，在不改变原有业务代码的前提下，将横切逻辑代码切入到原有业务逻辑流程中；

#### AOP解决了什么问题

![横切逻辑代码](https://img-blog.csdnimg.cn/20200902104922248.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA2ODk0NDA=,size_16,color_FFFFFF,t_70#pic_center)

AOP在不改变业务逻辑的情况下，增强横切逻辑代码，根本上解耦合，避免横切逻辑代码重复问题；方便系统扩展（日志，事务管理，权限校验）等系统通用功能；

简单理解：在不改变原有代码的前提下，可以增加新的功能；

#### 为什么叫面向切面编程

- 「面」：垂直的业务逻辑流程，这里理解为一个面；
- 「切」：在不改动原有逻辑代码（增加面的大小）的前提下，增加（切入）新的通用功能；

#### 补充

- 只能在原有代码逻辑执行前后增加新功能；
- 配合OOP才能最大化发挥AOP特性（OOP将业务划分单元，AOP在单元的基础上扩展功能）；

## 自定义简单IOC/AOP实现

### IOC容器简单实现

1. 定义`Bean.xml`配置文件，维护需要管理类
```xml
<?xml version="1.0" encoding="utf-8" ?>
<!--根标签，里面包含bean标签，每个bean标签表示一个类配置-->
<beans>
<!--bean标签：表示一个类配置，id：从BeanFactory获取实例的唯一ID，class：表示需要IOC容器管理的类的全限定类名-->
<bean id="bankDao" class="com.wjy.dao.BankDaoImpl"/>
<bean id="bankService" class="com.wjy.service.BankServiceImpl">
    <!--property标签：表示类中需要注入的属性；name：表示注入属性名，采用set${name}的方式注入；ref:表示注入类对应的IOC容器id-->
    <property name="BankDao" ref="bankDao"/>
</bean>
</beans>
```
2. 定义`BeanFactory`解析配置文件，加载类，提供获取实例方法
```java
public class BeanFactory {

    /**
     * 一：读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
     * 二：对外提供获取实例对象的接口（根据id获取）
     */
    private static HashMap<String, Object> map = new HashMap<>();

    /**
     * 读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
     */
    static {
        try {
            // 解析配置文件
            final InputStream stream = BeanFactory.class.getClassLoader().getResourceAsStream("Bean.xml");
            final SAXReader reader = new SAXReader();
            final Document document = reader.read(stream);
            final Element beans = document.getRootElement();
            // 创建所有容器管理的bean
            final List<Element> list = beans.selectNodes("//bean");
            for (Element element : list) {
                final String id = element.attributeValue("id");
                final String aClass = element.attributeValue("class");
                final Class<?> clazz = Class.forName(aClass);
                // 反射创建类
                final Object obj = clazz.newInstance();
                map.put(id, obj);
            }
            // 注入属性
            final List<Element> properties = beans.selectNodes("//property");
            for (Element property : properties) {
                final String name = property.attributeValue("name");
                final String ref = property.attributeValue("ref");
                // 获取属性所在类
                final Element parent = property.getParent();
                final Object parentObj = map.get(parent.attributeValue("id"));
                // 找到注入方法
                for (Method method : parentObj.getClass().getMethods()) {
                    if (method.getName().equals("set" + name)) {
                        // 注入属性
                        method.invoke(parentObj, map.get(ref));
                    }
                }
            }
        } catch (DocumentException | ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对外提供获取实例对象的接口（根据id获取）
     */
    public static Object getBean(String id) {
        return map.get(id);
    }
}
```

3. 测试
```java
    @Test
    public void testIOC() {
        final BankService bankService = (BankService) BeanFactory.getBean("bankService");
        final boolean transfer = bankService.transfer(1, 2, 100.0);
        System.out.println(transfer);
    }
```

### AOP简单实现

1. 创建JDK代理工厂
```java
    public static <T> T jdkProxy(T obj) {
        final Object proxy = Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                try {
                    // 开启事务
                    TransactionManage.begin();
                    // 执行业务方法
                    final Object rst = method.invoke(obj, args);
                    // 提交事务
                    TransactionManage.commit();
                    return rst;
                } catch (Throwable t) {
                    // 回滚事务
                    TransactionManage.rollback();
                    throw t;
                }
            }
        });
        return (T) proxy;
    }
```

2. 创建cglib代理工厂
```java
    public static <T> T cglibProxy(T obj) {
        final Object t = Enhancer.create(obj.getClass(), new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                try {
                    // 开启事务
                    TransactionManage.begin();
                    // 执行业务方法
                    final Object tst = methodProxy.invoke(obj, objects);
                    // 提交事务
                    TransactionManage.commit();
                    return tst;
                } catch (Throwable t) {
                    // 回滚事务
                    TransactionManage.rollback();
                    throw t;
                }
            }
        });
        return (T) t;
    }
```

3. 使用代理工厂实现AOP
    创建对象的时候，使用代理工厂包装一层，后放入IOC容器，这样，每次从IOC容器获取的对象都是代理对象，每次执行都会执行增强方法；

## Spring-IOC

### 纯XML配置

#### 程序整合

引入spring-IOC容器依赖
```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.1.12.RELEASE</version>
</dependency>
```

##### JavaSE项目

1. 添加配置文件`applicationContext.xml`
2. 使用`ClassPathXmlApplicationContext或者FileSystemXmlApplicationContext`启动
```java
final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml"); // 推荐
// final FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("/src/main/resources/applicationContext.xml");
// 获取容器中Bean
final AccountDao accountDao = (AccountDao) context.getBean("accountDao");
```

##### web项目

0. 引入spring-web依赖
```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>5.1.12.RELEASE</version>
</dependency>
```
1. 添加配置文件`applicationContext.xml`
2. 配置`WEB-INF/web.xml`添加启动监听器
```xml
<!--指定spring配置文件路径-->
<context-param>
<param-name>contextConfigLocation</param-name>
<param-value>classpath:applicationContext.xml</param-value>
</context-param>
<!--使用spring监听器启动IOC容器-->
<listener>
<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```
3. Sevlet程序中使用
```java
final WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
proxyFactory = (ProxyFactory) applicationContext.getBean("proxyFactory");
```

#### Bean配置

##### Bean的三种管理方式

```xml
<!--spring ioc 实例化bean的三种方式-->
<!--方式一：无参构造方式（推荐）
在默认情况下，它会通过反射调用无参构造函数来创建对象。如果类中没有无参构造函数，将创建 失败。
-->
<!--<bean id="connectionUtils" class="com.wjy.utils.ConnectionUtils"/>-->


<!--另外两种方式，是为了将我们自己new的bean加入ioc容器管理-->
<!--方式二：静态方法-->
<!--<bean id="connectionUtils" class="com.wjy.factory.CreateBeanFactory" factory-method="getStaticConnectionUtils"/>-->
<!--方式三：实例化方法
此种方式和上面静态方法创建其实类似，区别是用于获取对象的方法不再是static修饰的了，而是 类中的一 个普通方法。
-->
<bean id="createFactory" class="com.wjy.factory.CreateBeanFactory"/>
<bean id="connectionUtils" factory-bean="createFactory" factory-method="getConnectionUtils"/>
```
```java
public class CreateBeanFactory {

    public static ConnectionUtils getStaticConnectionUtils() {
        return new ConnectionUtils();
    }

    public ConnectionUtils getConnectionUtils() {
        return new ConnectionUtils();
    }
}
```

##### Bean标签属性
在基于xml的IoC配置中，bean标签是最基础的标签。它表示了IoC容器中的一个对象。

- **id属性：** 用于给bean提供一个唯一标识。在一个标签内部，标识必须唯一。
- **class属性：** 用于指定创建Bean对象的全限定类名。
- **name属性：** 用于给bean提供一个或多个名称。多个名称用空格分隔。
- **factory-bean属性：** 用于指定创建当前bean对象的工厂bean的唯一标识。当指定了此属性之后， class属性失效。
- **factory-method属性：** 用于指定创建当前bean对象的工厂方法，如配合factory-bean属性使用， 则class属性失效。如配合class属性使用，则方法必须是static的。
- **init-method属性：** 用于指定bean对象的初始化方法，此方法会在bean对象装配后调用。必须是 一个无参方法。
- **destory-method属性：** 用于指定bean对象的销毁方法，此方法会在bean对象销毁前执行。它只 能为scope是singleton时起作用。
- **scope属性：** 用于指定bean对象的作用范围。通常情况下就是singleton。当要用到多例模式时， 可以配置为prototype。

| Scope | 描述 |
|---|---|
|  singleton | （默认）单例模式：一个IOC容器中只有一个实例 |
| prototype | 多例模式/原型模式，每次从IOC容器中获取，都创建新的实例 |
| request | 一个`HTTP Request`，创建一个实例 |
| session | 一个`HTTP Session`，创建一个实例 |
| application | 一个`ServletContext`，创建一个实例 |
| websocket | 一个`WebSocket`，创建一个实例 |

**单例模式：singleton**
对象出生:当创建容器时，对象就被创建了。 
对象活着:只要容器在，对象一直活着。 
对象死亡:当销毁容器时，对象就被销毁了。 
一句话总结:单例模式的bean对象生命周期与容器相同。
**多例模式：prototype**
对象出生:当使用对象时，创建新的对象实例。 
对象活着:只要对象在使用中，就一直活着。 
对象死亡:当对象⻓时间不用时，被java的垃圾回收器回收了。 
一句话总结:多例模式的bean对象，spring框架只负责创建，不负责销毁。

##### DI配置

**Spring依赖注入的分类**

- 按照注入方式分类：
    - **构造函数注入：** 需要被管理的类提供有参构造函数注入数据 `<constructor-arg/>`
    - **set方法注入：** 需要被管理类提供属性的set方法； `<property/>`
- 按照注入数据类型分类：
    - **基本类型：** 注入数据为基本数据类型和字符串形式的数据；使用`value`属性
    - **Bean类型：** 注入类型为实例化的`Bean`，而且是必须被`IOC`管理的`Bean`；使用`ref`属性
    - **复杂类型：** 注入类型为：`Array，List，Set，Map，Properties`；使用指定标签

**标签属性说明**

- constructor-arg标签：指定构造函数参数
    - index：指定参数位置，不能和name同时使用
    - name：指定参数名称，不能和index同时使用
    - type：指定参数类型，全限定类名
    - ref：指定IOC管理的Bean
    - value：指定基本数据类型和字符串
- property标签：指定set方法注入
    - name：指定set方法注入哪一个成员属性，这里写属性名
    - value：注入基本数据类型用
    - ref：注入IOC管理Bean用
- array标签：在`property`标签下写指定注入`数组`类型
    - value-type：指定数据类型，填全限定类名
- list标签：在`property`标签下写指定注入`List`类型
    - value-type：指定数据类型，填全限定类名
- set标签：在`property`标签下写指定注入`Set`类型
    - value-type：指定数据类型，填全限定类名
- value标签：在`array,list,set`标签下使用，指定具体值
    - type：指定数据类型，填全限定类名
- map标签：在`property`标签下写指定注入`Map`类型
- entry标签：在`map`标签下使用，指定具体map的值
    - key：指定key值
    - key-ref：指定key值，类型是IOC管理的Bean，不能和key同时使用
    - value：指定value值，基本类型
    - value-ref：指定value值为IOC管理Bean，不能喝value同时使用
    - value-type：指定value的类型，填全限定类名
- props标签：在`property`标签下写指定注入`Properties`类型
    - value-type：指定数据类型，填全限定类名
- prop标签：在`props`标签下使用，指定具体Properties的值
    - key：指定Properties的key
    - 标签包裹的数值为Properties的值

**Spring依赖注入例子**
```xml
<bean id="demo2" class="com.wjy.pojo.DemoDI2"/>
<bean id="demo" class="com.wjy.pojo.DemoDI">
    <!--构造器注入：-->

    <!--参数索引方式注入 不方便使用-->
    <!--<constructor-arg index="0" value="id1"/>-->
    <!--<constructor-arg index="1" value="1"/>-->
    <!--<constructor-arg index="2" value="false"/>-->
    <!--<constructor-arg index="3" ref="demo2"/>-->

    <!--参数名称方式注入-->
    <constructor-arg name="id" value="id2"/>
    <constructor-arg name="num" value="2"/>
    <constructor-arg name="aBoolean" value="true"/>
    <constructor-arg name="di" ref="demo2"/>


    <!--Set方法注入：-->

    <!--字符串类型注入-->
    <property name="setId" value="id3"/>
    <!--Boolean类型注入-->
    <property name="setBoolean" value="true"/>
    <!--Number类型注入-->
    <property name="setNum" value="23"/>
    <!--Bean类型注入-->
    <property name="setDI" ref="demo2"/>
    <!--String数组类型注入-->
    <property name="strs">
        <array value-type="java.lang.String">
            <value>str1</value>
            <value>str2</value>
            <value>str3</value>
        </array>
    </property>
    <!--List类型注入-->
    <property name="list">
        <list>
            <value>list1</value>
            <value>list2</value>
            <value>list3</value>
        </list>
    </property>
    <!--Set类型注入-->
    <property name="set">
        <set>
            <value>set1</value>
            <value>set2</value>
            <value>set3</value>
        </set>
    </property>
    <!--Map类型注入-->
    <property name="map">
        <map>
            <entry key="key1" value="value1"/>
            <entry key="key2" value="value2"/>
            <entry key="key3" value="value3"/>
        </map>
    </property>
    <!--Properties类型注入-->
    <property name="properties">
        <props>
            <prop key="key1">pro1</prop>
            <prop key="key2">pro2</prop>
            <prop key="key3">pro3</prop>
        </props>
    </property>
</bean>
```
```java
public class DemoDI2 {
}
public class DemoDI {
    private String id;
    private int num;
    private boolean aBoolean;
    private DemoDI2 di;

    public DemoDI(String id, int num, boolean aBoolean, DemoDI2 di) {
        this.id = id;
        this.num = num;
        this.aBoolean = aBoolean;
        this.di = di;
    }

    private String setId;
    private int setNum;
    private boolean setBoolean;
    private DemoDI2 setDI;

    private Map map;
    private String[] strs;
    private List list;
    private Set set;
    private Properties properties;

    ...... 省略set方法
}
```

### XML和注解混合配置

**什么类使用注解，什么类使用xml注入**
1. 项目开发，使用注解方式
2. 第三jar的类使用xml配置

#### 整合步骤

1. 使用`@Component,@Service,@Repository`注解指定需要IOC容器管理类
2. 使用`@Autowired`注解指定属性自动注入，根据类型；
3. 配置文件中配置扫描注解包`<context:component-scan base-package="com.wjy"/>`
4. 启动项目

### 纯注解配置

Spring纯注解开发，只是将xml配置项移到注解配置，我们只需要将xml和注解一一对应迁移即可

#### 常用注解说明

| 注解 |  对应XML标签 | 说明 |
|---|---|---|
|`表示此类交给SpringIOC容器管理注解`|
|@Component| \<bean/>|表示此类交给SpringIOC容器管理|
|@Controller|\<bean/>|组合注解（组合了@Component注解），应用在MVC层（控制层）,DispatcherServlet会自动扫描注解了此注解的类，然后将web请求映射到注解了@RequestMapping的方法上。|
|@Service|\<bean/>|	组合注解（组合了@Component注解），应用在service层（业务逻辑层）|
|@Reponsitory|\<bean/>|	组合注解（组合了@Component注解），应用在dao层（数据访问层）|
|`表示从IOC容器中获取Bean注解`|
|@Autowired|\<property/>|Spring提供的工具（由Spring的依赖注入工具（BeanPostProcessor、BeanFactoryPostProcessor）根据类型自动注入。）|
|@Qualifier|/|`@Autowired`的辅助注解，帮助确认唯一Bean|
|@Resource|\<property/>|JSR-250提供的注解，注入属性使用，`JDK11删除`不建议使用|
|`以下是迁移配置文件，在配置类中使用的注解`|
|@Configuration|/|声明当前类是一个配置类（相当于一个Spring配置的xml文件）|
|@ComponentScan|\<context:component-scan/>|自动扫描指定包下所有使用@Service,@Component,@Controller,@Repository的类并注册|
|@PropertySource|\<context:property-placeholder/>|指定引入外部属性资源文件|
|@Value|/|指定从外部资源文件获取数据`@Value("${jdbc.url}")`|
|@Import|\<import/>|指定引入其他配置类|
|@Bean|\<bean/>|注解在方法上，声明当前方法的返回值为一个Bean。返回的Bean对应的类中可以定义init()方法和destroy()方法，然后在@Bean(initMethod=“init”,destroyMethod=“destroy”)定义，在构造之后执行init，在销毁之前执行destroy。|

#### 配置

##### 项目内类交给IOC容器管理和获取
```java
// 指定数据访问层，交给IOC容器管理
@Repository("accountDao")
public class JdbcAccountDaoImpl implements AccountDao {

    // 根据类型注入实例
    @Autowired
    private ConnectionUtils connectionUtils;
}
// 指定业务Service层交给IOC容器管理
@Service("transferService")
public class TransferServiceImpl implements TransferService {

    // 根据类型注入实例
    @Autowired
    // 对于注入类型有多实现的，使用此注解确定唯一，参数为id
    @@Qualifier("accountDao")
    private AccountDao accountDao;
}
// 指定业务Controller层交给IOC容器管理
@Controller("testController")
public class TestController {

    // 根据类型注入实例
    @Autowired
    private TransferService transferService;
}
// 指定其他类，交给IOC容器管理
@Component("transactionManager")
public class TransactionManager {
    ......
}
```

##### 配置文件迁移
```java
// 指定类为配置类
@Configuration
// 指定扫描包
@ComponentScan({"com.wjy"})
// 引入外部资源文件
@PropertySource({"classpath:jdbc.properties"})
// @Import({OtherConfig.class}) 引入其他配置类
public class SpringConfig {

    // 读取外部资源文件数据
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    // 表示方法返回值交给IOC容器管理,指定id
    @Bean("dataSource")
    public DataSource getDataSource() {
        final DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
```

#### 启动方式

##### JavaSE项目
```java
final ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
final AccountDao accountDao = (AccountDao) context.getBean("accountDao");
System.out.println(accountDao);
```

##### JavaWeb项目
修改`WEB-INF/web.xml`
```xml
<!--告诉ContextLoaderListener，我们使用注解启动-->
<context-param>
<param-name>contextClass</param-name>
<param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
</context-param>
<!--指定spring配置类路径-->
<context-param>
<param-name>contextConfigLocation</param-name>
<param-value>com.wjy.SpringConfig</param-value>
</context-param>
<!--使用spring监听器启动IOC容器-->
<listener>
<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

## Spring-IOC高级特性

### 懒（延迟）加载（Lazy-Init）
ApplicationContext 容器的默认行为是在启动服务器时将所有 singleton bean 提前进行实例化。
这样如果项目比较大，Spring管理的类比较多，会造成每次启动项目耗时比较长的问题，这里我们就需要延迟加载特性；
延迟加载：指程序启动时不会默认实例化类，只有在被管理的bean使用的时候才会实例化；

#### 使用场景
1. 开启延迟加载一定程度提高容器启动和运转性能
2. 对于不常使用的 Bean 设置延迟加载，这样偶尔使用的时候再加载，不必要从一开始该 Bean 就占用资源。

#### 启用延迟加载

**XML方式：**
- 全局配置：
    ```xml
    <beans default-lazy-init="true">
    <!-- no beans will be eagerly pre-instantiated... -->
    </beans>
    ```
    这样配置后，所有Bean都开启延迟加载特性
- 指定Bean配置
    ```xml
    <bean id="testBean" calss="com.wjy.LazyBean" lazy-init="true" />
    ```
**注解方式：**
- 全局配置：
    ```java
    // 指定类为配置类
    @Configuration
    // 指定扫描包
    @ComponentScan({"com.wjy"})
    // 引入外部资源文件
    @PropertySource({"classpath:jdbc.properties"})
    // 指定全局懒加载实例化Bean
    @Lazy
    public class SpringConfig {
    }
    ```
    在Spring配置类上添加`@Lazy`注解，即可开启全局懒加载
- 指定类配置：
    ```java
    @Component("transactionManager")
    @Lazy
    public class TransactionManager {
        ...... 省略代码
    }
    ```
    在指定类上添加`@Lazy`注解，即可开启懒加载

### FactoryBean和BeanFactory

**BeanFactory：** 是IOC容器的顶级接口，定义了IOC容器的基本功能，负责生产、管理和获取Bean；

**FactoryBean：** Spring管理的Bean有两种，一种是普通Bean，一种是工厂Bean（FactoryBean），FactoryBean可以自定义创建一个复杂Bean实例交给IOC容器管理；Spring框架的一些组件和其他框架整合Spring时会使用FactoryBean来实例化复杂Bean；

**额外话：** 其实指定Spring创建Bean有三种基础方式，`无参构造，静态方法，实例化方法`:
```xml
<!--方式一：无参构造方式（推荐）
在默认情况下，它会通过反射调用无参构造函数来创建对象。如果类中没有无参构造函数，将创建 失败。
-->
<!--<bean id="connectionUtils" class="com.wjy.utils.ConnectionUtils"/>-->

<!--另外两种方式，是为了将我们自己new的bean加入ioc容器管理-->
<!--方式二：静态方法-->
<!--<bean id="connectionUtils" class="com.wjy.factory.CreateBeanFactory" factory-method="getStaticConnectionUtils"/>-->
<!--方式三：实例化方法
此种方式和上面静态方法创建其实类似，区别是用于获取对象的方法不再是static修饰的了，而是 类中的一 个普通方法。
-->
<bean id="createFactory" class="com.wjy.factory.CreateBeanFactory"/>
<bean id="connectionUtils" factory-bean="createFactory" factory-method="getConnectionUtils"/>
```
其中，`静态方法，实例化方法`也可以实现自定义创建复杂Bean，那为什么还要加一个`FactoryBean`呢？
个人理解原因有这几个：
1. `静态方法，实例化方法`配置复杂，而`FactoryBean`配置和`无参构造`方式一样简单
2. 对于使用全注解开发项目，只能使用`FactoryBean`来自定义复杂Bean；

#### 如何使用

1. 创建自定义工厂Bean实现`FactoryBean`接口
    ```java
    public class CompanyFactoryBean implements FactoryBean<Company> {
        private String companyInfo; // 公司名称,地址,规模

        public void setCompanyInfo(String companyInfo) {
            this.companyInfo = companyInfo;
        }

        // 返回FactoryBean创建的Bean实例，如果isSingleton返回true，则该实例会放到Spring容器 的单例对象缓存池中Map
        @Override
        public Company getObject() throws Exception {
            // 模拟创建复杂对象Company
            Company company = new Company();
            String[] strings = companyInfo.split(",");
            company.setName(strings[0]);
            company.setAddress(strings[1]);
            company.setScale(Integer.parseInt(strings[2]));
            return company;
        }

        // 返回FactoryBean创建的Bean类型
        @Override
        public Class<?> getObjectType() {
            return Company.class;
        }
        
        // 返回作用域是否单例
        @Override
        public boolean isSingleton() {
            return true;
        }
    }
    ```

2. 添加配置指定IOC管理Bean
    ```xml
    <beans>
        <bean id="companyBean" class="com.wjy.factory.CompanyFactoryBean">
            <property name="companyInfo" value="测试，公司"/>
        </bean>
    </beans>
    ```
    或者使用注解方式`@Component`

3. 使用
    ```java
    // id前添加&获取工厂
    CompanyFactoryBean Factory = context.getBean("&companyBean");
    // 直接id获取工厂创建的类
    Company company = context.getBean("companyBean");
    ```

### 后置处理器
Spring提供了两种后处理bean的扩展接口，分别为 BeanPostProcessor 和
BeanFactoryPostProcessor，两者在使用上是有所区别的。

**BeanPostProcessor:** 用来处理`BeanFactory`实例化的`Bean`；
**BeanFactoryPostProcessor：** 用来拦截处理`BeanFactory`解析完配置后的`ConfigurableListableBeanFactory`其中包含所有配置的Bean的定义对象`BeanDefinition`,可以对他进一步处理；

这里我们之具体说一下`BeanPostProcessor`

该接口提供了两个方法，分别在Bean的初始化方法前和初始化方法后执行，具体这个初始化方法指的是 什么方法，类似我们在定义bean时，定义了init-method所指定的方法；

定义一个类实现了BeanPostProcessor，默认是会对整个Spring容器中所有的bean进行处理。如果要对 具体的某个bean处理，可以通过方法参数判断，两个类型参数分别为Object和String，第一个参数是每 个bean的实例，第二个参数是每个bean的name或者id属性的值。所以我们可以通过第二个参数，来判 断我们将要处理的具体的bean。

注意:处理是发生在Spring容器的实例化和依赖注入之后。

#### 实现方式：

创建自定义`Bean`实现`BeanPostProcessor`接口
```java
// 拦截实例化之后的对象（实例化了并且属性注入了）
@Component
public class MyBean implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("MyBeanPostProcessor  before init-method方法拦截处理");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("MyBeanPostProcessor  afterinit-method方法拦截处理");
        return bean;
    }
}
```
这样，在Bean实例化完成后，就可以执行`postProcessBeforeInitialization,postProcessAfterInitialization`方法了

#### 扩展
spring对Bean的声明周期提供了很多扩展，这里不一一细说，只贴一张图说明
![SpringBean的声明周期](https://img-blog.csdnimg.cn/20200905195917325.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA2ODk0NDA=,size_16,color_FFFFFF,t_70#pic_center)

## Spring-IOC源码分析

### Spring-IOC源码分析-整体执行流程

IoC容器是Spring的核心模块，是抽象了对象管理、依赖关系管理的框架解决方案。Spring 提供了很多 的容器，其中 BeanFactory 是顶层容器(根容器)，不能被实例化，它定义了所有 IoC 容器 必须遵从 的一套原则，具体的容器实现可以增加额外的功能，比如我们常用到的ApplicationContext，其下更具 体的实现如 ClassPathXmlApplicationContext 包含了解析 xml 等一系列的内容， AnnotationConfigApplicationContext 则是包含了注解解析等一系列的内容。

BeanFactory 容器继承体系：
![BeanFactory继承关系](https://img-blog.csdnimg.cn/20200906135445919.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA2ODk0NDA=,size_16,color_FFFFFF,t_70#pic_center)
通过其接口设计，我们可以看到我们一贯使用的 ApplicationContext 除了继承BeanFactory的子接口， 还继承了ResourceLoader、MessageSource等接口，因此其提供的功能也就更丰富了。

#### 核心加载方法`AbstractApplicationContext#refresh()`

AbstractApplicationContext类是spring项目内核心类，它实现了ApplicationContext接口，整合了`Listeners,Event,XXXProcessor`等特性;

其中`refresh()`是启动IOC容器的直接入口，里面定义了从加载配置，加载扩展，加载Listeners，实例化singletons类等方法：
```java
@Override
public void refresh() throws BeansException, IllegalStateException {
    synchronized (this.startupShutdownMonitor) {
        /**
         * 第一步:刷新前的预处理
         */
        prepareRefresh();

        /**
         * 第二步: 获取BeanFactory;默认实现是DefaultListableBeanFactory
         * 加载BeanDefition 并注册到 BeanDefitionRegistry
         */
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        /**
         * 第三步:BeanFactory的预准备工作(BeanFactory进行一些设置，比如context的类加 载器等)
         */
        prepareBeanFactory(beanFactory);

        try {
            /**
             * 第四步:BeanFactory准备工作完成后进行的后置处理工作
             */
            postProcessBeanFactory(beanFactory);

            /**
             * 第五步:实例化并调用实现了BeanFactoryPostProcessor接口的Bean
             */
            invokeBeanFactoryPostProcessors(beanFactory);

            /**
             * 第六步:注册BeanPostProcessor(Bean的后置处理器)
             */
            registerBeanPostProcessors(beanFactory);

            /**
             * 第七步:初始化MessageSource组件(做国际化功能;消息绑定，消息解析);
             */
            initMessageSource();

            /**
             * 第八步:初始化事件派发器
             */
            initApplicationEventMulticaster();

            /**
             * 第九步:子类重写这个方法，在容器刷新的时候可以自定义逻辑
             */
            onRefresh();

            /**
             * 第十步:注册应用的监听器。就是注册实现了ApplicationListener接口的监听器bean
             */
            registerListeners();

            /**
             * 第十一步:
             *      初始化所有剩下的非懒加载的单例bean
             *      初始化创建非懒加载方式的单例Bean实例(未设置属性)
             *      填充属性
             *      初始化方法调用(比如调用afterPropertiesSet方法、init-method方法)
             *      调用BeanPostProcessor(后置处理器)对实例bean进行后置处
             */
            finishBeanFactoryInitialization(beanFactory);

            /**
             * 第十二步:
             *      完成context的刷新。主要是调用LifecycleProcessor的onRefresh()方法，并且发布事件(ContextRefreshedEvent)
             */
            finishRefresh();
        } catch (BeansException ex) {
            if (logger.isWarnEnabled()) {
                logger.warn("Exception encountered during context initialization - " +
                        "cancelling refresh attempt: " + ex);
            }

            /**
             * 销毁已创建的单例以避免资源悬空
             */
            destroyBeans();

            /**
             * 重置“活动”标志。
             */
            cancelRefresh(ex);

            /**
             * 将异常传播给调用者。
             */
            throw ex;
        } finally {
            /**
             * 在Spring的核心中重置通用缓存，因为我们可能再也不需要单例bean的元数据了
             */
            resetCommonCaches();
        }
    }
}
```

### Spring-如何解决循环依赖：

#### 什么是循环依赖
循环依赖其实就是循环引用，也就是两个或者两个以上的 Bean 互相持有对方，最终形成闭环。比如A 依赖于B，B依赖于C，C又依赖于A。

**注意：** 这里不是函数的循环调用，是对象的相互依赖关系。循环调用其实就是一个死循环，除非有终结 条件。


Spring中循环依赖场景有: 

- 构造器的循环依赖(构造器注入)
- Field 属性的循环依赖(set注入)

其中，构造器的循环依赖问题无法解决，只能拋出 BeanCurrentlyInCreationException 异常，在解决
属性循环依赖时，spring采用的是提前暴露对象的方法。

#### spring循环依赖处理机制

- 单例 bean 构造器参数循环依赖(无法解决) 
    - 原因：构造参数注入时，对象还没有创建，无法获取对象引用
- prototype 原型 bean循环依赖(无法解决)
    - 原因：多例模式下，Bean创建后就不归IOC容器管理，无法注入其他对象中
- 单例bean通过setXxx或者@Autowired进行循环依赖

Spring 的循环依赖的理论依据基于 Java 的引用传递，当获得对象的引用时，对象的属性是可以延 后设置的，但是构造器必须是在获取引用之前。

Spring通过setXxx或者@Autowired方法解决循环依赖；

假设A依赖B，B依赖A：
1. 新建A，将引用放入三级缓存中
2. 发现A依赖B：
    1. 从一级缓存查询，没有
    2. 从二级缓存查询，没有
    3. 从三级缓存查询，没有
    4. 新建B,将B引用放入三级缓存中
        a. 发现依赖A
        b. 从一级缓存找，没有
        c. 从二级缓存找，没有
        d. 从三级缓存找到，返回注入
        e. 走B的剩余生命周期处理
    5. B新建完成，存入一级缓存（单例池）中
3. A注入B完成
4. A剩余生命周期处理完成，存入一级缓存中
![spring解决循环依赖](https://img-blog.csdnimg.cn/20200906191259766.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA2ODk0NDA=,size_16,color_FFFFFF,t_70#pic_center)

如果有三个或者四个循环引用，就一级一级初始化，都是从三级缓存中获取引用注入的；

#### 为什么使用三级缓存，而不是用二级缓存呢？

事实上，如果只是解决循环依赖问题，只有二级缓存就够用了，那为什么spring要使用三级缓存来处理循环依赖问题呢？
这里是因为，spring要确保，预先存储的引用（三级缓存中的），要和一级缓存中的引用一样，如果只是用二级缓存，对于那些，只是注入属性，没有其他额外处理的Spring Bean是没有问题的，它的引用就是一开始构造函数创建后的引用，但是对于那些，最终引用会变化的Spring Bean（比如：AOP生成的代理对象）就有大问题了，会出现，三级缓存中的引用（依赖注入的引用）和最终一级缓存中的引用不一样，导致从三级缓存中注入的属性，会丢失（AOP添加的）功能。
所以，Spring 使用三级缓存解决循环依赖，在从三级缓存注入到属性过程中，会预先（AOP部分处理）生成代理对象，然后注入属性，然后放入二级缓存（其他属性注入时可以直接从二级缓存取用同一个代理对象），最后在对象正常生命周期中会判断此对象是否已经生成过代理对象，如果生成过，则从二级缓存中取出，将前一个对象数据拷贝到代理对象中，再继续spring bean生命周期。
这样就保证了及解决了循环依赖问题，又保证了最后获取的spring bean是同一个。

## Spring AOP讲解

未实现AOP业务流程：
![未实现AOP业务流程](https://img-blog.csdnimg.cn/20200907182541749.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA2ODk0NDA=,size_16,color_FFFFFF,t_70#pic_center)
上图中红色部分，全是通用的横切逻辑代码，我们可以用AOP特性抽离横切代码。

实现AOP业务流程：
![实现AOP业务流程](https://img-blog.csdnimg.cn/20200907182337458.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA2ODk0NDA=,size_16,color_FFFFFF,t_70#pic_center)
上图中的XX切面，就是我们抽离的横切逻辑代码。

### Spring-AOP术语

| 术语 | 解释 |
|---|---|
| Joinpoint(连接点) | 指可以把增强代码，加入到业务主线中的点。从上图中可以看出，连接点指的就是方法（图中红色的方法名）。使用动态代理技术，可以方法执行前后切入增强代码。 |
| Pointcut(切入点) | 指Spring指定要切入增强代码的连接点。从上图中可以看出，判断访问权限拦截的三个方法既是连接点也是切入点，而register表现层方法只是连接点不是切入点。 |
| Advice(通知/增强) | 指提供增强功能的方法。由于增强可以在切入点前后或者异常捕获时执行，这里可以细分为：**前置通知**，**后置通知**，**异常通知**，**最终通知**，**环绕通知**。 |
| Target(目标对象) | 它指的是代理的目标对象。即被代理对象。 |
| Proxy(代理) | 它指的是一个类被AOP织入增强后，产生的代理类。即代理对象。 |
| Weaving(织入) | 指把增强应用到代理对象并创建新的代理对象的过程。spring采用动态代理织入，而AspectJ采用编译期织入和类装载期织入。 |
| Aspect(切面) | 它指定是增强的代码所关注的方面，把这些相关的增强代码定义到一个类中，这个类就是切面类。例如，事务切面，它里面定义的方法就是和事务相关的，像开启事务，提交事务，回滚事务等等，不会定义其他与事务无关的方法。 |

实际在Spring使用中，我们只会用到`Pointcut,Advice,Aspect`这几个概念：
- Aspect：切面，封装增强业务代码，指定增强代码在哪个切点的哪个方位执行
- Pointcut：切点，指定我们要在哪一个方法切入`Aspect`的增强功能
- Advice：增强（增强业务+执行方位），指定`Aspect`的功能在`Pointcut`方法的哪个方位（前、后、异常后、finally，环绕）进行切入

### Spring配置AOP

需要引入依赖包：如果有引入spring其他jar包中依赖aop模块这里可以不用引入
```xml
    <!--spring aop的jar包支持-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-aop</artifactId>
      <version>5.1.12.RELEASE</version>
    </dependency>

    <!--spring依赖第三方的aop框架aspectj的jar-->
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>1.8.13</version>
    </dependency>
```

我们分三种方式讲解配置

#### 纯XML配置AOP
Aspect切面类：
```java
public class LogUtils {

    /**
     * 业务逻辑开始之前执行
     */
    public void beforeMethod(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            System.out.println(arg);
        }
        System.out.println("业务逻辑开始执行之前执行.......");
    }

    /**
     * 业务逻辑结束时执行（无论异常与否）
     */
    public void afterMethod() {
        System.out.println("业务逻辑结束时执行，无论异常与否都执行.......");
    }

    /**
     * 异常时时执行
     */
    public void exceptionMethod() {
        System.out.println("异常时执行.......");
    }

    /**
     * 业务逻辑正常时执行，参数名称可以配置是指定`retVal`
     */
    public void successMethod(Object retVal) {
        System.out.println("业务逻辑正常时执行.......");
    }

    /**
     * 环绕通知 可替代其他四种通知，不推荐与其他四种通知同时使用。
     * 环绕通知 类似代理模式拦截方法
     *
     */
    public Object arroundMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("环绕通知中的beforemethod....");
        Object result = null;
        try{
            // 控制原有业务逻辑是否执行
            // result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        }catch(Exception e) {
            System.out.println("环绕通知中的exceptionmethod....");
        }finally {
            System.out.println("环绕通知中的after method....");
        }

        return result;
    }
}
```
```xml
<!--进行aop相关的xml配置,配置aop的过程其实就是把aop相关术语落地-->
<!--横切逻辑bean-->
<bean id="logUtils" class="com.wjy.utils.LogUtils"></bean>

<!--使用config标签表明开始aop配置,在内部配置切面aspect-->
<!--aspect = 切入点（锁定方法）+ 方位（锁定方法中的特殊时机）+ 横切逻辑 -->
<aop:config>

    <!-- 配置一个日志切面 -->
    <aop:aspect id="logAspect" ref="logUtils">
        <!--切入点锁定我们需要拦截的方法，使用aspectj语法表达式：
        返回值 类全限定名.方法(参数)

        返回值：可以用 * 代替任意返回值
        类全限定名：可以使用*代替任意包，或者代替任意类，使用..代替任意层级的包（零到多个层级任意名称的包）
        方法：可以使用*代替任意方法
        参数：可以使用*代替任意参数（必须有参数）使用..代替任意参数（可以没有参数）
        -->
        <aop:pointcut id="pt1" expression="execution(* com.wjy.service.impl.TransferServiceImpl.*(..))"/>

        <!--方位信息,pointcut-ref关联切入点-->
        <!--aop:before前置通知/增强-->
        <aop:before method="beforeMethod" pointcut-ref="pt1"/>
        <!--aop:after，最终通知，无论如何都执行-->
        <aop:after method="afterMethod" pointcut-ref="pt1"/>
        <!--aop:after-returnning，正常执行通知，returning：指定返回值注入的参数名称-->
        <aop:after-returning method="successMethod" returning="retVal" pointcut-ref="pt1"/>
        <!--aop:after-throwing，异常通知-->
        <aop:after-throwing method="exceptionMethod" pointcut-ref="pt1"/>

        <!--环绕通知配置，不推荐和以上四种同时使用-->
        <aop:around method="arroundMethod" pointcut-ref="pt1"/>
    </aop:aspect>
</aop:config>
```

#### XML和注解混合配置
类上添加注解：
```java
@Component
// 指定类为切面类
@Aspect
public class LogUtils {

    /**
     * 配置切入点
     */
    @Pointcut("execution(* com.wjy.service.impl.TransferServiceImpl.*(..))")
    public void pt1() {

    }

    /**
     * 业务逻辑开始之前执行
     */
    @Before("pt1()")
    public void beforeMethod(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            System.out.println(arg);
        }
        System.out.println("业务逻辑开始执行之前执行.......");
    }

    /**
     * 业务逻辑结束时执行（无论异常与否）
     */
    @After("pt1()")
    public void afterMethod() {
        System.out.println("业务逻辑结束时执行，无论异常与否都执行.......");
    }

    /**
     * 异常时时执行
     */
    @AfterThrowing("pt1()")
    public void exceptionMethod() {
        System.out.println("异常时执行.......");
    }

    /**
     * 业务逻辑正常时执行
     */
    @AfterReturning(value = "pt1()", returning = "retVal")
    public void successMethod(Object retVal) {
        System.out.println("业务逻辑正常时执行.......");
    }

    /**
     * 环绕通知 可替代其他四种通知，不推荐与其他四种通知同时使用。
     * 环绕通知 类似代理模式拦截方法
     */
    // 指定为环绕通知，由于不推荐和以上四种同时使用，这里注释掉
    /*@Around("pt1()")*/
    public Object arroundMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("环绕通知中的beforemethod....");
        Object result = null;
        try {
            // 控制原有业务逻辑是否执行
            // result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } catch (Exception e) {
            System.out.println("环绕通知中的exceptionmethod....");
        } finally {
            System.out.println("环绕通知中的after method....");
        }
        return result;
    }
}
```
XML删除所有关于AOP配置添加自动AOP代理配置
```xml
    <!--
        开启aop注解驱动
        proxy-target-class:true强制使用cglib
    -->
    <aop:aspectj-autoproxy/>
```

#### 纯注解配置
Aspect类按照`XML和注解混合配置`方式注解配置。

在配置类上面添加启动Aspect自动代理注解
```java
@Configuration
@EnableAspectJAutoProxy
public class SpringConfig {
    ......
}
```

#### Expression表达式简单说明

配置切点的时候，spring使用`expression`表达式定位方法，这里简单说明一下如何写：

- 唯一定位：
    ```expression
    expression="execution(public void com.wjy.service.impl.TransferServiceImpl.transfer(String,String,int))"
    ```
    要将方法作用域，返回值类型，类全限定名，方法名，方法参数类型列表全部写上
- 批量定位：
    批量定位就是，有些地方可以用`*`或者`.`来表示任意名称，`*`和`.`的区别是，`*`最少有一个，`.`可以一个都没有
    方法作用域可以省略，其他可以用`*`,`.`替换
    ```expression
    expression="execution(* com.wjy.service..*.*(..))
    ```
    上面表示，拦截`com.wjy.service`包及任意层级子包下任意类的任意方法，参数可有可无，返回值任意类型

#### SpringAOP实现流程
这里就不分析源码了，只是简单总结一下。

1. 解析配置文件（XML/注解）获取AOP配置信息
2. SpringBean声明周期，后置处理器的时候，会将所有AOP配置织入到SpringBean中生成代理对象

好了，这样最后注入的SpringBean就是拥有AOP增强的代理对象了。

**注意：** AOP增强织入过程中使用了动态代理技术`cglib`或者`jdk`,Spring会自动根据当前Bean是否实现接口，动态选择，如果实现接口使用`jdk`，如果没有使用`cglib`，也可以配置强制使用`cglib`（`proxy-target-class="true"`,这也是为什么Spring官方建议`@Transaction`使用在类或者方法上，因为如果加载接口上，有强制使用`cglib`类代理，那么是获取不到`@Transaction`注解信息的）

## Spring声明式事务的支持

**编程式事务：** 在业务代码里面添加事务控制代码，硬编码控制事务的方法叫做编程式事务；

**声明式事务：** 通过XML或者注解配置的方式达到事务控制的目的，叫做声明式事务；

### 事务介绍

#### 什么是事务？

事务（Transaction），在计算机术语中，指将多个单独的操作组合在一起，其中一个操作执行失败，所有执行的操作全部撤回，只有当所有操作全部执行成功，才算成功，这多个操作组合成的整体叫做事务；

例如：A给B转账100元，分两个操作，A减100元，B加100元，只有当这两个操作都执行成功，这个转账事务才算成功；如果B加100元失败，A则必须回退减100元操作。

#### 事务的四大特性

| 特性 | 描述 |
|---|---|
|原子性（Atomicity）|指事务是一个不可分割的单位，事务中的**操作**要么全部成功，要么全部失败|
|一致性（Consistency）|事务执行前后，**数据**从一个一致性状态变为另一个一致性状态（数据按照预期变化）<br/>例如：A给B转账100元事务<br/>事务前状态：A有1000元，B有1000元；<br/>事务后状态：A有900元，B有1100元；|
|隔离性（Isolation）|在多个事务同时执行过程中，各个**事务之间的影响**；这里又分为四个隔离级别，我们后面会讲到；<br/>例如：脏读问题<br/>1. A发起事务1，查询账户有1000元，此时事务1未关闭；<br/>2. B发起事务2，给A转账100元，此时事务2也未关闭；<br/>3. A在事务1中，再次查询账户，发现有1100元，读到了事务2未提交的数据；<br/>这是脏读问题。|
|持久性（Durability）|指事务一旦提交成功，他对**数据的改变是永久性的**，接下来即使服务器崩溃关闭，也不会对数据有任何影响。|

#### 事务的隔离级别

##### 事务并发问题

1. 脏读：
    一个事务，读取到另一个事务未提交的数据
2. 不可重复读：
    一个事务读取到另一个事务已经提交的**Update**数据（事务内前后查询同一个数据不一致）
    - 场景：
        a. A开启事务1，查询账户有1000元，未关闭事务1
        b. B开启事务2，给A转账100元，提交事务2
        c. A在事务1内再次查询账户，有1100元，和第一次查询数据不一致；
3. 幻读（虚读）：
    一个事务读取到另一个事务已经提交的**Insert**和**Delete**数据（事务内前后统计同一条件数据条数不同）
    - 场景：
        a. 事务1查询员工总数有10人
        b. 事务2新增2个员工，提交事务2
        c. 事务1再次查询员工有12人

##### 数据库定义四种隔离级别

隔离级别依次提升，数据安全性依次提高，数据库查询效率依次降低

1. 读未提交（read-uncommitted）
    在一个事务中，可以读取其他事物未提交的数据变化；这种能读取其他事物未提交的数据变化的现象叫脏读，**生产环境请勿使用**。
2. 读已提交（read-committed）
    在一个事务中，可以读取其他事物已提交的数据变化；由于同一事物中两次读取可能得到不一样的结果，所以也交不可重复读。
3. 可重复读（repeatable-read）
    Mysql的默认隔离级别，在一个事务中，可以重复读取到事务刚开始时看到的数据，并不会因为其他事物提交数据而改变，避免了脏读，不可重复读；但是他无法解决幻读问题。
4. 串行化（serializable）
    这是最高的隔离级别，它强制事务串行执行，避免了幻读；它会在每一行数据上加锁，，多以会导致大量超时和锁争用问题。

| 隔离级别 | 读数据一致性 | 脏读 | 不可重复的 | 幻读|
|---|---|---|---|---|
|未提交读|最低级别，只保证不读取物理上损坏的数据|有|有|有|
|已提交读|语句级|无|有|有|
|可重复读|事务级|无|无|有|
|串行化|最高级别，事务级|无|无|无|

**扩展：**
关于Mysql使用了`快照读，当前读`,`MVCC,Next-Lock,GAP锁`等思想实现了可重复读

#### 事务的传播行为

事务的控制往往是在程序的`Service`层，而`Service`层的方法我们有时候，需要互相调用，而每个`Service`方法都可能有自己的事务，这时就需要事务传播行为的概念。

以下我们讲解一下`Spring`定义的事务传播
| 事务定义 | 说明 |
|---|---|
|**PROPAGATION_REQUIRED**|如果当前没有事务，就新建一个事务，如果已经存在一个事务中， 加入到这个事务中。**这是最常⻅的选择。**|
|**PROPAGATION_SUPPORTS**|支持当前事务，如果当前没有事务，就以非事务方式执行。|
|PROPAGATION_MANDATORY|使用当前的事务，如果当前没有事务，就抛出异常。|
|PROPAGATION_REQUIRES_NEW|新建事务，如果当前存在事务，把当前事务挂起。|
|PROPAGATION_NOT_SUPPORTED|以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。|
|PROPAGATION_NEVER|以非事务方式执行，如果当前存在事务，则抛出异常。|
|PROPAGATION_NESTED|如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与PROPAGATION_REQUIRED类似的操作。|

**注意：**
- `PROPAGATION_REQUIRES_NEW`和`PROPAGATION_NESTED`是独立事务，但是如果被调用方法事务回滚后异常抛出，而外部方法没捕获还是会导致外部方法事务回滚；只有try_catch处理后外部方法事务才不会回滚
- `PROPAGATION_NESTED`作为事务嵌套，同时兼具了required,requires_new的特性。
    - 当外层事务回滚时`PROPAGATION_NESTED`与`PROPAGATION_REQUIRED`同时内层全部进行回滚
    - 当内层事务回滚时，`PROPAGATION_NESTED`与`PROPAGATION_REQUIRES_NEW`不影响外层事务

### Spring声明式事务配置

spring声明式事务管理顶层接口：
```java
public interface PlatformTransactionManager {

    /**
     * 获取事务状态信息
     */
	TransactionStatus getTransaction(@Nullable TransactionDefinition definition) throws TransactionException;

    /**
     * 提交事务
     */
	void commit(TransactionStatus status) throws TransactionException;

    /**
     * 回滚事务
     */
	void rollback(TransactionStatus status) throws TransactionException;

}
```
**作用：**

此接口是Spring的事务管理器核心接口。Spring本身并不支持事务实现，只是负责提供标准，应用底层支持什么样的事务，需要提供具体实现类。此处也是策略模式的具体应用。在Spring框架中，也为我们 内置了一些具体策略，例如:`DataSourceTransactionManager`，`HibernateTransactionManager`等。(HibernateTransactionManager事务管理器在 spring-orm-5.1.12.RELEASE.jar中)

#### 纯XML方式配置

1. 导入依赖
    ```xml
    <!--引入spring声明式事务相关-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-tx</artifactId>
        <version>5.1.12.RELEASE</version>
    </dependency>
    ```
2. XML配置
    ```xml
    <!--spring声明式事务配置，声明式事务就是配置一个aop-->
    <!--横切逻辑-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <constructor-arg name="dataSource" ref="dataSource"></constructor-arg>
    </bean>

    <!--配置事务增强-->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <!--定制事务细节，传播行为、隔离级别等-->
        <tx:attributes>
            <!--
            一般性配置
            这里建议项目中约定好前缀，根据前缀配置
            read-only：是否只读事务
            propagation：事务传播行为
            isolation：事务隔离级别，DEFAULT：使用数据库配置的隔离级别
            timeout：事务超时，-1不设置
            rollback-for：指定遇到哪些异常回滚，默认是RuntimeException
            -->
            <tx:method name="*" read-only="false" propagation="REQUIRED" isolation="DEFAULT" timeout="-1" rollback-for="java.lang.Exception"/>
            <!--针对查询的覆盖性配置-->
            <tx:method name="query*" read-only="true" propagation="SUPPORTS" rollback-for="java.lang.Exception"/>
        </tx:attributes>
    </tx:advice>

    <!--配置事务AOP切面-->
    <aop:config>
        <!--advice-ref 指向事务增强=横切逻辑+方位-->
        <aop:advisor advice-ref="txAdvice" pointcut="execution(* com.wjy.service.impl.TransferServiceImpl.*(..))"/>
    </aop:config>
    ```

#### XML注解结合配置
XML配置事务管理器
```xml
<!--spring声明式事务配置，声明式事务就是配置一个aop-->
<!--横切逻辑-->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <constructor-arg name="dataSource" ref="dataSource"></constructor-arg>
</bean>
<!--声明式事务的注解驱动-->
<tx:annotation-driven transaction-manager="transactionManager"/>
```
Java代码注解指定切入点
```java
@Service("transferService")
// 这里指定，类内所有public方法支持事务
@Transactional(rollbackFor = Exception.class)
public class TransferServiceImpl implements TransferService {

    @Autowired
    @Qualifier("accountDao")
    private AccountDao accountDao;

    // 这里指定具体方法支持事务，会覆盖类上注解配置
    @Transactional(transactionManager = "transactionManager",rollbackFor = Exception.class,propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,readOnly = false)
    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {

            Account from = accountDao.queryAccountByCardNo(fromCardNo);
            Account to = accountDao.queryAccountByCardNo(toCardNo);

            from.setMoney(from.getMoney()-money);
            to.setMoney(to.getMoney()+money);

            accountDao.updateAccountByCardNo(to);
            int c = 1/0;
            accountDao.updateAccountByCardNo(from);

    }
}
```

#### 纯注解配置

1. 删除XML配置
2. 注解配置类添加启用事务注解配置
```java
@Configuration
// 启动事务注解
@EnableTransactionManagement
public class SpringConfig {

    // 配置数据源
    @Bean("dataSource")
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("");
        dataSource.setUrl("");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }

    // 配置事务管理器
    @Bean("transactionManager")
    public PlatformTransactionManager getTransactionManager() {
        final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(getDataSource());
        return transactionManager;
    }
}
```
3. 需要事务管理的`Service`层方法添加`@Transactional`注解

#### 注解如何配置全局事务增强
```java
@Aspect
@Configuration
public class TransactionalAopConfig {

    /**
     * 配置方法过期时间，默认-1,永不超时
     */
    private final static int METHOD_TIME_OUT = 5000;

    /**
     * 配置切入点表达式
     */
    private static final String POINTCUT_EXPRESSION = "execution(* com.wjy.service..*.*(..))";

    /**
     * 事务管理器
     */
    @Resource
    private PlatformTransactionManager transactionManager;


    @Bean
    public TransactionInterceptor txAdvice() {
        /*事务管理规则，声明具备事务管理的方法名**/
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        /*只读事务，不做更新操作*/
        RuleBasedTransactionAttribute readOnly = new RuleBasedTransactionAttribute();
        readOnly.setReadOnly(true);
        readOnly.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        /*当前存在事务就使用当前事务，当前不存在事务就创建一个新的事务*/
        RuleBasedTransactionAttribute required = new RuleBasedTransactionAttribute();
        /*抛出异常后执行切点回滚,这边你可以更换异常的类型*/
        required.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        /*PROPAGATION_REQUIRED:事务隔离性为1，若当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值*/
        required.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        /*设置事务失效时间，如果超过5秒，则回滚事务*/
        required.setTimeout(METHOD_TIME_OUT);
        Map<String, TransactionAttribute> attributesMap = new HashMap<>(30);
        //设置增删改上传等使用事务
        attributesMap.put("save*", required);
        attributesMap.put("remove*", required);
        attributesMap.put("update*", required);
        attributesMap.put("batch*", required);
        attributesMap.put("clear*", required);
        attributesMap.put("add*", required);
        attributesMap.put("append*", required);
        attributesMap.put("modify*", required);
        attributesMap.put("edit*", required);
        attributesMap.put("insert*", required);
        attributesMap.put("delete*", required);
        attributesMap.put("do*", required);
        attributesMap.put("create*", required);
        attributesMap.put("import*", required);
        //查询开启只读
        attributesMap.put("select*", readOnly);
        attributesMap.put("get*", readOnly);
        attributesMap.put("valid*", readOnly);
        attributesMap.put("list*", readOnly);
        attributesMap.put("count*", readOnly);
        attributesMap.put("find*", readOnly);
        attributesMap.put("load*", readOnly);
        attributesMap.put("search*", readOnly);
        source.setNameMap(attributesMap);
        return new TransactionInterceptor(transactionManager, source);
    }

    /**
     * 设置切面=切点pointcut+通知TxAdvice
     */
    @Bean
    public Advisor txAdviceAdvisor() {
        /* 声明切点的面：切面就是通知和切入点的结合。通知和切入点共同定义了关于切面的全部内容——它的功能、在何时和何地完成其功能*/
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        /*声明和设置需要拦截的方法,用切点语言描写*/
        pointcut.setExpression(POINTCUT_EXPRESSION);
        /*设置切面=切点pointcut+通知TxAdvice*/
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }

}
```

#### 注意：

1. `@Transactional`只能被应用到public方法上, 对于其它非public的方法,如果标记了@Transactional也不会报错,但方法没有事务功能。
2. `@Transactional`默认回滚异常是`RuntimeException`,对于非`RuntimeException`异常如`Exception`则不会回滚，需要手动指定`@Transactional(rollbackFor = Exception.class)`，`Exception`才能回滚。
3. `@Transactional`注解可以被应用于接口定义和接口方法、类定义和类的public方法上。**但是**Spring团队的建议是你在具体的类（或类的方法）上使用 @Transactional 注解，而不要使用在类所要实现的任何接口上。
    你当然可以在接口上使用 @Transactional 注解，但是这将只能当你设置了基于接口的代理时它才生效。因为注解是 不能继承 的，这就意味着如果你正在使用基于类的代理时，那么事务的设置将不能被基于类的代理所识别，而且对象也将不会被事务代理所包装（将被确认为严重的）。因 此，请接受Spring团队的建议并且在具体的类上使用 @Transactional 注解。

    