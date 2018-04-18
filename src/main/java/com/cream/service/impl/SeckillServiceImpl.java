package com.cream.service.impl;

import com.cream.dao.SeckillDao;
import com.cream.dao.SuccessKilledDao;
//import com.cream.dao.cache.RedisDao;
import com.cream.dto.Exposer;
import com.cream.dto.SeckillExecution;
import com.cream.entity.Seckill;
import com.cream.entity.SuccessKilled;
import com.cream.enums.SeckillStatEnum;
import com.cream.exception.RepeatKillException;
import com.cream.exception.SeckillCloseException;
import com.cream.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cream.service.SeckillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Author:Cream
 * Date:2018/4/15
 * Description:
 */
@Service
public class SeckillServiceImpl implements SeckillService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //注入Service依赖
    @Resource
    private SeckillDao seckillDao;

    @Resource
    private SuccessKilledDao successKilledDao;

//    @Resource
//    private RedisDao redisDao;

    //md5 盐值字符串，作用是混淆md5
    private final String salt = "qwpopklsj23423^&*a(#!^&%2:qw2@3assd";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //优化点：缓存优化
        Seckill seckill = seckillDao.queryById(seckillId);
        /**
         * get from cache
         * if null
         *  get db
         *  else
         *   put cache
         *   locgoin
         */

        if (seckill == null){
            return new Exposer(false,seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //系统当前时间
        Date nowTime = new Date();
        if (nowTime.getTime()<startTime.getTime() || nowTime.getTime()>endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        //转化特定字符串的过程 此过程不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    @Override
    @Transactional
    /**
     * 使用注解控制事务的优点：
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格。
     * 2.保证事务方法的执行时间尽可能的短，不要穿插其他网络操作RPC/HTTP 或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如果只有一条修改操作，只读操作不需要事务控制。
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5==null||!md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑：减库存 + 购买记录行为
        Date nowTime = new Date();
        try {
            //减库存，热点商品竞争
            int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
            if (updateCount<=0){
                //没有更新到记录，秒杀结束，rollback
                throw new SeckillCloseException("seckill is closed");
            } else {
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
                //唯一：seckillId , userPhone
                if (insertCount<=0){
                    //重复秒杀
                    throw new RepeatKillException("seckill repeated");
                }else {
                    //秒杀成功 commit
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }
            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            //所有编译期异常 转化为运行期异常
            throw new SeckillException("seckill inner error:"+ e.getMessage());
        }
    }

    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        return null;
    }


    /**
     * 进行md5 加密
     * @param seckillId
     * @return
     */
    private String getMD5(long seckillId){
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

}
