package com.cream.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.cream.entity.Seckill;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import org.slf4j.Logger;

/**
 * Author:Cream
 * Date:2018/4/15
 * Description:
 *
 */
public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    public RedisDao(String id,int port) {
        jedisPool = new JedisPool(id,port);
    }

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    /**
     * 从Redis中 取出Seckill对象
     * @param seckillId
     * @return
     */
    public Seckill getSeckill(long seckillId){
        //redis 操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:"+seckillId;
                //并没有实现内部序列化操作
                //get -> byte[] -> 反序列化 -> Object(seckill)
                //采用自定义序列化
                // protostuff:pojo
                byte[] bytes = jedis.get(key.getBytes());
                //缓存中后取到bytes
                if (bytes!=null){
                    //空对象
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                    //seckill 反序列化
                    return seckill;
                }
            }finally {
                jedis.close();
            }

        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 向Redis里放seckill对象
     * @param seckill
     * @return
     */
    public String putSeckill(Seckill seckill){
        //set Object(Seckill) ->序列化 ->byte[]
        try{
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill,schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存 一小时
                int timeout = 60 * 60;
                String result = jedis.setex(key.getBytes(),timeout,bytes);
                return result;
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

}
