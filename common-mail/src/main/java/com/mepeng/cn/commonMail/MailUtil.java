package com.mepeng.cn.commonMail;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件发送
 */
public class MailUtil {
    private static final String HOST = MailConfig.host;
    private static final Integer PORT = MailConfig.port;
    private static final String USERNAME = MailConfig.userName;
    private static final String PASSWORD = MailConfig.passWord;
    private static final String emailForm = MailConfig.emailForm;
    private static final String personal = MailConfig.personal;
    private static JavaMailSenderImpl mailSender = createMailSender();

    /**
     * 自定义 邮件发送器
     * @param host 服务器
     * @param port  邮箱端口
     * @param username  邮箱
     * @param password  邮箱授权码
     * @param personal 发送人
     * @return
     */
    public static JavaMailSenderImpl createMailSender(String host,int port
            ,String username,String password, String personal ) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(password);
        sender.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", "25000");
        //p.setProperty("mail.smtp.auth", "false");
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        sender.setJavaMailProperties(p);
        return sender;

    }

    /**
     * 邮件发送器
     * @return 配置好的工具
     */
    private static JavaMailSenderImpl createMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(HOST);
        sender.setPort(PORT);
        sender.setUsername(USERNAME);
        sender.setPassword(PASSWORD);
        sender.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", "25000");
        //p.setProperty("mail.smtp.auth", "false");
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        sender.setJavaMailProperties(p);
        System.out.println("createMailSender HOST:"+HOST+",PORT:"+PORT+",USERNAME:"+USERNAME+",PASSWORD:"+PASSWORD);
        return sender;
    }

    /**
     * 发送邮件(自定义邮箱发送者)
     * @param mailSender 邮箱发送者
     * @param from 发送邮箱
     * @param personal 发送
     * @param to 接受人
     * @param subject 邮件主题
     * @param html 邮件内容
     * @param files 邮件附件
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static void sendMail(JavaMailSenderImpl mailSender,String from,String personal,
                                String to,String  subject,String html,List<File> files) throws MessagingException, UnsupportedEncodingException, MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(from, personal);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        messageHelper.setPriority(30000);
        if(files!=null) {
            for (File file : files) {
                messageHelper.addAttachment(file.getName(), file);//附件
            }
        }
        mailSender.send(mimeMessage);
    }
    /**
     * 发送邮件(自定义邮箱发送者)
     * @param mailSender 邮箱发送者
     * @param from 发送邮箱
     * @param personal 发送
     * @param to 接受人
     * @param cc 抄送人
     * @param subject 邮件主题
     * @param html 邮件内容
     * @param files 邮件附件
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static void sendMail(JavaMailSenderImpl mailSender,String from,String personal,
                                String[] to,String[] cc,String  subject,String html,List<File> files) throws MessagingException, UnsupportedEncodingException, MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(from, personal);
        messageHelper.setTo(to);
        if(cc!=null&&cc.length>0){
            messageHelper.setCc(cc);
        }
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        messageHelper.setPriority(30000);
        if(files!=null) {
            for (File file : files) {
                messageHelper.addAttachment(file.getName(), file);//附件
            }
        }
        mailSender.send(mimeMessage);
    }
    /**
     * 发送邮件(自定义邮箱发送者)
     * @param mailSender 邮箱发送者
     * @param from 发送邮箱
     * @param personal 发送
     * @param to 接受人
     * @param subject 邮件主题
     * @param html 邮件内容
     * @param list 邮件附件 List<Map<String,Object>> Map<String,Object> name:附件名  fileIsm:java.io.InputStream
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static void sendMailIsm(JavaMailSenderImpl mailSender,String from,String personal,
                                   String to,String  subject,String html,List<Map<String,Object>> list) throws MessagingException, IOException, MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(from, personal);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        messageHelper.setPriority(30000);
        if(list!=null) {
            for (Map<String,Object> map : list) {
                messageHelper.addAttachment(MimeUtility.encodeWord(map.get("name").toString()), new ByteArrayResource(IOUtils.toByteArray((InputStream) map.get("fileIsm"))));
            }
        }
        mailSender.send(mimeMessage);
    }
    /**
     * 发送邮件(自定义邮箱发送者)
     * @param mailSender 邮箱发送者
     * @param from 发送邮箱
     * @param personal 发送
     * @param to 接受人
     * @param cc 抄送人
     * @param subject 邮件主题
     * @param html 邮件内容
     * @param list 邮件附件 List<Map<String,Object>> Map<String,Object> name:附件名  fileIsm:java.io.InputStream
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static void sendMailIsm(JavaMailSenderImpl mailSender,String from,String personal,
                                   String to,String[] cc,String  subject,String html,List<Map<String,Object>> list) throws MessagingException, IOException, MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(from, personal);
        messageHelper.setTo(to);
        if(cc!=null&&cc.length>0){
            messageHelper.setCc(cc);
        }
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        messageHelper.setPriority(30000);
        if(list!=null) {
            for (Map<String,Object> map : list) {
                messageHelper.addAttachment(MimeUtility.encodeWord(map.get("name").toString()), new ByteArrayResource(IOUtils.toByteArray((InputStream) map.get("fileIsm"))));
            }
        }
        mailSender.send(mimeMessage);
    }
    /**
     * 发送邮件(配置文件中配置邮箱发送者)
     * @param to 接受人
     * @param subject 邮件主题
     * @param html 邮件内容
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static void sendMail(String to,String  subject,String html) throws MessagingException, UnsupportedEncodingException, MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(emailForm, personal);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        messageHelper.setPriority(30000);
        mailSender.send(mimeMessage);
    }

    /**
     * 发送邮件(配置文件中配置邮箱发送者)
     * @param to 接受人
     * @param cc 抄送人
     * @param subject 邮件主题
     * @param html 邮件内容
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static void sendMail(String to,String[] cc,String  subject,String html) throws MessagingException, UnsupportedEncodingException, MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(emailForm, personal);
        messageHelper.setTo(to);
        if(cc!=null&&cc.length>0){
            messageHelper.setCc(cc);
        }
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        messageHelper.setPriority(30000);
        mailSender.send(mimeMessage);
    }

    /**
     * 发送邮件
     *
     * @param to 接受人
     * @param subject 主题
     * @param html 发送内容
     * @param list 邮件附件 List<Map<String,Object>> Map<String,Object> name:附件名  fileIsm:java.io.InputStream
     * @throws MessagingException 异常
     * @throws UnsupportedEncodingException 异常
     */
    public static void sendMailIsm(String to, String subject,String html,List<Map<String,Object>> list) throws MessagingException, IOException, MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(emailForm, personal);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        messageHelper.setPriority(30000);
        if(list!=null) {
            for (Map<String,Object> map : list) {
                messageHelper.addAttachment(MimeUtility.encodeWord(map.get("name").toString()), new ByteArrayResource(IOUtils.toByteArray((InputStream) map.get("fileIsm"))));
            }
        }
        mailSender.send(mimeMessage);
    }
    /**
     * 发送邮件
     *
     * @param to 接受人
     * @param cc 抄送人
     * @param subject 主题
     * @param html 发送内容
     * @param list 邮件附件 List<Map<String,Object>> Map<String,Object> name:附件名  fileIsm:java.io.InputStream
     * @throws MessagingException 异常
     * @throws UnsupportedEncodingException 异常
     */
    public static void sendMailIsm(String to,String[] cc,String subject,String html,List<Map<String,Object>> list) throws MessagingException, IOException, MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(emailForm, personal);
        messageHelper.setTo(to);
        if(cc!=null&&cc.length>0){
            messageHelper.setCc(cc);
        }
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        messageHelper.setPriority(30000);
        if(list!=null) {
            for (Map<String,Object> map : list) {
                messageHelper.addAttachment(MimeUtility.encodeWord(map.get("name").toString()), new ByteArrayResource(IOUtils.toByteArray((InputStream) map.get("fileIsm"))));
            }
        }
        mailSender.send(mimeMessage);
    }
    /**
     * 发送邮件
     *
     * @param to 接受人
     * @param subject 主题
     * @param html 发送内容
     * @throws MessagingException 异常
     * @throws UnsupportedEncodingException 异常
     */
    public static void sendMail(String to, String subject,String html, List<File> files) throws MessagingException, UnsupportedEncodingException, MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(emailForm, personal);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        messageHelper.setPriority(30000);
        if(files!=null) {
            for (File file : files) {
                messageHelper.addAttachment(file.getName(), file);//附件
            }
        }
        mailSender.send(mimeMessage);
    }

    /**
     * 发送邮件
     *
     * @param to 接受人
     * @param cc 抄送人
     * @param subject 主题
     * @param html 发送内容
     * @throws MessagingException 异常
     * @throws UnsupportedEncodingException 异常
     */
    public static void sendMail(String to,String[] cc, String subject,String html, List<File> files) throws MessagingException, UnsupportedEncodingException, MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(emailForm, personal);
        messageHelper.setTo(to);
        if(cc!=null&&cc.length>0){
            messageHelper.setCc(cc);
        }
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        messageHelper.setPriority(30000);
        if(files!=null) {
            for (File file : files) {
                messageHelper.addAttachment(file.getName(), file);//附件
            }
        }
        mailSender.send(mimeMessage);
    }
}

