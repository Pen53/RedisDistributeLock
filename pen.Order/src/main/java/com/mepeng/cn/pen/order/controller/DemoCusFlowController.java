package com.mepeng.cn.pen.order.controller;

import com.alibaba.fastjson.JSON;
import com.mepeng.cn.pen.common.annotation.LoginIntercept;
import com.mepeng.cn.pen.common.annotation.TxConnection;
import com.mepeng.cn.pen.common.constants.MQConstants;
import com.mepeng.cn.pen.common.dao.DAOUtil;
import com.mepeng.cn.pen.common.dao.PageInfoDto;
import com.mepeng.cn.pen.common.dao.ResponseMsg;
import com.mepeng.cn.pen.common.domains.DTO.LoginInfoDto;
import com.mepeng.cn.pen.common.domains.DTO.RequestPageInfoDto;
import com.mepeng.cn.pen.common.domains.PO.DemoPO;
import com.mepeng.cn.pen.common.mq.MqSender;
import com.mepeng.cn.pen.common.util.bean.ApplicationContextHelper;
import org.javalite.activejdbc.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@LoginIntercept
@RequestMapping("/demoCusFlowController")
@RestController
public class DemoCusFlowController {
    public static void main(String[] args) {
        LoginIntercept lt = DemoCusFlowController.class.getAnnotation(LoginIntercept.class);
        System.out.println("lt:"+lt);
    }
    @Autowired
    MqSender mqSender;

    /**
     * 测试从中台获取登陆信息
     * @return
     */
    @LoginIntercept
    @RequestMapping(value = "/loginIntercept", method = RequestMethod.GET)
    @ResponseBody
    public String loginIntercept(){
        System.out.println("DemoCusFlowController ---loginIntercept is doing...");
        LoginInfoDto loginInfoDto = ApplicationContextHelper.getBeanByType(LoginInfoDto.class);
        System.out.println("loginInfoDto json:"+JSON.toJSONString(loginInfoDto));
        return "loginIntercept is ok loginInfoDto:"+JSON.toJSONString(loginInfoDto);
    }
    @TxConnection
    @RequestMapping(value = "/demoConn3", method = RequestMethod.GET)
    @ResponseBody
    public String Demo3(){
        System.out.println("Democontroller --demoConn3 is doing.");
        DemoPO entity = new DemoPO();
        entity.set("name","name1");
        entity.set("crt_date",new Date());
        boolean flag = entity.saveIt();
        System.out.println("flag:"+flag);
        entity = new DemoPO();
        entity.set("name","name2");
        entity.set("crt_date",new Date());
        flag = entity.saveIt();
        System.out.println("flag2:"+flag);
        return "ok3";
    }

    @TxConnection
    @RequestMapping(value = "/msDemo", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMsg msDemo(){
        ResponseMsg r = new ResponseMsg();
        try {
            System.out.println("DemoCusFlowController msDemo is doing. mqSender:"+mqSender);
//            Map<String,Object> dto = new HashMap<>();
//            Date cur = new Date();
//            dto.put("orderNo","orderNo_" + System.currentTimeMillis());
//            dto.put("cur",cur);
//            dto.put("curTime",cur.getTime());
            DemoInfoTestDTO dto = new DemoInfoTestDTO();
            dto.setOrderNo("orderNo_" + System.currentTimeMillis());
            System.out.println("dto.toString:"+dto.toString());
            //mqSender.send(MQConstants.JKYX_ORDER_EXCHANGE, MQConstants.JKYX_ORDER_AUDIT_KEY, dto.toString());
            mqSender.send(MQConstants.JKYX_ORDER_EXCHANGE, MQConstants.JKYX_ORDER_AUDIT_KEY, dto);
            System.out.println("DemoCusFlowController msDemo is doing over. ");
            r.setMsg("success");
            r.setData(dto);
            r.setStatus(true);
        }catch (Exception e){
            r.setMsg(e.getMessage());
            r.setStatus(false);
        }
        return r;
    }
    @TxConnection
    @RequestMapping(value = "/demoConnPO", method = RequestMethod.GET)
    @ResponseBody
    public String DemoPO(){
        System.out.println("DemoCusFlowController --demoConnPO is doing.");
        DemoPO entity = DemoPO.findById(1);
        Object name = entity.get("name");
        System.out.println("Democontroller --DemoPO is doing over.name:"+name);
        return "ok:"+name;
    }
    @TxConnection
    @RequestMapping(value = "/demoConn", method = RequestMethod.GET)
    @ResponseBody
    public String Demo(){
        System.out.println("DemoCusFlowController --demoConn is doing.");
        List<Map> list = Base.findAll("SELECT * FROM Demo WHERE id = ?", 1);
        for(Map map:list){
            System.out.println("map:"+map);
        }
        System.out.println("Democontroller --Demo is doing over.");
        return "ok";
    }
    @TxConnection
    @RequestMapping(value = "/demoConnPage", method = RequestMethod.GET)
    @ResponseBody
    public String demoConnPage(){
        System.out.println("DemoCusFlowController --demoConn1 is doing.");
        DemoPO entity = DemoPO.findById(1);
        Object name = entity.get("name");
        System.out.println("Democontroller --Demo is doing over.name:"+name);
        String n1 = entity.getString("name");

        Object crt_date = entity.get("crt_date");
        System.out.println("n1:"+n1+",crt_date:"+crt_date);

        RequestPageInfoDto requestPageInfoDto = requestPageInfoDto = new RequestPageInfoDto();
        requestPageInfoDto.setLimit("2");
        requestPageInfoDto.setOffset("0");
        requestPageInfoDto.setSort("crt_date");
        requestPageInfoDto.setOrder("desc");

        String sqlFinal = "SELECT * FROM Demo t WHERE t.`crt_date` >= ? AND t.`crt_date` <= ?";
        List<Object> queryParam = new ArrayList<>();

        queryParam.add("2020-04-13 00:00:00");
        queryParam.add("2020-05-15 23:59:59");

        PageInfoDto reuslt = DAOUtil.pageQuery(requestPageInfoDto, sqlFinal, queryParam);
        return JSON.toJSONString(reuslt);
    }
}
