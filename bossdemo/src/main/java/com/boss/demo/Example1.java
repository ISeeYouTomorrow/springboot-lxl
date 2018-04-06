package com.boss.demo;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * @author lxl
 * @description JSON格式转换问题，使用fastjson对json数据解析
 * @create 2018/4/5
 */
public class Example1 {

    /**
     * <p>静态方法，格式化json对象</p>
     * @param o 字符串或者hashmap
     * @return
     * @throws Exception
     */
    public static String jsonFormat(Object o) throws Exception {
            JsonFormatter formatter = new JsonFormatter(o);
            return formatter.toString();
    }

    /**
     * json格式化
     */
    private static final class JsonFormatter{

        private final static String ROOT = "";
        private Map<String,Object> map;

        public JsonFormatter(Object object) throws Exception {
            map = new LinkedHashMap<>();
            format(object,ROOT);
        }

        private void format(Object object,String parentKey) throws Exception {
            JSONObject jsonObject ;
            try {
                jsonObject = JSONObject.parseObject(object.toString());
            } catch (Exception e) {
                throw new RuntimeException("非法的json串");
            }
            Set<String> keys = jsonObject.keySet();
            Iterator<String> it = keys.iterator();
            String key;
            if(null != parentKey && !parentKey.equals(ROOT)){
                parentKey = parentKey+".";
            }else{
                parentKey = ROOT;
            }
            while (it.hasNext()){
                key = it.next();
                object = jsonObject.get(key);

                key = parentKey+ key;
                if(object instanceof JSONObject){
                    format(object,key);
                }else {
                    map.put(key,object);
                }
            }
        }

        public String toString() {
            return new JSONObject(map).toJSONString();
        }
    }

}
