

# ORM（对象关系映射）

![image-20231105131429421](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231105131429421.png)

# 1. 第一个Mybatis程序

## 1.1 创建数据库

![image-20231105132501818](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231105132501818.png)

![image-20231105133252735](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231105133252735.png)

![image-20231105133302497](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231105133302497.png)

## 1.2 创建Project项目

resources目录：放在这个目录当中的一般是资源文件，配置文件。直接放到resources目录下的资源，等同于放到类的根路径下

- 第一步：打包方式：jar
- 第二步：引入依赖 
  - mybatis依赖
  - mysql驱动依赖

![image-20231105153200037](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231105153200037.png)

- 第三步：编写mybatis核心配置文件：mybatis-config.xml

- 第四步：编写XxxMapper.xml文件

- 第五步：在mybatis-config.xml文件中指定XxxMapper.xml

  ​             resource属性会自动从类的根路径下开始查找资源

- 第六步：编写Mybatis程序（使用mybatis类库，编写mybatis程序，连接数据库，做增删改查就行）

![image-20231105155419690](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231105155419690.png)

mybatis中两个主要的配置文件：

- mybatis-config.xml：核心配置文件，主要配置连接数据库的信息等
- XxxMapper.xml：编写SQL语句的配置文件



mybatis中，负责执行SQL语句的对象：SqlSession

SqlSession是专门用来执行SQL语句的，是一个Java程序和数据库之间的一次会话

HttpSession：浏览器和服务器之间的会话

想要获取SqlSession对象，需要先获取SqlSessionFactory对象，通过SqlSessionFactory工厂来生产SqlSession对象

如何获取SqlSessionFactory对象？

- 先获取SqlSessionFactoryBuilder对象
- 通过SqlSessionFactoryBuilder对象的build方法，获取SqlSessionFactory对象



mybatis的核心对象

- SqlSessionFactoryBuilder
- SqlSessionFactory
- SqlSession

SqlSessionFactoryBuilder --> SqlSessionFactory  -->  SqlSession



## 1.3 程序细节

- mybatis中sql语句结尾可省略“;”
- Resources.getResourceAsStream

​		凡是Resources单词，一般加载资源的方式都是从类的根路径下开始加载

​		优点：项目的移植性强

- InputStream is = new FileInputStream("d:\\mybatis-config.xml")

  移植性差，违背OCP原则

- mybatis核心配置文件不一定取名为mybatis-config.xml，可以是其他名字；存放路径也不一定在类的根路径下，可以放到其他位置，但为了项目的移植性和健壮性，最好将配置文件放在类的根路径

- ClassLoader.getSystemClassLoader()   获取系统的类加载器

​		系统类加载器有一个方法 getResourceAsStream  ：从类的根路径加载资源

**通过源码分析发现：Resources.getResourceAsStream()底层就是ClassLoader.getSystemClassLoader().getResourceAsStream()**

![image-20231105224146780](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231105224146780.png)

- CarMapper.xml文件名是固定的吗？ --不是



## 1.4 深度剖析mybatis的事务管理机制

在mybatis-config.xml文件中，可以通过以下的配置进行mybatis的事务管理

```xml-dtd
<transactionManager type="JDBC"/>
```

- type属性值：

  - JDBC
  - MANAGED

- 在mybatis中提供两种事务管理机制

  - JDBC事务管理：

    mybatis框架自己管理事务，采用原生的JDBC代码管理事务

    ​		conn.setAutoCommit(false);

    ​		....业务处理...

    ​		conn.commit();     手动提交事务

    使用JDBC事务管理器，底层创建的事务管理器对象：JdbcTransaction对象

    ```java
    SqlSession sqlSession = sqlSessionFactory.openSession(true);    
    //设置为true，表示没有开启事务，因为不会执行conn.setAutoCommit(false); 
    //如果没有在JDBC代码中执行conn.setAutoCommit(false); 那么autoCommit为true
    //如果autoCommit为true，就表示没有开启事务，只要执行任意一条DML语句就提交一次（不建议，因为没有开启事务）
    ```

    JDBC事务中的事务：

    ​		如果没有在JDBC代码中执行conn.setAutoCommit(false);  默认autoCommit为true

  - MANAGED事务管理

