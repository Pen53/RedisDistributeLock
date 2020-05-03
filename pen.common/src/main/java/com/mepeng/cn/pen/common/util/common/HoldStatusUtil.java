package com.mepeng.cn.pen.common.util.common;

/**
* 定义状态叠加规则，如锁定状态
* @author zhangxianchao
* @date 2017年3月29日
*/

public class HoldStatusUtil {

    
//    // 以下变量为TM_VEHICLE表中HOLD_STATUS字段的描述。在SYSTEM_STATUS表中具体存在纪录
//    public final static long VEHICLE_OK = 0; // 表示车辆OK
//    public final static long DAMAGED_VEHICLE_HOLD = 1; // 质损车
//    public final static long DEVIATION_HOLD = 2; // DEVIATION车辆
//    public final static long MANUAL_DEVIATION_HOLD = 4; // 遗漏的DEVIATION车辆
//    public final static long TIP_ASSIGN_HOLD = 8; // 寄存车ASSIGN
//    public final static long TIP_BACK = 16; // 寄存车退回
//    public final static long BORROWED_VEHICLE_HOLD = 32; // 借用车借车
//    public final static long BORROWED_RETURN_HOLD = 64; // 借用车归还
//    public final static long VSC_CHANGE_APPLY_HOLD = 128; // 申请更改vsc
//    public final static long REASSEMBLE_APPLY_HOLD = 256; // 申请改装
//    public final static long RETURN_BACK_APPLY_HOLD = 512; // 退车申请
//    public final static long CALL_OFF_HOLD = 1024; // CALL车
//    public final static long DISCOUNT_APPLY_HOLD = 2048; // 合同车申请
//    public final static long TRANSFER_APPLY_HOLD = 4096; // 调拨调出申请
//    public final static long COLOR_CHANGE_HOLD = 8192; // 车色更改
//    public final static long SHIP_TO_CHANGE_HOLD = 16384; // 发送指令更改
//    public final static long ASC_HOLD = 32768; // 转ASC车辆
//    public final static long COMPANY_EXPENSED_HOLD = 65536; // 公司费用化车
//    public final static long ESTIMATE_HOLD = 131072; // 评估车
//    public final static long TRY_PRODUCE_HOLD = 262144; // 试生产车
//    public final static long BAD_DAMAGED_HOLD = 524288; // 严重质损车
//    public final static long LOST_HOLD = 1048576; // 损失车
//    public final static long OVERSTOCK_HOLD = 2097152; // 积压车
//    public final static long RETURN_BACK_HOLD = 4194304; // 退回车
//    public final static long VIN_DELETED = 8388608; // VIN删除
//    public final static long FREEZE_VEHICLE_HOLD = 16777216; // 车辆冻结
//    //车辆状态码
//    public final static long VEHICLE_BATCH_HOLD = 33554432;//车辆BatchHOLD
//    public final static long VEHICLE_SGM_BLOCK = 67108864;//车辆SGMBLOCK
//    public final static long VEHICLE_OTHER_BLOCK = 134217728;//车辆其它原因BLOCK
    
    /**
     * 
    * 设置叠加状态
    * @author Administrator
    * @date 2017年3月29日
    * @param statusCode:目前的hold 值
    * @param holdType：需要增加的hold值
    * @return
     */
    public static long setHold(long statusCode, long holdType) {
        if (isHold(statusCode, holdType)) {
        } else {
            statusCode += holdType;
        }
        return statusCode;
    }
    
    
    /**
     * 
    * 释放锁定状态值
    * @author zhangxianchao
    * @date 2017年3月29日
    * @param statusCode：当前值
    * @param holdType：释放的锁定值
    * @return
     */
    public static long releaseHold(long statusCode, long holdType) {
        if (isHold(statusCode, holdType)) {
            statusCode -= holdType;
        }
        return statusCode;
    }

    /**
     * 
    * 判断是否被某个hold 锁定
    * @author zhangxianchao
    * @date 2017年3月29日
    * @param statusCode
    * @param holdType
    * @return
     */
    public static boolean isHold(long statusCode, long holdType) {
        long[] holdList = getHoldList(statusCode);
        if (holdList == null)
            return false;
        for (int i = 0; i < holdList.length; i++) {
            if (holdList[i] == holdType)
                return true;
        }
        return false;
    }
    
    /**
     * 
    * 获取所有叠加的状态
    * @author zhangxianchao
    * @date 2017年3月29日
    * @param statusCode
    * @return
     */
    public static long[] getHoldList(long statusCode) {
        if (statusCode == 0)
            return null;
        long temp = statusCode;
        int i = 0;
        int j = 0;
        long[] resultArray = new long[32];
        while (temp != 1) {
            if (temp % 2 == 1) {
                resultArray[i] = sqr(2, j);
                i++;
            }
            temp /= 2;
            j++;
        }
        resultArray[i] = sqr(2, j);
        return resultArray;
    }
    
    /**
     * 
    * 执行2的乘机（猜的）
    * @author zhangxianchao
    * @date 2017年3月29日
    * @param root
    * @param power
    * @return
     */
    private static long sqr(long root, long power) {
        if (power == 0)
            return 1;
        long temp = root;
        while (power > 1) {
            root *= temp;
            power -= 1;
        }
        return root;
    }
    
}
