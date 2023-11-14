package com.wdd.mybatis.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;


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
    private static ThreadLocal<SqlSession> local = new ThreadLocal<>();

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
        SqlSession sqlSession = local.get();
        if(sqlSession == null){
            sqlSession = sqlSessionFactory.openSession();
            local.set(sqlSession);
        }

        return sqlSession;
    }

    /**
     * 关闭SqlSession对象(从当前线程中移除SqlSession对象)
     */
    public static void close(SqlSession sqlSession){
        if(sqlSession != null){
            sqlSession.close();
            local.remove();
        }
    }


}
