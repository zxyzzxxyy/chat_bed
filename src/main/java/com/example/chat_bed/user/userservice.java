package com.example.chat_bed.user;

import com.example.chat_bed.Annotation.ExplanAnnotation;
import com.example.chat_bed.util.Jwt;
import com.example.chat_bed.util.email;
import com.example.chat_bed.util.getrandom;
import com.example.chat_bed.util.phonecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Service
public class userservice {

    @Autowired
    private userdao dao;
    @Autowired
    private phonecode phonecode;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private getrandom r;
    @Autowired
    private Jwt jwt;
    @Autowired
    private email email;

    /**
     * 设置签名延迟
     * @param map
     * @param request
     * @return
     */
    @ExplanAnnotation(name="setdelay",explain = "设置签名延迟")
    public HashMap setdelay(HashMap map,Map request){
        HashMap map1 = new HashMap();
        try {
            dao.setdelay(map);
            redisTemplate.delete("user:setting:"+map.get("userid").toString());
            map1.put("code",1001);
            map1.put("msg","设置成功");
        }catch (Exception e){
            map1.put("code",1000);
            map1.put("msg","设置失败");
        }finally {
            return map1;
        }
    }

    /**
     * 设置请求头
     * @param map
     * @param request
     * @return
     */
    @ExplanAnnotation(name="setreferer",explain = "设置请求头")
    public HashMap setreferer(HashMap map,Map request){
        HashMap map1 = new HashMap();
        System.out.println(map);
        try {
            dao.setreferer(map);
            redisTemplate.delete("user:setting:"+map.get("userid").toString());
            map1.put("code",1001);
            map1.put("msg","设置成功");
        }catch (Exception e){
            System.out.println(e);
            map1.put("code",1000);
            map1.put("msg","设置失败");
        }finally {
            return map1;
        }

    }

    /**
     * 设置请求签名
     * @param map
     * @param request
     * @return
     */
    @ExplanAnnotation(name="setsign",explain = "设置请求签名")
    public HashMap setsign(HashMap map,Map request){
        HashMap map1 = new HashMap();
        System.out.println(map);
        //map.put("userid",Integer.valueOf(request.get("userid").toString()));

        try {
            dao.setsign(map);
            redisTemplate.delete("user:setting:"+map.get("userid").toString());
            map1.put("code",1001);
            map1.put("msg","设置成功");
        }catch (Exception e){
            System.out.println(e);
            map1.put("code",1000);
            map1.put("msg","设置失败");
        }finally {
            return map1;
        }

    }

