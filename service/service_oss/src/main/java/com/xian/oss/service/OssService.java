package com.xian.oss.service;

import org.springframework.web.multipart.MultipartFile;


public interface OssService {
    String uploadAvatarFile(MultipartFile file);

    String uploadCoverFile(MultipartFile file);

    String uploadBannerFile(MultipartFile file);
}
