package com.mepeng.cn.pen.common.domains;

import com.mepeng.cn.pen.common.constants.FrameworkCommonConstants;
import com.mepeng.cn.pen.common.constants.FrameworkDictCodeConstants;
import com.mepeng.cn.pen.common.dao.DAOUtil;
import com.mepeng.cn.pen.common.domains.DTO.LoginInfoDto;
import com.mepeng.cn.pen.common.exception.DALException;
import com.mysql.cj.util.StringUtils;
import org.javalite.activejdbc.ColumnMetadata;
import org.javalite.activejdbc.MetaModel;
import org.javalite.activejdbc.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class BaseModel extends Model {
    private static final Logger logger         = LoggerFactory.getLogger(BaseModel.class);
    public static final String Dealer_Code    = FrameworkCommonConstants.PUBLIC_DEALER_CODE_NAME;
    public static final String ORGANIZATIONID = FrameworkCommonConstants.PUBLIC_ORGANIZATION_NAME;
    public static final String IS_DELETE    = FrameworkCommonConstants.PUBLIC_IS_DELETE_NAME;
    public static final String DATA_SOURCE_CHANNEL = FrameworkCommonConstants.PUBLIC_DATA_SOURCE_CHANNEL_NAME;

    /**
     * 定义保存之前处理方案
     *
     * @author zhangxc
     * @date 2016年7月7日 (non-Javadoc)
     * @see org.javalite.activejdbc.CallbackSupport#beforeSave()
     */
    @Override
    public void beforeSave() {
        // 得到当前用户的DealerCode
//        LoginInfoDto currentUserInfo = DAOUtil.getSessionUserInfo();
//
//        if (this.getId() == null) {
//            //设置创建人
//            set("created_by", "-1");
//            //设置是否删除为否
//            if (isExistsIsDeleteColumn()&& this.getString(IS_DELETE) == null) {
//                set(IS_DELETE, FrameworkDictCodeConstants.STATUS_IS_NOT);
//            }
//            //如果当前用户信息存在
//            if(currentUserInfo != null){
//                if (isExistsDealerCodeColumn()) {
//                    if (this.getString(Dealer_Code) == null) {
//                        set(Dealer_Code, currentUserInfo.getDealerCode());
//                    }else{
//                        //校验校验合法性
//                        validateAclData(currentUserInfo);
//                    }
//                }
//                if (isExistsOrganiztionIdColumn()) {
//                    if (this.getLong(ORGANIZATIONID) == null) {
//                        set(ORGANIZATIONID, currentUserInfo.getOrgId());
//                    }
//                }
//                // 如果不为空
//                if(!StringUtils.isNullOrEmpty(currentUserInfo.getUserAccount()))
//                    set("created_by", currentUserInfo.getUserAccount());
//
//                //设置数据来源渠道
//                if (isExistsDataSourceChannelColumn()) {
//                    if (this.getInteger(DATA_SOURCE_CHANNEL) == null) {
//                        set(DATA_SOURCE_CHANNEL, currentUserInfo.getDataSourceChannel());
//                    }
//                }
//
//            }
//        } else {
//            if (isExistsDealerCodeColumn()) {
//                validateAclData(currentUserInfo);
//            }
//        }
//        // 在修改的情况下，只修改update_by 值
//        if (currentUserInfo != null && currentUserInfo.getUserAccount() != null) {
//            set("updated_by", currentUserInfo.getUserAccount());
//        }else{
//            set("updated_by", "-1");
//        }

    }

    public boolean insert() {
        // 得到当前用户的DealerCode
        LoginInfoDto currentUserInfo = DAOUtil.getSessionUserInfo();

        if (this.getId() == null) {
            // 设置创建人
            set("created_by", "-2");
            // 设置是否删除为否
            if (isExistsIsDeleteColumn() && this.getString(IS_DELETE) == null) {
                set(IS_DELETE, FrameworkDictCodeConstants.STATUS_IS_NOT);
            }
            // 如果当前用户信息存在
            if (currentUserInfo != null) {
                if (isExistsDealerCodeColumn()) {
                    if (this.getString(Dealer_Code) == null) {
                        set(Dealer_Code, currentUserInfo.getDealerCode());
                    } else {
                        // 校验校验合法性
                        validateAclData(currentUserInfo);
                    }
                }
                if (isExistsOrganiztionIdColumn()) {
                    if (this.getLong(ORGANIZATIONID) == null) {
                        set(ORGANIZATIONID, currentUserInfo.getOrgId());
                    }
                }
                // 如果不为空
                if (!StringUtils.isNullOrEmpty(currentUserInfo.getUserAccount()))
                    set("created_by", currentUserInfo.getUserAccount());

                // 设置数据来源渠道
                if (isExistsDataSourceChannelColumn()) {
                    if (this.getInteger(DATA_SOURCE_CHANNEL) == null) {
                        set(DATA_SOURCE_CHANNEL, currentUserInfo.getDataSourceChannel());
                    }
                }

            }
        } else {
            if (isExistsDealerCodeColumn()) {
                validateAclData(currentUserInfo);
            }
        }
        // 在修改的情况下，只修改update_by 值
        if (currentUserInfo != null && currentUserInfo.getUserAccount() != null) {
            set("updated_by", currentUserInfo.getUserAccount());
        } else {
            set("updated_by", "-1");
        }

        return super.insert();
    }


    /**
     *
     * 判断是否有数据新增及修改权限
     * @author zhangxianchao
     * @date 2017年4月12日
     * @param currentUserInfo
     */
    private void validateAclData(LoginInfoDto currentUserInfo){
        String dealerCode = this.getString(Dealer_Code);
        if (currentUserInfo != null && currentUserInfo.getDealerCode() != null && dealerCode != null
                && !dealerCode.equals(currentUserInfo.getDealerCode())&& !isGroupDealer(dealerCode,currentUserInfo.getDealerCode())&& !isGroupDealer(currentUserInfo.getDealerCode(),dealerCode)) {
            logger.info("Dealer_code:" + dealerCode + ";Info:" + currentUserInfo.getDealerCode());
            throw new DALException("数据操作异常");
        }
    }

    /**
     * 如果有经销商信息，检查经销商代码是否与当前session 中代码一致
     *
     * @author zhangxc
     * @date 2016年6月30日 (non-Javadoc)
     * @see org.javalite.activejdbc.CallbackSupport#afterLoad()
     */
    @Override
    public void afterLoad() {
        // 如果有经销商信息，检查经销商代码是否与当前session 中代码一致
        if (isExistsDealerCodeColumn()) {
            // 得到当前用户的DealerCode
            String currentDealerCode = DAOUtil.getSessionDealerCode();
            String dealerCode = this.getString(Dealer_Code);
            if (currentDealerCode != null && dealerCode != null && !"-1".equals(dealerCode)
                    && !dealerCode.equals(currentDealerCode)&& !isGroupDealer(dealerCode,currentDealerCode)&& !isGroupDealer(currentDealerCode,dealerCode)) {
                logger.info("Dealer_code:" + dealerCode + ";Info:" + currentDealerCode);
                throw new DALException("数据操作异常");
            }
        }
    }

    /**
     * 在删除之前进行检查
     *
     * @author zhangxc
     * @date 2016年7月7日 (non-Javadoc)
     * @see org.javalite.activejdbc.CallbackSupport#beforeDelete()
     */
    @Override
    public void beforeDelete() {
        // 如果有经销商信息，检查经销商代码是否与当前session 中代码一致
        if (isExistsDealerCodeColumn()) {
            // 得到当前用户的DealerCode
            String currentDealerCode = DAOUtil.getSessionDealerCode();
            String dealerCode = this.getString(Dealer_Code);
            if (currentDealerCode != null && dealerCode != null && !dealerCode.equals(currentDealerCode)) {
                logger.info("Dealer_code:" + dealerCode + ";Info:" + currentDealerCode);
                throw new DALException("数据操作异常");
            }
        }
    }



    /**
     * 判断是否包含经销商代码
     *
     * @author zhangxc
     * @date 2016年6月30日
     * @return 如果包含，返回true,否则返回false
     */
    private boolean isExistsDealerCodeColumn() {
        return isExistsDefineColumn(Dealer_Code);
    }

    /**
     * 判断是否存在ORGNAIZTION_ID
     *
     * @author zhangxc
     * @date 2016年11月13日
     * @return
     */
    private boolean isExistsOrganiztionIdColumn() {
        return isExistsDefineColumn(ORGANIZATIONID);
    }

    /**
     * 判断是否包含经销商代码
     *
     * @author zhangxc
     * @date 2016年6月30日
     * @return 如果包含，返回true,否则返回false
     */
    private boolean isExistsIsDeleteColumn() {
        return isExistsDefineColumn(IS_DELETE);
    }

    /**
     * 判断是否存在某个字段
     *
     * @author zhangxc
     * @date 2016年11月13日
     * @param columnName
     * @return
     */
    private boolean isExistsDefineColumn(String columnName) {
        MetaModel metaModel = this.getMetaModelLocal();
        Map<String, ColumnMetadata> columnMetas = metaModel.getColumnMetadata();

        for (Map.Entry<String, ColumnMetadata> entry : columnMetas.entrySet()) {
            if (columnName.equalsIgnoreCase(entry.getKey())) {
                return true;
            }
        }
        return false;
    }
    /**
     *
     * 判断某一个经销商是否属于某一个经销商集团
     * @author zhangxc
     * @date 2017年3月23日
     * @param dealerCode
     * @return
     */
    private boolean isGroupDealer(String dealerCode,String currentDealerCode){
//        TenantDealerMappingService dealerMappingService = ApplicationContextHelper.getBeanByType(TenantDealerMappingService.class);
//        String tenantMappingCode = dealerMappingService.getGroupCodeByDealerCode(dealerCode);
//        logger.debug("dealerCode:"+dealerCode+";GroupCode:"+tenantMappingCode);
//        if(currentDealerCode.equals(tenantMappingCode)){
//            return true;
//        }
        return false;
    }

    public Map<String, ColumnMetadata> getColumnMetadata() {
        return this.getMetaModelLocal().getColumnMetadata();
    }


    /**
     * 判断是否包含数据来源渠道
     *
     * @author sunduoduo
     * @date 2018年3月12日
     * @return 如果包含，返回true,否则返回false
     */
    private boolean isExistsDataSourceChannelColumn() {
        return isExistsDefineColumn(DATA_SOURCE_CHANNEL);
    }
}