    /**
     * 获取用户token
     * @param map
     * @param request
     * @return
     */
    @ExplanAnnotation(name="gettoken",explain = "获取用户token")
    public HashMap gettoken(HashMap map, Map request){
        HashMap map1 = new HashMap();
        try {
            String token = dao.gettoken(map);
            if(token==null){
                map1.put("code",1002);
                map1.put("msg","用户名或密码错误");
            }
            else{
                map1.put("code",1001);
                map1.put("msg","获取成功");
                map1.put("APItoken",token);
            }
        }catch(Exception e){
            map1.put("code",1000);
            map1.put("msg","系统错误");
        }
        finally {
            return map1;
        }
    }
    /**
     * 用户登录
     * @param map{
     *     username : 用户名 或 手机号
     *     password : 密码
     * }
     * @return jwt token
     */
    @ExplanAnnotation(name="login",explain = "用户登录")
    public HashMap login(HashMap map,Map request){
        HashMap map1 = new HashMap();
        try {
            HashMap userinfo = dao.login(map);
            System.out.println("userinfo");
            System.out.println(userinfo);
            if (userinfo == null) {
                map1.put("code", 1002);
                map1.put("msg", "用户名或密码错误");
            } else {
                String token = jwt.createToken(userinfo.get("userid").toString(),userinfo.get("key").toString());

                map1.put("token", token);
                map1.put("code", 1001);
                map1.put("msg", "登陆成功");
            }
        }catch(Exception e){
            System.out.println(e);
            map1.put("code",1000);
            map1.put("msg","系统错误");
        }
        finally {
            return map1;
        }
    }
    /**
     * 验证短信验证码
     * @param map{
     *     phone : 电话号码
     *     code : 验证码
     *     password : 密码
     * }
     * @return {
     *     code : 状态码
     *     msg : 返回消息
     * }
     */
    @ExplanAnnotation(name="verify_phone",explain = "验证手机短信验证码完成注册")
    public HashMap verify_phone(HashMap map, Map request){
        HashMap map1 = new HashMap();
        map1.put("code",1000);
        map1.put("msg","系统错误");
        try {
            if (redisTemplate.opsForValue().get((String) map.get("phone")) != null &&
                    ((String)((HashMap)( redisTemplate.opsForValue().get((String)map.get("phone").toString()))).get("code").toString()).equals((String)(map.get("code")).toString()))
            {
                if(map.get("password").toString().length()<7){
                    map1.put("code",1003);
                    map1.put("msg","密码小于七位");
                }else {
                    String key = r.random("string", 26);
                    map.put("key", key);
                    map1.put("code", 1001);
                    map1.put("msg", "注册成功");
                    dao.register(map);
                    redisTemplate.delete((String) map.get("phone"));
                }
            }
            else{
                map1.put("code",1002);
                map1.put("msg","验证码错误");
            }
        }catch (Exception e) {
            //System.out.println(e);
        }finally {
            return map1;
        }

    }
    /**
     * 手机号注册
     * @param map{
     *     phone : 电话号码
     * }
     * @return {
     *     code : 状态码
     *     msg : 返回消息
     * }
     */
    @ExplanAnnotation(name="register",explain = "用户注册")
    public HashMap register(HashMap map,Map request){
        HashMap map1 = new HashMap();
        try {
            if(dao.jugeregister(map)==0){
                Integer lasttime= ( redisTemplate.opsForValue().get((String)map.get("phone")))!=null ? (Integer)((HashMap)( redisTemplate.opsForValue().get((String)map.get("phone")))).get("time") :0 ;
                if(lasttime<((int)(System.currentTimeMillis()/1000)-60)) {
                    //生成验证码
                    Integer code = r.random("int",6);
                    //将 手机号:{验证码,发送时间} 存储到reids 设置过期时间10分钟
                    HashMap temp = new HashMap();
                    temp.put("code", code);
                    temp.put("time", ((int)(System.currentTimeMillis()/1000)));
                    redisTemplate.opsForValue().set((String) map.get("phone"), temp, 10, TimeUnit.MINUTES);
                    //调用腾讯云短信API 发送验证码
                    phonecode.phonecode(code, map.get("phone").toString());
                    map1.put("code", 1001);
                    map1.put("msg", "验证码已发送");
                }
                else{
                    map1.put("code", 1003);
                    map1.put("msg", "发送频繁");
                }
            }
            else{
                map1.put("code",1002);
                map1.put("msg","手机号已注册");
            }
        }catch (Exception e){
            //System.out.println(e);
            map1.put("code",1000111111);
            map1.put("msg","系统错误");
        }finally {
            return map1;
        }
    }
    @ExplanAnnotation(name="test",explain = "测试")
    public HashMap test(HashMap map,Map map1){
        map.put("msg","访问成功");
        return map;
    }

