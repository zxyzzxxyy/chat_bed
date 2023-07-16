package com.example.chat_bed.AMQP;

import com.alibaba.fastjson.JSONObject;
import com.example.chat_bed.user.userdao;
import com.example.chat_bed.util.email;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class recv {
    @Autowired
    private userdao dao;
    @Autowired
    private email email;

    @RabbitListener(queues = "simple.queue")
    public void test(String msg){
        System.out.println("接收到消息: "+msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "msg.system1"),
            exchange = @Exchange(name="login.exchange",type = ExchangeTypes.DIRECT),
            key = {"warn","success"}
    ))
    public void log_login_msg(String msg) throws ClassNotFoundException {
        HashMap map = new HashMap();
        map = JSONObject.parseObject(msg,HashMap.class);
        System.out.println("********************\n");
        String newlogin ="发现新设备尝试登录"+
                "\n登录IP: "+ map.get("ip")+
                "\n登录地址: "+ map.get("address")+
                "\n登录时间: "+ map.get("visit_time");
        System.out.println(newlogin);
        System.out.println("已记录于数据库");
        System.out.println("********************\n");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "msg.system2"),
            exchange = @Exchange(name="login.exchange",type = ExchangeTypes.DIRECT),
            key = {"warn"}
    ))
    public void warn_login_msg(String msg) throws ClassNotFoundException {
        HashMap map = new HashMap();
        map = JSONObject.parseObject(msg,HashMap.class);
        System.out.println("********************\n");
        System.out.println("warn_login_msg接收到消息: ");
        System.out.println("登陆失败");
        System.out.println("操作结束");
        System.out.println("********************\n");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "msg.system3"),
            exchange = @Exchange(name="login.exchange",type = ExchangeTypes.DIRECT),
            key = {"success"}
    ))
    public void success_login_msg(String msg) throws ClassNotFoundException {
        HashMap map = new HashMap();
        map = JSONObject.parseObject(msg,HashMap.class);
        System.out.println("********************\n");
        System.out.println("success_login_msg接收到消息: ");
        System.out.println("正常登录 邮件通知");
        System.out.println("操作结束");
        System.out.println("********************\n");
        String newlogin ="发现新设备尝试登录"+
                "\n登录IP: "+ map.get("ip")+
                "\n登录地址: "+ map.get("address")+
                "\n登录时间: "+ map.get("visit_time");
        String emailaddress = dao.jugeemailbinding(map);
        if(email!=null){
            map.put("email",emailaddress);
            map.put("msg",newlogin);
            map.put("title","登录通知");
           // email.send(map);
        }
        System.out.println(email);
    }
}
