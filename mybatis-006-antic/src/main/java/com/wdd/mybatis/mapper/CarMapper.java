package com.wdd.mybatis.mapper;

import com.wdd.mybatis.pojo.Car;

import java.util.List;

public interface CarMapper {
    /**
     * 插入car信息，并且使用生成的主键值
     * @param car
     * @return
     */
    int insertCarUseGeneratedKeys(Car car);

    /**
     * 根据汽车品牌进行模糊查询
     * @param brand
     * @return
     */
    List<Car> selectByBrandLike(String brand);

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    int deleteBatch(String ids);

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
