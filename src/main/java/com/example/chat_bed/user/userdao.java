package com.example.chat_bed.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Mapper
public interface userdao {
     Integer jugeregister(HashMap map);
     void register(HashMap map);
     Integer jugetoken(String token);
     HashMap login(HashMap map);
     String gettoken(HashMap map);
     void setreferer(HashMap map);
     void setsign(HashMap map);
     void setdelay(HashMap map);
     void changepassword(HashMap map);
     String getkey(Integer userid);
     Integer jugeemail(HashMap map);
     void bindingemail(HashMap map);
     String jugeemailbinding(HashMap map);
     HashMap login_code(HashMap map);
     HashMap getuserinfo(HashMap map);
     void setheadimage(HashMap map);
     void setspace(HashMap map);
     HashMap getheself(HashMap map);
}
