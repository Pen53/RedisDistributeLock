package com.mepeng.cn.pen.order.service;

import com.mepeng.cn.pen.common.domains.PO.DemoInfoPO;
import com.mepeng.cn.pen.order.controller.DemoInfoTestDTO;

public interface DemoInfoService {

    public void save(DemoInfoPO po);

    public void testTransaction();

    public void testTransaction1();

    public void testTransaction2();

    public void save(DemoInfoTestDTO dto);

}