​				mybatis不再负责事务管理，交给其他容器负责，如Spring

​				如果单纯只有mybatis的情况下配置MANAGED，就没有事务

​			**只要autoCommit为true，就表示没有开启事务**

## 1.5 junit单元测试

导入jar包

![image-20231105235359057](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231105235359057.png)

![image-20231105235435590](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231105235435590.png)



## 1.6 mybatis集成日志组件

### 1.6.1 mybatis常见的继承的日志组件：

- SLF4J

- LOG4J

- LOG4J2

- STDOUT_LOGGING：标准日志，mybatis框架本身已经实现了这种标准，只要开启即可

  如何开启：在mybatis-config.xml文件中使用settings标签进行开启

如何使用日志实现：

settings标签

![image-20231106203529355](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231106203529355.png)

![image-20231106204142102](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231106204142102.png)



### 1.6.2 集成logback日志框架

logback日志框架实现了slf4j标准。

- 第一步：引入logback依赖

  ```xml
  <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <version>1.2.11</version>
  </dependency>
  ```

- 第二步：引入logback所必须的xml配置文件（**配置文件的名字必须叫 logback.xml 或者 logback-test.xml ,必须放到类的根路径**)

  logback.xml 

```xml
<?xml version="1.0" encoding="UTF-8"?>

<configuration debug="false">
    <!-- 控制台输出 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
            <pattern>[%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!--mybatis log configure-->
    <logger name="com.apache.ibatis" level="TRACE"/>
    <logger name="java.sql.Connection" level="DEBUG"/>
    <logger name="java.sql.Statement" level="DEBUG"/>
    <logger name="java.sql.PreparedStatement" level="DEBUG"/>

    <!-- 日志输出级别,logback日志级别包括五个：TRACE < DEBUG < INFO < WARN < ERROR -->
    <root level="DEBUG">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="FILE"/>
    </root>

</configuration>
```



## 1.7 mybatis工具类编写

```java
/**
 * mybatis工具类
 */
public class SqlSessionUtil {
    //工具类的构造方法一般都是私有化的
    //工具类中的所有方法都是静态的，直接采用类名即可调用，不需要new对象
    //为了防止new对象，构造方法私有化
    private SqlSessionUtil(){}

    private static SqlSessionFactory sqlSessionFactory;

    //类加载时执行
    //SqlSessionUtil工具类在进行第一次加载的时候，解析mybatis-config.xml文件，创建SqlSessionFactory对象
    static {
        try {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /*
    public static SqlSession openSession(){
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        //sqlSessionFactory对象：一个sqlSessionFactory对应一个environment，一个environment通常是一个数据库
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsReader("mybatis-config.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession;
    }
     */

    /**
     * 获取会话对象
     * @return  会话对象
     */
    public static SqlSession openSession(){
        return sqlSessionFactory.openSession();
    }


}
```



# 2. 使用mybatis完成CRUD

C：Create

R：Retrieve

U：Update

D：Delete

## 2.1 insert

JDBC中的占位符是 ？

mybatis中使用 #{}

- java中可以使用Map给SQL语句的占位符传值：

```java
Map<String,Object> map = new HashMap<>();
 map.put("carNum","1111");
 map.put("brand","比亚迪");
 map.put("guidePrice",10.0);
 map.put("produceTime","2011-11-3");
 map.put("carType","电车");
 int count = sqlSession.insert("insertCar",map);
```

```xml
<insert id="insertCar">
    insert into t_car(id,car_num,brand,guide_price,produce_time,car_type)
    values(null,#{carNum},#{brand},#{guidePrice},#{produceTime},#{carType})
</insert>
```

  #{写map集合的key，如果key不存在，获取的是null}  key要见名知意

- 使用POJO类给SQL语句的占位符传值

  ```java
  Car car = new Car(null, "3333", "比亚迪秦", 30.0, "2022-1-12", "新能源");
  int count = sqlSession.insert("insertCar",car);
  ```

  ```xml
  <insert id="insertCar">
      insert into t_car(id,car_num,brand,guide_price,produce_time,car_type)
      values(null,#{carNum},#{brand},#{guidePrice},#{produceTime},#{carType})
  </insert>
  ```

  #{写pojo属性名}   其实找的是getter方法getXxx()中的xxx

