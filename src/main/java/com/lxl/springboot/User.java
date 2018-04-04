package com.lxl.springboot;

/**
 * @author lxl lukas
 * @description
 * @create 2018/1/28
 */
public class User {

    public int hashCode;
    private Integer id;
    private String userName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "id: "+id+" ,userName: "+userName;
    }

    public int getHashCode() {
        return hashCode;
    }
}
