
/** 
*Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dms.web
*
* @File name : LoginInfoDto.java
*
* @Author : zhangxc
*
* @Date : 2016年6月30日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年6月30日    zhangxc    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/

package com.mepeng.cn.pen.common.domains.DTO;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 记录登录的相关信息
 * 
 * @author zhangxc
 * @date 2016年6月30日
 */
@Component
@Scope("request")
public class LoginInfoDto {

    private String  dealerCode;
    private String  groupCode;
    private String  userAccount;
    private Long    dealerId;
    private String  dealerName;
    private String  dealerShortName;
    private String  userName;
    private Long    userId;
    private Locale  locale;
    private String  employeeNo;
    private Integer gender;
    private Long    orgId;
    private Integer orgType;
    private Long    employeeId;
    private Long positionId; // 职务ID  new add it
    private String positionName;//职务名称
    private String phone;//电话
    private String mobile;//手机
    private Integer productType;//经销商购买产品类型
    private Integer dataSourceChannel;//数据来源渠道
    private Integer isPartCustomer; //是否往来客户账户
    private Integer dataType;//new add it

    // 设置可以管理的仓库
    private String  canAccessStores;
    private String  carLoadDepot;     // 整车仓库权限
    private String  purchaseDepot;    // 配件仓库权限
    private String  suppliesDepot;    // 用品仓库权限
    private Map     repair;           // 维修权限
    private Map     purchase;         // 配件权限
    private List    preferentialMode; // 优惠模式权限
    private boolean leanCheck; //TODO CHECK

    /**
     * method:module:url,分隔符使用：FrameworkConstants.ACL_RESOUCCE_SPLIT
     */

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    private String orgCode;
    private String orgName;

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userName) {
        this.userAccount = userName;
    }

    @Override
    public String toString() {
        return "LoginInfoDto [dealerCode=" + dealerCode + ", userName=" + userAccount + ",groupCode=" + groupCode + ",employeeId="+employeeId+",orgType="+orgType+",userAccount="+userAccount+"]"+Integer.toHexString(hashCode());
    }

    public Long getDealerId() {
        return dealerId;
    }

    public void setDealerId(Long dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerShortName() {
        return dealerShortName;
    }

    public void setDealerShortName(String dealerShortName) {
        this.dealerShortName = dealerShortName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getCanAccessStores() {
        return canAccessStores;
    }

    public void setCanAccessStores(String canAccessStores) {
        this.canAccessStores = canAccessStores;
    }

    public String getCarLoadDepot() {
        return carLoadDepot;
    }

    public void setCarLoadDepot(String carLoadDepot) {
        this.carLoadDepot = carLoadDepot;
    }

    public String getPurchaseDepot() {
        return purchaseDepot;
    }

    public void setPurchaseDepot(String purchaseDepot) {
        this.purchaseDepot = purchaseDepot;
    }

    public Map getRepair() {
        return repair;
    }

    public void setRepair(Map repair) {
        this.repair = repair;
    }

    public Map getPurchase() {
        return purchase;
    }

    public void setPurchase(Map purchase) {
        this.purchase = purchase;
    }

    public List getPreferentialMode() {
        return preferentialMode;
    }

    public void setPreferentialMode(List preferentialMode) {
        this.preferentialMode = preferentialMode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Integer getOrgType() {
        return orgType;
    }

    public void setOrgType(Integer orgType) {
        this.orgType = orgType;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
    
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSuppliesDepot() {
        return suppliesDepot;
    }

    public void setSuppliesDepot(String suppliesDepot) {
        this.suppliesDepot = suppliesDepot;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Integer getDataSourceChannel() {
        return dataSourceChannel;
    }

    public void setDataSourceChannel(Integer dataSourceChannel) {
        this.dataSourceChannel = dataSourceChannel;
    }

    public Integer getIsPartCustomer() {
        return isPartCustomer;
    }

    public void setIsPartCustomer(Integer isPartCustomer) {
        this.isPartCustomer = isPartCustomer;
    }

    public void setLeanCheck(boolean leanCheck) {
        this.leanCheck = leanCheck;
    }

    public boolean getLeanCheck() {
        return leanCheck;
    }

    public Long getPositionId() { return positionId;}
    public void setPositionId(Long positionId) { this.positionId = positionId;}
    public Integer getDataType() { return dataType;}
    public void setDataType(Integer dataType) { this.dataType = dataType;}
}
