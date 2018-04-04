package com.lxl.webone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class WeboneApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeboneApplication.class, args);
	}

	@RequestMapping(value = "/hello")
	public String hello(){
		Date date = new Date();
		System.out.println("date :"+date.toString());


		return "hello,i m spring boot!";
	}

	/**
	 * 根据手机号获取注册码
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/getcode",method = RequestMethod.POST)
	public Object getCode(@RequestParam String mobile){
		System.out.println("getcode for mobile: "+mobile);
		if(null != MsgCoder.getInstance().getCode(mobile)){
			System.out.println("mobile exist");
			return new Code(mobile,MsgCoder.getInstance().getCode(mobile));
		}else {
			System.out.println("mobile not exist");
			Integer code = (int) ((Math.random() * 9 + 1) * 100000);
			MsgCoder.getInstance().putCode(mobile, code.toString());
		}
		return new Code(mobile,MsgCoder.getInstance().getCode(mobile));
	}

	/**
	 * 校验注册码
	 * @param mobile
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/checkCode",method = RequestMethod.POST)
	@ResponseBody
	public String checkCode(@RequestParam String mobile,@RequestParam String code){
		String result = "success";
		System.out.println("mobile = "+mobile+" ;code: "+code);

		String tempCode = MsgCoder.getInstance().getCode(mobile);
		if(code!=null && tempCode != null && code .equals(tempCode)){
			MsgCoder.getInstance().remove(mobile);
			System.out.println("注册成功!"+MsgCoder.getInstance().getCount());
		}else {
			result = "error";
		}
		return result;
	}


	class Code{
		private String mobile;

		private String code;

		public Code(String mobile, String code) {
			this.mobile = mobile;
			this.code = code;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
	}
	static final class MsgCoder{
		private Map<String ,String> codeMap = null;

		private MsgCoder(){
			codeMap = new HashMap<>();
		}

		private static class MsgCoderHandler{
			private static MsgCoder msgCoder = new MsgCoder();
		}

		public static synchronized MsgCoder getInstance(){
			return MsgCoderHandler.msgCoder;
		}

		public void putCode(String mobile,String code){
			codeMap.put(mobile, code);
		}

		public void remove(String mobile){
			codeMap.remove(mobile);
		}

		public String getCode(String mobile){
			return codeMap.get(mobile);
		}

		public void clear(){
			codeMap.clear();
		}

		public Integer getCount() {
			return codeMap.size();
		}
	}


}
