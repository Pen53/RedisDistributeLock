package com.mepeng.cn.pen.common.annotation;

import com.mepeng.cn.pen.common.configurer.ActiveJdbcConfig;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.javalite.activejdbc.DB;
import org.javalite.activejdbc.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 打开连接池 切面
 */
@Component
@Aspect
public class TxConnectionAspect {
    @Autowired
    private DataSourceProvider4DruidDS dataSourceProvider4DruidDS;

    private String dbName = "default";
    //当前连接connection
    private ThreadLocal<HashMap<String, Connection>> tlCon = new ThreadLocal();
    //当前连接connection版本 只有版本为1时提交事务(即最开始开启事务的conncection 方法执行完后提交事务)
    private ThreadLocal<HashMap<String, Integer>> tlConVersion = new ThreadLocal();
    //当前连接TransactionStatus
    private  ThreadLocal<HashMap<String, TransactionStatus>> tlTxn = new ThreadLocal();
    //数据源缓存
    private Map<String, Object> dsMap = new ConcurrentHashMap();
    //事务平台管理器 PlatformTransactionManager缓存
    private Map<String, PlatformTransactionManager> txnMap = new ConcurrentHashMap<>();

    private static final Logger log = LoggerFactory.getLogger(TxConnectionAspect.class);

    @Autowired
    private ActiveJdbcConfig activeJdbcConfig;
    @Autowired
    ApplicationContext ac;
    /**
      * 切入点
      */
    @Pointcut("@annotation(com.mepeng.cn.pen.common.annotation.TxConnection) ")
    public void entryPoint() {
        // 无需内容
    }

    /**
     * 开始前置通知时,开启数据库连接 并且开启事务
     * @param joinPoint
     */
    @Before("entryPoint()")
    public void before(JoinPoint joinPoint) {
        log.info("=====================开始执行前置通知==================");
        try {
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class<?> targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();

            StringBuilder paramsBuf = new StringBuilder();
            for (Object arg : arguments) {
                paramsBuf.append(arg);
                paramsBuf.append("&");
            }
            log.info("[X用户]执行了类:" + targetName + ",方法名：" + methodName + ",参数:"+ paramsBuf.toString());
            log.info("=====================执行前置通知结束==================");

        } catch (Throwable e) {
            log.info("around " + joinPoint + " with exception : " + e.getMessage());
        }
    }

    /**
     * 开始后置通知时,关启数据库连接 并且关启事务
     * @param joinPoint
     */
//    @After("entryPoint()")
    public void after(JoinPoint joinPoint) {
        log.info("=====================开始执行后置通知==================");
        try {
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class<?> targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();

            StringBuilder paramsBuf = new StringBuilder();
            for (Object arg : arguments) {
                paramsBuf.append(arg);
                paramsBuf.append("&");
            }
            log.info("[X用户]执行了类:" + targetName + ",方法名：" + methodName + ",参数:"+ paramsBuf.toString());
            log.info("=====================执行后置通知结束==================");

        } catch (Throwable e) {
            log.info("around " + joinPoint + " with exception : " + e.getMessage());
        }
    }

