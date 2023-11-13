

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

现实业务中，可能存在分表存储数据的情况，因为一张表存，数据量大，查询效率低。

可以将数据有规律的分表存储，这样查询的效率高，因为扫描的数据量变少了

日志表：

![image-20231109205549642](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109205549642.png)

![image-20231109205627260](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109205627260.png)

![image-20231109205744338](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109205744338.png)

使用#{}

![image-20231109205827127](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109205827127.png)

![image-20231109205907723](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109205907723.png)

报错

![image-20231109205948852](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109205948852.png)

使用${}

![image-20231109210020861](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109210020861.png)

![image-20231109210031569](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109210031569.png)



## 7.4 批量删除

一次删除多条记录

批量删除的SQL语句两种写法：

- delete from t_car where id=1 or id=2 or id=3;

- delete from t_car where id in(1,2,3);

  ```xml
  <delete id="deleteBatch">
      delete from t_car where id in(${ids})
  </delete>
  ```



## 7.5 模糊查询

根据汽车品牌进行模糊查询

select * from t_car where brand like '%奔驰%'



错误示范：

![image-20231109212644626](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109212644626.png)

![image-20231109212653704](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109212653704.png)

![image-20231109212702084](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109212702084.png)







方法一：${}![image-20231109212823484](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109212823484.png)

![image-20231109212836316](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109212836316.png)



方法二：concat函数，mysql数据库中的函数，进行字符串拼接

![image-20231109213232544](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109213232544.png)

方法三：

![image-20231109213441587](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109213441587.png)



## 7.6 别名机制

![image-20231109214607128](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109214607128.png)

![image-20231109214658965](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109214658965.png)



## 7.7 mybatis-config.xml中的mappers标签

mapper标签的属性：

- resource：从类的根路径开始查找资源，配置文件需要放到类路径当中
- url：绝对路径，移植性差
- class：mapper接口的全限定接口名，带包名

​			eg:  <mapper class="com.wdd.mybatis.mapper.CarMapper"/>

​				mybatis框架会自动去com/wdd/mybatis/mapper目录下找CarMapper.xml文件

​		所以使用class要保证CarMapper.xml和CarMapper接口必须在同一目录下，并且名字一致

![image-20231109215823196](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109215823196.png)

![image-20231109215855657](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109215855657.png)

![image-20231109215937263](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109215937263.png)



最终写法：使用package标签

![image-20231109220208757](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109220208757.png)



## 7.8 配置模板文件

![image-20231109221250426](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109221250426.png)

![image-20231109221318291](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231109221318291.png)

## 7.9 插入数据时获取自动生成的主键

```java
/**
 * 插入car信息，并且使用生成的主键值
 * @param car
 * @return
 */
int insertCarUseGeneratedKeys(Car car);
```

```xml
<!--
useGeneratedKeys="true"  使用自动生成的主键值
keyProperty="id"  指定主键值赋值给对象的哪个属性，这个就表示将主键值赋值给Car对象的id属性
-->
<insert id="insertCarUseGeneratedKeys" useGeneratedKeys="true" keyProperty="id">
    insert into t_car values(null,#{carNum},#{brand},#{guidePrice},#{produceTime},#{carType})
</insert>
```





# 8. mybatis参数处理（关键！）

## 8.1 单个参数

单个简单类型参数

- byte short int long double float char
- Byte Short Integer Long Double Float Character
- String
- java.util.Date
- java.sql.Date



### 8.1.1 单个简单类型参数之Long类型

```java
/**
 * 当接口中的方法参数只有一个（单个参数），并且参数的数据类型都是简单类型
 * 根据 id/name/birth/sex
 */
List<Student> selectById(Long id);
```

```xml
<select id="selectById" resultType="Student" parameterType="java.lang.Long">
    select * from t_student where id = #{id}
</select>
```

parameterType 属性的作用：告诉mybatis框架，这个方法的参数类型是什么类型

mybatis框架自身带有类型自动推断机制，所以大部分情况下parameterType属性可以省略

底层：

​	 select * from t_student where id = ?

jdbc给?传值   ps.setXxx(第几个?,传什么值)

​		ps.setLong(1,1L)
​    	ps.setString(1,"zhangsan")
   	 ps.setInt(1,2);
   	 ps.setDate(1,new Date())

mybatis底层调用哪个setXxx(),取决与parameterType属性的值
注意：mybatis框架内置了很多别名，参考开发手册

![image-20231111122205700](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111122205700.png)



### 8.1.2 单个简单类型参数之Date类型

