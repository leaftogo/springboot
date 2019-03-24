

[TOC]



# 本节课目的

* 了解springboot的特性
* 了解如何使用springboot构建项目
* 一些开发时需要了解的东西
* 了解Mybatis以及如何在springboot项目中集成Mybatis
* 如何集成其他的东西(从百度 or google开始，以druid为示例)

# 建立springboot项目

## 使用SpringInitializer

1. 选择依赖项
2. 开始~~cv~~写代码
3. run

## 文件基本结构

__以下对文件夹的描述如果以"你可以"的方式开头，表示这个文件夹是你自由创建命名的__

__如果没有则表示这个文件夹是springinitializer创建的or不可缺少的结构文件夹__

* main
  * java
    * com
      * example
        * demo   （所有的代码文件，在这里__自由__建立文件夹以区分java类）
          * Controller （你可以这样命名表示这里面的类都是Controller）
          * Service  （同Controller）
          * Entity （你可以这样命名表示这里面是Mybatis所需要的实体类，后面解释）
          * Mapper  （你可以这样命名表示这里面是Mybatis所需要的映射类，后面解释）
          * DemoApplication（__一定要有__这个文件，关键是里面的类注解@SpringBootApplication）
          * 其他的文件(夹)，取决于个人
  * resources  （所有的静态文件和配置文件）
    * static (除html文件以外的其他静态文件:js,css,图片)(这里的文件不能过大，static中的文件会被打包) 
      * js (你可以这样命名表示这里面是js文件)
      * css (你可以这样命名表示这里面是css文件)
    * templates (所有的html文件)
    * application.properties  (配置文件)
    * application.yml    (配置文件，和properties区别在于配置的格式不一样)
    * 其他的文件(夹)或配置文件，取决于个人
* test  (单元测试文件夹)

# Springboot的一些基本类和注解

## Controller类：@Controller和@RestController

* 列出和Controller类相关的一些注解
  * @RequestMapping
  * @RequestParam
  * @RequestBody
  * @ResponseBody

* 直接看示例代码讲结构

* @Controller和@RestController都作用于类，表示这个类是一个Controller。

* @RestController等于@Controller + @ResponseBody

* 通过@RequestMapping注解来表示接口的访问方式：

  ```
  @RequestMapping(value = "/user",method = RequestMethod.GET)
  //value参数表示的是这个接口的url，method参数表示的是接口访问的方法，这里是get
  public String user(){
      return "user"; 
  }
  
  //post
  @RequestMapping(value = "/add",method = RequestMethod.POST)
  public String add(){
      
  }
  ```

  

## @Service

* @Service注解标记的类将会在项目初始化的时候被Spring扫描并被尝试实例化加入容器

## @Component 

* @Component注解标记的类也同样会被Spring扫描并被实例化加入容器
* @Component @Controller 和@Service性质差不多。
  * @Service用于标注业务层组件 
  * @Controller用于标注控制器
  * 当一个组件不好归类时可以用@Component标记

## Configuration类：@Configuration

* 被@Configuration注解标记的类成Configuration类
* @Configuration注解的配置类有如下要求
  * 配置类不可以被__final__关键字修饰
  * 配置类不可以是匿名类
* springboot会尝试在Configuration类中找@Bean标记的方法，并运行这个方法

## @Bean

* @Bean作用于方法

  ```
  @Bean
  public JedisPoolConfig getRedisConfig() {
      JedisPoolConfig config = new JedisPoolConfig();
      return config;
  }
  ```

* 被@Bean注解的方法，Spring将会认为这个方法会返回一个对象，并且在项目初始化的时候调用一次这个方法，获得返回的对象并将其存入自己的容器中。该对象的默认名字为该方法名。你也可以为其指定name:

  ```
  @Bean(name="xiaoming123456")
  public JedisPoolConfig getRedisConfig() {
      JedisPoolConfig config = new JedisPoolConfig();
      return config;
  }
  ```

* 一般来说，通过@Bean你可以自己生成一些你需要使用的对象，将其交给Spring管理。你不一定会直接访问这些对象，也有一部分是你自己定义的对象，如果你希望在其他地方直接访问它们，可以通过如下方法来实现

  ```
  ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
  Object bean1 = context.getBean("BeanTest");
  ```

