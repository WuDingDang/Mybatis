package com.wdd.mybatis.mapper;

import com.wdd.mybatis.pojo.Student;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StudentMapper {
    /**
     * 当接口中的方法参数只有一个（单个参数），并且参数的数据类型都是简单类型
     * 根据 id/name/birth/sex
     */
    List<Student> selectById(Long id);
    List<Student> selectByName(String name);
    List<Student> selectByBirth(Date birth);
    List<Student> selectBySex(Character sex);

    /**
     * 保存学生信息，通过map参数，以下是单个参数，但参数类型不是简单类型，是map集合
     * @param map
     * @return
     */
    int insertStudentByMap(Map<String,Object> map);

    /**
     * 保存学生信息，通过POJO参数。Student是单个参数，但不是简单类型
     * @param student
     * @return
     */
    int insertStudentByPOJO(Student student);

    /**
     * 根据name和sex查询学生信息
     * 多个参数，mybatis框架底层是怎么做的？
     *       mybatis框架会自动创建一个map集合，并且map集合是以这种方式存储的
     *          map.put("arg0",name);
     *          map.put("arg1",sex);
     *          map.put("param1",name);
     *          map.put("param2",sex);
     * @param name
     * @param sex
     * @return
     */
    List<Student> selectByNameAndSex(String name,Character sex);

    /**
     * Param注解
     * mybatis框架底层的实现原理：
     *  map.put("name",name);
     *  map.put("sex",sex);
     * @param name
     * @param sex
     * @return
     */
    List<Student> selectByNameAndSex2(@Param("name") String name, @Param("sex") Character sex);
}