![image-20231111123144573](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111123144573.png)

```java
List<Student> selectByBirth(Date birth);
```

```xml
<select id="selectByBirth" resultType="Student">
    select * from t_student where birth = #{birth}
</select>
```

```java
@Test
public void testSelectByBirth() throws Exception{
    SqlSession sqlSession = SqlSessionUtil.openSession();
    StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date birth = sdf.parse("1998-10-11");
    List<Student> students = mapper.selectByBirth(birth);
    students.forEach(student -> System.out.println(student));
    sqlSession.close();
}
```



### 8.1.3 参数是Map集合

![image-20231111125258245](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111125258245.png)

```java
/**
 * 保存学生信息，通过map参数，以下是单个参数，但参数类型不是简单类型，是map集合
 * @param map
 * @return
 */
int insertStudentByMap(Map<String,Object> map);
```

parameterType 可省略

```xml
<insert id="insertStudentByMap" parameterType="map">
    insert into t_student(id,name,age,height,birth,sex) values (null,#{name},#{age},#{height},#{birth},#{sex})
</insert>
```

```java
@Test
public void testInsertStudentByMap(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
    Map<String,Object> map = new HashMap<>();
    map.put("name","王五");
    map.put("age",23);
    map.put("height",1.72);
    map.put("birth",new Date());
    map.put("sex",'男');
    mapper.insertStudentByMap(map);
    sqlSession.commit();
    sqlSession.close();
}
```



### 8.1.4 参数是POJO类

```java
/**
 * 保存学生信息，通过POJO参数。Student是单个参数，但不是简单类型
 * @param student
 * @return
 */
int insertStudentByPOJO(Student student);
```

parameterType 可省略

```xml
<insert id="insertStudentByPOJO" parameterType="student">
    insert into t_student(id,name,age,height,birth,sex) values (null,#{name},#{age},#{height},#{birth},#{sex})
</insert>
```

```java
@Test
public void testInsertStudentByPOJO(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
    Student student = new Student();
    student.setName("赵六");
    student.setAge(18);
    student.setHeight(1.61);
    student.setBirth(new Date());
    student.setSex('女');
    mapper.insertStudentByPOJO(student);
    sqlSession.commit();
    sqlSession.close();
}
```



## 8.2 多参数

### 8.2.1 arg0/param1

```java
/**
 * 根据name和sex查询学生信息
 * 多个参数，mybatis框架底层是怎么做的？
 *       mybatis框架会自动创建一个map集合，并且map集合是以这种方式存储的
 *          map.put("arg0",name);
 *          map.put("arg1",sex);
 *          map.put("param1",name);
 *          map.put("param2",sex);
 * @param name
 * @param sex
 * @return
 */
List<Student> selectByNameAndSex(String name,Character sex);
```

![image-20231111160146556](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111160146556.png)



![image-20231111160036090](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111160036090.png)



低版本的mybatis中，使用#{0}，#{1}，#{2}，...

高版本使用：  #{arg0},#{arg1},#{arg2},#{arg3},...

或者   #{param0},#{param1},#{param2},#{param3},...

```xml
<select id="selectByNameAndSex" resultType="student">
    <!-- select * from t_student where name =#{arg0} and sex = #{arg1}  -->
        <!--select * from t_student where name =#{param1} and sex = #{param2}-->
        select * from t_student where name =#{arg0} and sex = #{param2}
</select>
```



### 8.2.2 使用Param注解

```java
/**
 * Param注解
 * mybatis框架底层的实现原理：
 *  map.put("name",name);
 *  map.put("sex",sex);
 * @param name
 * @param sex
 * @return
 */
List<Student> selectByNameAndSex2(@Param("name") String name, @Param("sex") Character sex);
```

![image-20231111190906562](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111190906562.png)

但是可以使用param1,param2

![image-20231111191032828](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111191032828.png)

```xml
  <select id="selectByNameAndSex2" resultType="student">
        select * from t_student where name =#{name} and sex = #{sex}
        <!--select * from t_student where name =#{param1} and sex = #{param2}-->
    </select>
```

```java
@Test
public void testSelectByNameAndSex2(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
    List<Student> students = mapper.selectByNameAndSex2("赵六", '女');
    students.forEach(student -> System.out.println(student));
    sqlSession.close();
}
```





### 8.2.3 Param注解源码分析

代理模式：

- 代理对象
- 代理方法
- 目标对象
- 目标方法

![image-20231111192332513](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111192332513.png)

![image-20231111192527119](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111192527119.png)