* Spring仅会调用一次被@Bean注解的方法。

# Springboot的特性

## 依赖注入

代码示例：（注入了UserInfoMapper类）

```
public class UserService {

    @Autowired
    UserInfoMapper userInfoMapper;

    //register()需要使用到UseInfoMapper类的add()，这里使用@Autowired注入UseInfoMapper
    public boolean register(String openid,String username,String user_pic_url){
        if(hasOpenid(openid)){
            return true;
        }else{
            userInfoMapper.add(new UserInfoEntity(openid,username,user_pic_url,"",0));
            return false;
        }
    }
}
```

### @Autowried注解

* 这个注解作用于一个对象，你不需要对它修饰的对象赋值。
* 使用@Autowried注入的对象是单例的
* 你可以用它在一个类中注入任意数量的其他类，比如像下面这样@Autowried的嵌套：__一个注入了其他类的类__(UserService类中被注入了UserInfoMapper类)__也可以被注入进其他的类__(注入进RegisterController类)

```
public class UserService {

    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    WeiXinService weiXinService;

    //register()需要使用到UseInfoMapper类的add()，这里使用@Autowired注入UseInfoMapper
    public boolean register(String openid,String username,String user_pic_url){
        if(hasOpenid(openid)){
            return true;
        }else{
            userInfoMapper.add(new UserInfoEntity(openid,username,user_pic_url,"",0));
            return false;
        }
    }
    
    public boolean hasOpenid(String openid){
        List<UserInfoEntity> list = userInfoMapper.findEntityByOpenid(openid);
        if(list == null || list.size() == 0) return false;
        else return true;
    }
}

//在RegisterController类中注入了UserService类
public class RegisterController{

    @Autowried
    UserService userService;
    
    public String register(HttpServletRequest request){
        String openid = request.getParameter("openid");
        String username = request.getParameter("username");
        String user_pic_url = request.getParameter("user_pic_url");
        if(userService.register(openid,username,user_pic_url)){
            return "register success";
        }else{
            return "register false";
        }
    }
}
```

* 嵌套实现逻辑：在包扫描的时候建立好各个类之间的注入关系，然后从任意一个类开始尝试注入，即尝试实例化它所含有的被注入的类，有以下规则：

  ```
  1. 任何类被单例实例化的时候首先从对象仓库中查找这个类对象，假如该类被预先实例化过，将这个存在仓库中的对象返回。如果没有，进入2
  （这实现了单例注入）
  2. 任何类被单例实例化的时候都要尝试扫描自己以查看自己是否有@Autowired注入的类，如果有，首先尝试实例化这个内含的注入的类。
  3. 如果内含的注入类对象也都被实例化(或者获得对象仓库中的对象)了，则尝试实例化本体，并将生成的对象存入对象仓库，并返回这个对象。
  （因为一个类可能会在不同的地方被注入多次，所以需要存进仓库，这保证了下一次注入是单例的）
  ```



## 内嵌servlet容器

* 原来的部署流程：

  * 打war包  -->    放进tomcat的webapp   -->  等待tomcat定时扫描webapp包发现这个新的war包并解压部署。

* 内嵌tomcat：自带的web服务器

  * 需要你在application.yml或者application.properties中为它指定：端口，编码，根路由。

  * yml配置示例 (yml和properties的格式下面会讲)

  * ```
    server:
      tomcat:
        uri-encoding: UTF-8
      port: 8080
      servlet:
        context-path: /welcome
    ```

* 在如下依赖中包含了内嵌的tomcat容器依赖

  ```
  <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  ```

## 请求参数注入

你可以在一个Controller中的方法里指定任意数量的任意的特殊参数，springboot会在一次请求到来时，自动将这些参数装配好并调用你的方法将它们传进来。

### 注入原生Servlet开发中的一些类：HttpSession，HttpServletRequest，HttpServletResponse

```
@RequestMapping(value = "/test")
public Object search(HttpSession session,HttpServletRequest,request){
    String username = session.getAttribute("username");//像原生servlet开发一样使用session对象
    
    String password = request.getParameter("password");//拿到一个url中的参数或者是请求体中的key-value参数
}
```

