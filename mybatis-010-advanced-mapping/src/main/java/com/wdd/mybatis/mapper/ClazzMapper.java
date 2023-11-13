package com.wdd.mybatis.mapper;

import com.wdd.mybatis.pojo.Clazz;

public interface ClazzMapper {
    /**
     * 分步查询第二步：根据id获取班级信息
     * @param id
     * @return
     */
    Clazz selectByIdStep2(Integer id);

    /**
     * 根据班级编号查询班级信息
     * @param id
     * @return
     */
    Clazz selectByCollection(Integer id);

    /**
     * 分步查询：第一步：根据班级编号获取班级信息
     * @param id
     * @return
     */
    Clazz selectByStep1(Integer id);
}
