package com.example.chat_bed.Aop;

import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Aspect
public class ImageLoggerAspect {
    @Autowired
    private Ip2regionSearcher ip2regionSearcher;
    @Autowired
    private Aoplog log;


    /**
     * AOP 记录图片访问信息
     * @param joinpoint
     */
    @Before("execution(public * com.example.chat_bed.image.imageservice.getimage(..))")
    public void before(JoinPoint joinpoint){
        String[] str = (Arrays.toString(joinpoint.getArgs())).substring(1,Arrays.toString(joinpoint.getArgs()).length()-1).split(",",2);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String millisecondStrings = formatter.format(System.currentTimeMillis());
        Map map = mapStringToMap(str[1]);         //Object强转换为Map
        HashMap map1 = new HashMap();
        System.out.println(map);
        //System.out.println(Arrays.toString(str));
        if(null!=map.get("Referer"))
            map1.put("referer",map.get("Referer"));//获取请求头
        if(null!=map.get("ip")) {
            map1.put("ip", map.get("ip"));//获取IP地址
            map1.put("address",ip2regionSearcher.getAddress((String)map.get("ip")));//获取物理地址
        }

        map1.put("name",str[0]);
        map1.put("time",millisecondStrings);
       log.newimagelog(map1);
       log.lookadd(str[0]);
    }


    public static Map<String,String> mapStringToMap(String str){
        str = str.substring(1, str.length()-1);
        String[] strs = str.split(",");
        Map<String,String> map = new HashMap<String, String>();
        for (String string : strs) {
            String key = string.split("=")[0];
            String value = string.split("=")[1];
            // 去掉头部空格
            String key1 = key.trim();
            String value1 = value.trim();
            map.put(key1, value1);
        }
        return map;
    }
}
