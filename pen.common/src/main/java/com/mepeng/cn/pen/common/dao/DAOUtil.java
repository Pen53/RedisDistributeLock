
/** 
*Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dms.function
*
* @File name : DAOUtil.java
*
* @Author : zhangxc
*
* @Date : 2016年7月6日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年7月6日    zhangxc    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/

package com.mepeng.cn.pen.common.dao;

import com.mepeng.cn.pen.common.constants.FrameworkDictCodeConstants;
import com.mepeng.cn.pen.common.domains.BaseModel;
import com.mepeng.cn.pen.common.domains.DTO.LoginInfoDto;
import com.mepeng.cn.pen.common.domains.DTO.RequestPageInfoDto;
import com.mepeng.cn.pen.common.exception.DALException;
import com.mepeng.cn.pen.common.util.bean.ApplicationContextHelper;
import com.mepeng.cn.pen.common.util.common.CommonUtils;
import com.mepeng.cn.pen.common.util.common.StringUtils;
import com.mepeng.cn.pen.common.util.service.RegexReplaceCallBack;
import org.javalite.activejdbc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.util.*;

/**
 * 定义数据库层的util 方法
 * 
 * @author zhangxc
 * @date 2016年7月6日
 */

public class DAOUtil {

    // 定义日志接口
    private static final Logger logger = LoggerFactory.getLogger(DAOUtil.class);

    /**
     * 屏蔽私有构造方法
     * 
     * @author zhangxc
     * @date 2017年1月15日
     */
    private DAOUtil(){
    }

