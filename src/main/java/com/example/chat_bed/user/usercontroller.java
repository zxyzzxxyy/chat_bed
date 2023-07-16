package com.example.chat_bed.user;


import com.example.chat_bed.util.Jwt;
import com.example.chat_bed.util.requestinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class usercontroller {
    @Autowired
    private userservice service;
    @Autowired
    private Jwt jwt;

    /**
     *用户登录
     * @param map
     * @return
     */
    @PostMapping("/login")
    public HashMap login(@RequestBody HashMap map, HttpServletRequest request){
        System.out.println(map);
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.login(map,ip);
    }

    /**
     *用户注册
     * @param map
     * @return
     */
    @PostMapping("/register")
    public HashMap register(@RequestBody HashMap map,HttpServletRequest request){
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.register(map, ip);
    }

    /**
     *验证手机验证码
     * @param map
     * @return
     */
    @PostMapping("/verify_phone")
    public HashMap verify_phone(@RequestBody HashMap map,HttpServletRequest request){
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.verify_phone(map,ip);
    }

    /**
     * 获取用户token
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/gettoken")
    public HashMap gettoken(@RequestBody HashMap map,HttpServletRequest request){
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.gettoken(map,ip);
    }

    /**
     * 设置图片请求头
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/setreferer")
    public HashMap setreferer(@RequestBody HashMap map,HttpServletRequest request){
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        map.put("userid",(jwt.getClaimByName(request.getHeader("cookie").toString(),"userid")));
        return service.setreferer(map,ip);

    }

    /**
     *  设置图片签名秘钥
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/setsign")
    public HashMap setsign(@RequestBody HashMap map,HttpServletRequest request){
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        map.put("userid",(jwt.getClaimByName(request.getHeader("cookie").toString(),"userid")));
        return service.setsign(map,ip);

    }

    /**
     * 设置图片签名时间戳延迟范围
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/setdelay")
    public HashMap setdelay(@RequestBody HashMap map,HttpServletRequest request){
        map.put("userid",(jwt.getClaimByName(request.getHeader("cookie").toString(),"userid")));
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.setdelay(map,ip);

    }

    /**
     * 没用
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/test")
    public HashMap test(@RequestBody HashMap map,HttpServletRequest request){
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.test(map,ip);
    }

    /**
     * 修改密码
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/changepassword")
    public HashMap changepassword(@RequestBody HashMap map,HttpServletRequest request){
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.changepassword(map,ip);
    }

    /**
     * 验证手机短信验证码修改密码
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/verify_phone_changepassword")
    public HashMap verify_phone_changepassword(@RequestBody HashMap map,HttpServletRequest request){
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.verify_phone_changepassword(map,ip);
    }

    /**
     * 绑定邮箱
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/bindingemail")
    public HashMap bindingemail(@RequestBody HashMap map,HttpServletRequest request){
        map.put("userid",(jwt.getClaimByName(request.getHeader("cookie").toString(),"userid")));
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.bindingemail(map,ip);
    }

    /**
     * 验证邮箱
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/verify_email")
    public HashMap verify_email(@RequestBody HashMap map,HttpServletRequest request){
        map.put("userid",(jwt.getClaimByName(request.getHeader("cookie").toString(),"userid")));
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.verify_email(map,ip);
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
    @PostMapping("/login_phone")
    public HashMap login_phone(@RequestBody HashMap map,HttpServletRequest request){
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.login_phone(map,ip);
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
    @PostMapping("/verify_phone_login")
    public HashMap verify_phone_login(@RequestBody HashMap map,HttpServletRequest request){
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.verify_phone_login(map,ip);
    }
    /**
     * 获取个人信息
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/getmyself")
    public HashMap getmyself(@RequestBody HashMap map,HttpServletRequest request){
        map.put("userid",(jwt.getClaimByName(request.getHeader("cookie").toString(),"userid")));
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.getmyself(map,ip);
    }
    /**
     * 设置头像
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/setheadimage")
    public HashMap setheadimage(@RequestBody HashMap map,HttpServletRequest request){
        map.put("userid",(jwt.getClaimByName(request.getHeader("cookie").toString(),"userid")));
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.setheadimage(map,ip);
    }

    /**
     * 设置空间是否可见
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/setspace")
    public HashMap setspace(@RequestBody HashMap map,HttpServletRequest request){
        map.put("userid",(jwt.getClaimByName(request.getHeader("cookie").toString(),"userid")));
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.setspace(map,ip);
    }

    /**
     * 获取他人信息
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/getheself")
    public HashMap getheself(@RequestBody HashMap map,HttpServletRequest request){
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.getheself(map,ip);
    }
}
