package com.mepeng.cn.pen.common.constants;

public class MQConstants {

    /**
     * 订单服务MQ交换机名称
     */
    public final static String JKYX_ORDER_EXCHANGE = "jkyx_order_exchange";
    
        
    /**
     * 线索服务MQ交换机名称
     */
    public final static String CDMP_CLUE_EXCHANGE = "cdmp_clue_exchange";
    
    /**
     * yunyang服务MQ交换机名称
     */
    public final static String YUNYANG_SERVER_EXCHANGE = "yunyang_server_exchange";
    
    /**
     * 支付单同步队列
     */
    public final static String YUNYANG_PAY_ORDER_SYNC_QUEUE = "yunyang_pay_order_sync_queue";

    /**
     * 支付单同步KEY
     */
    public final static String YUNYANG_PAY_ORDER_SYNC_KEY = "yunyang_pay_order_sync_key";
    
    /**
     * 试驾订单状态变更队列
     */
    public final static String YUNYANG_DRIVE_ORDER_STATUS_CHANGE_QUEUE = "yunyang_drive_order_status_change_queue";

    /**
     * 试驾订单状态变更KEY
     */
    public final static String YUNYANG_DRIVE_ORDER_STATUS_CHANGE_KEY = "yunyang_drive_order_status_change_key";
    
   
    
    /**
     * 用户（店员）信息同步 接口队列
     */
    public final static String YUNYANG_EMPLOYEE_INFO_SYNC_QUEUE = "yunyang_employee_info_sync_queue";

    /**
     * 用户（店员）信息同步 接口KEY
     */
    public final static String YUNYANG_EMPLOYEE_INFO_SYNC_KEY = "yunyang_employee_info_sync_key";
    
    /**
     * 线索结果反馈队列
     */
    public final static String CDMP_CLUE_RES_FEEDBACK_QUEUE = "cdmp_clue_res_feedback_queue";

    /**
     * 线索结果反馈KEY
     */
    public final static String CDMP_CLUE_RES_FEEDBACK_KEY = "cdmp_clue_res_feedback_key";
    
    /**
     * 客户线索新增队列
     */
    public final static String CDMP_CUS_CLUE_ADD_QUEUE = "cdmp_cus_clue_add_queue";

    /**
     * 客户线索新增KEY
     */
    public final static String CDMP_CUS_CLUE_ADD_KEY = "cdmp_cus_clue_add_key";

    /**
     * 订单审核队列
     */
    public final static String JKYX_ORDER_AUDIT_QUEUE = "jkyx_order_audit_queue";

    /**
     * 订单审核KEY
     */
    public final static String JKYX_ORDER_AUDIT_KEY = "order_audit_key";


    /**
     * 订单取消队列
     */
    public final static String JKYX_ORDER_CANCEL_QUEUE = "jkyx_order_cancel_queue";

    /**
     * 订单取消KEY
     */
    public final static String JKYX_ORDER_CANCEL_KEY = "order_cancel_key";
    
    
    /**
     * 订单结清队列
     */
    public final static String JKYX_ORDER_SETTLE_QUEUE = "jkyx_order_settle_queue";
    
    /**
     * 订单结清KEY
     */
    public final static String JKYX_ORDER_SETTLE_KEY = "order_settle_key";
    
    /**
     * 销售出库锁定队列
     */
    public final static String JKYX_ORDER_OUTSTOCKLOCK_QUEUE = "jkyx_order_outStockLock_queue";
    
    /**
     * 销售出库锁定KEY
     */
    public final static String JKYX_ORDER_OUTSTOCKLOCK_KEY = "order_outStockLock_key";
    
    /**
     * 还原入库队列
     */
    public final static String JKYX_ORDER_BACKOUTSTOCK_QUEUE = "jkyx_order_backOutStock_queue";
    
    /**
     * 还原入库KEY
     */
    public final static String JKYX_ORDER_BACKOUTSTOCK_KEY = "order_backOutStock_key";

    /**
     * 还原入库队列
     */
    public final static String JKYX_ORDER_CHANGEVEHICLE_QUEUE = "jkyx_order_changevehicle_queue";

    /**
     * 还原入库KEY
     */
    public final static String JKYX_ORDER_CHANGEVEHICLE_KEY = "jkyx_order_changevehicle_key";

    
    /**
     * 实销信息录入队列
     */
    public final static String JKYX_ORDER_SELLMESSAGE_QUEUE = "jkyx_order_SellMessage_queue";
    
    /**
     * 实销信息录入KEY
     */
    
    public final static String JKYX_ORDER_SELLMESSAGE_KEY = "order_SellMessage_key";
    
    /**
     * 实销信息审核队列
     */
    public final static String JKYX_ORDER_AUDITMESSAGE_QUEUE = "jkyx_order_auditMessage_queue";
    
