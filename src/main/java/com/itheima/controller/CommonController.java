package com.itheima.controller;

import com.itheima.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //UUID随机生成名字 防止重名
        String fileName = UUID.randomUUID() + suffix;
        File dir = new File(basePath);

        if(!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            int len = 0;
            byte[] data = new byte[1024];

            response.setContentType("image/jpeg");
            FileInputStream inputStream = new FileInputStream(new File(basePath + name));
            ServletOutputStream outputStream = response.getOutputStream();
            while((len = inputStream.read(data)) != -1) {
                outputStream.write(data,0,len);
                outputStream.flush();
            }

            inputStream.close();
            outputStream.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
