package com.mepeng.cn.pen.order.domains.PO;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("employee")
@IdName("id")
public class Employee extends Model {
    private static final long serialVersionUID = 1L;
}
