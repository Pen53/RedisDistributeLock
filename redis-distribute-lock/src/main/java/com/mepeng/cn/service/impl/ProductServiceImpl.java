package com.mepeng.cn.service.impl;

import com.mepeng.cn.domain.Product;
import com.mepeng.cn.mapper.ProductDao;
import com.mepeng.cn.service.ProductService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

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
}
