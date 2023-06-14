package com.xian.oss.service.Impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.xian.oss.service.OssService;
import com.xian.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadAvatarFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        // 1.在文件名称里面添加随机的唯一值
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        // 2.创建新的文件夹(按照日期存储)
        String dirName = new DateTime().toString("yyyy/MM/dd/");
        String avatarPath = "course/avatar/";
        return uploadFile(file, fileName, uuid, dirName, avatarPath);
    }

    @Override
    public String uploadCoverFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        // 1.在文件名称里面添加随机的唯一值
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        // 2.创建新的文件夹(按照日期存储)
        String dirName = new DateTime().toString("yyyy/MM/dd/");
        String avatarPath = "course/cover/";
        return uploadFile(file, fileName, uuid, dirName, avatarPath);
    }

    @Override
    public String uploadBannerFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        // 1.在文件名称里面添加随机的唯一值
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        // 2.创建新的文件夹(按照日期存储)
        String dirName = new DateTime().toString("yyyy/MM/dd/");
        String avatarPath = "course/banner/";
        return uploadFile(file, fileName, uuid, dirName, avatarPath);
    }

    private String uploadFile(MultipartFile file, String fileName, String uuid, String dirName, String avatarPath) {
        OSS ossClient = new OSSClientBuilder().build(ConstantPropertiesUtils.END_POINT, ConstantPropertiesUtils.KEY_ID, ConstantPropertiesUtils.KEY_SECRET);
        fileName = avatarPath +dirName+uuid+fileName;
        try {
            InputStream inputStream = file.getInputStream();
            ossClient.putObject(ConstantPropertiesUtils.BUCKET_NAME, fileName, inputStream);
            return "https://"+ConstantPropertiesUtils.BUCKET_NAME+"."+ConstantPropertiesUtils.END_POINT+"/"+fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
