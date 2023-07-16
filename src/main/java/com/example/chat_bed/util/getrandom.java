package com.example.chat_bed.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class getrandom {

    /**
     * 返回一个随机数
     * @param type 随机数的类型  String string Integer int
     * @param size 随机数的长度
     * @param <T> String Integer
     * @return
     */
    public <T> T random(String type,Integer size){
        if(type.equals("String")||type.equals("string")){
            return (T)get_String(size);
        }
        else if(type.equals("Integer")||type.equals("int")){
            return (T)get_Integer(size);
        }
        else{
            return (T)null;
        }
    }

    /**
     * Integer类型随机数
     * @param size 长度
     * @return
     */
    public Integer get_Integer(Integer size){
        int i=1;
        while(size>1){
            i=i*10;
            size--;
        }
        return (int) ((Math.random() * 9 + 1) * i);
    }

    /**
     * String 类型随机数
     * @param size 长度
     * @return
     */
    public String get_String(Integer size){
        StringBuffer temp = new StringBuffer();
        Random random = new Random();
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for(int j=0;j<size;j++) {
            int sub = random.nextInt(61);
            temp.append(str.substring(sub, sub + 1));
        }
        return temp.toString();
    }
}
