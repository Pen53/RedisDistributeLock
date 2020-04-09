package com.mepeng.cn.util;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCrt() {
        return crt;
    }

    public void setCrt(Date crt) {
        this.crt = crt;
    }

    private int id;
    private String name;
    private Date crt;
}