### 使用@RequestParam注入你指定key的url参数

```
@RequestMapping(value = "/test")
public Object search(@RequestParam("verify_code") String verify_code){
        
}
```

传参时url为

```
http://ip/uri?verify_code=xx
```

__注意__：

* url中的参数的key必须和@RequestParam中标注的一样，@RequestParam注解之后的声明变量的变量名随意，但最好一样，避免混淆
* @RequestParam只能接受url参数，请求体中的参数请使用@RequestBody

### 使用@RequestBody自动注入json

```
@RequestMapping(value = "/test")
public Object search(@RequestBody JSONObject jsonObject){
        
}
```

### @RequestBody和@RequestParam

* @RequestBody用来获得请求体中的参数，@RequestParam用来获得请求头中的参数

* 你可以在同一个方法里同时使用这两个注解来提取参数

* 一旦使用@RequestBody，则该方法只能通过POST访问
* 一个POST访问的方法中最多只能有一个@RequestBody
* post和get中，可以使用多个@RequestParam

## 获得文件参数

Multipartfile

## 天然的序列化

* 使用@RestController注解修饰的Controller默认返回的值将会被转换成json，并写入响应体。

* 例如：

  ```
  @RequestMapping(value = "/test")
  public String search(){
  	return "yes";
  }
  ```

  将会返回

  ```
  "yes"       //注意它是有双引号的
  ```

  而在servlet中使用如下的方式发送响应

  ```
  response.getWriter().write("yes");
  ```

  将会返回

  ```
  yes
  ```

* 同样你还能够返回别的东西

  ```
  @RequestMapping(value = "/test")
  public Map search(){
      Map map = new HashMap();
      map.put("username","仓仓子");
      map.put("stuId","201721xxxx");
  	return map;
  }
  ```

  将返回一个json对象

  ```
  {"username":"仓仓子","stuId":"201721xxxx"}
  ```

* 返回List将会返回一个json数组

* 最重要的是，它能够返回自定义Bean

  ```
  @RequestMapping(value = "/test")
  public Student search(){
      Student student = new Student();
      
      student.id = 1;
      student.username = "仓仓子";
      student.studyCode = "201721xxxx";
      
      return student;
  }
  
  public class Student{
      public int id;
      public String username;
      public String studyCode;
  }
  ```

  将会返回

  ```
  {"id":1,"username":"仓仓子","stuId":"201721xxxx"}
  ```

* 返回自定义Bean的原理是，springboot获取到你的返回对象后，首先尝试将它转化成json(这个过程称为序列化)，然后写入响应中。即从一个Object对象，转化成一个String对象(json格式)(你们可以尝试自己实现下这个过程，通过代理来拿Object的属性名和值。)

## 配置简化

* springboot所需要的所有配置都写在application.properties中。这里的properties只是表示配置文件的格式，你也可以使用yaml的格式来写，那么文件就应该命名为application.yml (yml是简写)

* 如果你使用yml格式来写，即你把配置信息以yml格式保存在了application.yml中，你必须在application.yml的同级路径下建立一个空的application.properties文件，因为springboot默认读取application.properties，只有application.properties为空的情况下才会认为用户有其他格式的配置文件，从而尝试读取application.yml。

* 举个例子就知道两种格式的区别了。yml相对来说更简单明了。

  在application.properties中配置数据库

  ```
  spring.datasource.driver-class-name=com.mysql.jdbc.Driver
  spring.datasource.url=jdbc:mysql://localhost:3306/company?allowMultiQueries=true
  spring.datasource.username=root
  spring.datasource.password=xxxx
  ```

   相同的内容用yml格式来写

  ```
  spring:
    datasource:
      driver-class-name: com.mysql.jdbc.Driver #这是一条注释
      url: jdbc:mysql://localhost:3306/company?allowMultiQueries=true #这还是一条注释
      username: root
      password: xxx
  ```

* __注意__：yml格式是以键值对形式表示的，在每个键值对的冒号后面必须有一个空格，否则将因格式问题导致项目无法启动。

