package com.mepeng.cn.mapper;

import com.mepeng.cn.domain.Product;

public interface ProductDao {

    Product selectById(Long productId);

    void updateStocksById(Long productId, Integer stocks);
}