![image-20231111193644283](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111193644283.png)



# 9. 查询专题

## 9.1 返回Car

```java
/**
 * 根据id查询car
 * @param id
 * @return
 */
Car selectById(Long id);
```

```xml
<select id="selectById" resultType="car">
    select
        id,
        car_num as carNum,
        brand,
        guide_price as guidePrice,
        produce_time as produceTime,
        car_type as carType
    from t_car where id = #{id}
</select>
```

```java
@Test
public void testSelectById(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    Car car = mapper.selectById(3L);
    System.out.println(car);
    sqlSession.close();
}
```



## 9.2 返回多个Car

```java
/**
 * 获取所有car
 * @return
 */
List<Car> selectAll();
```

```xml
<select id="selectAll" resultType="car">
    select
        id,
        car_num as carNum,
        brand,
        guide_price as guidePrice,
        produce_time as produceTime,
        car_type as carType
    from t_car
</select>
```

```java
@Test
public void testSelectAll(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    List<Car> cars = mapper.selectAll();
    cars.forEach(car -> System.out.println(car));
    sqlSession.close();
}
```



## 9.3 模糊查询的结果可能是多个，但是采用一个POJO对象接收

**如果结果有多个，会报错**

```java
/**
 * 根据品牌进行模糊查询
 * 模糊查询的结果可能是多个，但是采用一个POJO对象接收
 * @param brand
 * @return
 */
Car selectByBrandLike(String brand);
```

```xml
<select id="selectByBrandLike" resultType="car">
    select
        id,
        car_num as carNum,
        brand,
        guide_price as guidePrice,
        produce_time as produceTime,
        car_type as carType
    from t_car where brand like "%"#{brand}"%"
</select>
```

```java
@Test
public void testSelectByBrandLike(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    Car car = mapper.selectByBrandLike("比亚迪");
    System.out.println(car);
    sqlSession.close();

}
```

![image-20231111200215424](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111200215424.png)

![image-20231111200225762](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111200225762.png)



## 9.4 返回Map

```java
/**
 * 根据id获取汽车信息，将汽车信息放到map集合中
 * Map<String,Object>
 *     k             v
 *     "id"          3
 *     "car_num"    1003
 *     "brand"      丰田
 *     ...
 * @return
 */
Map<String,Object> selectByIdRetMap(Long id);
```

```xml
  <!--
    resultType="java.util.Map"的别名：map
    -->
    <select id="selectByIdRetMap" resultType="map">
        select
            id,
            car_num as carNum,
            brand,
            guide_price as guidePrice,
            produce_time as produceTime,
            car_type as carType
        from t_car where id = #{id}
    </select>
```

```java
@Test
public void testSelectByIdRetMap(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    Map<String, Object> car = mapper.selectByIdRetMap(3L);
    System.out.println(car);
    sqlSession.close();
}
```

![image-20231111201148360](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111201148360.png)



## 9.5 返回多个Map

```java
/**
 * 查询所有car，返回一个放Map集合的List集合
 * @return
 */
List<Map<String,Object>> selectAllRetListMap();
```

```xml
<select id="selectAllRetListMap" resultType="map">
    select * from t_car
</select>
```

```java
@Test
public void testSelectAllRetListMap(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    List<Map<String, Object>> maps = mapper.selectAllRetListMap();
    maps.forEach(map -> System.out.println(map));
    sqlSession.close();
}
```

![image-20231111201814114](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111201814114.png)



## 9.5 返回Map<String,Map>

![image-20231111202032534](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111202032534.png)

```java
/**
 * 查询所有Car，返回一个大Map
 * Map集合的key是每条记录的主键值
 * Map集合的value是每条记录
 * @return
 */
@MapKey("id")    //将查询结果的id值作为整个大Map集合的key
Map<Long,Map<String,Object>> selectAllRetMap();
```

```xml
<select id="selectAllRetMap" resultType="map">
    select * from t_car
</select>
```

```java
@Test
public void testSelectAllRetMap(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    Map<Long, Map<String, Object>> map = mapper.selectAllRetMap();
    System.out.println(map);
    sqlSession.close();
}
```

结果：