    /**
     * 判断对应条件的数据是否存在
     * 
     * @author zhangxc
     * @date 2017年2月15日
     * @param subQuery
     * @param params
     * @return
     */
    public static <T extends Model> boolean isExistsByDealer(Class<T> objClass, String subQuery, Object... params) {
        LoginInfoDto loginInfo = getSessionUserInfo();
        Model model = ModelDelegate.findFirst(objClass, subQuery + getDealerSubString(loginInfo),
                                              getDealerParamList(loginInfo,
                                                                 new ArrayList<Object>(Arrays.asList(params))).toArray());
        if (model != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据PO 的Class 返回对应的经销商权限过滤的SQL
     * 
     * @date 2016年7月6日
     * @return
     */
    public static <T extends Model> T findFirstByDealer(Class<T> poClass, String subQuery, Object... params) {
        LoginInfoDto loginInfo = getSessionUserInfo();
        return ModelDelegate.findFirst(poClass, subQuery + getDealerSubString(loginInfo),
                                       getDealerParamList(loginInfo,
                                                          new ArrayList<Object>(Arrays.asList(params))).toArray());
    }

    /**
     * 根据当前经销商查询对应的数据
     * 
     * @author zhangxc
     * @date 2017年2月15日
     * @param subQuery
     * @param params
     * @return
     */
    public static <T extends Model> LazyList<T> findByDealer(Class<T> objClass, String subQuery, Object... params) {
        LoginInfoDto loginInfo = getSessionUserInfo();
        return ModelDelegate.where(objClass, subQuery + getDealerSubString(loginInfo),
                                   getDealerParamList(loginInfo,
                                                      new ArrayList<Object>(Arrays.asList(params))).toArray());
    }

    /**
     * 判断对应条件的数据是否存在(权限范围，主要用于基础数据功能)
     * 
     * @author zhangxc
     * @date 2017年2月15日
     * @param subQuery
     * @param params
     * @return
     */
    public static <T extends Model> boolean isExistsByGroup(Class<T> objClass, String subQuery, Object... params) {
        LoginInfoDto loginInfo = getSessionUserInfo();
        Model model = ModelDelegate.findFirst(objClass, subQuery + getDealerGroupString(loginInfo),
                                              getDealerGroupParamList(loginInfo,
                                                                      new ArrayList<Object>(Arrays.asList(params))).toArray());
        if (model != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据PO 的Class 返回对应的经销商权限过滤的SQL
     *
     * @date 2016年7月6日
     * @return
     */
    public static <T extends Model> T findFirstByGroup(Class<T> poClass, String subQuery, Object... params) {
        LoginInfoDto loginInfo = getSessionUserInfo();
        return ModelDelegate.findFirst(poClass, subQuery + getDealerGroupString(loginInfo),
                                       getDealerGroupParamList(loginInfo,
                                                               new ArrayList<Object>(Arrays.asList(params))).toArray());
    }

    /**
     * 根据当前经销商查询对应的数据
     * 
     * @author zhangxc
     * @date 2017年2月15日
     * @param subQuery
     * @param params
     * @return
     */
    public static <T extends Model> LazyList<T> findByGroup(Class<T> objClass, String subQuery, Object... params) {
        LoginInfoDto loginInfo = getSessionUserInfo();
        return ModelDelegate.where(objClass, subQuery + getDealerGroupString(loginInfo),
                                   getDealerGroupParamList(loginInfo,
                                                           new ArrayList<Object>(Arrays.asList(params))).toArray());
    }

    /**
     * 获得经销商所有权的数据
     * 
     */
    private static String getDealerGroupString(LoginInfoDto loginInfo) {
        if (loginInfo != null) {
            if (loginInfo.getOrgType() == FrameworkDictCodeConstants.DATA_TYPE_BY_OWNER) {
                return " and " + BaseModel.Dealer_Code + " in(?,?,'-1')";
            } else {
                return " and " + BaseModel.Dealer_Code
                       + " in (select t2.DEALER_CODE from tm_org_relation t1,tm_org_relation t2 where t1.DEALER_CODE = ?"
                       + " and t1.ORGDEPT_ID = t1.CHILD_ORG_ID and t2.ORGDEPT_ID = t1.ORGDEPT_ID and t2.ORGDEPT_TYPE in (15061003,15061004))";
            }
        } else {
            return " and " + BaseModel.Dealer_Code + " in(?,?,'-1')";
        }
    }

    /**
     * 获得经销商的信息
     */
    private static String getDealerSubString(LoginInfoDto loginInfo) {
        if (loginInfo != null) {
            if (loginInfo.getOrgType() == FrameworkDictCodeConstants.DATA_TYPE_BY_OWNER) {
                return " and " + BaseModel.Dealer_Code + " in(?,'-1')";
            } else {
                return " and " + BaseModel.Dealer_Code + " in(?,'-1')";
            }
        } else {
            return " and " + BaseModel.Dealer_Code + " in(?)";
        }
    }

    /**
     * 获得经销商参数信息,如果用户已经登录，则拼装当前经销商的经销商及经销商集团代码
     * 
     */
    private static List<Object> getDealerParamList(LoginInfoDto loginInfo, List<Object> paramList) {
        if (loginInfo != null) {
            if (loginInfo.getOrgType() == FrameworkDictCodeConstants.DATA_TYPE_BY_OWNER) {
                paramList.add(loginInfo.getDealerCode());
            } else {
                paramList.add(loginInfo.getDealerCode());
            }
        } else {
            paramList.add("-1");
        }
        return paramList;
    }

    /**
     * 获得经销商参数信息,如果用户已经登录，则拼装当前经销商的经销商及经销商集团代码
     */
    private static List<Object> getDealerGroupParamList(LoginInfoDto loginInfo, List<Object> paramList) {
        if (loginInfo != null) {
            if (loginInfo.getOrgType() == FrameworkDictCodeConstants.DATA_TYPE_BY_OWNER) {
                paramList.add(loginInfo.getDealerCode());
                paramList.add(loginInfo.getGroupCode());
            } else {
                paramList.add(loginInfo.getDealerCode());
            }
        } else {
            paramList.add("-1");
            paramList.add("-1");
        }
        return paramList;
    }

    /**
     * 根据sql 语句进行查询
     * 
     * @date 2016年7月6日
     * @param sql
     * @param queryParam
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map findFirst(String sql, List<Object> queryParam, boolean... args) {
        boolean isGroup = false;
        if (args != null && args.length >= 1) {
            isGroup = args[0];
        }
        return commonFindFirst(sql, queryParam, null, isGroup);
    }

    /**
     * 根据自定义接口查询对应的数据
     * 
     * @author zhangxc
     * @date 2016年7月7日
     * @param sql
     * @param queryParam
     * @param defindProcessor
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map findFirst(String sql, List<Object> queryParam, DefinedRowProcessor defindProcessor,
                                boolean... args) {
        return commonFindFirst(sql, queryParam, defindProcessor, args);
    }

    /**
     * 通用查找数据库的第一条数据
     * 
     * @author zhangxc
     * @date 2016年12月17日
     * @param sql
     * @param queryParam
     * @param defindProcessor
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static Map commonFindFirst(String sql, List<Object> queryParam, DefinedRowProcessor defindProcessor,
                                       boolean... args) {
        boolean isGroup = false;
        if (args != null && args.length >= 1) {
            isGroup = args[0];
        }

        // 根据SQL 语句返回对应的数据
        List<Map> result = commonFindAll(sql, queryParam, true, defindProcessor, null, isGroup);
        /**
         * 对返回的条数进行控制
         */
        if (CommonUtils.isNullOrEmpty(result)) {
            return null;
        }
        if (result.size() == 1) {
            return result.get(0);
        } else {
            throw new DALException("返回的条数不正确");
        }
    }

    /**
     * 执行权限查询
     * 
     * @author zhangxc
     * @date 2016年8月1日
     * @param sql
     * @param queryParam
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<Map> findAll(String sql, List<Object> queryParam, boolean... args) {
        return findAll(sql, true, queryParam, args);
    }

    /**
     * 根据sql 语句进行查询
     * 
     * @date 2016年7月6日
     * @param sql
     * @param queryParam
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<Map> findAll(String sql, boolean isAclCheck, List<Object> queryParam, boolean... args) {
        return commonFindAll(sql, queryParam, isAclCheck, null, null, args);
    }

    /**
     * 根据sql 语句进行查询()
     * 
     * @date 2016年7月6日
     * @param sql
     * @param queryParam
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<Map> findAll(String sql, List<Object> queryParam, boolean isAclCheck,
                                    DefinedRowProcessor defindProcessor, boolean... args) {
        return commonFindAll(sql, queryParam, isAclCheck, defindProcessor, null, args);
    }

    /**
     * 根据自定义接口查询对应的数据
     * 
     * @author zhangxc
     * @date 2016年7月7日
     * @param sql
     * @param queryParam
     * @param defindProcessor
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<Map> findAll(String sql, List<Object> queryParam, DefinedRowProcessor defindProcessor,
                                    boolean... args) {
        return commonFindAll(sql, queryParam, true, defindProcessor, null, args);
    }

    /**
     * 执行数据权限控制范围内的查询
     * 
     * @author Administrator
     * @date 2016年12月5日
     * @param sql
     * @param queryParam
     * @param menuId
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<Map> findAll(String sql, List<Object> queryParam, String menuId, boolean... args) {
        return commonFindAll(sql, queryParam, true, null, menuId, args);
    }

    /**
     * 根据自定义接口查询对应数据权限范围的数据
     * 
     * @author Administrator
     * @date 2016年12月6日
     * @param sql
     * @param queryParam
     * @param defindProcessor
     * @param menuId
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<Map> findAll(String sql, List<Object> queryParam, DefinedRowProcessor defindProcessor,
                                    String menuId, boolean... args) {
        return commonFindAll(sql, queryParam, true, defindProcessor, menuId, args);
    }

    /**
     * 制定通用查询所有数据的方法
     * 
     * @author zhangxc
     * @date 2016年12月17日
     * @param sql
     * @param queryParam
     * @param isAclCheck
     * @param defindProcessor
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static List<Map> commonFindAll(String sql, List<Object> queryParam, boolean isAclCheck,
                                           DefinedRowProcessor defindProcessor, String menuId, boolean... args) {
        boolean isGroup = false;
        if (args != null && args.length >= 1) {
            isGroup = args[0];
        }

        List<Object> queryParamList = queryParam;
        if (queryParam == null) {
            queryParamList = new ArrayList<>();
        }
        // 获得封装了数据权限的SQL
        String sqlFinal;
        if (isAclCheck) {
            // 进行用户权限控制
            if (menuId != null) {
                sqlFinal = getUserAclSql(sql, queryParamList, menuId, isGroup);
            } else {
                // 进行经销商权限控制
                sqlFinal = getDealerAclSql(sql, queryParamList,isGroup);
            }

        } else {
            sqlFinal = sql;
        }
        if (defindProcessor != null) {
            RowProcessor processor = Base.find(sqlFinal, queryParamList.toArray());
            processor.with(defindProcessor);
            return defindProcessor.getResult();
        } else {
            logger.debug("SQL:" + sqlFinal + ";param:" + Arrays.toString(queryParamList.toArray()));
            return Base.findAll(sqlFinal, queryParamList.toArray());
        }
    }

    /**
     * 根据sql 语句进行分页查询
     * 
     * @date 2016年7月6日
     * @param sql
     * @param queryParam
     * @return
     */
    public static PageInfoDto pageQuery(String sql, List<Object> queryParam, boolean... args) {
        return commonPageQuery(sql, queryParam, null, null, args);
    }

    public static PageInfoDto pageQuery(RequestPageInfoDto requestPageInfoDto,String sql, List<Object> queryParam, boolean... args) {
        return commonPageQuery(requestPageInfoDto,sql, queryParam, null, null, args);
    }

    /**
     * 根据sql 语句进行查询第1页的分页数据
     * 
     * @date 2016年7月6日
     * @param sql
     * @param queryParam
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<Map> firstPageQuery(int pageSize, String sql, List<Object> queryParam, boolean... args) {
        return commonFirstPageQuery(pageSize, sql, queryParam, null, null, args);
    }

    /**
     * 根据sql 语句进行分页查询,通过自定义实现接口
     * 
     * @date 2016年7月6日
     * @param sql
     * @param queryParam
     * @return
     */
    public static PageInfoDto pageQuery(String sql, List<Object> queryParam, DefinedRowProcessor processor,
                                        boolean... args) {
        return commonPageQuery(sql, queryParam, processor, null, args);
    }

    /**
     * 根据sql 语句进行数据权限控制范围内的分页查询
     * 
     * @author Administrator
     * @date 2016年12月5日
     * @param sql
     * @param queryParam
     * @param menuId
     * @return
     */
    public static PageInfoDto pageQuery(String sql, List<Object> queryParam, String menuId, boolean... args) {
        return commonPageQuery(sql, queryParam, null, menuId, args);
    }

    /**
     * 根据sql 语句进行查询第1页的分页数据
     * 
     * @date 2016年7月6日
     * @param sql
     * @param queryParam
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<Map> firstPageQuery(int pageSize, String sql, List<Object> queryParam, String menuId,
                                           boolean... args) {
        return commonFirstPageQuery(pageSize, sql, queryParam, null, menuId, args);
    }

    /**
     * 根据sql 语句进行分页查询,通过自定义实现接口
     * 
     * @date 2016年7月6日
     * @param sql
     * @param queryParam
     * @return
     */
    public static PageInfoDto pageQuery(String sql, List<Object> queryParam, DefinedRowProcessor processor,
                                        String menuId, boolean... args) {
        return commonPageQuery(sql, queryParam, processor, menuId, args);
    }

    /**
     * 通用功能查询
     * 
     * @author zhangxc
     * @date 2016年7月7日
     * @param sql 查询的SQL 语句
     * @param queryParam 查询的参数
     * @param processor 转换器
     * @return
     */
    private static PageInfoDto commonPageQuery(String sql, List<Object> queryParam, DefinedRowProcessor processor,
                                               String menuId, boolean... args) {
        // 获取分页信息
        RequestPageInfoDto requestPageInfoDto = getRequestPageInfo();
        Integer pageSize = Integer.parseInt(requestPageInfoDto.getLimit());
        String sort = requestPageInfoDto.getSort();
        String order = requestPageInfoDto.getOrder();
        Integer offset = Integer.parseInt(requestPageInfoDto.getOffset());
        int page = (offset / pageSize) + 1;

        // 定义排序字段
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
        // 获取分页对象
        Paginator paginator = getPaginator(pageSize, sql, queryParam, orders == null ? null : orders.toString(),
                                           processor, menuId, args);

        // 设置分页结果
        PageInfoDto pageDto = new PageInfoDto();
        pageDto.setTotal(paginator.getCount());

        // 设置分页结果
        pageDto.setRows(getOnePageResult(paginator, page, processor));
        return pageDto;
    }

    private static PageInfoDto commonPageQuery(RequestPageInfoDto requestPageInfoDto,String sql, List<Object> queryParam, DefinedRowProcessor processor,
                                               String menuId, boolean... args) {
        // 获取分页信息
        Integer pageSize = Integer.parseInt(requestPageInfoDto.getLimit());
        String sort = requestPageInfoDto.getSort();
        String order = requestPageInfoDto.getOrder();
        Integer offset = Integer.parseInt(requestPageInfoDto.getOffset());
        int page = (offset / pageSize) + 1;

        // 定义排序字段
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
        // 获取分页对象
        Paginator paginator = getPaginator(pageSize, sql, queryParam, orders == null ? null : orders.toString(),
                processor, menuId, args);

        // 设置分页结果
        PageInfoDto pageDto = new PageInfoDto();
        pageDto.setTotal(paginator.getCount());

        // 设置分页结果
        pageDto.setRows(getOnePageResult(paginator, page, processor));
        return pageDto;
    }
    /**
     * 通用功能查询
     * 
     * @author zhangxc
     * @date 2016年7月7日
     * @param sql 查询的SQL 语句
     * @param queryParam 查询的参数
     * @param processor 转换器
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static List<Map> commonFirstPageQuery(int pageSize, String sql, List<Object> queryParam,
                                                  DefinedRowProcessor processor, String menuId, boolean... args) {
        Paginator paginator = getPaginator(pageSize, sql, queryParam, null, processor, menuId, args);

        return getOnePageResult(paginator, 1, processor);
    }

    /**
     * 获取分页结果
     * 
     * @author zhangxc
     * @date 2017年2月20日
     * @param paginator
     * @param page
     * @param processor
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static List<Map> getOnePageResult(Paginator paginator, int page, DefinedRowProcessor processor) {
        // 查询第1页的数据
        List<Map> results;
        if (processor == null) {
            results = paginator.getPage(page);
        } else {
            paginator.getPage(1, processor);
            results = processor.getResult();
        }
        return results;
    }

    /**
     * 获得分页对象
     * 
     * @author zhangxc
     * @date 2017年2月20日
     * @param pageSize
     * @param sql
     * @param queryParam
     * @param orders
     * @param processor
     * @param menuId
     * @return
     */
    private static Paginator getPaginator(int pageSize, String sql, List<Object> queryParam, String orders,
                                          DefinedRowProcessor processor, String menuId, boolean... args) {
        boolean isGroup = false;
        if (args != null && args.length >= 1) {
            isGroup = args[0];
        }

        /**
         * 不需要做dealer等数据权限控制,提供查询效率
         */
        boolean isDealer = false;
        if(args!=null&&args.length>=2){
            isDealer = args[1];
        }

        List<Object> queryParamList = queryParam;
        // 创建queryParam 对象
        if (queryParam == null) {
            queryParamList = new ArrayList<>();
        }

        // 获得封装了数据权限的SQL
        String sqlFinal;
        // 进行用户权限控制
        if (menuId != null) {
            sqlFinal = getUserAclSql(sql, queryParamList, menuId, isGroup);
        } else if(isDealer){
            sqlFinal=sql;
        }else{
            // 进行经销商权限控制
            sqlFinal = getDealerAclSql(sql, queryParamList,isGroup);

        }
        // 构建分页对象
        Paginator paginator = new Paginator(pageSize, sqlFinal, queryParamList.toArray());
        // 如果排序不为Null
        if (orders != null) {
            paginator.orderBy(orders.toString());
        }
        return paginator;
    }

    /**
     * 获取拼装后Dealer_Code 的SQL 语句
     * 
     * @author zhangxc
     * @date 2016年7月7日
     * @param sql
     * @param queryParam
     * @param isGroup:用来判断是否查询所属范围的数据
     * @return
     */
    private static String getDealerAclSql(String sql, List<Object> queryParam, boolean isGroup) {
        String executeSql = sql;
        // 拼装Dealer_code 字段
        LoginInfoDto loginInfo = getSessionUserInfo();

        // 如果存在Dealer Code信息
        if (!StringUtils.isNullOrEmpty(loginInfo) && !StringUtils.isNullOrEmpty(loginInfo.getDealerCode())) {
            StringBuilder sbFinal = new StringBuilder();
            if (!isGroup) {
                sbFinal.append("select * from (").append(sql).append(") tt where 1=1 ").append(getDealerSubString(loginInfo));
                queryParam = getDealerParamList(loginInfo, queryParam);
            } else {
                sbFinal.append("select * from (").append(sql).append(") tt where 1=1 ").append(getDealerGroupString(loginInfo));
                queryParam = getDealerGroupParamList(loginInfo, queryParam);
            }

            executeSql = sbFinal.toString();
        }
        logger.debug("SQL:" + executeSql + ";param:" + Arrays.toString(queryParam.toArray()));
        return executeSql;
    }

    /**
     * 获取拼装后Dealer_Code 的SQL 语句
     */
    public static String getDealerAclTableSql(String tableName, List<Object> queryParam, boolean... args) {
        boolean isGroup = false;
        if (args != null && args.length >= 1) {
            isGroup = args[0];
        }

        // 拼装Dealer_code 字段
        String sql = tableName;
        // 拼装Dealer_code 字段
        LoginInfoDto loginInfo = getSessionUserInfo();
        if (!StringUtils.isNullOrEmpty(loginInfo) && !StringUtils.isNullOrEmpty(loginInfo.getDealerCode())) {
            StringBuilder sbFinal = new StringBuilder();
            if (!isGroup) {
                sbFinal.append("(select * from ").append(tableName).append(" where 1=1 ").append(getDealerSubString(loginInfo)).append(")");
                queryParam = getDealerParamList(loginInfo, queryParam);
            } else {
                sbFinal.append("(select * from ").append(tableName).append(" where 1=1 ").append(getDealerGroupString(loginInfo)).append(")");
                queryParam = getDealerGroupParamList(loginInfo, queryParam);
            }

            sql = sbFinal.toString();
        }
        return sql;
    }

    /**
     * 获取该菜单页面下的数据权限范围控制的sql语句
     */
    private static String getUserAclSql(String sql, List<Object> queryParam, String menuId, boolean isGroup) {

        String sqlFinal = sql;

        logger.debug("SQL:" + sqlFinal + ";param:" + Arrays.toString(queryParam.toArray()));
        return sqlFinal;
    }

    /**
     * 获得字段的本地化值
     * 
     * @author zhangxc
     * @date 2016年8月29日
     * @return
     */
    public static String getLocaleFieldValue(Map<String, Object> rowMap, String fieldName) {
        String dbLanguage = getDBLanguage();
        if (dbLanguage == null) {
            return (String) rowMap.get(fieldName);
        } else {
            String returnValue;
            if ((returnValue = (String) rowMap.get(fieldName + "_" + dbLanguage)) != null) {
                return returnValue;
            }
            return (String) rowMap.get(fieldName + "_" + dbLanguage.toLowerCase());
        }
    }

    /**
     * 获得当前
     * 
     * @author zhangxc
     * @date 2016年8月29日
     * @return
     */
    private static String getDBLanguage() {
        LoginInfoDto loginInfo = ApplicationContextHelper.getBeanByType(LoginInfoDto.class);
        Locale locale = loginInfo.getLocale();
        if (locale != null) {
            String language = locale.getLanguage();
            return language.split("_")[0].toUpperCase();
        } else {
            return null;
        }
    }

    /**
     * 根据SQL语句参数执行对应的SQL,主要用于不同数据库中不同的SQL语句的实现
     * 
     * @author zhangxc
     * @date 2016年12月26日
     * @param sql
     * @param paramterMap
     */
    public static void execSqlByParamter(String sql, final Map<String, Object> paramterMap) {
        final List<Object> paramList = new ArrayList<>();
        // 对字符串结构进行替换分解
        String sqlResult = StringUtils.getMatcherPatternStr(sql, "#\\[([\\S]+)\\]", new RegexReplaceCallBack() {

            @Override
            public String replace(String matcherGroup) {
                paramList.add(paramterMap.get(matcherGroup));
                return "?";
            }
        });
        logger.debug("Sql:" + sqlResult + ";" + Arrays.toString(paramList.toArray()));
        // 执行对应的SQL 语句
        Base.exec(sqlResult, paramList.toArray());
    }

    /**
     * 执行批处理操作，如insert .... select 语句
     * 
     * @author zhangxc
     * @date 2017年1月3日
     * @param sql
     */
    public static void execBatchPreparement(String sql, List<Object> params) {
        PreparedStatement ps = Base.startBatch(sql);
        logger.debug("Sql:" + sql + ";" + Arrays.toString(params.toArray()));
        Base.addBatch(ps, params.toArray());
        Base.executeBatch(ps);
    }

    /**
     * 获得当前用户的经销商代码
     * 
     * @author zhangxc
     * @date 2016年11月9日
     * @return
     */
    public static String getSessionDealerCode() {
        try {
            LoginInfoDto loginInfo = ApplicationContextHelper.getBeanByType(LoginInfoDto.class);
            return loginInfo.getDealerCode();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        ;
        return null;
    }

    /**
     * 获得当前用户的经销商代码
     * 
     * @author zhangxc
     * @date 2016年11月9日
     * @return
     */
    public static LoginInfoDto getSessionUserInfo() {
        try {
            LoginInfoDto loginInfo = ApplicationContextHelper.getBeanByType(LoginInfoDto.class);
            return loginInfo;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        ;
        return null;
    }

    /**
     * 获取分页信息
     * 
     * @author zhangxc
     * @date 2017年3月20日
     * @return
     */
    private static RequestPageInfoDto getRequestPageInfo() {
        RequestPageInfoDto requestPageInfoDto = null;
        try {
            // 获取分页信息
            requestPageInfoDto = ApplicationContextHelper.getBeanByType(RequestPageInfoDto.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            requestPageInfoDto = new RequestPageInfoDto();
            requestPageInfoDto.setLimit("99999");
            requestPageInfoDto.setOffset("0");
        }
        ;
        return requestPageInfoDto;

    }

    /**
     * 
    * @author taoweifeng
    * @date 2018年5月22日
    * @param aclTableAlias
    * @param sqlsb
    * @param queryParam
    * @param type 填写参数时为dealercode=当前经销商,没参数时候过滤dealercode in (当前,厂商)
     */
    public static void appendAclSql(String aclTableAlias, StringBuilder sqlsb, List<Object> queryParam,
                                    boolean... type) {
        if (sqlsb == null || queryParam == null) {
            throw new DALException("权限拦截参数不正确");
        }

        // 拼装dealer_code 字段
        LoginInfoDto loginInfo = getSessionUserInfo();
        // 判断是否为业务单据,如果type不为空则
        if (StringUtils.isNullOrEmpty(type)) {
            sqlsb.append(getDealerSubString(loginInfo, aclTableAlias));
        } else {
            sqlsb.append(getDealerSubString(loginInfo, aclTableAlias, type));
        }
        getDealerParamList(loginInfo, queryParam);
    }

    /**
     * 
    * @author taoweifeng
    * @date 2018年5月22日
    * @param aclTableAlias
    * @param sqlsb
    * @param queryParam
    * @param type 填写参数时为dealercode=当前经销商,没参数时候过滤dealercode in (当前,厂商)
     */
    public static void appendAclSql(String aclTableAlias, StringBuffer sqlsb, List<Object> queryParam, boolean... type) {
        if (sqlsb == null || queryParam == null) {
            throw new DALException("权限拦截参数不正确");
        }

        // 拼装dealer_code 字段
        LoginInfoDto loginInfo = getSessionUserInfo();
        // 判断是否为业务单据,如果type不为空则
        if (!StringUtils.isNullOrEmpty(type) && type.length > 0) {
            sqlsb.append(getDealerSubString(loginInfo, aclTableAlias, type));
        } else {
            sqlsb.append(getDealerSubString(loginInfo, aclTableAlias));
        }
        getDealerParamList(loginInfo, queryParam);
    }

    /**
     * @param loginInfo
     * @param tableAlias
     * @return
     */
    private static String getDealerSubString(LoginInfoDto loginInfo, String tableAlias, boolean... type) {
        String tableAliasNew = StringUtils.isNullOrEmpty(tableAlias) ? "" : tableAlias + ".";
        if (!StringUtils.isNullOrEmpty(type) && type.length > 0) {
            if (loginInfo != null) {
                return " and " + tableAliasNew + BaseModel.Dealer_Code + " in(?,'"+FrameworkDictCodeConstants.DEALER_CODE+"')";
            } else {
                return " and " + tableAliasNew + BaseModel.Dealer_Code + " in(?)";
            }
        } else {
            return " and " + tableAliasNew + BaseModel.Dealer_Code + " =?";
        }
    }

}
