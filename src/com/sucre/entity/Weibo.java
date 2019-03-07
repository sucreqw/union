package com.sucre.entity;

import com.sucre.factor.Factor;
import com.sucre.utils.MyUtil;

abstract public class Weibo {
    private int NO;
    private String id;
    private String pass;
    private String name;
    private String cookie;
    private String uid;
    private String s;
    private int level;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int status;
    private String events;

    public int getNO() {
        return NO;
    }

    public void setNO(int nO) {
        NO = nO;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    abstract public int Actions(int index, String mission);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }


    @Override
    public String toString() {
        //return "Weibo [NO=" + NO + ", id=" + id + ", pass=" + pass + ", name=" + name + ", cookie=" + cookie + ", uid="
        //	+ uid + ", s=" + s + ", level=" + level + ", status=" + status + ", events=" + events + "]";

        return cookie + "|" + uid + "|" + id + "|" + pass + "|" + s;
    }

    public Weibo() {
        super();
    }

    public Weibo(String id, String pass) {
        this.id = id;
        this.pass = pass;
    }

    // 从文件数据里加载数据！
    public Weibo(String inputdata) {
        load(inputdata);
    }

    public void load(String inputdata) {
        try {
            String[] temp = inputdata.split("\\|");
            if (temp.length == 8) {
                //key.txt
                this.cookie = temp[6];
                this.uid = temp[5];
                this.s = temp[2];
                this.id = "";
                this.pass = "";
            } else {
                this.cookie = temp[0];
                this.uid = temp[1];
                this.id = temp[2];
                this.pass = temp[3];
                this.s = temp.length >= 5 ? temp[4] : "";
            }
        } catch (Exception e) {
            MyUtil.print("导入weibo数据出错!", Factor.getGui());
        }
    }

}



