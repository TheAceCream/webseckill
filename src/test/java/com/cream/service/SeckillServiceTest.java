package com.cream.service;

import com.cream.dao.SeckillDao;
import com.cream.dto.Exposer;
import com.cream.dto.SeckillExecution;
import com.cream.entity.Seckill;
import com.cream.exception.RepeatKillException;
import com.cream.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Author:Cream
 * Date:2018/4/16
 * Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void getById() {
        long id = 1000;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}",seckill);
    }


    //测试代码完整逻辑，注意可重复执行
    @Test
    public void exportSeckillLogic() {
        /**
         *Exposer{
         *  exposed=true,
         *  md5='688cd5b08d080daa8fc609e575bbe1ee',
         *  seckillId=1000,
         *  now=0,start=0,end=0}
         */
        long id = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()){
            logger.info("exposer={}"+exposer);
            long phone = 130000000122L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id,phone,md5);
                logger.info("result={}",seckillExecution);
            }catch (RepeatKillException e){
                logger.error(e.getMessage());
            }catch (SeckillCloseException e){
                logger.error(e.getMessage());
            }
        }else {
            //秒杀未开启
            /**
             * Exposer{
             *  exposed=false,
             *  md5='null',
             *  seckillId=1001,
             *  now=1523869445005, start=1446307200000, end=1446393600000}
             */
            logger.warn("exposer={}",exposer);
        }
    }


}