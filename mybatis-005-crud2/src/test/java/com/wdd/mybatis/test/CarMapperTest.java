package com.wdd.mybatis.test;

import com.wdd.mybatis.mapper.CarMapper;
import com.wdd.mybatis.pojo.Car;
import com.wdd.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class CarMapperTest {
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
    @Test
    public void testDelete(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        CarMapper mapper = sqlSession.getMapper(CarMapper.class);
        int count = mapper.deleteById(5L);
        System.out.println(count);
        sqlSession.commit();

    }
    @Test
    public void testUpdate(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        CarMapper mapper = sqlSession.getMapper(CarMapper.class);
        Car car = new Car(6L,"1006","宝马X6",56.9,"2023-10-13","燃油车");
        int count = mapper.updateCar(car);
        System.out.println(count);
        sqlSession.commit();

    }
    @Test
    public void testSelect(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        CarMapper mapper = sqlSession.getMapper(CarMapper.class);
        Car car = mapper.selectById(6L);
        System.out.println(car);

    }
    @Test
    public void testSelectAll(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        CarMapper mapper = sqlSession.getMapper(CarMapper.class);
        List<Car> cars = mapper.selectAll();
        for(Car car:cars){
            System.out.println(car);
        }

    }
}
