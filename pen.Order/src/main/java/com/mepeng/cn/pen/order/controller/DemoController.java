package com.mepeng.cn.pen.order.controller;

import com.alibaba.fastjson.JSON;
import com.mepeng.cn.pen.common.annotation.TxConnection;
import com.mepeng.cn.pen.common.dao.DAOUtil;
import com.mepeng.cn.pen.common.dao.DefinedRowProcessor;
import com.mepeng.cn.pen.common.dao.PageInfoDto;
import com.mepeng.cn.pen.common.dao.ResponseMsg;
import com.mepeng.cn.pen.common.domains.DTO.RequestPageInfoDto;
import com.mepeng.cn.pen.common.domains.PO.DemoInfoPO;
import com.mepeng.cn.pen.common.mq.MqSender;
import com.mepeng.cn.pen.common.util.common.StringUtils;
import com.mepeng.cn.pen.order.domains.MakeInstrumentationUtil;
import com.mepeng.cn.pen.order.domains.PO.Employee;
import com.mepeng.cn.pen.order.service.DemoInfoService;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/demoController")
public class DemoController {
    @Autowired
    MqSender mqSender;

    public ResponseMsg msDemo(){
        ResponseMsg r = new ResponseMsg();
        try {
//            System.out.println("DemoController msDemo is doing. ");
//            OrderAuditToSmpsDTO dto = new OrderAuditToSmpsDTO();
//            dto.setOrderNo("orderNo_" + System.currentTimeMillis());
//            dto.setAuditDate(System.currentTimeMillis() + "");
//            mqSender.send(MQConstants.JKYX_ORDER_EXCHANGE, MQConstants.JKYX_ORDER_AUDIT_KEY, dto);
//            System.out.println("DemoController msDemo is doing over. ");
            r.setMsg("success");
            r.setData(true);
        }catch (Exception e){
            r.setMsg(e.getMessage());
            r.setStatus(false);
        }
        return r;
    }
    @TxConnection
    @RequestMapping(value = "/demoNEW1", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMsg demoNEW1(@RequestParam Map<String, Object> queryParam) {
        ResponseMsg r = new ResponseMsg();
        System.out.println("6666-----DemoController id demoNEW1 doing queryParam:"+queryParam);
        try {

            Employee.createIt("first_name", "John", "last_name", "Doe");

            r.setData(1);
            r.setStatus(true);

        }catch (Exception e){
            e.printStackTrace();
            r.setData(0);
            r.setMsg(e.getMessage());
            r.setStatus(false);
        }
        return r;
    }
    @TxConnection
    @RequestMapping(value = "/demoNEW2", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMsg demoNEW2(@RequestParam Map<String, Object> queryParam) {
        ResponseMsg r = new ResponseMsg();
        System.out.println("6666-----DemoController id demoNEW2 doing queryParam:"+queryParam);
        try {
            MakeInstrumentationUtil.make();
            Employee e = Employee.findById(3);
            System.out.println("e:"+e);

            r.setData(1);
            r.setStatus(true);

        }catch (Exception e){
            e.printStackTrace();
            r.setData(0);
            r.setMsg(e.getMessage());
            r.setStatus(false);
        }
        return r;
    }

    @TxConnection
    @RequestMapping(value = "/demo", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMsg demo(@RequestParam Map<String, Object> queryParam) {
        ResponseMsg r = new ResponseMsg();
        System.out.println("6666-----DemoController id demo doing queryParam:"+queryParam);
        try {
            r.setData(1);
            r.setStatus(true);

        }catch (Exception e){
            e.printStackTrace();
            r.setData(0);
            r.setMsg(e.getMessage());
            r.setStatus(false);
        }
        return r;
    }

    @TxConnection
    @RequestMapping(value = "/demoConn", method = RequestMethod.GET)
    @ResponseBody
    public String Demo(){
        System.out.println("Democontroller --demoConn is doing.");
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
        System.out.println("Democontroller --demoConn1 is doing.");
        LazyList<Model> list = DemoInfoPO.findAll();
        System.out.println(list.size());
        for(Model m:list){
            System.out.println("m:"+m);
        }
        DemoInfoPO entity = DemoInfoPO.findById(1);
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
        queryParam.add("2020-04-15 23:59:59");

        PageInfoDto reuslt = DAOUtil.pageQuery(requestPageInfoDto, sqlFinal, queryParam);
        return JSON.toJSONString(reuslt);
    }

    @TxConnection
    @RequestMapping(value = "/demoConn1", method = RequestMethod.GET)
    @ResponseBody
    public String Demo1(){
        System.out.println("Democontroller --demoConn1 is doing.");
        DemoInfoPO entity = DemoInfoPO.findById(1);
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
        String sort = requestPageInfoDto.getSort();
        String order = requestPageInfoDto.getOrder();
        Integer offset = Integer.parseInt(requestPageInfoDto.getOffset());
        Integer pageSize = Integer.parseInt(requestPageInfoDto.getLimit());;
        int page = (offset / pageSize) + 1;


        String sqlFinal = "SELECT * FROM Demo t WHERE t.`crt_date` >= ? AND t.`crt_date` <= ?";
        List<Object> queryParam = new ArrayList<>();

        queryParam.add("2020-04-13 00:00:00");
        queryParam.add("2020-04-15 23:59:59");
        Paginator paginator = new Paginator(pageSize, sqlFinal, queryParam.toArray());
        StringBuilder orders = null;
        if (!StringUtils.isNullOrEmpty(sort)) {
            orders = new StringBuilder();
            String[] sortSplitArray = sort.split(",");
            String[] orderSplitArray = order.split(",");
            for (int i = 0; i < sortSplitArray.length; i++) {
                orders.append(sortSplitArray[i]).append(" ").append(orderSplitArray[i]);
                if (i != (sortSplitArray.length - 1)) {
                    orders.append(",");
                }
            }
        }
        if (orders != null) {
            paginator.orderBy(orders.toString());
        }
        PageInfoDto pageDto = new PageInfoDto();
        pageDto.setTotal(paginator.getCount());
        DefinedRowProcessor processor = null;
        // 设置分页结果
        //pageDto.setRows(getOnePageResult(paginator, page, processor));

        List<Map> results;
        if (processor == null) {
            results = paginator.getPage(page);
        } else {
            paginator.getPage(1, processor);
            results = processor.getResult();
        }
        // 设置分页结果
        pageDto.setRows(results);

        return "ok1";
    }

    @TxConnection
    @RequestMapping(value = "/demoConn2", method = RequestMethod.GET)
    @ResponseBody
    public String Demo2(){
        System.out.println("Democontroller --demoConn2 is doing.");
        DemoInfoPO entity = DemoInfoPO.findById(1);
        Object name = entity.get("name");
        System.out.println("Democontroller --Demo is doing over.name:"+name);
        String n1 = entity.getString("name");

        Object crt_date = entity.get("crt_date");
        //entity.set("crt_date",new Date());
        Date cur = new Date();
        int affect = DemoInfoPO.update("name=?,crt_date=?", "id=?", "测试23", cur, 1);
        System.out.println("n1:"+n1+",crt_date:"+crt_date+",cur:"+cur+",affect:"+affect);
        //entity.saveIt();

        return "ok3";
    }

    @TxConnection
    @RequestMapping(value = "/demoConn3", method = RequestMethod.GET)
    @ResponseBody
    public String Demo3(){
        System.out.println("Democontroller --demoConn3 is doing.");
        DemoInfoPO entity = new DemoInfoPO();
        entity.set("name","name1");
        entity.set("crt_date",new Date());
        boolean flag = entity.saveIt();
        System.out.println("flag:"+flag);
        entity = new DemoInfoPO();
        entity.set("name","name2");
        entity.set("crt_date",new Date());
        flag = entity.saveIt();
        System.out.println("flag2:"+flag);
        return "ok3";
    }

    @TxConnection
    @RequestMapping(value = "/demoConn4", method = RequestMethod.GET)
    @ResponseBody
    public String Demo4(){
        System.out.println("Democontroller --demoConn4 is doing.");
        DemoInfoPO entity = new DemoInfoPO();
        entity.set("name","name4");
        entity.set("crt_date",new Date());
        boolean flag = entity.saveIt();
        System.out.println("flag4:"+flag);
        entity = new DemoInfoPO();
        entity.set("name","name5");
        entity.set("crt_date",new Date());
        flag = entity.saveIt();
        System.out.println("flag5:"+flag);
        if("123".length()==3){
            throw new RuntimeException("test runtimeException");
        }
        return "ok4";
    }
    @Autowired
    private DemoInfoService demoInfoService;

    @TxConnection
    @RequestMapping(value = "/demoConn5", method = RequestMethod.GET)
    @ResponseBody
    public String Demo5(){
        System.out.println("Democontroller --Demo5 is doing.");
        DemoInfoPO entity = new DemoInfoPO();
        entity.set("name","name_Demo5");
        entity.set("crt_date",new Date());
        demoInfoService.save(entity);
        return "okDemo5";
    }

    @TxConnection
    @RequestMapping(value = "/demoConn6", method = RequestMethod.GET)
    @ResponseBody
    public String Demo6(){
        System.out.println("Democontroller --Demo6 is doing.");
        demoInfoService.testTransaction();
        return "okDemo6";
    }

    @TxConnection
    @RequestMapping(value = "/demoConn7", method = RequestMethod.GET)
    @ResponseBody
    public String Demo7(){
        System.out.println("包含2个TxConnection 注释,测试事务Democontroller --Demo6 is doing.");
        demoInfoService.testTransaction1();
        return "okDemo6";
    }
    @TxConnection
    @RequestMapping(value = "/demoConn8", method = RequestMethod.GET)
    @ResponseBody
    public String Demo8(){
        System.out.println("包含2个TxConnection 注释,测试事务回滚Democontroller --Demo6 is doing.");
        demoInfoService.testTransaction2();
        return "okDemo6";
    }
}
