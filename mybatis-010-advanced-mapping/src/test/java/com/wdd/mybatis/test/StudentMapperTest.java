package com.wdd.mybatis.test;

import com.wdd.mybatis.mapper.StudentMapper;
import com.wdd.mybatis.pojo.Student;
import com.wdd.mybatis.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class StudentMapperTest {
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

    @Test
    public void testStudentResultMapAssociation(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        Student student = mapper.selectByIdAssociation(1);
        System.out.println(student);
        sqlSession.close();
    }

    @Test
    public void testSelectByIdStep1(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        Student student = mapper.selectByIdStep1(1);
        System.out.println(student);
        sqlSession.close();
    }

}