# springboot的配置文件内容：application.yml

首先看一个示例

```
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource  #这是druid自己定义的
    url: jdbc:mysql://localhost:3306/demo?useSSL=false  #这是你的jdbc_url
    username: root  #数据库连接用户名
    password: aaaaaaaaaaa   #数据库连接密码
    initialSize: 10  #初始化的时候有几条连接
    minIdle: 10  #连接池中最少保持几条连接
    maxActive: 100  #连接池中最大保持几条连接
    maxWait: 60000
    timeBetweenEvictionRunsMillis: 60000
    minEvictableIdleTimeMillis: 300000
    validationQuery: SELECT 'x'
    testWhileIdle: true
    testOnBorrow: false
    testOnReturn: false
    poolPreparedStatements: true
    maxPoolPreparedStatementPerConnectionSize: 200   #PreparedStatement缓存大小
    filters: stat,wall          #stat:web监控   wall：sql防火墙
    connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
    useGlobalDataSourceStat: true
    loginUsername: root    
    loginPassword: zv9VJ11111    
    test-while-idle: true 
    driver-class-name: com.mysql.cj.jdbc.Driver    #jdbc驱动名，这个一定要有
  thymeleaf:
    cache: false  #模板是否使用缓存，本地开发的时候建议设置为false，部署后设置为true
    prefix: classpath:/templates/    #模板的路径前缀
    suffix: .html  #模板后缀名
    encoding: UTF-8  #模板的编码
    mode: HTML5  #模板是html5
    check-template: true
  http:
    encoding:
      force: true
      charset: UTF-8
      enabled: true
  servlet:
    multipart:
      max-file-size: 5MB
      max-request-size: 10MB
  redis:
    database: 0  #默认使用0号redis数据库
    host: localhost  #redis的域名/ip，localhost表示是本地redis
    port: 6379  #redis端口号
    password:  #redis密码，留空表示连接的redis密码为空
    jedis:  #redis连接池jedis配置
      pool:
        max-active: 30
        max-wait: -1
        max-idle: 30
        min-idle: 2
    timeout: 1000
  resources:  #静态资源路径设置，springboot将会在设置的几个值中以从前到后顺序查找静态资源
    static-locations: classpath:/META-INF/resources/,classpath:/resources,classpath:/static/,classpath:/public/
server:  #内嵌tomcat的配置
  tomcat:
    uri-encoding: UTF-8
  port: 8080
  servlet:
    context-path: /demo
```

以上的内容一般来说是一个springboot项目所必要的(除非你没用mysql，druid，redis等等组件，那你可以去掉)

它们的含义你们可以自己查，这里只提几条。

你们也可以自己去找一些模板来用。

同时你也可以在application.yml中自己定义你自己的参数，比如我加一条：

```
weixin:
  redirect_back:
    url: http://127.0.0.1/redirect/login?status=200
```

## 自定义参数的必要之处

设置自定义值的必要之处：如果你的项目的某个参数可能因时间变化而变化，而你把这个值写死在代码中，那么当这个值变化的时候，你如果想要对已经上线的项目进行更改，就需要：1.改代码 2.重新编译打包 3.更换服务器上的包 4.重启服务器(5分钟起步)。而在配置文件中定义值，其他地方只是引用这个值，你需要做的事情就是：1.改配置 2.重启服务器 (20秒左右)     后者大大减少了服务的下线时间。这样做能够更好的提供稳定的服务。

任何时候，都建议把可能更换的参数写进配置文件，或者是引用全局变量的形式。

## 获得application.yml中的自定义参数

现在我需要获得这个值，那么我就需要用@Value注解来获取

* @Value注解作用于一个变量

* 被@Value标记的变量只需要被声明，不需要你去给它赋值，springboot会在项目初始化的时候扫描到这个注解，然后尝试从application.yml中获得这个值。

* 示例：

  ```
  通过这样的写法来获得上面那个yml展示中的weixin.redirect_back.url
  @Value("${weixin.redirect_back.url}")
  String weiXinRedirectBackUrl;        //只需要声明，不需要赋值
  ```

