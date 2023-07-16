package com.example.chat_bed.config;

import com.example.chat_bed.image.imagedao;
import com.example.chat_bed.util.Jwt;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class ImageInterceptor implements HandlerInterceptor {

    @Autowired
    private imagedao dao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private Jwt jwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String imagename=request.getRequestURL().substring(request.getRequestURL().lastIndexOf("/")+1,request.getRequestURL().length());
        Integer time=0;
        String sign="";
        Map<String, String[]> params = request.getParameterMap();
        System.out.println(params.toString());
        if(!Arrays.toString(params.get("t")).equals("[ul]")){

            time=Integer.valueOf(Arrays.toString(params.get("t")).toString().substring(1,Arrays.toString(params.get("t")).toString().length()-1));
        }
        if(!Arrays.toString(params.get("sign")).equals("[ul]")){

            sign=Arrays.toString(params.get("sign")).toString().substring(1,Arrays.toString(params.get("sign")).toString().length()-1);
        }
        String referer = request.getHeader("Referer");
        String user_setting = "user:setting:"+(imagename.split("_",2)[0]);
        System.out.println(time+" "+sign);
        //判断是否有图片作者设置信息 如果没有则从数据库获取存到reids
        if(!redisTemplate.hasKey(user_setting)) {
            redisTemplate.opsForValue().set(user_setting,dao.getusersetting(Integer.valueOf(imagename.split("_",2)[0])));
        }
        HashMap setting = (HashMap) redisTemplate.opsForValue().get(user_setting);
        //如果请求头设置不为空则判断请求头是否一致

        if(setting.get("referer")!=null) {
            if(!setting.get("referer").equals(referer)) {
                System.out.println("referer:"+referer);
                if(referer.equals("http://127.0.0.1:8080/")||
                        referer.equals("http://127.0.0.1:8081/")||
                        referer.equals("http://localhost:8080/")||
                        referer.equals("http://localhost:8081/")||
                        referer.equals("https://image.txtz.link/")||
                        referer.equals("http://127.0.0.1:8080")||
                        referer.equals("http://127.0.0.1:8081")||
                        referer.equals("http://localhost:8080")||
                        referer.equals("http://localhost:8081")||
                        referer.equals("https://image.txtz.link")
                ){
                  return true;
                }
                else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    System.out.println("请求头不对");
                    return false;
                }
            }else{
                System.out.println("请求头正确");
            }
        }
        //如果签名秘钥不为空则判断签名是否一致
        System.out.println("sign: "+setting.get("sign"));
        if(setting.get("sign")!=null&&!sign(imagename,sign,time,setting.get("sign").toString(),Integer.valueOf(setting.get("delay").toString()))){

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
       // System.out.println("后置方法，正在执行");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
       // System.out.println("完成方法，正在执行");
    }

    /**
     * 签名验证
     * @param imagename 图片名
     * @param signmd5 签名
     * @param time 时间戳
     * @param sign 签名秘钥
     * @param delay 允许延迟范围
     * @return
     */
    public Boolean sign(String imagename,String signmd5,Integer time,String sign,Integer delay){
        System.out.println("进入签名验证:");
        Integer nowtime =(int)(System.currentTimeMillis()/1000);
        if(nowtime-delay>time){
            System.out.println("时间不对");
            return false;
        }else{

        }
        String temp = DigestUtils.md5Hex(sign+imagename+time);
        if(temp.equals(signmd5)){
            System.out.println("签名匹配");
            return true;
        }
        else {
            System.out.println("签名不匹配");
            return false;
        }
    }

}
