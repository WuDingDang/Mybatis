package com.wdd.mybatis.test;

import com.wdd.mybatis.mapper.ClazzMapper;
import com.wdd.mybatis.pojo.Clazz;
import com.wdd.mybatis.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class ClazzMapperTest {
    @Test
    public void testSelectByCollection(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        ClazzMapper mapper = sqlSession.getMapper(ClazzMapper.class);
        Clazz clazz = mapper.selectByCollection(1000);
        System.out.println(clazz);
        sqlSession.close();
    }

    @Test
    public void testSelectByStep1(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        ClazzMapper mapper = sqlSession.getMapper(ClazzMapper.class);
        Clazz clazz = mapper.selectByStep1(1000);
        System.out.println(clazz);
        sqlSession.close();
    }
}
