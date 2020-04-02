package com.mepeng.cn.service.impl;

import com.mepeng.cn.domain.Product;
import com.mepeng.cn.mapper.ProductDao;
import com.mepeng.cn.service.ProductService;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    @Qualifier(value = "redissonClient")
    @Autowired
    private RedissonClient redissonClient;
    /**
     * 秒杀商品
     * @param productId 商品ID
     * @param number 数量
     * @return
     */
    @Override
    public Boolean seckillProduct(Long productId, Integer number) {
        String key = "seckill_stock_lock_" + productId;
        RLock lock = redissonClient.getLock(key);
        try {
            //获取分布式锁
            lock.lock();
            Product product = productDao.selectById(productId);
            if ( product!=null && product.getStocks() == 0) {
                return false;
            }
            Integer stocks = product.getStocks() - 1;
            productDao.updateStocksById(productId,stocks);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return true;
    }

    /**
     * 改进通过锁定10S后释放，把库存数量减好放入redis中
     * 业务处理好了后　从redis拿到库存数量并且更新到数据库表
     * @param productId
     * @param number
     * @return
     */
    @Override
    public Boolean seckillProductOther(Long productId, Integer number) {

        String key = "seckill_stock_lock_" + productId;
        RLock lock = redissonClient.getLock(key);
        try {
            //获取分布式锁
            lock.lock(10, TimeUnit.SECONDS);//10s后自动释放
            RMap<Object, Object> map = redissonClient.getMap(key);
            Object obj = map.get("stock");
            if(obj==null){
                //redis缓存中不存在 获取锁 初始化 redis库存数量
                RLock lockGlobal = redissonClient.getLock("global_"+key);
                lockGlobal.lock();
                try {

                    obj = map.get("stock");//再次获取redis中的库存数 防止库存不对
                    if(obj!=null){
                        int stock = (int) map.get("stock");
                        if ((stock - number) >= 0) {
                            map.put("stock", (stock - number));
                            productDao.updateStocksById(productId, stock - number);
                        } else {
                            return false;
                        }
                    }

                    Product product = productDao.selectById(productId);
                    if (product != null) {
                        int stock = product.getStocks();
                        if ((stock - number) >= 0) {
                            map.put("stock", (stock - number));
                            productDao.updateStocksById(productId, stock - number);
                        } else {
                            return false;
                        }
                    }else{
                        return false;
                    }

                }catch (Exception ee){
                    ee.printStackTrace();
                }finally {
                    if(lock.isLocked()) {
                        lock.unlock();
                    }
                    if(lockGlobal.isLocked()){
                        lockGlobal.unlock();
                    }
                }
                return true;

            }else {
                int stock = (int) map.get("stock");
                if ((stock - number) >= 0) {
                    map.put("stock", (stock - number));
                    productDao.updateStocksById(productId, stock - number);
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(lock.isLocked()) {
                lock.unlock();
            }
        }
        return true;
    }
}
