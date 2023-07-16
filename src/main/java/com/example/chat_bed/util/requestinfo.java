package com.example.chat_bed.util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class requestinfo {
    public static Map requestinfo(HttpServletRequest request){
        Map map = new HashMap();
        if(null!=request.getRemoteAddr())
        map.put("ip",request.getRemoteAddr());
        if(null!=request.getHeader("Referer"))
        map.put("Referer",request.getHeader("Referer"));
        if(null!=request.getHeader("cookie"))
        map.put("cookie",request.getHeader("cookie"));
        return map;
    }
}
