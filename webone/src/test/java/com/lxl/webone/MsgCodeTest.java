package com.lxl.webone;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author lxl lukas
 * @description
 * @create 2018/4/2
 */
public class MsgCodeTest {

    public static void main(String[] args) {
        for(int i =1;i<1000;i++){
            MsgThread msgThread = new MsgThread("1373385"+String.format("%04d",i));
            msgThread.start();
        }
    }


    /**
     * juit不支持多线程
     */
    public void msgCodeTest(){
        try {
            sendPost(url1,"mobile=13733850586");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String url1 = "http://192.168.43.181:8011/api/member/sendMsg";
    private static String url2 = "http://192.168.43.181:8011/api/member/regMember";
    static class MsgThread extends Thread{
        private final String mobile;

        public MsgThread(String mobile) {
            this.mobile = mobile;
        }

        @Override
        public void run() {
            try {
                String result =  sendPost(url1,"type=1&phone_num="+mobile);
//                System.out.println("response: "+result);
                JSONObject jsonObject = new JSONObject(result);
                String data = jsonObject.getString("data");
                JSONObject json = new JSONObject(data);
                String code = json.getString("code");


                Thread.sleep(Math.round(Math.random())*1000);


                String result2 = sendPost(url2,"mobile="+mobile+"&code="+code+"&pwd=96e79218965eb72c92a549dd5a330112");
                System.out.println("response: "+result+" [ "+result2+" ]");
            } catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @作用 使用urlconnection
     * @param url
     * @param Params
     * @return
     * @throws IOException
     */
    public static String sendPost(String url,String Params)throws IOException {
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response="";
        try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            httpUrl = new URL(url);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //POST请求
            out = new OutputStreamWriter(
                    conn.getOutputStream());
            out.write(Params);
            out.flush();
            //读取响应
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                response+=lines;
            }
            reader.close();
            // 断开连接
            conn.disconnect();

//            System.out.println("response: "+response.toString());
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }

        return response;
    }

}
