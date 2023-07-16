package com.example.chat_bed.API;

import com.drew.imaging.ImageProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class apicontroller {

    @Autowired
    private apiservice service;

    /**
     * 上传图片
     * @param file
     * @param token
     * @return
     * @throws ImageProcessingException
     * @throws IOException
     */
    @PostMapping("/upload")
    public HashMap uploadFile(@RequestParam("file") MultipartFile file , @RequestHeader("token") String token) throws ImageProcessingException, IOException {
        return service.File(file,token);
    }

    /**
     * 上传图片
     * @param token
     * @param map
     * @return
     * @throws ImageProcessingException
     * @throws IOException
     */
    @PostMapping("/upload_url")
    public HashMap uploadFile_url(@RequestBody HashMap map,  @RequestHeader("token") String token) throws ImageProcessingException, IOException {
        return service.File1(map.get("url").toString(),token);
    }
}
