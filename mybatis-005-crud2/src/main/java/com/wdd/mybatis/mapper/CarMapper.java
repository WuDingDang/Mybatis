package com.wdd.mybatis.mapper;

import com.wdd.mybatis.pojo.Car;

import java.util.List;

public interface CarMapper {
    int insert(Car car);
    int deleteById(Long id);
    int updateCar(Car car);
    Car selectById(Long id);
    List<Car> selectAll();
}
