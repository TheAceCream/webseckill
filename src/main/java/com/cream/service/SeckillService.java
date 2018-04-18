package com.cream.service;

import com.cream.dto.Exposer;
import com.cream.dto.SeckillExecution;
import com.cream.entity.Seckill;
import com.cream.exception.RepeatKillException;
import com.cream.exception.SeckillCloseException;
import com.cream.exception.SeckillException;

import java.util.List;

/**
 * Author:Cream
 * Date:2018/4/15
 * Description:
 * 业务接口：站在"使用者"的角度
 * 三个方面：
 * 1.方法定义粒度
 * 2.参数
 * 3.返回值类型(return 类型/异常)
 */
public interface SeckillService {

    /**
     * 查询所有秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 通过id 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启 输出 秒杀接口地址
     * 秒杀未开启 输出 系统时间 和 秒杀时间
     * @param seckillId
     * @return
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */
    SeckillExecution executeSeckill(long seckillId,long userPhone,String md5) throws SeckillException, RepeatKillException, SeckillCloseException;

    /**
     * 执行秒杀过程操作 （通过存储过程实现）
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecution executeSeckillProcedure(long seckillId,long userPhone,String md5);


}
