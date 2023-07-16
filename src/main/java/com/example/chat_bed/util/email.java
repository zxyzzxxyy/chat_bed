package com.example.chat_bed.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;

@Component
public class email {

    @Autowired
    JavaMailSender javaMailSender;

    public void send(HashMap map){
        // 构建一个邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件主题
        message.setSubject(map.get("title").toString());
        // 设置邮件发送者，这个跟application.yml中设置的要一致
        message.setFrom("2483242070@qq.com");
        // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
        // message.setTo("10*****16@qq.com","12****32*qq.com");
        message.setTo((String)map.get("email"));
        // 设置邮件抄送人，可以有多个抄送人
        //message.setCc("12****32*qq.com");
        // 设置隐秘抄送人，可以有多个
        //message.setBcc("7******9@qq.com");
        // 设置邮件发送日期
        message.setSentDate(new Date());
        // 设置邮件的正文
        message.setText(map.get("msg").toString());
        // 发送邮件
       // javaMailSender.send(message);
    }
}
