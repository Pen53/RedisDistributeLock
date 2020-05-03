package com.mepeng.cn.pen.order.controller;

import com.mepeng.cn.pen.order.domains.MakeInstrumentationUtil;
import com.mepeng.cn.pen.order.domains.PO.Employee;
import org.javalite.activejdbc.DB;

public class Demo {
//    new DB("corporation").open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/test",
//                                       "root", "p@ssw0rd");
//       new DB("university").open("oracle.jdbc.driver.OracleDriver",
//                                         "jdbc:oracle:thin:@localhost:1521:xe",
//                                         "activejdbc", "activejdbc");
//
//       Employee.deleteAll();
//       Student.deleteAll();
//
//       Employee.createIt("first_name", "John", "last_name", "Doe");
//       Employee.createIt("first_name", "Jane", "last_name", "Smith");
//
//       Student.createIt("first_name", "Mike", "last_name", "Myers");
//       Student.createIt("first_name", "Steven", "last_name", "Spielberg");
//
//       System.out.println("*** Employees ***");
//       Employee.findAll().dump();
//       System.out.println("*** Students ***");
//       Student.findAll().dump();
//
//       new DB("corporation").close();
//       new DB("university").close();
public static void main(String[] args) {
    //MakeInstrumentationUtil.make(); // 这里创建Instrumentation
    new DB("default").open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/seata?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8",
            "root", "andy123L");

    //Employee.deleteAll();
//    Employee.createIt("first_name", "John", "last_name", "Doe");
//    Employee.createIt("first_name", "Jane", "last_name", "Smith");

    System.out.println("*** Employees ***");
    Employee.findAll().dump();

    Employee e = Employee.findById(3);
    System.out.println("e:"+e);
    new DB("default").close();
}
}