    /**
     * 修改密码
     * @param map{
     *     phone : 电话号码
     * }
     * @return {
     *     code : 状态码
     *     msg : 返回消息
     * }
     */
    @ExplanAnnotation(name="changepassword",explain = "修改密码")
    public HashMap changepassword(HashMap map,Map map11){
        HashMap map1 = new HashMap();
        try {
            if(dao.jugeregister(map)==1){
                Integer lasttime= ( redisTemplate.opsForValue().get("changepassword:"+(String)map.get("phone")))!=null ? (Integer)((HashMap)( redisTemplate.opsForValue().get("changepassword:"+(String)map.get("phone")))).get("time") :0 ;
                if(lasttime<((int)(System.currentTimeMillis()/1000)-60)) {
                    //生成验证码
                    Integer code = r.random("int",6);
                    //将 手机号:{验证码,发送时间} 存储到reids 设置过期时间10分钟
                    HashMap temp = new HashMap();
                    temp.put("code", code);
                    temp.put("time", ((int)(System.currentTimeMillis()/1000)));
                    redisTemplate.opsForValue().set("changepassword:"+(String) map.get("phone"), temp, 10, TimeUnit.MINUTES);
                    //调用腾讯云短信API 发送验证码
                    phonecode.phonecode(code, map.get("phone").toString());
                    System.out.println(temp);
                    map1.put("code", 1001);
                    map1.put("msg", "验证码已发送");
                }
                else{
                    map1.put("code", 1003);
                    map1.put("msg", "发送频繁");
                }
            }
            else{
                map1.put("code",1002);
                map1.put("msg","手机号未注册");
            }
        }catch (Exception e){
            //System.out.println(e);
            map1.put("code",1000111111);
            map1.put("msg","系统错误");
        }finally {
            return map1;
        }
    }

    /**
     * 验证短信验证码修改密码
     * @param map{
     *     phone : 电话号码
     *     code : 验证码
     *     password : 密码
     * }
     * @return {
     *     code : 状态码
     *     msg : 返回消息
     * }
     */
    @ExplanAnnotation(name="verify_phone_changepassword",explain = "验证手机短信验证码修改密码")
    public HashMap verify_phone_changepassword(HashMap map, Map request){
        HashMap map1 = new HashMap();
        map1.put("code",1000);
        map1.put("msg","系统错误");
        try {
            if (redisTemplate.opsForValue().get("changepassword:"+(String) map.get("phone")) != null &&
                    ((String)((HashMap)( redisTemplate.opsForValue().get("changepassword:"+(String)map.get("phone").toString()))).get("code").toString()).equals((String)(map.get("code")).toString()))
            {
                if(map.get("password").toString().length()<7){
                    map1.put("code",1003);
                    map1.put("msg","密码小于七位");
                }else {
                    map1.put("code", 1001);
                    map1.put("msg", "修改成功");
                    String key = r.random("string", 26);
                    map.put("key",key);
                    System.out.println(map);
                    dao.changepassword(map);
                    redisTemplate.delete("changepassword:"+(String) map.get("phone"));
                }
            }
            else{
                map1.put("code",1002);
                map1.put("msg","验证码错误");
            }
        }catch (Exception e) {
            //System.out.println(e);
        }finally {
            return map1;
        }

    }
    /**
     * 发送短信验证码登录
     * @param map{
     *     phone : 电话号码
     * }
     * @return {
     *     code : 状态码
     *     msg : 返回消息
     * }
     */
    @ExplanAnnotation(name="login_phone",explain = "发送手机短信验证码登录")
    public HashMap login_phone(HashMap map, Map request){
        HashMap map1 = new HashMap();
        try {
            if(dao.jugeregister(map)==1){
                Integer lasttime= ( redisTemplate.opsForValue().get("login"+(String)map.get("phone")))!=null ? (Integer)((HashMap)( redisTemplate.opsForValue().get("login"+(String)map.get("phone")))).get("time") :0 ;
                if(lasttime<((int)(System.currentTimeMillis()/1000)-60)) {
                    //生成验证码
                    Integer code = r.random("int",6);
                    //将 手机号:{验证码,发送时间} 存储到reids 设置过期时间10分钟
                    HashMap temp = new HashMap();
                    temp.put("code", code);
                    temp.put("time", ((int)(System.currentTimeMillis()/1000)));
                    redisTemplate.opsForValue().set("login"+(String)map.get("phone"), temp, 10, TimeUnit.MINUTES);
                    //调用腾讯云短信API 发送验证码
                    phonecode.phonecode(code, map.get("phone").toString());
                    map1.put("code", 1001);
                    map1.put("msg", "验证码已发送");
                }
                else{
                    map1.put("code", 1003);
                    map1.put("msg", "发送频繁");
                }
            }
            else{
                map1.put("code",1002);
                map1.put("msg","手机号未注册");
            }
        }catch (Exception e){
            System.out.println(e);
            map1.put("code",1000111111);
            map1.put("msg","系统错误");
        }finally {
            return map1;
        }

    }
    /**
     * 验证短信验证码登录
     * @param map{
     *     phone : 电话号码
     *     code : 验证码
     * }
     * @return {
     *     code : 状态码
     *     msg : 返回消息
     * }
     */
    @ExplanAnnotation(name="verify_phone_login",explain = "验证短信验证码登录")
    public HashMap verify_phone_login(HashMap map, Map request){
        HashMap map1 = new HashMap();
        map1.put("code",1000);
        map1.put("msg","系统错误");
        try {
            if (redisTemplate.opsForValue().get("login"+(String) map.get("phone")) != null &&
                    ((String)((HashMap)( redisTemplate.opsForValue().get("login"+(String)map.get("phone").toString()))).get("code").toString()).equals((String)(map.get("code")).toString()))
            {
                HashMap userinfo = dao.login_code(map);
                String token = jwt.createToken(userinfo.get("userid").toString(),userinfo.get("key").toString());
                map1.put("token", token);
                map1.put("code", 1001);
                map1.put("msg", "登陆成功");
            }
            else{
                map1.put("code",1002);
                map1.put("msg","验证码错误");
            }
        }catch (Exception e) {
            //System.out.println(e);
        }finally {
            return map1;
        }

    }