eg:  getUsername()  --->   #{username}

  mybatis在底层给 ？传值的时候，先要获取值，通过调用pojo对象的get方法，如 car.getCarnum() ,  car.getBrand()



## 2.2 delete

删除id是10的数据

```java
int count = sqlSession.delete("deleteById", 10);
```

```xml
<delete id="deleteById">
    delete from t_car where id = #{id}
</delete>
```

如果占位符只有一个，#{}里可以随意



## 2.3 update

根据id修改某条记录

```java
Car car = new Car(4L, "9999", "丰田2.0", 30.3, "2021-12-09", "燃油车");
int count = sqlSession.update("updateById",car);
```

```xml
<update id="updateById">
    update t_car set car_num=#{carNum},brand=#{brand},guide_price=#{guidePrice},produce_time=#{produceTime},car_type=#{carType} where id=#{id}
</update>
```



## 2.4 select 

### 2.4.1 查一个（根据id查询）

```java
//mybatis底层执行select语句之后，一定会返回一个结果集对象ResultSet
//JDBC中叫ResultSet，接下来mybatis应该从ResultSet中取数据，封装java对象
Object car = sqlSession.selectOne("selectById", 1);
```

```xml
<select id="selectById" resultType="com.wdd.mybatis.pojo.Car">
    select * from t_car where id = #{id}
</select>
```

**select标签中resultType属性用来告诉mybatis，查询结果集封装成什么类型的java对象**

**resultType通常写全限定类名 **

输出结果：

Car{id=1, carNum='null', brand='宝马520Li', guidePrice=null, produceTime='null', carType='null'}

**carNum，guidePrice，produceTime，carType没有赋上值的原因？**

select * from t_car where id = 1

执行结果：![image-20231106225828746](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231106225828746.png)

查询结果中的列名car_num，guide_price，produce_time，car_type和Car类中的属性名对不上

解决：

起别名

```xml
<select id="selectById" resultType="com.wdd.mybatis.pojo.Car">
    <!-- select * from t_car where id = #{id} -->
    select id,car_num as carNum,brand,guide_price as guidePrice,produce_time as produceTime,car_type as carType from t_car where id = #{id}
</select>
```

### 2.4.2 查所有

```java
List<Object> cars = sqlSession.selectList("selectAll");
cars.forEach(car -> System.out.println(car));
```

```xml
<select id="selectAll" resultType="com.wdd.mybatis.pojo.Car">
    select id,car_num as carNum,brand,guide_price as guidePrice,produce_time as produceTime,car_type as carType from t_car
</select>
```

resultType封装的是结果集的类型，不是List类型，是List集合中元素的类型

selectList方法：mybatis通过这个方法就可以得知需要一个List集合，会自动返回一个List集合



## 2.5 namespace	

![image-20231106231528043](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231106231528043.png)

![image-20231106231607318](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231106231607318.png)



# 3. mybatis核心配置文件

## 3.1 environments标签 多环境

![image-20231107204924885](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231107204924885.png)

![image-20231107204939022](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231107204939022.png)



### 3.1.1 transactionManager 标签

![image-20231107205942003](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231107205942003.png)

-  作用：配置事务管理器，指定mybatis具体使用什么方式管理事务

-  type属性的两个值：

  - JDBC:使用原生的JDBC代码管理事务
                conn.setAutoCommit(false);
                ...
                conn.commit();
  - MANAGED:mybatis不再负责事务管理，将事务管理交给其他JEE容器管理（如Spring)

- 不区分大小写

- 在mybatis中提供了一个事务管理器接口：Transaction

  该接口下有两个实现类：

  -  JdbcTransaction
  - ManagedTransaction

   若type="JDBC"，底层会实例化JdbcTransaction对象

   若type="MANAGED"，底层会实例化ManagedTransaction对象



### 3.1.2 dataSource

![image-20231107211342479](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231107211342479.png)

- dataSource被称为数据源

- 作用:为程序提供Connection对象（给程序提供Connection对象的都叫数据源）

- 数据源实际上是一套规范，JDK中有这套规范：javax.sql.DataSource