* 对于application.properties的示例

  ```
  weixin.redirect_back.url=http://127.0.0.1/redirect/login?status=200
  ```

  上面的@Value标签也可以获得这里的application.properties中的url的值

* 如果springboot无法获得你指定的@Value参数中的值，或者发现了这个值但是类型转换错误，那么springboot将因初始化失败中止启动

# springboot的其他操作

## 自定义初始化任务：@PostConstruct

* 这个注解其实不是spring提供的，是servlet提供的。但是一般情况下你们应该可以用到它的功能，就提一下

* 注解作用：被@Postconstruct标记的方法，会在springboot启动完毕之后被调用一次，且仅调用一次。这样可以用来做一些初始化的任务，比如检查一下数据库的数据，或者是检查一下项目使用的接口是否可以调用，或者是打印一下自己设置的参数的值看看是否正常。

* 当然你们也可以用定时任务实现，即fixedDelay设置一个很大的值。

  ```
  @Postconstruct
  public void init(){
      //在springboot其他组件什么的初始化都结束之后执行
  }
  ```

## 定时任务：@Scheduled

* @Scheduled注解作用于方法，被标记的方法会被springboot当作一个定时方法，springboot会按照你指定的规则计算该在什么时侯执行这个方法，比如在项目初始化的时候执行一个配置方法，每天凌晨2点项目请求量小的时候扫描数据库做一些数据处理等等。

* 示例：

  ```
  //每天4点（24小时）执行
  @Scheduled(cron = "0 0 4 * * ? ")
  public void authRefresh() {
     System.out.println("现在是北京时间4点整");
  }
  ```

* 使用：

  * 在项目入口类使用@EnableScheduling

    ```
    @EnableScheduling
    @SpringBootApplication
    public class TanmuApplication {
    
    	public static void main(String[] args) {
    		SpringApplication.run(TanmuApplication.class, args);
    	}
    }
    ```

  * 使用@Scheduled注解标记你自己的方法，并且设置符合规则的值。

* 在@Scheduled中有四个键值，分别是cron，fixedDelay，fixedRate，initialDelay

  * cron规则见https://www.cnblogs.com/junrong624/p/4239517.html 或者自行搜索

  * fixedDelay表示，这个方法会在项目初始化的时候就被执行，fixedDelay的值表示在每一次执行完成之后延迟多长时间执行，单位：毫秒

    ```
    //服务启动时运行，设置10天的间隔日期
    @Scheduled(fixedDelay = 1000*60*60*24*10)
    public void questionMateListRefresh() {
        
    }
    ```

  * fixedRate表示，这个方法会在项目初始化的时候就被执行，fixedRate的值表示在每一次执行任务开始之后延迟多长时间再一次这个方法，单位：毫秒

  * initialDelay表示，这个方法会在容器启动后，延迟多长时间第一次执行这个方法，此后每一次执行任务都按照fixedDelay或者fixedRate的值和规则来。initialDelay可以和fixedDelay配合使用

  * fixedDelay，fixedRate，cron彼此冲突，在同一个方法上你只能使用其中的一个。


## 在springboot中使用过滤器:@Filter

* 你的过滤器类需要实现  javax.servlet.Filter 接口

* 向springboot注册你的过滤器 (以下任一种皆可)

  * 第一种方法：使用@WebFilter注解注释你的过滤器类

  * 第二种方法：通过FilterRegistrationBean类注册

    ```
    @Configuration
    public class FilterConfig {
    
        @Bean //被@Bean标记的方法会在项目初始化的时候被执行一边，获取一个对象，这里是TestFilter类
        public Filter TestFilter(){ //这里返回的是你的过滤器类对象
            return new TestFilter();
        }
    
        @Bean
        public FilterRegistrationBean IdentityRegistration() {
            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(new DelegatingFilterProxy("TestFilter"));//你的过滤器的类名
            registration.addUrlPatterns("/login");//你的过滤器拦截的路径
            registration.setName("TestFilter");//你的过滤器的类名
            registration.setOrder(3);//该过滤器优先级
            return registration;
        }
        
        //每一个过滤器都必须有上面两种方法
    }
    ```

## springboot自带的线程池

