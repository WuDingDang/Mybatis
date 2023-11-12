package com.wdd.mybatis.mapper;

import com.wdd.mybatis.pojo.Student;

public interface StudentMapper {
    /**
     * 根据id获取学生信息，同时获取学生关联的班级信息
     * @param id
     * @return
     */
    Student selectById(Integer id);

}
