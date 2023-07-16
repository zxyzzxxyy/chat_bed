package com.example.chat_bed.Aop;

import lombok.Data;

@Data
public class UserLogger {
    //用户ID
    private Integer userid;
    //用户名
    private String username;
    //密码
    private String password;
    //手机号
    private String phone;
    //邮箱
    private String email;
    //JWT凭证
    private String token;
    //IP地址
    private String ip;
    //物理地址
    private String address;
    //访问时间
    private String visit_time;
    //请求头
    private String referer;
    //方法名
    private String method_name;
    //方法注释
    private String explain;
    //API凭证
    private String apitoken;
    //操作结果
    private String status;
}

