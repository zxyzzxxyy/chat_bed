package com.example.chat_bed.image;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Mapper
@Component
public interface imagedao {
    Integer getuserimagenum(Integer userid);
    void userimagenumadd(Integer userid);
    Integer getuserid(String token);
    void newimage(image image);
    String getreferer(String imagename);
    void updateRererer(HashMap map);
    HashMap getusersetting(Integer userid);
    Integer getimagenum();
    HashMap getimagemsg(HashMap map);
    List getmyimagelist(HashMap map);
    List getheimagelist(HashMap map);
    Integer jugespace(HashMap map);
}