{

​	16={car_num=8888, id=16, guide_price=28.80, produce_time=2011-10-19, brand=丰田凯美瑞, car_type=燃油车}, 

​	17={car_num=1004, id=17, guide_price=89.30, produce_time=2022-4-23, brand=奔驰s300, car_type=燃油车}, 

​	3={car_num=1003, id=3, guide_price=30.00, produce_time=2000-01-12, brand=丰田, car_type=燃油车}, 

​	4={car_num=9999, id=4, guide_price=30.30, produce_time=2021-12-09, brand=丰田2.0, car_type=燃油车},

​    6={car_num=1006, id=6, guide_price=56.90, produce_time=2023-10-13, brand=宝马X6, car_type=燃油车},

​    12={car_num=1111, id=12, guide_price=10.00, produce_time=2011-11-3, brand=比亚迪, car_type=电车}, 

​    13={car_num=3333, id=13, guide_price=30.00, produce_time=2022-1-12, brand=比亚迪秦, car_type=新能源}, 

​    15={car_num=2222, id=15, guide_price=110.00, produce_time=2023-11-2, brand= 小鹏, car_type=燃油车}

}



## 9.6 resultMap结果映射

查询结果列名和java对象的属性名对应不上解决

- 方法一：as 给列起别名
- 方法二：使用resultMap进行结果映射
- 方法三：是否开启驼峰命名自动映射（配置settings）



### 9.6.1 使用resultMap标签进行结果映射

```java
/**
 * 查询所有car，使用resultMap标签进行结果映射
 * @return
 */
List<Car> selectAllByReultMap();
```

```xml
   <!--
        定义一个结果映射，在这个结果映射当中指定数据库表的字段名和Java类的属性名的对应关系
        type属性：用来指定POJO类的类名
        id属性：指定resultMap的唯一标识，这个id要在select标签中使用
    -->
    <resultMap id="carResultMap" type="car">
        <!-- 如果数据库表中有主键，建议配置一个id标签，虽然不是必须的，但是可以让mybatis提高效率 -->
        <id property="id" column="id"/>
        <!--
            property后面填写POJO类的属性名
            column后面填写数据库表的字段名
         -->
        <result property="carNum" column="car_num"/>
        <!--  property和column是一样的，可以省略 -->
    <!--<result property="brand" column="brand"/>-->
        <result property="guidePrice" column="guide_price"/>
        <result property="produceTime" column="produce_time"/>
        <result property="carType" column="car_type"/>
    </resultMap>
<!-- resultMap标签用来指定使用哪个结果映射，resultMap后面的值是resultMap的id -->
    <select id="selectAllByReultMap" resultMap="carResultMap">
        select * from t_car
    </select>
```

```java
@Test
public void testSelectAllByReultMap(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    List<Car> cars = mapper.selectAllByReultMap();
    cars.forEach(car -> System.out.println(car));
    sqlSession.close();
}
```

![image-20231111205535653](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111205535653.png)



### 9.6.2 是否开启驼峰命名自动映射

前提：属性名遵循Java命名规范，数据库表列名遵循SQL命名规范

Java命名规范：首字母小写，后面每个单词首字母大写，遵循驼峰命名方式

SQL命名规范：全部小写，单词间用下划线分割

![image-20231111205808412](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111205808412.png)

![image-20231111205837844](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111205837844.png)

![image-20231111205918971](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111205918971.png)

```xml
<!-- mybatis的全局设置 -->
<settings>
    <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```

```java
/**
 * 查询所有car，启用驼峰命名自动映射机制
 * @return
 */
List<Car> selectByMapUnderscoreToCamelCase();
```

```xml
<select id="selectByMapUnderscoreToCamelCase" resultType="car">
    select * from t_car
</select>
```

```java
@Test
public void testSelectByMapUnderscoreToCamelCase(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    List<Car> cars = mapper.selectByMapUnderscoreToCamelCase();
    cars.forEach(car -> System.out.println(car));
    sqlSession.close();
}
```



## 9.7 返回总记录条数 Long

```java
/**
 * 获取car的总记录条数
 * @return
 */
Long selectTotal();
```

```xml
<select id="selectTotal" resultType="long">
    select count(*) from t_car
</select>
```

```java
@Test
public void testSelectTotal(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    Long total = mapper.selectTotal();
    System.out.println(total);
}
```



# 10. 动态SQL

![image-20231111212000776](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231111212000776.png)



## 10.1 if标签

```java
/**
 * 多条件查询
 * @param brand
 * @param guidePrice
 * @param carType
 * @return
 */
List<Car> selectByMultiCondition(@Param("brand") String brand, @Param("guidePrice") Double guidePrice,@Param("carType") String carType);
```

