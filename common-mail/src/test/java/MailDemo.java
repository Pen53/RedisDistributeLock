import com.mepeng.cn.commonMail.MailUtil;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.MessagingException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试发送邮件
 */
public class MailDemo {
    public static void main(String[] args) {
        try {
            File file1 = new File("d:\\daohang.jpg");
            File file2 = new File("d:\\balking.txt");
            File file3 = new File("d:\\QQ截图.png");
            File file4 = new File("d:\\t_address1.xls");
            List<Map<String,Object>> files = new ArrayList<Map<String,Object>>();
            Map<String, Object> m1 = new HashMap<String,Object>();
            m1.put("name",file1.getName());
            try {

                m1.put("fileIsm",new FileInputStream(file1));
                System.out.println("m1:"+m1);
                files.add(m1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map<String, Object> m2 = new HashMap<String,Object>();
            m2.put("name",file2.getName());
            try {

                m2.put("fileIsm",new FileInputStream(file2));
                System.out.println("m2:"+m2);
                files.add(m2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map<String, Object> m3 = new HashMap<String,Object>();
            m3.put("name",file3.getName());
            try {

                m3.put("fileIsm",new FileInputStream(file3));
                System.out.println("m3:"+m3);
                files.add(m3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map<String, Object> m4 = new HashMap<String,Object>();
            m4.put("name",file4.getName());
            try {

                m4.put("fileIsm",new FileInputStream(file4));
                System.out.println("m4:"+m4);
                files.add(m4);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String to = "luoxiaobin10291111@163.com";
            System.out.println("send mail begin..to:"+to);

            String host = "smtp.qq.com";
            int port = 465;
            String username="872502023@qq.com";
            String pwd = "yghpmusxtbqybbge";
            String personal = "Even";

            JavaMailSenderImpl mailSender = MailUtil.createMailSender(host, port, username, pwd, personal);
            MailUtil.sendMailIsm(mailSender,username,personal,to,"Demo01","123html1234562233",files);
            System.out.println("send mail ok..");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main12(String[] args) {
        try {
            File file1 = new File("d:\\daohang.jpg");
            File file2 = new File("d:\\balking.txt");
            File file3 = new File("d:\\QQ截图.png");

            List<File> files = new ArrayList<File>();
            files.add(file1);
            files.add(file2);
            files.add(file3);
            String to = "xxxx@qq.com";
            System.out.println("send mail begin..to:"+to);

            String host = "smtp.qq.com";
            int port = 465;
            String username="xxx@qq.com";
            String pwd = "mlgutkbfknqvbdgg";
            String personal = "mepeng彭";
            JavaMailSenderImpl mailSender = MailUtil.createMailSender(host, port, username, pwd, personal);
            //to = "xxxx@163.com";
            MailUtil.sendMail(mailSender,username,personal,to,"s1503","html123456",files);
            System.out.println("send mail ok..");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public static void main3(String[] args) {
        try {
            File file1 = new File("d:\\daohang.jpg");
            File file2 = new File("d:\\balking.txt");
            File file3 = new File("d:\\QQ截图.png");

            List<File> files = new ArrayList<File>();
            files.add(file1);
            files.add(file2);
            files.add(file3);
            String to = "xxxx@qq.com";
            System.out.println("send mail begin..to:"+to);
            MailUtil.sendMail(to,"subject04","123456some content.",files);
            System.out.println("send mail ok..");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
