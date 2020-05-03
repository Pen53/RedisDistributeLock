package com.mepeng.cn.pen.order.service.impl;

import com.mepeng.cn.pen.common.annotation.TxConnection;
import com.mepeng.cn.pen.common.domains.PO.DemoInfoPO;
import com.mepeng.cn.pen.common.domains.PO.DemoPO;
import com.mepeng.cn.pen.order.controller.DemoInfoTestDTO;
import com.mepeng.cn.pen.order.service.DemoInfoService;
import org.javalite.activejdbc.Model;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DemoInfoServiceImpl implements DemoInfoService {

    @Override
    public void save(DemoInfoPO po) {
        boolean flag = po.saveIt();
        System.out.println("DemoInfoServiceImpl -- save DemoInfoPO flag:"+flag);
    }

    @Override
    public void testTransaction() {
        Date cur = new Date();
        int affect = DemoInfoPO.update("name=?,crt_date=?", "id=?", "测试23", cur, 1);

        int affect2 = DemoInfoPO.update("name=?,crt_date=?", "id=?", "测试34", cur, 2);
        System.out.println("DemoInfoServiceImpl -- testTransaction affect:"+affect+",affect2:"+affect2);
        if("123".length()==3){
            throw  new RuntimeException("测试事务回滚");
        }
    }

    @TxConnection
    @Override
    public void testTransaction1() {
        Date cur = new Date();
        int affect = DemoInfoPO.update("name=?,crt_date=?", "id=?", "测试67", cur, 1);

        int affect2 = DemoInfoPO.update("name=?,crt_date=?", "id=?", "测试78", cur, 2);
        System.out.println("DemoInfoServiceImpl -- testTransaction1 affect:"+affect+",affect2:"+affect2);

    }

    @TxConnection
    @Override
    public void testTransaction2() {
        Date cur = new Date();
        int affect = DemoInfoPO.update("name=?,crt_date=?", "id=?", "测试67", cur, 1);

        int affect2 = DemoInfoPO.update("name=?,crt_date=?", "id=?", "测试78", cur, 2);
        System.out.println("DemoInfoServiceImpl -- testTransaction2 affect:"+affect+",affect2:"+affect2);
        if("123".length()==3){
            throw  new RuntimeException("2个TxConnection注释 测试事务回滚");
        }
    }

    @TxConnection
    @Override
    public void save(DemoInfoTestDTO dto) {
        Model entity = DemoPO.findFirst("name = ?", dto.getOrderNo());

        if(entity!=null){
            System.out.println("--------dto.orderNo"+dto.getOrderNo()+" is savetoDb---------");
            return ;
        }
        DemoPO po = new DemoPO();
        po.set("name",dto.getOrderNo());
        po.set("crt_date",new Date());
        boolean r = po.saveIt();
        System.out.println("DemoInfoServiceImpl save(DemoInfoTestDTO dto) r:"+r);
    }
}