* 使用：

  * 在入口类使用@EnableAsync注解

  * 创建异步任务配置类

    ```
    @Configuration
    public class SpringTaskExcutor implements AsyncConfigurer {
    
        @Override
        public Executor getAsyncExecutor(){
            ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
            taskExecutor.setCorePoolSize(2); //线程池最少线程数量
            taskExecutor.setMaxPoolSize(100); //最多线程数量
            taskExecutor.setQueueCapacity(100); //队列大小
            taskExecutor.setKeepAliveSeconds(300); //线程最大空闲时间
            poolExecutor.setThreadNamePrefix("my-pool-");// 线程名称前缀
            taskExecutor.initialize();
            return taskExecutor;
        }
    
        @Override
        public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler(){
            return null;
        }
    }
    ```

  * 在你想要异步调用的方法上使用@Async注解

    ```
    @Async
    public void test(){
        while(true){
            
        }
    }
    ```

  * 在其他地方调用这个方法

    ```
    public void buy(){
        test();     //上面的test()方法里面是个死循环，如果同步执行，那么buy()将阻塞在test（）
        return;     //但实际上是使用另一个线程去调用test()，而buy()所在的线程会结束回到上一级方法
    }
    ```

  * 千万注意：不要让你的异步线程有任何死锁或者while(true)的情况，也要控制好不要让线程池高负荷，因为@Async实现的异步是利用了springboot自己的线程池，这个线程池是和所有客户端以及服务端自己的异步任务共享的，一旦线程池高负荷，那么客户端请求因为无法获得一个空闲的线程，将会阻塞在调用异步方法的地方。

# 开发上的一些东西

## 日志

* 日志是指，控制台打印的所有东西，包括servlet容器启动日志，你自己打印的东西，各种异常，定时任务信息

* 为什么要注意日志：通过日志来追踪你自己的一些运行时才会出现的bug(因为可能会有各种奇葩情况)，或者你的某些任务执行会输出一些关于服务健康性的信息。

* 日志文件的存放路径

  * tomcat默认路径为/var/logs/tomcat8/  或者/var/logs/tomcat9   或者  tomcat根目录下的logs文件夹

    在这个路径下有一个catalina.out(对于linux) 一般来说tomcat会把war包项目运行时的输出默认存入catalina.out,这就可能导致它体积很大，因为一个tomcat可以启动多个war包项目，并且多个人的项目的日志都打在catalina.out里面，彼此管理起来也很麻烦。

    建议你们对一个项目建立一个专门的文件夹(与项目同名)来存放log

* 日志级别：由最严重到最不严重从上到下排列

  * Off：关闭日志输出，不输出任何东西
  * Fatal：相当严重，此类错误已经无法修复，并且如果系统继续运行下去的话后果严重。
  * 
  * Error：可修复，但无法确定系统会正常的工作下去
  * Warn：可修复，系统可继续运行下去
  * Info：无错误，系统正常运行。打印一些你感兴趣的或者重要的信息，这个可以用于生产环境中输出程序运行的一些重要信息，但是不能滥用，避免打印过多的日志
  * Debug：指出细粒度信息事件对调试应用程序是非常有帮助的，主要用于开发过程中打印一些运行信息
  * 
  * Trace：很低的日志级别，一般不会使用。
  * All：最低的日志级别，输出所有级别的内容
  * 任何情况下，指定日志级别为某一个等级A，那么A等级的日志和级别比A严重的日志都会被输出

  一般来说，常使用Debug到Fatal(左右包含)这五个级别，Fatal用的比较少(error都有了，还敢不修等fatal?)

* springboot整合日志：推荐使用slf4j

## 连接池(不仅仅是Mysql的连接)

* 数据库连接的建立和销毁都有较大的开销。

* 连接池就是一次性创建很多个连接对象，有人需要用连接就分配一个，连接使用结束之后就将其放回连接池

* 自己实现连接池，一个代码示例

  ```
  https://github.com/leaftogo/Tool/tree/master/connectionpool
  ```

* 使用开源连接池：druid c3p0 DBCP

# 贼好用的Mybatis

## 基本介绍

