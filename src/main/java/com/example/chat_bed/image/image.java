package com.example.chat_bed.image;
import lombok.Data;

@Data
public class image {
    //    图片ID
    private Integer userid;
    //    图片名称
    private String name;
    //    图片尺寸
    private String size;
    //    图片宽度
    private Integer width;
    //    图片高度
    private Integer height;
    //    访问总数
    private Integer looknum;
    //    上传时间
    private Integer uploadtime;
    //    作者ID
    private Integer author;
    //    原图片链接
    private String url;
    //    喜欢次数
    private Integer likenum;
    //    收藏次数
    private Integer savenum;


}