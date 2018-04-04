package com.lxl.springcache.bean;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author lxl lukas
 * @description
 * @create 2018/1/29
 */
@Entity
@Table(name = "person")
public class Person implements Serializable{
    private Integer id;
    private String name;
    private Integer age;
    private String address;

    public Person() {
    }

    public Person(String name, Integer age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public Person(Integer id,String name, Integer age, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "id: "+id+" ;name: "+name+" ; age: "+age+" ; address: "+address;
    }
}
