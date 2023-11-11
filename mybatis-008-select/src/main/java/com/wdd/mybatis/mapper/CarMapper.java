package com.wdd.mybatis.mapper;

import com.wdd.mybatis.pojo.Car;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface CarMapper {
    /**
     * 根据id查询car
     * @param id
     * @return
     */
    Car selectById(Long id);

    /**
     * 获取所有car
     * @return
     */
    List<Car> selectAll();

    /**
     * 根据品牌进行模糊查询
     * 模糊查询的结果可能是多个，但是采用一个POJO对象接收
     * @param brand
     * @return
     */
    Car selectByBrandLike(String brand);

    /**
     * 根据id获取汽车信息，将汽车信息放到map集合中
     * Map<String,Object>
     *     k             v
     *     "id"          3
     *     "car_num"    1003
     *     "brand"      丰田
     *     ...
     * @return
     */
    Map<String,Object> selectByIdRetMap(Long id);

    /**
     * 查询所有car，返回一个放Map集合的List集合
     * @return
     */
    List<Map<String,Object>> selectAllRetListMap();

    /**
     * 查询所有Car，返回一个大Map
     * Map集合的key是每条记录的主键值
     * Map集合的value是每条记录
     * @return
     */
    @MapKey("id")    //将查询结果的id值作为整个大Map集合的key
    Map<Long,Map<String,Object>> selectAllRetMap();

    /**
     * 查询所有car，使用resultMap标签进行结果映射
     * @return
     */
    List<Car> selectAllByReultMap();

    /**
     * 查询所有car，启用驼峰命名自动映射机制
     * @return
     */
    List<Car> selectByMapUnderscoreToCamelCase();

    /**
     * 获取car的总记录条数
     * @return
     */
    Long selectTotal();

}
