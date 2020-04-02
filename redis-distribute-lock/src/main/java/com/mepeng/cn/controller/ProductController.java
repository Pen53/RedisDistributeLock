package com.mepeng.cn.controller;

import com.mepeng.cn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    /**
     * 秒杀商品测试
     * @return
     */
    @ResponseBody
    @GetMapping("seckillTest")
    public String seckillProductTest() {
        Boolean flag = productService.seckillProduct(1L, 1);
        if(flag ==true){
            return "创建订单成功";
        } else{
            return "库存不足";
        }
    }

    /**
     * 秒杀商品测试
     * @return
     */
    @ResponseBody
    @GetMapping("seckillTest2")
    public String seckillProductTest2() {
        Boolean flag = productService.seckillProductOther(1L, 1);
        if(flag ==true){
            return "创建订单成功";
        } else{
            return "库存不足";
        }
    }
}
