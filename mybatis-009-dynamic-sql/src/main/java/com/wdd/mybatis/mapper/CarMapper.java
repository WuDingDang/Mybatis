package com.wdd.mybatis.mapper;

import com.wdd.mybatis.pojo.Car;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarMapper {
    /**
     * 多条件查询
     * @param brand
     * @param guidePrice
     * @param carType
     * @return
     */
    List<Car> selectByMultiCondition(@Param("brand") String brand, @Param("guidePrice") Double guidePrice,@Param("carType") String carType);

    /**
     * 使用where标签，让where子句更加智能
     * @param brand
     * @param guidePrice
     * @param carType
     * @return
     */
    List<Car> selectByMultiConditionWithWhere(@Param("brand") String brand, @Param("guidePrice") Double guidePrice,@Param("carType") String carType);

    /**
     * 使用trim标签
     * @param brand
     * @param guidePrice
     * @param carType
     * @return
     */
    List<Car> selectByMultiConditionWithTrim(@Param("brand") String brand, @Param("guidePrice") Double guidePrice,@Param("carType") String carType);

    /**
     * 更新car
     * @param car
     * @return
     */
    int updateById(Car car);

    /**
     * 使用set标签更新car
     * @param car
     * @return
     */
    int updateBySet(Car car);

    /**
     * 使用choose when otherwise 标签
     * @param brand
     * @param guidePrice
     * @param carType
     * @return
     */
    List<Car> selectByChoose(@Param("brand") String brand,@Param("guidePrice")Double guidePrice,@Param("carType")String carType);

    /**
     * 批量删除 foreach标签
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") Long[] ids);

    /**
     * 批量插入
     * @param cars
     * @return
     */
    int insertBatch(@Param("cars") List<Car> cars);

    /**
     * 根据id批量删除，使用or
     * @param ids
     * @return
     */
    int deleteByIds2(@Param("ids") Long[] ids);

}
