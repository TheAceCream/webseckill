package com.cream.dao;

import com.cream.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * Author:Cream
 * Date:2018/4/15
 * Description:
 */
public interface SuccessKilledDao {

    /**
     * 插入购买明细
     * 可以过滤掉重复的
     * @param seckillId
     * @param userPhone
     * @return 插入的行数
     */
    int insertSuccessKilled(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);


    /**
     * 根据id查询SuccessKilled
     * 并携带秒杀产品对象实体
     * @param seckillId
     * @param userPhone
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);

}