    /**
     * 实销信息审核KEY
     */
    public final static String JKYX_ORDER_AUDITMESSAGE_KEY = "order_auditMessage_key";
    
    /**
     * 退车审核(OEM)队列
     */
    public final static String JKYX_ORDER_BACKCARAUDIT_QUEUE = "jkyx_order_backCarAudit_queue";
    
    /**
     * 退车审核(OEM)KEY
     */
    public final static String JKYX_ORDER_BACKCARAUDIT_KEY = "order_backCarAudit_key";
     
    /**
     * 退车入库队列
     */
    public final static String JKYX_ORDER_RETURNINVENTORY_QUEUE = "jkyx_order_returnInventory_queue";
    
    /**
     * 退车入库KEY
     */
    public final static String JKYX_ORDER_RETURNINVENTORY_KEY = "order_returnInventory_key";
    
    /**
     * 调拨审核队列
     */
    public final static String JKYX_ORDER_ALLOCATIONAUDIT_QUEUE = "jkyx_order_allocationAudit_queue";
    
    /**
     * 调拨审核KEY
     */
    public final static String JKYX_ORDER_ALLOCATIONAUDIT_KEY = "order_allocationAudit_key";
    
    /**
     * 调拨调出方出库队列
     */
    public final static String JKYX_ORDER_OUTSTOCK_QUEUE = "jkyx_order_outStock_queue";
    
    /**
     * 调拨调出方出库KEY
     */
    public final static String JKYX_OUTSTOCK_KEY = "order_outStock_key";
    
    /**
     * 移库申请队列
     */
    public final static String JKYX_ORDER_INSTOCK_QUEUE = "jkyx_order_inStock_queue";
    
    /**
     * 移库申请KEY
     */
    public final static String JKYX_INSTOCK_KEY = "order_inStock_key";
    
    /**
     * 移库审核队列
     */
    public final static String JKYX_ORDER_MOVEVEHICLEAUDIT_QUEUE = "jkyx_order_moveVehicleAudit_queue";
    
    /**
     * 移库审核KEY
     */
    public final static String JKYX_MOVEVEHICLEAUDIT_KEY = "order_moveVehicleAudit_key";
    
    /**
     * 移库调出方出库队列
     */
    public final static String JKYX_ORDER_OUTSTOCKDIAO_QUEUE = "jkyx_order_outStockDiao_queue";
    
    /**
     * 移库调出方出库KEY
     */
    public final static String JKYX_OUTSTOCKDIAO_KEY = "order_outStockDiao_key";
    
    /**
     * 移库调入方入库队列
     */
    public final static String JKYX_ORDER_INSTOCKDIAO_QUEUE = "jkyx_order_inStockDiao_queue";
    
    /**
     * 移库调入方入库KEY
     */
    public final static String JKYX_INSTOCKDIAO_KEY = "order_inStockDiao_key";
    
    /**
     * 线上订单取消申请队列
     */
    public final static String JKYX_OL_ORDER_CANCEL_APPLY_QUEUE = "jkyx_ol_order_cancel_apply_queue";

    /**
     * 线上订单取消申请KEY
     */
    public final static String JKYX_OL_ORDER_CANCEL_APPLY_KEY = "ol_order_cancel_apply_key";
    
    /**
     *  线上订单的订金支付流水接收,队列 
     */
    public final static String ONLINE_ORDER_DEPOSIT_QUEUE = "online_order_deposit_queue";

    /**
     *  线上订单的订金支付流水接收,KEY
     */
    public final static String ONLINE_ORDER_DEPOSIT_KEY = "online_order_deposit_key";
    
    
    /**
     *  支付单查询接口接收,队列 
     */
    public final static String YUNYANG_PAYMENT_ORDER_QUERY_QUEUE = "payment_order_query_queue";

    /**
     * 支付单查询接口接收,KEY
     */
    public final static String YUNYANG_PAYMENT_ORDER_QUERY_KEY = "payment_order_query_key";
    
    
    /**
     * 订单退款申请拒绝通知接收接口KEY
     */
    public final static String JKYX_APPLYREFUSE_KEY = "order_applyRefuse_key";
    
    /**
     * 订单退款申请拒绝通知接收接口队列
     */
    public final static String JKYX_ORDER_APPLYREFUSE_QUEUE = "jkyx_order_applyRefuse_queue";

    /**
     * 线索接收队列
     */
    public final static String CDMP_CLUE_RECEIVE_QUEUE = "cdmp_clue_receive_queue";
    /**
     * 线索接收KEY
     */
    public final static String CDMP_CLUE_RECEIVE_KEY = "cdmp_clue_receive_key";

}
