package com.wdd.mybatis.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;


public class MybatisCompleteTest {
    public static void main(String[] args) {
        SqlSession sqlSession = null;
        try {
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("mybatis-config.xml"));
            //开启会话（底层会开启事务）
            sqlSession = sqlSessionFactory.openSession();
            //执行sql语句
            int count = sqlSession.insert("insertCar");
            System.out.println(count);
            //提交事务
            sqlSession.commit();
        } catch (Exception e) {
            //回滚事务
            if(sqlSession != null){
                sqlSession.rollback();
            }
           e.printStackTrace();
        }finally {
            //关闭会话（释放资源）
            sqlSession.close();

        }
    }
}
