package com.example.chat_bed.image;

import com.auth0.jwt.JWT;
import com.drew.imaging.ImageProcessingException;
import com.example.chat_bed.util.Jwt;
import com.example.chat_bed.util.requestinfo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin
public class imagecontroller {

    @Autowired
    private imageservice service;
    @Autowired
    private Jwt jwt;
    @Autowired
    private imagedao dao;

    /**
     * 上传图片
     * @param file
     * @param
     * @return
     * @throws ImageProcessingException
     * @throws IOException
     */
    @PostMapping("/upload")
    public HashMap uploadFile(@RequestParam("file") MultipartFile file ,@RequestHeader("cookie") String cookie) throws ImageProcessingException, IOException {
        if(jwt.getClaimByName(cookie,"userid").equals("error")){
            HashMap map1 = new HashMap();
            map1.put("code",1002);
            map1.put("msg","没有权限");
            return map1;
        }
        Integer userid =Integer.valueOf(jwt.getClaimByName(cookie,"userid"));
        if(userid==null)return null;
        return service.File(file,userid);
    }

    /**
     * 上传图片
     * @param
     * @param map
     * @return
     * @throws ImageProcessingException
     * @throws IOException
     */
    @PostMapping("/upload_url")
    public HashMap uploadFile_url(@RequestBody HashMap map,@RequestHeader("cookie") String cookie) throws ImageProcessingException, IOException {
        Integer userid =Integer.valueOf(jwt.getClaimByName(cookie,"userid"));
        if(userid==null) {
            HashMap map1= new HashMap();
            map1.put("code",1002);
            map1.put("msg","没有权限");
            return map1;
        }
        return service.File1(map.get("url").toString(),userid);
    }
    /**
     * 获取图片
     * @param imagename
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/image/{imagename}"  )
    public void getImage(@PathVariable("imagename") String imagename, HttpServletRequest request, HttpServletResponse response) throws Exception {
    //public String getImage(@PathVariable("imagename") String imagename, HttpServletRequest request) throws Exception {
        System.out.println(imagename);
        Map<String, Object> map = new HashMap<>();
        map.put("imagename",imagename);
        map.put("ip",request.getHeader("ip"));
        //System.out.println(map);
        response.setHeader("Cache-Control","max-age=2592000");
        if(imagename.indexOf("?")!=-1){
            imagename=imagename.split("\\?")[0];
        }
        String size="";
        HashMap map1 = new HashMap();
        map1.put("imagename",imagename);
        size=(dao.getimagemsg(map1)).get("size").toString();
        response.setHeader("Content-Length",size);
        if(imagename.split("\\.",2)[1].equals("jpg"))
        response.setContentType("image/jpeg");
        else{
            response.setContentType("image/png");
        }
        response.flushBuffer();

        ServletOutputStream out = response.getOutputStream();
        InputStream in  =service.getimage(imagename, map);
        int len=0;
        byte[] buffer = new byte[1024];
        while((len = in.read(buffer))>0){
            out.write(buffer,0,len);
        }
        in.close();
        out.close();
        //return "yes";
    }

    /**
     * 修改图片请求头
     * @param map
     * @param token
     * @return
     */
    @PostMapping("/user/updateReferer")
    public HashMap updateReferer(@RequestBody HashMap map,@RequestHeader("userid") String token) {
        return service.updateReferer(map,Integer.parseInt(token));
    }

    /**
     * 获取图片总数
     * @param map
     * @param token
     * @return
     */
    @PostMapping("/getimagenum")
    public HashMap getimagenum(@RequestBody HashMap map) {
        return service.getimagenum();
    }
    /**
     * 获取图片总数
     * @param map
     * @param token
     * @return
     */
    @PostMapping("/getimagemsg")
    public HashMap getimagemsg(@RequestBody HashMap map,HttpServletRequest request){
        map.put("userid",(jwt.getClaimByName(request.getHeader("cookie").toString(),"userid")));
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.getimagemsg(map,ip);
    }
    /**
     * 获取我上传的图片(一次25张)
     * @param map
     * @param token
     * @return
     */
    @PostMapping("/getmyimagelist")
    public HashMap getmyimagelist(@RequestBody HashMap map,HttpServletRequest request){
        map.put("userid",(jwt.getClaimByName(request.getHeader("cookie").toString(),"userid")));
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.getmyimagelist(map,ip);
    }

    /**
     * 获取他人上传的图片(一次30张)
     * @param map
     * @param token
     * @return
     */
    @PostMapping("/getheimagelist")
    public HashMap getheimagelist(@RequestBody HashMap map,HttpServletRequest request){
        HashMap ip = new HashMap();ip.put("ip",map.get("ip"));
        return service.getheimagelist(map,ip);
    }
}