```xml
<select id="selectByMultiCondition" resultType="car">
    select * from t_car where 1=1
    <!--
        1.if标签中test属性是必须的
        2.if标签中test属性的值是false或者true
        3.如果test是true，则if标签中的sql语句就会拼接，反之，则不会拼接
        4.test属性中可以使用的是：
            当使用@Param注解，test中要出现的是@Param注解指定的参数名
            当没有使用@Param注解，test中要出现的是：param1,param2,param3/arg0,arg1,arg2
            当使用了POJO，那么test中出现的是类的属性名
        5.在mybatis的动态SQL中，不能使用&&，只能使用and
     -->
    <if test="brand != null and brand != ''">
        and brand like "%"#{brand}"%"
    </if>
    <if test="guidePrice != null and guidePrice != ''">
        and guide_price > #{guidePrice}
    </if>
    <if test="carType != null and carType != ''">
        and car_type = #{carType}
    </if>
</select>
```

![image-20231112135518064](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112135518064.png)

```java
@Test
public void testSelectByMultiCondition(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    List<Car> cars = mapper.selectByMultiCondition("比亚迪", 20.0, "新能源");
    cars.forEach(car -> System.out.println(car));
    sqlSession.close();
}
```

![image-20231112135602239](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112135602239.png)



## 10.2 where标签

作用：让where子句更加动态智能

- 所有条件都为空时，where标签不会生成where子句
- 自动去除某些条件**前面**多余的and或or

```java
/**
 * 使用where标签，让where子句更加智能
 * @param brand
 * @param guidePrice
 * @param carType
 * @return
 */
List<Car> selectByMultiConditionWithWhere(@Param("brand") String brand, @Param("guidePrice") Double guidePrice,@Param("carType") String carType);
```

```xml
<select id="selectByMultiConditionWithWhere" resultType="car">
    select * from t_car
    <!--
        where标签是专门负责where子句动态生成的
    -->
    <where>
        <if test="brand != null and brand != ''">
             brand like "%"#{brand}"%"
        </if>
        <if test="guidePrice != null and guidePrice != ''">
            and guide_price > #{guidePrice}
        </if>
        <if test="carType != null and carType != ''">
            and car_type = #{carType}
        </if>
    </where>
</select>
```

```java
@Test
public void testSelectByMultiConditionWithWhere(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    //条件都不为空
    //List<Car> cars = mapper.selectByMultiConditionWithWhere("比亚迪", 20.0, "新能源");
    //条件都是空
    //List<Car> cars = mapper.selectByMultiConditionWithWhere("", null, "");
    //第一个条件为空
    List<Car> cars = mapper.selectByMultiConditionWithWhere("", 20.0, "新能源");
    cars.forEach(car -> System.out.println(car));
    sqlSession.close();
}
```

条件都不为空

![image-20231112140933847](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112140933847.png)

条件都是空

![image-20231112140847933](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112140847933.png)第一个条件为空

![image-20231112140909082](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112140909082.png)



## 10.3 trim标签

```java
/**
 * 使用trim标签
 * @param brand
 * @param guidePrice
 * @param carType
 * @return
 */
List<Car> selectByMultiConditionWithTrim(@Param("brand") String brand, @Param("guidePrice") Double guidePrice,@Param("carType") String carType);
```

```xml
<select id="selectByMultiConditionWithTrim" resultType="car">
    select * from t_car
    <!--
        prefix 加前缀
        suffix  加后缀
        prefixOverrides  删除前缀
        suffixOverrides  删除后缀
    -->
    <!--
   prefix="where" : 在trim标签的所有内容前面加where
   suffixOverrides="and|or"：把trim标签中内容的后缀and或者or去掉
   -->
    <trim  prefix="where" suffixOverrides="and|or">

        <if test="brand != null and brand != ''">
            brand like "%"#{brand}"%" and
        </if>
        <if test="guidePrice != null and guidePrice != ''">
            guide_price > #{guidePrice} and
        </if>
        <if test="carType != null and carType != ''">
            car_type = #{carType}
        </if>
    </trim>
</select>
```

```java
@Test
public void testSelectByMultiConditionWithTrim(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    List<Car> cars = mapper.selectByMultiConditionWithTrim("比亚迪", null, "");
    cars.forEach(car -> System.out.println(car));
    sqlSession.close();
}
```

![image-20231112161454444](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112161454444.png)

![image-20231112161525622](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112161525622.png)



## 10.4 set标签

 ### 10.4.1 普通update

```java
/**
 * 更新car
 * @param car
 * @return
 */
int updateById(Car car);
```

```xml
<update id="updateById">
    update t_car set
        car_num = #{carNum},
        brand = #{brand},
        guide_price = #{guidePrice},
        produce_time = #{produceTime},
        car_type = #{carType}
    where id = #{id}
</update>
```

