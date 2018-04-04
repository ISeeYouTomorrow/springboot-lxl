package com.lxl.webone;

import com.lxl.webone.dao.domain.Product;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试thymeleaf
 * @author lxl lukas
 * @description
 * @create 2018/1/31
 */
@Controller
@RequestMapping("/learn")
public class PageController {

    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("/index");

        List<Product> products = new ArrayList<>();
        for(int i=0;i<10;i++){
            Product p = new Product();
            p.setName("洗衣液"+i);
            p.setPrice(i);
            p.setId(i);
            products.add(p);
        }
        mv.addObject("products",products);
        return mv;
    }

}
