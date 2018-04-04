package com.lxl.springmorecache.dao;

import com.lxl.springmorecache.bean.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper{
    Product select(@Param("id")
                    long id);
    void update(Product product);
}
