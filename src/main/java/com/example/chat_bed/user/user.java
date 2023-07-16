package com.example.chat_bed.user;

import lombok.Data;

@Data
public class user {
//    用户ID
    private Integer userid;
//    用户名
    private String username;
//    用户密码
    private String password;
//    用户电话号
    private String phone;
//    用户邮箱
    private String email;
//    用户凭证
    private String token;
//    已上传图片总数
    private Integer image_num;
//    权限
    private Integer jurisdiction;


}
