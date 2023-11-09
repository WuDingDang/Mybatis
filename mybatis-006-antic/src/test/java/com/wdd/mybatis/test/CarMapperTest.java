package com.wdd.mybatis.test;

import com.wdd.mybatis.mapper.CarMapper;
import com.wdd.mybatis.pojo.Car;
import com.wdd.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class CarMapperTest {
    @Test
    public void testInsertCarUseGeneratedKeys(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        CarMapper mapper = sqlSession.getMapper(CarMapper.class);
        Car car = new Car(null,"1004","奔驰s300",89.3,"2022-4-23","燃油车");
        int count = mapper.insertCarUseGeneratedKeys(car);
        System.out.println(car);
        sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void testSelectByBrandLike(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        CarMapper mapper = sqlSession.getMapper(CarMapper.class);
        List<Car> cars = mapper.selectByBrandLike("奔驰");
        cars.forEach(car -> System.out.println(car));
        sqlSession.close();

    }
    @Test
    public void testDeleteBatch(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        CarMapper mapper = sqlSession.getMapper(CarMapper.class);
        int count = mapper.deleteBatch("1,2");
        System.out.println(count);
        sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void testSelectByCarType(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        CarMapper mapper = sqlSession.getMapper(CarMapper.class);
        List<Car> cars = mapper.selectByCarType("新能源");
        for(Car car : cars){
            System.out.println(car);
        }
        sqlSession.close();
    }

    @Test
    public void testSelectAllByAscOrDesc(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        CarMapper mapper = sqlSession.getMapper(CarMapper.class);
        List<Car> cars = mapper.selectAllByAscOrDesc("asc");
        for(Car car : cars){
            System.out.println(car);
        }
        sqlSession.close();

    }

}