```java
@Test
public void testUpdateById(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    Car car = new Car(6L,null,"比亚迪汉",null,null,"电车");
    mapper.updateById(car);
    sqlSession.commit();
    sqlSession.close();
}
```

![image-20231112163605137](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112163605137.png)

设空值的也会更新

![image-20231112162945187](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112162945187.png)



### 10.4.2 使用set标签更新

只更新提交的数据是不为空的字段，会去掉最后多余的 “，”

```java
/**
 * 使用set标签更新car
 * @param car
 * @return
 */
int updateBySet(Car car);
```

```xml
<update id="updateBySet">
    update t_car
    <set>
        <if test="carNum != null and carNum != ''">car_num = #{carNum},</if>
        <if test="brand != null and brand != ''">brand = #{brand},</if>
        <if test="guidePrice != null and guidePrice != ''">guide_price = #{guidePrice},</if>
        <if test="produceTime != null and produceTime != ''">produce_time = #{produceTime},</if>
        <if test="carType != null and carType != ''">car_type = #{carType},</if>
    </set>
    where id = #{id}
</update>
```

```java
@Test
public void testUpdateBySet(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    Car car = new Car(6L,null,"比亚迪byd",null,null,"电车");
    mapper.updateBySet(car);
    sqlSession.commit();
    sqlSession.close();
}
```

![image-20231112163641904](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112163641904.png)



## 10.5 choose when otherwise

三个标签通常放一起使用

只有一个分支会被选择

```java
/**
 * 使用choose when otherwise 标签
 * @param brand
 * @param guidePrice
 * @param carType
 * @return
 */
List<Car> selectByChoose(@Param("brand") String brand,@Param("guidePrice")Double guidePrice,@Param("carType")String carType);
```

```xml
<select id="selectByChoose" resultType="car">
    select * from t_car
    <where>
        <choose>
            <when test="brand != null and brand != ''">
                brand like "%"#{brand}"%"
            </when>
            <when test="guidePrice != null and guidePrice != ''">
                guide_price > #{guidePrice}
            </when>
            <otherwise>
                car_type = #{carType}
            </otherwise>
        </choose>
    </where>
</select>
```

```java
@Test
public void testSelectByChoose(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    //条件都不为空
    List<Car> cars = mapper.selectByChoose("比亚迪",1.0,"新能源");
    cars.forEach(car -> System.out.println(car));
    sqlSession.close();
}
```

- 条件都不为空

​		只选择第一个条件

![image-20231112165241138](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112165241138.png)

- 第一个条件为空

![image-20231112165351412](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112165351412.png)

- 全为空

  选择otherwise中的

![image-20231112165501503](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112165501503.png)



## 10.6 foreach标签

foreach标签属性：

- collection：指定数组或集合

   如果不加注解mybatis底层：map.put("array",数组); / map.put("arg0",数组);

- item：数组或集合中的元素

- separator：循环之间的分隔符

- open:foreach循环拼接的所有sql语句的最前面以什么开始

- close:foreach循环拼接的所有sql语句的最后面以什么开始

### 10.6.1 批量删除

```java
/**
 * 批量删除 foreach标签
 * @param ids
 * @return
 */
int deleteByIds(@Param("ids") Long[] ids);
```

```xml
<delete id="deleteByIds">
    <!-- delete from t_car where id in(1,2,3,4) -->
    <!-- 方法一：
         delete from t_car where id in(
         <foreach collection="ids" item="id" separator=",">
             #{id}
         </foreach>
         )
     -->
    <!-- 方法二-->
    delete from t_car where id in
    <foreach collection="ids" item="id" separator="," open="(" close=")">
        #{id}
    </foreach>
 </delete>
```

```java
@Test
public void testDeleteByIds(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    Long[] ids = {15L,16L,17L};
    int count = mapper.deleteByIds(ids);
    System.out.println(count);
    sqlSession.commit();
    sqlSession.close();
}
```

![image-20231112171601009](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112171601009.png)



```java
/**
 * 根据id批量删除，使用or
 * @param ids
 * @return
 */
int deleteByIds2(@Param("ids") Long[] ids);
```

```xml
<delete id="deleteByIds2">
    delete from t_car where
    <foreach collection="ids" item="id" separator="or">
        id = #{id}
    </foreach>
</delete>
```

```java
@Test
public void testDeleteByIds2(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    Long[] ids = {22L,23L,24L};
    mapper.deleteByIds2(ids);
    sqlSession.commit();
    sqlSession.close();
}
```