- 自己也可以编写数据源组件，只要实现javax.sql.DataSource接口，实现接口中所有方法

  比如可以自己写一个数据库连接池（数据库连接池是提供对象的，所以数据库连接池就是一个数据源）

- 常见的数据源组件（常见的数据库连接池）：

  druid，c3p0，dbcp等

- type属性用来指定数据源类型，就是指定具体使用什么方式获取Connection对象

  有三个值：

  - UNPOOLED：不使用数据库连接池技术，每次请求后，都创建新的Connection对象

  ![image-20231107213429985](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231107213429985.png)

  - POOLED：使用mybatis自己实现的数据库连接池

  - JNDI：集成其他第三方数据库连接池

    JNDI是一套规范，大部分Web容器都实现了JNDI规范：如Tomcat，Jetty，WebLogic，WebSphere

    JNDI是java命名目录接口
  
  

连接池优点：
- 每次获取连接都从池中拿，效率高

- 连接对象的创建数量可控

  
#### 3.1.2.1 poolMaximumActiveConnections

 连接池中最多的正在使用的连接对象的数量上限，最多有多少连接可以活动，默认10



#### 3.1.2.2 poolMaximumCheckoutTime

超时时间的设置，默认20s



#### 3.1.2.3 poolTimeToWait

默认每隔20s打印日志，并尝试获取连接



#### 3.1.2.4 poolMaximumIdleConnections

最多空闲数量



## 3.2 properties标签

1.

![image-20231107214746435](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231107214746435.png)



  ![image-20231107214807027](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231107214807027.png)

2.加properties文件

![image-20231107215034435](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231107215034435.png)

![image-20231107215131631](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231107215131631.png)



# 4. 手写Mybatis框架

略



# 5.web中应用mybatis



# 6.面向接口进行CRUD

## 6.1 Mapper

![image-20231108221121494](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231108221121494.png)

## 6.2Mapper.xml

![image-20231108221157213](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231108221157213.png)

## 6.3 test

insert

```java
@Test
public void testInsert(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    System.out.println(sqlSession);
    //面向接口获取接口的代理对象
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    Car car = new Car(null,"8888","丰田凯美瑞",28.8,"2011-10-19","燃油车");
    int count = mapper.insert(car);
    System.out.println(count);
    sqlSession.commit();
}
```

delete

```java
@Test
public void testDelete(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    int count = mapper.deleteById(5L);
    System.out.println(count);
    sqlSession.commit();

}
```

update

```java
@Test
public void testUpdate(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    Car car = new Car(6L,"1006","宝马X6",56.9,"2023-10-13","燃油车");
    int count = mapper.updateCar(car);
    System.out.println(count);
    sqlSession.commit();

}
```

select

```java
@Test
public void testSelect(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    Car car = mapper.selectById(6L);
    System.out.println(car);

}
```

selectAll

```java
@Test
public void testSelectAll(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    List<Car> cars = mapper.selectAll();
    for(Car car:cars){
        System.out.println(car);
    }

}
```



# 7. 小技巧

## 7.1 #{} 和 ${}

#{}:底层使用PreparedStatement；特点：先进行SQL语句编译，然后给SQL语句占位符？传值，可以避免SQL注入的风险

${}:底层使用Statement；特点：先进行SQL拼接，然后对SQL语句进行编译

Statement存在SQL注入的风险

原则：优先使用#{}



## 7.2 查询所有信息排序

![image-20231108223724336](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231108223724336.png)

![image-20231108223801200](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231108223801200.png)

![image-20231108223734951](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231108223734951.png)

- 使用#{asc}

  ![image-20231108223900893](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231108223900893.png)

select id, car_num as carNum, brand, guide_price as guidePrice, produce_time as produceTime, car_type as carType from t_car order by produce_time 'asc'  **会有单引号报错**



- 使用${asc}

select id, car_num as carNum, brand, guide_price as guidePrice, produce_time as produceTime, car_type as carType from t_car order by produce_time asc   

不报错

**如果需要sql语句的关键字放到sql语句中，只能使用${}，因为#{}是以值的形式放到sql语句中**



## 7.3 向sql中拼接表名

**需要使用${}**

