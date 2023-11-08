package com.wdd.mybatis.mapper;

import com.wdd.mybatis.pojo.Car;

import java.util.List;

public interface CarMapper {
    /**
     * 根据汽车类型获取汽车信息
     * @param catType
     * @return
     */
    List<Car> selectByCarType(String catType);

    /**
     * 查询所有汽车信息，asc升序，desc降序
     * @param ascOrDesc
     * @return
     */
    List<Car> selectAllByAscOrDesc(String ascOrDesc);

}
