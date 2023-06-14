package com.xian.oss.controller;

import com.xian.entities.R;
import com.xian.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/eduoss/fileoss")
public class OssController {
    @Autowired
    private OssService ossService;
    //上传头像的方法
    @PostMapping("/avatar")
    public R uploadOssAvatar(MultipartFile file) {
        //获取上传的文件 MultipartFile
        //返回上传到oss的路径
        String url = ossService.uploadAvatarFile(file);
        return R.ok().data("url",url);
    }
    @PostMapping("/cover")
    public R uploadOssCover(MultipartFile file) {
        //获取上传的文件 MultipartFile
        //返回上传到oss的路径
        String url = ossService.uploadCoverFile(file);
        return R.ok().data("url",url);
    }
    @PostMapping("/banner")
    public R uploadOssBanner(MultipartFile file) {
        //获取上传的文件 MultipartFile
        //返回上传到oss的路径
        String url = ossService.uploadBannerFile(file);
        return R.ok().data("url",url);
    }

}