![image-20231112213716663](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112213716663.png)

### 10.6.2 批量插入

```java
/**
 * 批量插入
 * @param cars
 * @return
 */
int insertBatch(@Param("cars") List<Car> cars);
```

```xml
<insert id="insertBatch">
    insert into t_car values
    <foreach collection="cars" item="car" separator=",">
        (null,#{car.carNum},#{car.brand},#{car.guidePrice},#{car.produceTime},#{car.carType})
    </foreach>
</insert>
```

```java
@Test
public void testInsertBatch(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    CarMapper mapper = sqlSession.getMapper(CarMapper.class);
    Car car1 = new Car(null,"1001","劳斯莱斯1",802.8,"2011-10-09","燃油车");
    Car car2 = new Car(null,"1002","劳斯莱斯2",766.5,"2021-11-29","燃油车");
    Car car3 = new Car(null,"1004","劳斯莱斯3",500.2,"2015-08-23","燃油车");
    List<Car> cars = new ArrayList<>();
    cars.add(car1);
    cars.add(car2);
    cars.add(car3);
    mapper.insertBatch(cars);
}
```

![image-20231112213038006](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112213038006.png)



## 10.7 sql标签和include标签

sql标签：声明sql片段

include标签：将声明的sql片段包含到某个sql语句中

作用：代码复用，易维护

![image-20231112214601182](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112214601182.png)



# 11. mybatis高级映射及延迟加载

## 11.1准备

![image-20231112220645644](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112220645644.png)

![image-20231112220654373](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112220654373.png)

![image-20231112220219512](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112220219512.png)



多对一：多个学生对应一个班级

多对一映射实体类的设计

![image-20231112221231422](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112221231422.png)

![image-20231112221349261](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231112221349261.png)



## 11.2 多对一映射的方式

### 11.2.1 级联属性映射

```java
/**
 * 根据id获取学生信息，同时获取学生关联的班级信息
 * @param id
 * @return
 */
Student selectById(Integer id);
```

```xml
<resultMap id="studentResultMap" type="student">
    <id property="sid" column="sid"/>
    <result property="sname" column="sname"/>
    <result property="clazz.cid" column="cid"/>
    <result property="clazz.cname" column="cname"/>
</resultMap>
<!-- 多对一映射的第一种方式：一条SQL语句，级联属性映射 -->
<select id="selectById" resultMap="studentResultMap">
    select
        s.sid,s.sname,c.cid,c.cname
    from t_stu s left join t_clazz c on s.cid = c.cid
    where s.sid = #{sid}
</select>
```

```java
@Test
public void testSelectById(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
    Student student = mapper.selectById(1);
    System.out.println(student.getSid());
    System.out.println(student.getSname());
    System.out.println(student.getClazz().getCid());
    System.out.println(student.getClazz().getCname());
    sqlSession.close();
}
```



### 11.2.2 association

```java
/**
 * 一条SQL语句，association
 * @param id
 * @return
 */
Student selectByIdAssociation(Integer id);
```

```xml
<resultMap id="studentResultMapAssociation" type="student">
    <id property="sid" column="sid"/>
    <result property="sname" column="sname"/>
    <!--
   association : 一个Student对象关联一个Clazz对象
        property：提供要映射的POJO类的属性名
        javaType：用来指定要映射的java类型
   -->
    <association property="clazz" javaType="clazz">
        <id property="cid" column="cid"/>
        <result property="cname" column="cname"/>
    </association>
</resultMap>
<select id="selectByIdAssociation" resultMap="studentResultMapAssociation">
    select
        s.sid,s.sname,c.cid,c.cname
    from
        t_stu s left join t_clazz c on s.cid = c.cid
    where s.sid = #{sid}
</select>
```

```java
@Test
public void testStudentResultMapAssociation(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
    Student student = mapper.selectByIdAssociation(1);
    System.out.println(student);
    sqlSession.close();
}
```



### 11.2.3 分步查询

两条SQL语句

优点：

- 可复用
- 支持懒加载（延迟加载）



StudentMapper.java

```java
/**
 * 分步查询第一步：根据学生id查询学生信息
 * @param id
 * @return
 */
Student selectByIdStep1(Integer id);
```

StudentMapper.xml

