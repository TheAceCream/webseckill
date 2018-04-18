package com.cream.dao;

import com.cream.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Author:Cream
 * Date:2018/4/16
 * Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        //第一次 1
        //第二次 0
        //通过 id和userPhone 联合主键保证了秒杀一次
        long id = 1000L;
        long phone = 13000000000L;
        int insertCount = successKilledDao.insertSuccessKilled(id,phone);
        System.out.println("insertCount=" + insertCount);
    }

    @Test
    public void queryBiIdWithSeckill() {
        /**
         SuccessKilled{
            seckillId=1000,
            userPhone=13000000000,
            state=0,
            createTime=Mon Apr 16 14:46:08 CST 2018,
            seckill=Seckill{seckillId=1000, name='1000元秒杀iphone6', number=100, startTime=Sun Nov 01 00:00:00 CST 2015, endTime=Mon Nov 02 00:00:00 CST 2015, createTime=Sun Apr 15 15:43:13 CST 2018}}

         Seckill{
            seckillId=1000,
            name='1000元秒杀iphone6',
            number=100,
            startTime=Sun Nov 01 00:00:00 CST 2015,
            endTime=Mon Nov 02 00:00:00 CST 2015,
            createTime=Sun Apr 15 15:43:13 CST 2018}
         */
        long id = 1000L;
        long phone = 13000000000L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id,phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}