    /**
     * 环绕通知处理处理
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("entryPoint()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Integer version = 1;
        String targetName = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        String key = activeJdbcConfig.getId();//connection key
        String dsId = "dsId:"+key;//数据源ID
        String txnId = "txnId:"+key;//事务ID
        try {

            HashMap<String, Connection> tlConMap = (HashMap)tlCon.get();

            HashMap<String, Integer> tlConVersionMap = (HashMap)tlConVersion.get();
            if(tlConVersionMap==null){
                tlConVersionMap = new HashMap<>();
                version = 1;
                tlConVersionMap.put(key,version+1);
                tlConVersion.set(tlConVersionMap);
            }else{
                version = tlConVersionMap.get(key);
                if(version==null){
                    version = 1;
                    tlConVersionMap.put(key,version+1);
                }else{
                    tlConVersionMap.put(key,version+1);
                }
            }
            System.out.println("version:"+version+",cur thread id:"+Thread.currentThread().getId()+",name:"+Thread.currentThread().getName());
            if(tlConMap==null){
                //本地线程没有设置
                tlConMap = new HashMap<>();
                tlConMap.put(key,null);
                tlCon.set(tlConMap);
            }
            Connection conn = (Connection)tlConMap.get(key);//从本地线程获取一个连接connection
            if (conn != null) {
                log.info("reuse conn : {}={}@{}", new Object[]{key, conn.getClass().getSimpleName(), Integer.toHexString(conn.hashCode())});
                //return conn;
                if(conn.isClosed()){

                }
            } else {
                //如果本地线程没有conncetion 从数据库数据源 获取一个
                Object ds = null;
//                if (this.ac.containsBean(dsId)) {
//                    //本服务有没有数据源
//                    ds = this.ac.getBean(dsId);
//                } else {
                    if(dsMap.containsKey(dsId)){
                        ds = dsMap.get(dsId);
                    }else {
                        //没有数据源 构建一个数据源
                        //DataSourceProvider4DruidDS dsDru = new DataSourceProvider4DruidDS();
                        ds = dataSourceProvider4DruidDS.buildDataSource(activeJdbcConfig.getUrl(), activeJdbcConfig.getDriver(), activeJdbcConfig.getUser(), activeJdbcConfig.getPassword());
                        dsMap.put(dsId, ds);
                    }
//                }

                HashMap<String, TransactionStatus> tlTxnMap = tlTxn.get();
                if(tlTxnMap==null){
                    tlTxnMap = new HashMap<>();
                    tlTxnMap.put(txnId,null);
                    tlTxn.set(tlTxnMap);
                }
                TransactionStatus ts = (TransactionStatus)tlTxnMap.get(txnId);
                if (ts != null) {
                    log.info("reuse txn : {}={}@{}", new Object[]{txnId, ts.getClass().getSimpleName(), Integer.toHexString(ts.hashCode())});
                } else {
                    PlatformTransactionManager ptm = null;
                    if(txnMap.containsKey(txnId)){
                        //有缓存
                        ptm = txnMap.get(txnId);
                    }else{
                        //DataSourceProvider4DruidDS dsDru = new DataSourceProvider4DruidDS();

                        ptm = dataSourceProvider4DruidDS.buildTransactionManager((DataSource)ds);
                        this.txnMap.put(txnId, ptm);
                    }


                    DefaultTransactionDefinition dtd = new DefaultTransactionDefinition();
//                    if (timeout > 0) {
//                        dtd.setTimeout(timeout);
//                    }

                    ts = ptm.getTransaction(dtd);
                    tlTxnMap.put(txnId, ts);//tlTxn
                    log.info("begin txn : {}={}@{}", new Object[]{txnId, ts.getClass().getSimpleName(), Integer.toHexString(ts.hashCode())});
                }
                //从数据源获取连接
                conn = ((DataSource)ds).getConnection();
                conn.setAutoCommit(false);//设置自动提交否
            }
            if(version==1) {
                //执行前打开connection
                DB db =  new DB(this.dbName);//构建DB
                db.attach(conn);
                log.info("version:1 db.attach(conn) conn : {}={}@{}", new Object[]{key, conn.getClass().getSimpleName(), Integer.toHexString(conn.hashCode())});
            }
            tlConMap.put(key,conn);//将当前连接放入本地线程中
            if(version==1) {
                log.info("ThreadLocal version:"+version+"---tlConVersion:{},tlTxn:{},tlCon:{}  put .",
                        Integer.toHexString(tlConVersion.hashCode()),
                        Integer.toHexString(tlTxn.hashCode()),
                        Integer.toHexString(tlCon.hashCode()));
                HashMap<String, Integer> logM1 = tlConVersion.get();
                HashMap<String, TransactionStatus> logM2 = tlTxn.get();
                HashMap<String, Connection> logM3 = tlCon.get();
                log.info("version:"+version+"---tlConVersion:{},tlTxn:{},tlCon:{}  put .",
                        Integer.toHexString(logM1.get(key).hashCode()),
                        Integer.toHexString(logM2.get(txnId).hashCode()),
                        Integer.toHexString(logM3.get(key).hashCode()));
            }
//            Registry.instance().init(this.dbName);//注册连接
            Registry.instance().getConfiguration();
            Object result = point.proceed();

            //执行后 关闭connection 并且提交事务
            try {
                //只有当版本为1时提交事务
                if(version==1) {
                    endTxn(true);
                    log.info("version:"+version+"---提交事务."+"targetName:"+targetName+",methodName:"+methodName);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return result;
        }catch (Exception e){
            //任何版本都能回滚事务 (如果版本为2使回滚事务,然后版本1的执行提交事务时,有兼容检查是否事务已经完成)
            try {
                endTxn(false);
                log.info("version:"+version+"---回滚事务."+"targetName:"+targetName+",methodName:"+methodName+",msg:"+e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw e;
        }finally {
            if(version==1) {
                DB db =  new DB(this.dbName);//构建DB
                db.detach();//解除绑定
                HashMap<String, Connection> tl = tlCon.get();
                if(tl!=null){
                    Connection conn = tl.get(key);
                    if(conn!=null) {
                        if (!conn.isClosed()) {
                            try {
                                conn.close();
                                log.info("finally close conn success : {}={}@{}", new Object[]{key, conn.getClass().getSimpleName(), Integer.toHexString(conn.hashCode())});
                            } catch (Exception e) {
                                log.info("finally close conn Exception:{}",e);
                            }
                        }
                        log.info("ThreadLocal version:"+version+"---conn:{},   remove it .",Integer.toHexString(conn.hashCode()));
                    }

                }

                tlConVersion.remove();
                tlTxn.remove();
                tlCon.remove();
                log.info("ThreadLocal version:"+version+"---tlConVersion:{},tlTxn:{},tlCon:{}  remove Content .",
                        Integer.toHexString(tlConVersion.hashCode()),
                        Integer.toHexString(tlTxn.hashCode()),
                        Integer.toHexString(tlCon.hashCode()));
            }
        }
//        try {
//             handleAround(point);// 处理日志
//        } catch (Exception e) {
//            log.error("日志记录异常", e);
//        }
//        return result;
    }

    /**
     * around日志记录
     * @param point
     * @throws Exception
     */
    public void handleAround(ProceedingJoinPoint point) throws Exception {
        Signature sig = point.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        // 方法名称
        String methodName = currentMethod.getName();
        // 获取注解对象
        TxConnection aLog = currentMethod.getAnnotation(TxConnection.class);
        // 类名
        String className = point.getTarget().getClass().getName();
        // 方法的参数
        Object[] params = point.getArgs();

        StringBuilder paramsBuf = new StringBuilder();
        for (Object arg : params) {
            paramsBuf.append(arg);
            paramsBuf.append("&");
        }
        // 处理log。。。。
        log.info("[X用户]执行了类:" + className + ",方法名：" + methodName + ",参数:"
                + paramsBuf.toString());
    }

