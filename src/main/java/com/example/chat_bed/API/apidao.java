package com.example.chat_bed.API;


import com.example.chat_bed.image.image;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Component
@Mapper
public interface apidao {
    Integer getuserimagenum(Integer userid);
    void userimagenumadd(Integer userid);
    void newimage(image image);

}
