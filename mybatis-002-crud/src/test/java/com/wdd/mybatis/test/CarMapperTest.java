package com.wdd.mybatis.test;

import com.wdd.mybatis.pojo.Car;
import com.wdd.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarMapperTest {

    @Test
    public void testNamespace(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
       // List<Object> cars = sqlSession.selectList("selectAll");
        //namespace.id
        List<Object> cars = sqlSession.selectList("xxxaaaa.selectAll");
        cars.forEach(car -> System.out.println(car));
        sqlSession.close();
    }

    @Test
    public void testSelectAll(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        //mybatis底层执行select语句之后，一定会返回一个结果集对象ResultSet
        //JDBC中叫ResultSet，接下来mybatis应该从ResultSet中取数据，封装java对象
        List<Object> cars = sqlSession.selectList("selectAll");
        cars.forEach(car -> System.out.println(car));
        sqlSession.close();
    }

    @Test
    public void testSelectById(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        //mybatis底层执行select语句之后，一定会返回一个结果集对象ResultSet
        //JDBC中叫ResultSet，接下来mybatis应该从ResultSet中取数据，封装java对象
        Object car = sqlSession.selectOne("selectById", 1);
        System.out.println(car.toString());
        sqlSession.close();
    }

    @Test
    public void testUpdateById(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        Car car = new Car(4L, "9999", "丰田2.0", 30.3, "2021-12-09", "燃油车");
        int count = sqlSession.update("updateById",car);
        System.out.println(count);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void testDeleteById(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        int count = sqlSession.delete("deleteById", 10);
        System.out.println(count);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void testInsertCarByPOJO(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        Car car = new Car(null, "3333", "比亚迪秦", 30.0, "2022-1-12", "新能源");
        int count = sqlSession.insert("insertCar",car);
        System.out.println(count);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void testInsertCar(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        //先使用Map集合进行数据封装
        Map<String,Object> map = new HashMap<>();
//        map.put("k1","1111");
//        map.put("k2","比亚迪");
//        map.put("k3",10.0);
//        map.put("k4","2011-11-3");
//        map.put("k5","电车");
        map.put("carNum","1111");
        map.put("brand","比亚迪");
        map.put("guidePrice",10.0);
        map.put("produceTime","2011-11-3");
        map.put("carType","电车");
        //第一个参数:sqlId
        //第二个对象：封装数据的对象
        int count = sqlSession.insert("insertCar",map);
        System.out.println(count);
        sqlSession.commit();
        sqlSession.close();

    }
}
