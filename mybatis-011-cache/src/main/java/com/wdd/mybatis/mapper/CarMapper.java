package com.wdd.mybatis.mapper;

import com.wdd.mybatis.pojo.Car;

public interface CarMapper {
    /**
     * 根据id获取car信息
     * @param id
     * @return
     */
    Car selectById(Long id);
}