    /**
     * 绑定邮箱
     * @param map{
     *     phone : 电话号码
     * }
     * @return {
     *     code : 状态码
     *     msg : 返回消息
     * }
     */
    @ExplanAnnotation(name="bindingemail",explain = "绑定邮箱")
    public HashMap bindingemail(HashMap map,Map map11){
        HashMap map1 = new HashMap();
        try {
            if(dao.jugeemail(map)==0&&dao.jugeemailbinding(map)==null){
                Integer lasttime= ( redisTemplate.opsForValue().get("email:"+(String)map.get("email")))!=null ? (Integer)((HashMap)( redisTemplate.opsForValue().get("email:"+(String)map.get("email")))).get("time") :0 ;

                if(lasttime<((int)(System.currentTimeMillis()/1000)-60)) {
                    //生成验证码
                    String code = r.random("string",6);
                    //将 邮箱:{验证码,发送时间} 存储到reids 设置过期时间10分钟
                    HashMap temp = new HashMap();
                    temp.put("code", code);
                    map.put("msg","您的验证码是: "+ code);
                    map.put("title","邮箱绑定");
                    temp.put("time", ((int)(System.currentTimeMillis()/1000)));
                    redisTemplate.opsForValue().set("email:"+(String) map.get("email"), temp, 10, TimeUnit.MINUTES);
                    email.send(map);
                    System.out.println(temp);
                    map1.put("code", 1001);
                    map1.put("msg", "验证码已发送");
                }
                else{
                    map1.put("code", 1003);
                    map1.put("msg", "发送频繁");
                }
            }
            else{
                map1.put("code",1002);
                map1.put("msg","邮箱已被绑定");
            }
        }catch (Exception e){
            System.out.println(e);
            map1.put("code",1000111111);
            map1.put("msg","系统错误");
        }finally {
            return map1;
        }
    }