```xml
<!--
两条SQL语句，完成多对一的分步查询
-->
<resultMap id="studentResultMapByStep" type="student">
    <id property="sid" column="sid"/>
    <result property="sname" column="sname"/>
    <association property="clazz"
                     select="com.wdd.mybatis.mapper.ClazzMapper.selectByIdStep2"
                     fetchType="lazy"
                     column="cid"/>
</resultMap>
<!--第一步：根据学生id查询学生所有信息 ，信息中含有班级的cid-->
<select id="selectByIdStep1" resultMap="studentResultMapByStep">
    select sid,sname,cid from t_stu where sid = #{sid}
</select>
```

ClazzMapper.java

```java
/**
 * 分步查询第二步：根据id获取班级信息
 * @param id
 * @return
 */
Clazz selectByIdStep2(Integer id);
```

ClazzMapper.xml

```xml
<!-- 分步查询第二步：根据cid获取班级信息 -->
<select id="selectByIdStep2" resultType="clazz">
    select cid,cname from t_clazz where cid = #{cid}
</select>
```

```java
@Test
public void testSelectByIdStep1(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
    Student student = mapper.selectByIdStep1(1);
    System.out.println(student);
    sqlSession.close();
}
```

![image-20231113210756720](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231113210756720.png)



### 11.2.4 延迟加载（懒加载）

核心原理：用的时候再执行查询语句，不用的时候不查询

作用：提高性能

mybatis中如何开启延迟加载？

​	**association标签上添加fetchType="lazy"**，局部设置，只对当前association关联的sql语句起作用

默认不开启延迟加载，需要设置



全局设置

![image-20231113211703325](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231113211703325.png)

带有分步的都会延迟加载

![image-20231113211812866](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231113211812866.png)





## 11.3 一对多

![image-20231113212441055](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231113212441055.png)

![image-20231113212629142](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231113212629142.png)

 

### 11.3.1 collection

```java
/**
 * 根据班级编号查询班级信息
 * @param id
 * @return
 */
Clazz selectByCollection(Integer id);
```

```xml
<resultMap id="clazzResultMap" type="clazz">
    <id property="cid" column="cid"/>
    <result property="cname" column="cname"/>
    <!--
    ofType:指定集合中元素类型
    -->
    <collection property="stus" ofType="student">
        <id property="sid" column="sid"/>
        <result property="sname" column="sname"/>
    </collection>
</resultMap>
<select id="selectByCollection" resultMap="clazzResultMap">
    select c.cid,c.cname,s.sid,s.sname
    from t_clazz c left join t_stu s
    on c.cid = s.cid
    where c.cid = #{cid}
</select>
```

```java
@Test
public void testSelectByCollection(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    ClazzMapper mapper = sqlSession.getMapper(ClazzMapper.class);
    Clazz clazz = mapper.selectByCollection(1000);
    System.out.println(clazz);
    sqlSession.close();
}
```

![image-20231113213743559](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231113213743559.png)



### 11.3.2 分步查询

ClassMapper.java

```java
/**
 * 分步查询：第一步：根据班级编号获取班级信息
 * @param id
 * @return
 */
Clazz selectByStep1(Integer id);
```

ClassMapper.xml

```xml
<!-- 分步查询第一步：根据班级的cid查询班级信息 -->
<resultMap id="clazzResultMapStep" type="clazz">
    <id property="cid" column="cid"/>
    <result property="cname" column="cname"/>
    <collection property="stus"
                select="com.wdd.mybatis.mapper.StudentMapper.selectByCidStep2"
                column="cid"/>
</resultMap>
<select id="selectByStep1" resultMap="clazzResultMapStep">
    select cid,cname from t_clazz where cid = #{cid}
</select>
```

StudentMapper.java

```java
/**
 * 分步查询第二步：根据班级编号查学生信息
 * @param cid
 * @return
 */
List<Student> selectByCidStep2(Integer cid);
```

StudentMapper.xml

```xml
<select id="selectByCidStep2" resultType="student">
    select * from t_stu where cid = #{cid}
</select>
```

```java
@Test
public void testSelectByStep1(){
    SqlSession sqlSession = SqlSessionUtil.openSession();
    ClazzMapper mapper = sqlSession.getMapper(ClazzMapper.class);
    Clazz clazz = mapper.selectByStep1(1000);
    System.out.println(clazz);
    sqlSession.close();
}
```

![image-20231113215346275](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231113215346275.png)

![](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231113215255394.png)





# 12. mybatis缓存

![image-20231113220110281](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231113220110281.png)

![image-20231113220144046](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20231113220144046.png)

## 12.1 一级缓存

一级缓存默认开启，无需配置

原理：只要使用同一个SqlSession对象执行同一条SQL语句，就会走缓存
