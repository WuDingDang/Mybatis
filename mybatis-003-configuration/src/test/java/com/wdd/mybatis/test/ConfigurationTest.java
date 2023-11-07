package com.wdd.mybatis.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

public class ConfigurationTest {

    @Test
    public void testDataSource() throws Exception{
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("mybatis-config.xml"));
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        sqlSession1.insert("xxx.insertCar");
        sqlSession1.commit();
        sqlSession1.close();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        sqlSession2.insert("xxx.insertCar");
        sqlSession2.commit();
        sqlSession2.close();
    }

    @Test
    public void testEnvironment() throws Exception{
        //获取SqlSessionFactory对象(采用默认方式获取)
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        //获取默认环境
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("mybatis-config.xml"));
        //通过环境id使用指定环境
        sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("mybatis-config.xml"),"development");
    }
}
