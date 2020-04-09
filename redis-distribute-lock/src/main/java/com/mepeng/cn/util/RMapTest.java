package com.mepeng.cn.util;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RMapTest {
    public static void main(String[] args) {
        Config config = new Config();

        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://127.0.0.1:6379");
        //singleServerConfig.setPassword("9client!");

        RedissonClient redissonClient = Redisson.create(config);
        ReadWriteLock(redissonClient);
    }
    public static void main1(String[] args) {
        Config config = new Config();

        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://127.0.0.1:6379");
        //singleServerConfig.setPassword("9client!");

        RedissonClient redissonClient = Redisson.create(config);

        RMap<String, Object> map = redissonClient.getMap("myMap");
        boolean flag = map.expire(10, TimeUnit.SECONDS);
        System.out.println("myMap expire 10s flag:"+flag);
        //map.put("aaaa", "1234");
        //map.put("bbbb", "4321");

        System.out.println("bbbb:"+map.get("bbbb"));
        Student st = new Student();
        st.setId(1);
        st.setName("csf");
        st.setCrt(new Date());
        System.out.println("st before:"+st);
        //map.put("st", st);
        System.out.println("st:"+map.get("st"));
        Object st1 = map.get("st");
        if(st1!=null&&st1 instanceof  Student){
            Student s1 = (Student) st1;
            System.out.println("s1:"+s1);
        }
        RMap<String, Object> map1 = redissonClient.getMap("myMap2");
        System.out.println("map1:"+map1);

    }

    /**
     * 读写锁
     * 布式可重入读写锁允许同时有多个读锁和一个写锁处于加锁状态。
     * 当有读锁锁定时,写锁要锁定会等待
     * 当有写锁锁定时,读锁会等待
     * 同一时刻只会有一个写锁锁定,但是同一时刻可以有多个读锁锁定
     * @param redissonClient
     */
    public static void ReadWriteLock(RedissonClient redissonClient){
        RReadWriteLock rwlock = redissonClient.getReadWriteLock("anyRWLock");
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    System.out.println("start lock  threadName:"+Thread.currentThread().getName()+",sleep 65s");
                    rwlock.readLock().lock();
                    System.out.println("rwlock.readLock().lock() threadName:"+Thread.currentThread().getName()+",sleep 65s");
                    Thread.sleep(65_000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    rwlock.readLock().unlock();
                    System.out.println("unlock  threadName:"+Thread.currentThread().getName());
                }
            }
        });
        t1.setName("t1");
        t1.start();
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    System.out.println("start lock  threadName:"+Thread.currentThread().getName()+",sleep 50s");
                    rwlock.readLock().lock();
                    System.out.println("rwlock.readLock().lock() threadName:"+Thread.currentThread().getName()+",sleep 50s");
                    Thread.sleep(50_000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    rwlock.readLock().unlock();
                    System.out.println("unlock  threadName:"+Thread.currentThread().getName());
                }
            }
        });
        t2.setName("t2");
        t2.start();
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    System.out.println("start lock  threadName:"+Thread.currentThread().getName()+",sleep 70s");
                    rwlock.readLock().lock();
                    System.out.println("rwlock.readLock().lock() threadName:"+Thread.currentThread().getName()+",sleep 70s");
                    Thread.sleep(70_000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    rwlock.readLock().unlock();
                    System.out.println("unlock  threadName:"+Thread.currentThread().getName());
                }
            }
        });
        t3.setName("t3");
        t3.start();
        try {
            System.out.println("main sleep 25s next write thead start");
            Thread.sleep(25_000);
            System.out.println("main sleep 25s next write thead over");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread tWrite = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    System.out.println("start lock  threadName:"+Thread.currentThread().getName()+",sleep 30s");
                    rwlock.writeLock().lock();
                    System.out.println("rwlock.readLock().lock() threadName:"+Thread.currentThread().getName()+",sleep 30s");
                    Thread.sleep(30_000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    rwlock.writeLock().unlock();
                    System.out.println("unlock  threadName:"+Thread.currentThread().getName());
                }
            }
        });
        tWrite.setName("tWrite");
        tWrite.start();
    }
}
