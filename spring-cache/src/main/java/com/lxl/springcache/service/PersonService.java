package com.lxl.springcache.service;

import com.lxl.springcache.bean.Person;

public interface PersonService {
     Person save(Person person);

     void remove(Integer id);

     Person findOne(Person person);
}
