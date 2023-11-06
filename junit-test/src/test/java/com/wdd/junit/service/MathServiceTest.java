package com.wdd.junit.service;

import org.junit.Assert;
import org.junit.Test;

public class MathServiceTest {    //名字规范：测试类名+Test
    //一个业务方法对应一个测试方法
    //测试方法的规范：public void testXxx(){}
    //测试方法的方法名：以test开始：假设要测试的方法是test，那么测试方法名为testSum
    //被@Test 注解标注的方法就是一个单元测试方法
    @Test
    public void testSum(){
        //单元测试中有两个重要概念：
        //1.实际值（被测试的业务方法的真正执行结果）
        //2.期望值（执行这个业务方法之后，期望的执行结果）
        MathService mathService = new MathService();
        //获取实际值
        int actual = mathService.sum(1, 2);
        //期望值
        int excepted = 30;
        //加断言进行测试
        Assert.assertEquals(excepted,actual);

    }
    @Test
    public void testSub(){
        MathService mathService = new MathService();
        int actual = mathService.sub(10, 5);
        int excepted = 5;
        Assert.assertEquals(excepted,actual);

    }
}
