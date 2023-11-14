package com.wdd.mybatis.test;

import com.wdd.mybatis.mapper.CarMapper;
import com.wdd.mybatis.pojo.Car;
import com.wdd.mybatis.util.SqlSessionUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

public class CarMapperTest {
    @Test
    public void testSelectByIdSecondCache() throws Exception{
        //如果要获取不同的SqlSession对象，不能用工具类
        //SqlSession sqlSession = SqlSessionUtil.openSession();
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        CarMapper mapper1 = sqlSession1.getMapper(CarMapper.class);
        Car car1 = mapper1.selectById(3L);  //这行代码执行后，实际上数据缓存到一级缓存中（sqlSession1一级缓存）
        System.out.println(car1);
        sqlSession1.close();   //执行到这，会将sqlSession1这个一级缓存中的数据写入到二级缓存中
        //sqlSession1如果不关闭，二级缓存中没有数据
        CarMapper mapper2 = sqlSession2.getMapper(CarMapper.class);
        Car car2 = mapper2.selectById(3L);  //这行代码执行后，实际上数据缓存到一级缓存中（sqlSession2一级缓存）
        System.out.println(car2);

        sqlSession2.close();   //执行到这，会将sqlSession2这个一级缓存中的数据写入到二级缓存中
    }
    @Test
    public void testSelectById() throws Exception{
        //如果要获取不同的SqlSession对象，不能用工具类
        //SqlSession sqlSession = SqlSessionUtil.openSession();
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        CarMapper mapper1 = sqlSession1.getMapper(CarMapper.class);
        Car car1 = mapper1.selectById(3L);
        System.out.println(car1);
        CarMapper mapper2 = sqlSession2.getMapper(CarMapper.class);
        Car car2 = mapper2.selectById(3L);
        System.out.println(car2);
        sqlSession1.close();
        sqlSession2.close();
    }
//    @Test
//    public void testSelectById(){
//        SqlSession sqlSession = SqlSessionUtil.openSession();
//        CarMapper mapper = sqlSession.getMapper(CarMapper.class);
//        Car car1 = mapper.selectById(3L);
//        System.out.println(car1);
//        Car car2 = mapper.selectById(3L);
//        System.out.println(car2);
//        sqlSession.close();
//    }
}
