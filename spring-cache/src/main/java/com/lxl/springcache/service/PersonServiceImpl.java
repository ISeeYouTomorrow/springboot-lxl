package com.lxl.springcache.service;

import com.lxl.springcache.bean.Person;
import com.lxl.springcache.dao.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author lxl lukas
 * @description
 * @create 2018/1/29
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository repository;

    /**
     * CachePut缓存新增的或更新的数据到缓存,其中缓存名字是 person 。数据的key是person的id
     */
    @CachePut(value = "person", key = "#person.id")
    @Override
    public Person save(Person person) {
        Person p = repository.save(person);
        System.out.println("为id、key为:"+person.getId()+"数据做了缓存");
        return p;
    }

    /**
     *  CacheEvict 从缓存中删除key为id的数据
     * @param id
     */
    @CacheEvict(value = "person")
    @Override
    public void remove(Integer id) {
        System.out.println("删除了id、key为"+id+"的数据缓存");
        repository.delete(id);
    }

    /**
     * Cacheable缓存key为person 的id 数据到缓存people 中,如果没有指定key则方法参数作为key保存到缓存中
     * @param person
     * @return
     */
    @Cacheable(value = "person",key = "#person.id")
    @Override
    public Person findOne(Person person) {
        Person p = repository.findOne(person.getId());
        if(null != p)
            System.out.println("为id、key为:"+person.getId()+"数据做了缓存");
        return p;
    }
}