    /**
     * 验证邮箱
     * @param map{
     *     email : 邮箱
     *     code : 验证码
     * }
     * @return {
     *     code : 状态码
     *     msg : 返回消息
     * }
     */
    @ExplanAnnotation(name="verify_email",explain = "验证邮箱")
    public HashMap verify_email(HashMap map, Map request){
        HashMap map1 = new HashMap();
        map1.put("code",1000);
        map1.put("msg","系统错误");
        try {
            if (redisTemplate.opsForValue().get("email:"+(String) map.get("email")) != null &&
                    ((String)((HashMap)( redisTemplate.opsForValue().get("email:"+(String)map.get("email").toString()))).get("code").toString()).equals((String)(map.get("code")).toString()))
            {
                {
                    map1.put("code", 1001);
                    map1.put("msg", "绑定成功");
                    dao.bindingemail(map);
                    redisTemplate.delete("changepassword:"+(String) map.get("email"));
                }
            }
            else{
                map1.put("code",1002);
                map1.put("msg","验证码错误");
            }
        }catch (Exception e) {
            //System.out.println(e);
        }finally {
            return map1;
        }

    }
    /**
     * 获取个人信息
     * @param map{
     *     cookie : JWT
     * }
     * @return {
     *     code : 状态码
     *     data : 用户信息
     * }
     */
    @ExplanAnnotation(name="getmyself",explain = "获取个人信息")
    public HashMap getmyself(HashMap map, Map request){
        HashMap map1 = new HashMap();
        try {
                HashMap userinfo = dao.getuserinfo(map);
                userinfo.put("phone",userinfo.get("phone").toString().substring(0,3)+"****"+userinfo.get("phone").toString().substring(7,11));
                map1.put("data",userinfo);
                map1.put("code", 1001);
                map1.put("msg","success");
        }catch (Exception e) {
            map1.put("code",1000);
            map1.put("msg","系统错误");
            //System.out.println(e);
        }finally {
            return map1;
        }

    }
    /**
     * 设置头像
     * @param map{
     *     cookie : JWT
     * }
     * @return {
     *     code : 状态码
     *     data : 用户信息
     * }
     */
    @ExplanAnnotation(name="setheadimage",explain = "设置头像")
    public HashMap setheadimage(HashMap map, Map request){
        HashMap map1 = new HashMap();
        try {
            dao.setheadimage(map);
            map1.put("code", 1001);
            map1.put("msg","success");
        }catch (Exception e) {
            map1.put("code",1000);
            map1.put("msg","系统错误");
            //System.out.println(e);
        }finally {
            return map1;
        }

    }
    /**
     * 设置空间是否可见
     * @param map{
     *     phone : 电话号码
     *     code : 验证码
     *     password : 密码
     * }
     * @return {
     *     code : 状态码
     *     msg : 返回消息
     * }
     */
    @ExplanAnnotation(name="setspace",explain = "设置空间是否可见")
    public HashMap setspace(HashMap map,Map ip){
        HashMap return_map = new HashMap();
        try {
            dao.setspace(map);
            return_map.put("code", 1001);
            return_map.put("msg","success");
        }catch (Exception e) {
            return_map.put("code",1000);
            return_map.put("msg","系统错误");
            //System.out.println(e);
        }finally {
            return return_map;
        }
    }

    /**
     * 获取他人信息
     * @param map{
     *     cookie : JWT
     * }
     * @return {
     *     code : 状态码
     *     data : 用户信息
     * }
     */
    @ExplanAnnotation(name="getheself",explain = "获取他人信息")
    public HashMap getheself(HashMap map, Map request){
        HashMap map1 = new HashMap();
        try {
            HashMap userinfo = dao.getheself(map);
            System.out.println(userinfo.get("space")!=null);
            if(userinfo.get("space")!=null&&Integer.valueOf(userinfo.get("space").toString())==0) {
                map1.put("data", userinfo);
                map1.put("code", 1001);
                map1.put("msg", "success");
            }else {
                map1.put("code", 1003);
                map1.put("msg", "success");
            }
        }catch (Exception e) {
            map1.put("code",1000);
            map1.put("msg","系统错误");
            //System.out.println(e);
        }finally {
            return map1;
        }

    }
}