    /**
     * 后置通知抛出异常时,关启数据库连接 并且回滚事务
     * @param joinPoint
     * @param e
     */
//    @AfterThrowing(pointcut = "entryPoint()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {

    }

    /**
     * 提交事务
     * @param isCommit
     * @throws Exception
     */
    public void endTxn(boolean isCommit) throws Exception {
        HashMap<String, TransactionStatus> tlMap = (HashMap)tlTxn.get();
        String key = activeJdbcConfig.getId();//connection key
        String dsId = "dsId:"+key;//数据源ID
        String txnId = "txnId:"+key;//事务ID
        if (tlMap != null && tlMap.size() != 0) {
            //String key = (String)tlMap.keySet().toArray()[0];
            TransactionStatus ts = (TransactionStatus) tlMap.get(txnId);
            if (ts.isCompleted()) {
                log.info("txn already completed : {}={}@{}", new Object[]{key, ts.getClass().getSimpleName(), Integer.toHexString(ts.hashCode())});
            } else {
                PlatformTransactionManager ptm = null;
                if (txnMap.containsKey(txnId)) {
                    //有缓存
                    ptm = txnMap.get(txnId);
                } else {
                    //DataSourceProvider4DruidDS dsDru = new DataSourceProvider4DruidDS();
                    Object ds = null;
                    if (dsMap.containsKey(dsId)) {
                        ds = dsMap.get(dsId);
                    } else {
                        //没有数据源 构建一个数据源
                        ds = dataSourceProvider4DruidDS.buildDataSource(activeJdbcConfig.getUrl(), activeJdbcConfig.getDriver(), activeJdbcConfig.getUser(), activeJdbcConfig.getPassword());
                        dsMap.put(dsId, ds);
                    }
                    ptm = dataSourceProvider4DruidDS.buildTransactionManager((DataSource) ds);
                    this.txnMap.put(txnId, ptm);
                }

                Iterator var7;
                String cid;
                if (!(ptm instanceof JtaTransactionManager)) {
                    Connection conn = null;
                    if (tlCon.get() != null && ((HashMap) tlCon.get()).size() > 0) {
                        var7 = ((HashMap) tlCon.get()).keySet().iterator();

                        while (var7.hasNext()) {
                            cid = (String) var7.next();
                            conn = (Connection) ((HashMap) tlCon.get()).get(cid);
                            if (isCommit) {
                                conn.commit();
                                System.out.println(conn.getClass()+"--conn.commit()");
                                log.info("Commit conn : {}={}@{}", new Object[]{cid, conn.getClass().getSimpleName(), Integer.toHexString(conn.hashCode())});
                            } else {
                                conn.rollback();
                                log.info("Rollback conn : {}={}@{}", new Object[]{cid, conn.getClass().getSimpleName(), Integer.toHexString(conn.hashCode())});
                            }
                        }
                    }
                }

                if (isCommit) {
                    ptm.commit(ts);
                    log.info("Commit txn : {}={}@{}", new Object[]{key, ts.getClass().getSimpleName(), Integer.toHexString(ts.hashCode())});
                } else {
                    ptm.rollback(ts);
                    log.info("Rollback txn : {}={}@{}", new Object[]{key, ts.getClass().getSimpleName(), Integer.toHexString(ts.hashCode())});
                }
                //关闭连接connection
                if (tlCon.get() != null && ((HashMap) tlCon.get()).size() > 0) {

                    var7 = ((HashMap) tlCon.get()).keySet().iterator();
                    Connection conn = null;
                    while (var7.hasNext()) {
                        cid = (String) var7.next();
                        conn = (Connection) ((HashMap) tlCon.get()).get(cid);
                        if (!conn.isClosed()) {
                            conn.close();
                            log.info("close conn success : {}={}@{}", new Object[]{key, conn.getClass().getSimpleName(), Integer.toHexString(conn.hashCode())});
                        }
                    }

                    /*int idx = true;
                    var7 = ((HashMap) tlCon.get()).keySet().iterator();

                    while (var7.hasNext()) {
                        cid = (String) var7.next();
                        int idx = cid.indexOf("#");
                        if (idx > 0) {
                            this.doCloseConn(cid.substring(0, idx), cid.substring(idx + 1));
                        } else {
                            this.doCloseConn(cid, (String) null);
                        }
                    }*/
                }
            }
        }
    }
}
