package com.lxl.springcache;

import com.lxl.springcache.bean.Person;
import com.lxl.springcache.service.PersonService;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Map;

/**
 * 启动类中要记得开启缓存配置。
 *
 * 优先级顺序为：META-INF/resources > resources > static > public
 */
@ComponentScan(basePackages = "com.lxl.springcache")
@SpringBootApplication
@RestController
//@EnableConfigurationProperties({MSBean.class})
public class SpringCacheApplication extends WebMvcConfigurerAdapter{
	@Autowired
	private MSBean msBean;

	/**
	 * 自定义静态资源访问
	 * 自定义资源映射addResourceHandlers
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/lxl/**").addResourceLocations("file:D:/cv/");
		super.addResourceHandlers(registry);
	}

	/**
	 * 不需要Controller的页面跳转addViewControllers
	 * 以前写SpringMVC的时候，如果需要访问一个页面，
	 * 必须要写Controller类，然后再写一个方法跳转到页面，感觉好麻烦，其实重写WebMvcConfigurerAdapter中的addViewControllers方法即可达到效果了
	 * @param registry
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/toLogin").setViewName("login");
		super.addViewControllers(registry);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HRInterceptor()).addPathPatterns("/**").excludePathPatterns("/toLogin","/login");
		super.addInterceptors(registry);
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext run  = SpringApplication.run(SpringCacheApplication.class, args);
		Map<String,Object> pros = run.getEnvironment().getSystemProperties();
		for (String key:pros.keySet()
			 ) {
			System.out.println(key+" = "+pros.get(key));
		}
	}



	@Autowired
	private PersonService service;

	@Autowired
	private CacheManager cacheManager;

	@RequestMapping(value = "/put",method = RequestMethod.POST)
	public Person put(
			Person person
//			@RequestParam(required = true) String name,@RequestParam(required = true) String address,@RequestParam(required = true) Integer age
			){
//		Person person = new Person(name,age,address);
		System.out.println("save person: "+person);
		if(StringUtils.isBlank(person.toString())){
			System.out.println("person is null");
			return null;
		}
		showCache();
		return service.save(person);
	}

	//http://localhost:8080/able?id=1
	@RequestMapping("/able")
	@ResponseBody
	public Person cacheable(Person person){
		showCache();
		System.out.println("ms bean :"+msBean);
		return service.findOne(person);
	}

	//http://localhost:8080/evit?id=1
	@RequestMapping("/evit")
	public String  evit(Integer id){
		service.remove(id);
		showCache();
		return "ok";
	}


	private void showCache(){
		System.out.println("cacheManager :"+cacheManager);
		if(cacheManager instanceof EhCacheCacheManager){
			System.out.println("cacheManager name :EhCacheCacheManager");
		}else if(cacheManager instanceof ConcurrentMapCacheManager){
			System.out.println("cacheManager name :ConcurrentMapCacheManager");
		}else if(cacheManager instanceof GuavaCacheManager){
			System.out.println("cacheManager name :GuavaCacheManager");
		}else {

		}
		Class c = cacheManager.getClass();
		System.out.println("cacheManager: "+c.getClassLoader().getResource("").getPath());
	}
}
