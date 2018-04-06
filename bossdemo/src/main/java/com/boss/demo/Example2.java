package com.boss.demo;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author lxl
 * @description 按行进行数据存取
 * @create 2018/4/5
 */
public class Example2 {


    private  static  final char EQUAL = '=';
    private  static  final char SEP = ';';
    private  static  final CharSequence BR = "\\n";
    private  static  final char NEW_LINE = '\n';

    /**
     * <p>保存方法</p>
     * @param data 待存key-value集合元素的数据
     * @return 集合数组的文本存储结构
     */
    public static String store(HashMap<String,String>[] data) throws IllegalArgumentException{
        if(null == data || data.length == 0){
            throw new IllegalArgumentException("参数为空");
        }
        StringBuilder text = new StringBuilder();
//        text.append("text = ");
        for (HashMap<String,String> map:data
             ) {
            new Text(map, text);
        }
        return text.toString();
    }

    /**
     * <p>将文本字符串内容读取为数组</p>
     * @param text
     * @return
     */
    public static HashMap[] load(String text){
        if(null == text){
            throw new IllegalArgumentException("text is null");
        }
        return new BossArrayBuilder().buildMapArray(text);
    }

    /**
     * 自定义文本类
     */
    static final class Text{

        private StringBuilder sb;

        public Text(HashMap<String, String> map,StringBuilder sb) {
            this.sb = sb;
            if(null != map && map.size()>0){
                Iterator<String> it = map.keySet().iterator();
                String key,value;
                while (it.hasNext()){
                    key = it.next();
                    value = map.get(key);
                    sb.append(key).append(EQUAL).append(value);
                    if(it.hasNext()){
                        sb.append(SEP);
                    }
                }
            }
            sb.append(NEW_LINE);
        }

        @Override
        public String toString() {
            return sb.toString();
        }
    }

    /**
     * 自定义文本转换类
     */
    static final class BossArrayBuilder{

        public HashMap[] buildMapArray(String text){
            String[] datas = text2array(text);
            HashMap[] mapArray = new HashMap[datas.length];
            int i = 0;
            for (String data:datas
                    ) {
                mapArray[i++] = text2map(data);
            }
            return mapArray;
        }

        private String[] text2array(String text){
            String[] temp = text.split(String.valueOf(NEW_LINE));
            return temp;
        }

        private HashMap text2map(String text){
            String[] temp = text.split(String.valueOf(SEP));
            HashMap map = new HashMap(temp.length);
            String[] tm ;
            for (String kv:temp
                    ) {
                tm = kv.split(String.valueOf(EQUAL));
                map.put(tm[0],tm[1]);
            }
            return map;
        }
    }

}
