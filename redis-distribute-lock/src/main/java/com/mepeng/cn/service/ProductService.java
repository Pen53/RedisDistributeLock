package com.mepeng.cn.service;

public interface ProductService {
    public Boolean seckillProduct(Long productId, Integer number);

    public Boolean seckillProductOther(Long productId, Integer number);
}
