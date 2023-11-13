package com.wdd.mybatis.mapper;

import com.wdd.mybatis.pojo.Student;

import java.util.List;

public interface StudentMapper {
    /**
     * 根据id获取学生信息，同时获取学生关联的班级信息
     * @param id
     * @return
     */
    Student selectById(Integer id);

    /**
     * 一条SQL语句，association
     * @param id
     * @return
     */
    Student selectByIdAssociation(Integer id);

    /**
     * 分步查询第一步：根据学生id查询学生信息
     * @param id
     * @return
     */
    Student selectByIdStep1(Integer id);

    /**
     * 分步查询第二步：根据班级编号查学生信息
     * @param cid
     * @return
     */
    List<Student> selectByCidStep2(Integer cid);


}
