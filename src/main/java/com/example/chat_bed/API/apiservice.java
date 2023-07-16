package com.example.chat_bed.API;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.example.chat_bed.image.image;
import com.example.chat_bed.image.imagedao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import static com.example.chat_bed.config.util.point.pointToLatlong;

@Service
@Component
public class apiservice {

    @Autowired
    private imagedao dao;

    @Value("${imagepath}")
    private String imagepath;

    /**
     * 上传图片
     * @param files
     * @param token
     * @return
     * @throws ImageProcessingException
     * @throws IOException
     */
    public HashMap File(MultipartFile files,String token ) throws IOException, ImageProcessingException {
        HashMap map = new HashMap();
        Integer userid = dao.getuserid(token);
        File file = new File(files.getOriginalFilename());
        OutputStream out = null;
        //
        image image = new image();
        String filenewname=
                userid.toString()+"_"+
                        ((int)(System.currentTimeMillis()/1000000))+
                        dao.getuserimagenum(userid).toString()+
                        "."+files.getOriginalFilename().substring(files.getOriginalFilename().indexOf(".")+1);
        image.setName(filenewname);
        image.setAuthor(userid);
        image.setUploadtime((int)(System.currentTimeMillis()/1000));
        //
        try{
            //获取文件流，以文件流的方式输出到新文件
            InputStream in = files.getInputStream();
            out = new FileOutputStream(file);
            byte[] ss = files.getBytes();
            for(int i = 0; i < ss.length; i++){
                out.write(ss[i]);
            }
        }catch(IOException e){
            e.printStackTrace();
            map.put("code",1000);
            map.put("msg","无法获取链接图片");
            return map;
        }
        if (files.getSize() > 0) {
            image.setSize(files.getSize()+"");
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    String tagName = tag.getTagName();  //标签名
                    String desc = tag.getDescription(); //标签信息
                    if (tagName.equals("Image Height")) {
                        System.out.println("图片高度: " + desc);
                        String str[]=desc.split(" ",2);
                        image.setHeight(Integer.parseInt(str[0]));
                    } else if (tagName.equals("Image Width")) {
                        System.out.println("图片宽度: " + desc);
                        String str[]=desc.split(" ",2);
                        image.setWidth(Integer.parseInt(str[0]));
                    } else if (tagName.equals("Date/Time Original")) {
                        System.out.println("拍摄时间: " + desc);
                    } else if (tagName.equals("GPS Latitude")) {
                        System.out.println("纬度 : " + desc);
                        System.out.println("纬度(度分秒格式) : "+pointToLatlong(desc));
                    } else if (tagName.equals("GPS Longitude")) {
                        System.out.println("经度: " + desc);
                        System.out.println("经度(度分秒格式): "+pointToLatlong(desc));
                    }
                }
            }
            InputStream inputStream = files.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(imagepath+filenewname);
            int bytesum = 0;
            int byteread;
            byte[] buffer = new byte[1024];
            while ((byteread = inputStream.read(buffer)) != -1) {
                bytesum += byteread;
                fileOutputStream.write(buffer, 0, byteread);
            }
            fileOutputStream.close();
            dao.userimagenumadd(userid);
            dao.newimage(image);
            map.put("url",""+filenewname);
            map.put("code",1001);
            map.put("msg","上传成功");
        }else{
            map.put("code",1000);
            map.put("msg","上传失败");
        }
        return map;
    }

    public HashMap File1(String url, String token) throws IOException, ImageProcessingException {
        Integer userid = dao.getuserid(token);
        HashMap returnmap = new HashMap();
        try{
            String name = url.split("/")[url.split("/").length-1];
            String filenewname=
                    userid.toString()+"_"+
                            ((int)(System.currentTimeMillis()/1000000))+
                            dao.getuserimagenum(userid).toString()+
                            "."+name;
            URL url1 = new URL(url);
            URLConnection conn = url1.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream inputStream = conn.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(
                    imagepath
                            //"/springboot_temp/"
                            +filenewname);
            int bytesum = 0;
            int byteread;
            byte[] buffer = new byte[1024];

            while ((byteread = inputStream.read(buffer)) != -1) {
                bytesum += byteread;
                fileOutputStream.write(buffer, 0, byteread);
            }
            image image = new image();
            image.setName(filenewname);
            image.setAuthor(userid);
            image.setUploadtime((int)(System.currentTimeMillis()/1000));

            File file = new File(imagepath+name);
            MultipartFile files = new MockMultipartFile(file.getName(), file.getName(),
                    null, new FileInputStream(file));

            if (files.getSize() > 0) {
                image.setSize(files.getSize()+"");
                Metadata metadata = ImageMetadataReader.readMetadata(file);
                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        String tagName = tag.getTagName();  //标签名
                        String desc = tag.getDescription(); //标签信息
                        if (tagName.equals("Image Height")) {
                            System.out.println("图片高度: " + desc);
                            String str[]=desc.split(" ",2);
                            image.setHeight(Integer.parseInt(str[0]));
                        } else if (tagName.equals("Image Width")) {
                            System.out.println("图片宽度: " + desc);
                            String str[]=desc.split(" ",2);
                            image.setWidth(Integer.parseInt(str[0]));
                        } else if (tagName.equals("Date/Time Original")) {
                            System.out.println("拍摄时间: " + desc);
                        } else if (tagName.equals("GPS Latitude")) {
                            System.out.println("纬度 : " + desc);
                            System.out.println("纬度(度分秒格式) : "+pointToLatlong(desc));
                        } else if (tagName.equals("GPS Longitude")) {
                            System.out.println("经度: " + desc);
                            System.out.println("经度(度分秒格式): "+pointToLatlong(desc));
                        }
                    }
                }
                dao.userimagenumadd(userid);
                dao.newimage(image);
                returnmap.put("url", "https://www.txtz.club:8807/image/"+filenewname);
                returnmap.put("code",1001);
                returnmap.put("msg","上传成功");
            }

        }catch(IOException e){
            e.printStackTrace();
            returnmap.put("code",1000);
            returnmap.put("msg","上传失败");
        }finally {
            return returnmap;
        }
    }
}
