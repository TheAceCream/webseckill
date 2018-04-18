package com.cream.dao;

import com.cream.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Author:Cream
 * Date:2018/4/16
 * Description:
 * 配置Spring和Junit整合，junit启动时加载springIOC容器
 * spring-test,junit
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void queryById() throws Exception{
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
        /**
         * 1000元秒杀iphone6
         Seckill
         {seckillId=1000,
         name='1000元秒杀iphone6',
         number=100,
         startTime=Sun Nov 01 00:00:00 CST 2015,
         endTime=Mon Nov 02 00:00:00 CST 2015,
         createTime=Sun Apr 15 15:43:13 CST 2018}

         */
    }

    @Test
    public void queryAll() throws Exception {
        /**
         *
         Seckill{seckillId=1000, name='1000元秒杀iphone6', number=100, startTime=Sun Nov 01 00:00:00 CST 2015, endTime=Mon Nov 02 00:00:00 CST 2015, createTime=Sun Apr 15 15:43:13 CST 2018}
         Seckill{seckillId=1001, name='500元秒杀ipad2', number=200, startTime=Sun Nov 01 00:00:00 CST 2015, endTime=Mon Nov 02 00:00:00 CST 2015, createTime=Sun Apr 15 15:43:13 CST 2018}
         Seckill{seckillId=1002, name='300元秒杀小米4', number=300, startTime=Sun Nov 01 00:00:00 CST 2015, endTime=Mon Nov 02 00:00:00 CST 2015, createTime=Sun Apr 15 15:43:13 CST 2018}
         Seckill{seckillId=1003, name='200元秒杀红米note', number=400, startTime=Sun Nov 01 00:00:00 CST 2015, endTime=Mon Nov 02 00:00:00 CST 2015, createTime=Sun Apr 15 15:43:13 CST 2018}
         */
        List<Seckill> seckills = seckillDao.queryAll(0,100);
        for (Seckill temp : seckills) {
            System.out.println(temp);
        }

    }

    @Test
    public void reduceNumber() throws Exception{
        /**
         *
         update seckill set number = number - 1
         where seckill_id = ?
         and start_time <= ?
         and end_time >= ?
         and number > 0;
         Parameters: 1000(Long), 2018-04-16 14:40:01.159(Timestamp), 2018-04-16 14:40:01.159(Timestamp)
         Updates: 0
         */
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L,killTime);
        System.out.println("updateCount=" + updateCount);

    }




}