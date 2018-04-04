package com.lxl.springmorecache;

import com.lxl.springmorecache.bean.Product;
import com.lxl.springmorecache.dao.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class SpringMorecacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMorecacheApplication.class, args);
	}

	@Autowired
	private ProductMapper productMapper;

	@GetMapping("/{id}")
	public Product getProductInfo(
			@PathVariable("id")
					Long productId) {
		// TODO
		return productMapper.select(productId);
	}
	@PutMapping("/{id}")
	public Product updateProductInfo(
			@PathVariable("id")
					Long productId,
			@RequestBody
					Product newProduct) {
		// TODO
		newProduct.setId(productId);
		productMapper.update(newProduct);
		return newProduct;
	}
}
