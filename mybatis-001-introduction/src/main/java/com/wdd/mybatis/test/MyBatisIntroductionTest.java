package com.wdd.mybatis.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.FileInputStream;
import java.io.InputStream;

public class MyBatisIntroductionTest {
    public static void main(String[] args) throws Exception{
        //获取SqlSessionFactoryBuilder对象
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        //获取SqlSessionFactory对象
        //Resources.getResourceAsStream默认从类的根路径下开始查找资源
        //一般是一个数据库对应一个sqlSessionFactory对象
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        //InputStream is = Resources.getResourceAsStream("com/mybatis-config.xml");
        //InputStream is = new FileInputStream("d:\\mybatis-config.xml");
        //InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);

        //获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();   //若使用的事务管理是JDBC，底层会执行conn.setAutoCommit(false);
        //SqlSession sqlSession = sqlSessionFactory.openSession(true);    设置为true，最后不写 sqlSession.commit();
        //执行sql语句
        int insertCar = sqlSession.insert("insertCar");
        //手动提交
        sqlSession.commit();     //底层会执行conn.commit()
    }
}