__用好持久层框架能极大减少你的劳动量( >20% )。__

* 持久层框架是什么

* 有哪些持久层框架：Mybatis，Hibernate
* Mybatis的sql语法：Mybatis的sql在参数的写法上会不一样，其他语法与标准sql一致。
* Mybatis简化了哪些东西：
  * jdbc中执行sql操作的代码
  * 对查询到的ResultSet进行拿参数并对参数进行封装传回上一层方法(这里的序列化指的是序列化为Bean)

## 开始使用

* 在pom中添加依赖

  ```
  <dependency> //jdbc驱动
  			<groupId>org.springframework.boot</groupId>
  			<artifactId>spring-boot-starter-jdbc</artifactId>
  </dependency>
  
  <dependency>  //mysql连接
  			<groupId>mysql</groupId>
  			<artifactId>mysql-connector-java</artifactId>
  			<version>8.0.12</version>
  			<scope>compile</scope>
  </dependency>
  
  <dependency>  //mybatis本体
  			<groupId>org.mybatis.spring.boot</groupId>
  			<artifactId>mybatis-spring-boot-starter</artifactId>
  			<version>1.3.2</version>
  </dependency>
  ```

* 在@SpringBootApplication标记的入口类上加一个@MapperScan("")

  * 这里@MapperScan的值表示mybatis需要在哪个文件夹下去查找mybatis的mapper接口

  * 写法示例：

    ```
    @MapperScan("com.example.demo.Mapper")
    ```

* 写你的Mapper接口

  * 你的Mapper接口必须被@Mapper注解标记

  * 示例：

    ```
    @Mapper
    public interface UserDataMapper {
    
    }
    ```

  * Mapper接口中允许有五种方法注解：@Insert  @Select  @Update  @Delete             @Options

  * 它们都是方法注解，前四个互不兼容，同一个方法最多只能有一个且必需有一个

  * 前四个分别用来表示这是一个 插入/查询/更改/删除 sql

  * 示例（只是为了表示sql该写在哪里）：

    ```
    @Insert("你的sql，例如 insert into balabalabalabala")
    void add();
    
    @Select("select * from user where id=xxxxx")
    List<String> search();
    
    @Update("balabala")
    void update();
    
    @Delete("balabala")
    void delete();
    ```

  * 还是看代码：

    ```
    
    ```

  * 需要注意这些地方：
    * 如何写一个jdbc方法的传入参数

      * 可以是普通的java变量，也可以是自定义bean

    * 如何在sql中引用这些参数，即如何把这些参数写进sql (比如查询id为2017210000的学生，需要把这个学号传进sql中的where id = xxxx 的xxxx部分)

      * 比如传入的参数是自定义bean，那么mybatis会先找你写在sql里的参数名，然后尝试在你传入的参数中去拿一个同名参数的值，或者通过代理便利你的自定义bean的参数名来找同名参数。

        这样大大方便了你们去处理数据，直接序列化好传参就行。这是在牺牲性能换取开发效率。

    * 可以设置怎样的返回值

      * boolean String int    正常的变量类型

      * 自定义bean

        ```
        @Select("balabala")
        Bean searchById(int id);
        ```

      * List<Bean>  自定义bean的list    (一般是select返回了多条数据)

        ```
        @Select("balabala")
        List<Bean> searchById(int id);
        ```

    * 如何调用

      * 在其他类中用@AutoWired注入mapper类，然后调用类中的方法。

  * 关于Entity

    * Entity只是对于针对数据库表定义的Bean的称呼，每张表一个Entity类

    * 对于上面的传参可以传自定义Bean，你们就需要创建好合适的bean类，同时类名也不要容易混淆，最好同名加不同后缀，比如自定义Bean统一用       表名+"Entity"     mapper为：表名+"Mapper"
    * 你的Entity类可以用@Data注解标记，@Data会自动为一个类生成getter()和setter()。因为一旦数据库表字段过多，getter和setter啥的也会很多，看上去就不简洁。

# Maven的一些东西

maven不仅仅只是用来为项目添加依赖，它本身能够帮助你去创建项目和项目编译。

* git clone              mvn package           java -jar    一把梭